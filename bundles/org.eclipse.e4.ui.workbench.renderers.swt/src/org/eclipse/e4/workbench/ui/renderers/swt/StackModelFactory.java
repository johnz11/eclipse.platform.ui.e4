/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.workbench.ui.renderers.swt;

import java.util.List;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.e4.ui.model.application.MItemPart;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.model.application.MToolBar;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

public class StackModelFactory extends SWTPartFactory {

	Image viewMenuImage;

	public StackModelFactory() {
		super();
	}

	public Object createWidget(MPart<?> part) {
		Widget newWidget = null;

		if (!(part instanceof MStack))
			return null;

		Widget parentWidget = getParentWidget(part);
		if (parentWidget instanceof Composite) {

			// HACK!! Set up the close button style based on the 'Policy'
			// Perhaps this should be CSS-based ?
			boolean showCloseAlways = false;
			boolean showMinMax = false;
			int styleModifier = 0;
			if (part.getPolicy() != null && part.getPolicy().length() > 0) {
				String policy = part.getPolicy();
				if (policy.indexOf("ViewStack") >= 0) { //$NON-NLS-1$
					styleModifier = SWT.CLOSE;
					showMinMax = true;
				}
				if (policy.indexOf("EditorStack") >= 0) { //$NON-NLS-1$
					styleModifier = SWT.CLOSE;
					showCloseAlways = true;
					showMinMax = true;
				}
			}

			final CTabFolder ctf = new CTabFolder((Composite) parentWidget,
					SWT.BORDER | styleModifier);
			ctf.setUnselectedCloseVisible(showCloseAlways);
			ctf.setMaximizeVisible(showMinMax);
			ctf.setMinimizeVisible(showMinMax);

			// Create a single ViewForm class in which to host -all- views
			// ViewForm vf = new ViewForm(ctf, SWT.NONE);
			// Label vfLabel = new Label(vf, SWT.NONE);
			//			vfLabel.setText("This is a test"); //$NON-NLS-1$
			// vf.setTopLeft(vfLabel);

			bindWidget(part, ctf);
			ctf.setVisible(true);
			ctf.setSimple(false);
			ctf.setTabHeight(20);
			newWidget = ctf;
			final IEclipseContext folderContext = part.getContext();
			folderContext.set(IContextConstants.DEBUG_STRING, "TabFolder"); //$NON-NLS-1$
			final IEclipseContext toplevelContext = getToplevelContext(part);
			final IStylingEngine engine = (IStylingEngine) folderContext
					.get(IStylingEngine.class.getName());
			folderContext.runAndTrack(new Runnable() {
				public void run() {
					IEclipseContext currentActive = toplevelContext;
					IEclipseContext child;
					while (currentActive != folderContext
							&& (child = (IEclipseContext) currentActive
									.get("activeChild")) != null && child != currentActive) { //$NON-NLS-1$
						currentActive = child;
					}
					// System.out.println(cti.getText() + " is now " + ((currentActive == tabItemContext) ? "active" : "inactive"));   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

					if (currentActive == folderContext) {
						engine.setClassname(ctf, "active"); //$NON-NLS-1$
					} else {
						engine.setClassname(ctf, "inactive"); //$NON-NLS-1$
					}
				}
			}, ""); //$NON-NLS-1$
		}

		return newWidget;
	}

	public void postProcess(MPart<?> part) {
		if (!(part instanceof MStack))
			return;

		CTabFolder ctf = (CTabFolder) part.getWidget();
		CTabItem[] items = ctf.getItems();
		MPart<?> selPart = ((MStack) part).getActiveChild();

		// If there's none defined then pick the first
		if (selPart == null && part.getChildren().size() > 0) {
			((MStack) part).setActiveChild((MItemPart<?>) part.getChildren()
					.get(0));
			// selPart = (MPart) part.getChildren().get(0);
		} else {
			for (int i = 0; i < items.length; i++) {
				MPart<?> me = (MPart<?>) items[i].getData(OWNING_ME);
				if (selPart == me) {
					// Ensure the part is created
					if (items[i].getControl() == null)
						renderer.createGui(selPart);

					ctf.setSelection(items[i]);
				}
			}
		}
	}

	@Override
	public void childAdded(final MPart<?> parentElement, MPart<?> element) {
		super.childAdded(parentElement, element);

		if (element instanceof MItemPart<?>) {
			MItemPart<?> itemPart = (MItemPart<?>) element;
			CTabFolder ctf = (CTabFolder) parentElement.getWidget();
			int createFlags = 0;

			// if(element instanceof View && ((View)element).isCloseable())
			// createFlags = createFlags | SWT.CLOSE;

			CTabItem cti = findItemForPart(parentElement, element);
			if (cti == null)
				cti = new CTabItem(ctf, createFlags);

			cti.setData(OWNING_ME, element);
			cti.setText(itemPart.getName());
			cti.setImage(getImage(element));

			// Hook up special logic to synch up the Tab Items
			hookTabControllerLogic(parentElement, element, cti);

			// Lazy Loading: On the first pass through this method the
			// part's control will be null (we're just creating the tabs
			Control ctrl = (Control) element.getWidget();
			if (ctrl != null) {
				showTab((MItemPart<?>) element);
			}
		}
	}

	@Override
	public <P extends MPart<?>> void processContents(MPart<P> me) {
		Widget parentWidget = getParentWidget(me);
		if (parentWidget == null)
			return;

		// Lazy Loading: here we only create the CTabItems, not the parts
		// themselves; they get rendered when the tab gets selected
		List<P> parts = me.getChildren();
		if (parts != null) {
			for (MPart<?> childME : parts) {
				if (childME.isVisible())
					childAdded(me, childME);
			}
		}
	}

	private CTabItem findItemForPart(MPart<?> folder, MPart<?> part) {
		CTabFolder ctf = (CTabFolder) folder.getWidget();
		if (ctf == null)
			return null;

		CTabItem[] items = ctf.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getData(OWNING_ME) == part)
				return items[i];
		}
		return null;
	}

	private void hookTabControllerLogic(final MPart<?> parentElement,
			final MPart<?> childElement, final CTabItem cti) {
		// Handle visibility changes
		((EObject) childElement).eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (ApplicationPackage.Literals.MPART__VISIBLE.equals(msg
						.getFeature())) {
					MPart<?> changedPart = (MPart<?>) msg.getNotifier();
					if (changedPart.isVisible()) {
						childAdded(changedPart.getParent(), changedPart);
					} else {
						childRemoved(changedPart.getParent(), changedPart);
					}
				}
			}
		});

		// Handle label changes
		IObservableValue textObs = EMFObservables
				.observeValue((EObject) childElement,
						ApplicationPackage.Literals.MITEM__NAME);
		ISWTObservableValue uiObs = SWTObservables.observeText(cti);
		dbc.bindValue(uiObs, textObs, null, null);

		// Observe tooltip changes
		IObservableValue emfTTipObs = EMFObservables.observeValue(
				(EObject) childElement,
				ApplicationPackage.Literals.MITEM__TOOLTIP);
		ISWTObservableValue uiTTipObs = SWTObservables.observeTooltipText(cti);
		dbc.bindValue(uiTTipObs, emfTTipObs, null, null);

		// Handle tab item image changes
		((EObject) childElement).eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				MPart<?> sm = (MPart<?>) msg.getNotifier();
				if (ApplicationPackage.Literals.MITEM__ICON_URI.equals(msg
						.getFeature())) {
					Image image = getImage(sm);
					cti.setImage(image);
				}
			}
		});
	}

	@Override
	public void childRemoved(MPart<?> parentElement, MPart<?> child) {
		super.childRemoved(parentElement, child);

		CTabItem oldItem = findItemForPart(parentElement, child);
		if (oldItem != null) {
			oldItem.setControl(null); // prevent the widget from being disposed
			oldItem.dispose();
		}
	}

	@Override
	public void hookControllerLogic(final MPart<?> me) {
		super.hookControllerLogic(me);

		final MStack sm = (MStack) me;
		// Match the selected TabItem to its Part
		CTabFolder ctf = (CTabFolder) me.getWidget();
		ctf.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				MItemPart<?> newPart = (MItemPart<?>) e.item.getData(OWNING_ME);
				if (sm.getActiveChild() != newPart) {
					activate(newPart);
				}

				showTab(newPart);
			}
		});

		// Detect activation...picks up cases where the user clicks on the
		// (already active) tab
		ctf.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event event) {
				CTabFolder ctf = (CTabFolder) event.widget;
				MStack stack = (MStack) ctf.getData(OWNING_ME);
				MItemPart<?> part = stack.getActiveChild();
				if (part != null)
					activate(part);
			}
		});

		((EObject) me).eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (ApplicationPackage.Literals.MPART__ACTIVE_CHILD.equals(msg
						.getFeature())) {
					MStack sm = (MStack) msg.getNotifier();
					MPart<?> selPart = sm.getActiveChild();
					CTabFolder ctf = (CTabFolder) ((MStack) msg.getNotifier())
							.getWidget();
					CTabItem item = findItemForPart(sm, selPart);
					if (item != null) {
						// Lazy Loading: we create the control here if necessary
						// Note that this will result in a second call to
						// 'childAdded' but
						// that logic expects this
						Control ctrl = item.getControl();
						if (ctrl == null) {
							renderer.createGui(selPart);
						}

						ctf.setSelection(item);
					}
				}
			}
		});
	}

	private void showTab(MItemPart<?> part) {
		CTabFolder ctf = (CTabFolder) getParentWidget(part);
		Control ctrl = (Control) part.getWidget();
		CTabItem cti = findItemForPart(part.getParent(), part);
		cti.setControl(ctrl);
		// // HACK! reparent the control under the ViewForm
		// ViewForm vf = (ViewForm) ctf.getChildren()[0];
		// Label lbl = (Label) vf.getTopLeft();
		//		lbl.setText("This is view: " + part.getName()); //$NON-NLS-1$
		//
		// cti.setControl(vf);
		// ctrl.setParent(vf);
		// vf.setContent(ctrl);

		ToolBar tb = getToolbar(part);
		if (tb != null) {
			Control curTR = ctf.getTopRight();
			if (curTR != null)
				curTR.dispose();

			if (tb.getSize().y > ctf.getTabHeight())
				ctf.setTabHeight(tb.getSize().y);

			ctf.setTopRight(tb, SWT.RIGHT);
			ctf.layout(true);
		}

	}

	private ToolBar getToolbar(MItemPart<?> part) {
		if (part.getToolBar() == null && part.getMenu() == null)
			return null;

		MToolBar tbModel = part.getToolBar();
		CTabFolder ctf = (CTabFolder) getParentWidget(part);
		ToolBar tb = (ToolBar) createToolBar(part.getParent(), ctf, tbModel);

		// View menu (if any)
		if (part.getMenu() != null) {
			addMenuButton(part, tb, part.getMenu());
		}

		tb.pack();
		System.out.println("TB size = " + tb.getSize()); //$NON-NLS-1$
		return tb;
	}

	/**
	 * @param tb
	 */
	private void addMenuButton(MItemPart<?> part, ToolBar tb, MMenu menu) {
		ToolItem ti = new ToolItem(tb, SWT.PUSH);
		ti.setImage(getViewMenuImage());
		ti.setHotImage(null);
		ti.setToolTipText("View Menu"); //$NON-NLS-1$
		ti.setData("theMenu", menu); //$NON-NLS-1$
		ti.setData("thePart", part); //$NON-NLS-1$

		ti.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				showMenu((ToolItem) e.widget);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				showMenu((ToolItem) e.widget);
			}
		});
	}

	/**
	 * @param item
	 */
	protected void showMenu(ToolItem item) {
		MMenu menuModel = (MMenu) item.getData("theMenu"); //$NON-NLS-1$
		MItemPart<?> part = (MItemPart<?>) item.getData("thePart"); //$NON-NLS-1$
		Menu menu = (Menu) createMenu(part, item, menuModel);

		// EList<MMenuItem> items = menuModel.getItems();
		// for (Iterator iterator = items.iterator(); iterator.hasNext();) {
		// MMenuItem mToolBarItem = (MMenuItem) iterator.next();
		// MenuItem mi = new MenuItem(menu, SWT.PUSH);
		// mi.setText(mToolBarItem.getName());
		// mi.setImage(getImage(mToolBarItem));
		// }
		Rectangle ib = item.getBounds();
		Point displayAt = item.getParent().toDisplay(ib.x, ib.y + ib.height);
		menu.setLocation(displayAt);
		menu.setVisible(true);

		Display display = Display.getCurrent();
		while (!menu.isDisposed() && menu.isVisible()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		menu.dispose();
	}

	private Image getViewMenuImage() {
		if (viewMenuImage == null) {
			Display d = Display.getCurrent();

			Image viewMenu = new Image(d, 16, 16);
			Image viewMenuMask = new Image(d, 16, 16);

			Display display = Display.getCurrent();
			GC gc = new GC(viewMenu);
			GC maskgc = new GC(viewMenuMask);
			gc.setForeground(display
					.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
			gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));

			int[] shapeArray = new int[] { 6, 1, 15, 1, 11, 5, 10, 5 };
			gc.fillPolygon(shapeArray);
			gc.drawPolygon(shapeArray);

			Color black = display.getSystemColor(SWT.COLOR_BLACK);
			Color white = display.getSystemColor(SWT.COLOR_WHITE);

			maskgc.setBackground(black);
			maskgc.fillRectangle(0, 0, 16, 16);

			maskgc.setBackground(white);
			maskgc.setForeground(white);
			maskgc.fillPolygon(shapeArray);
			maskgc.drawPolygon(shapeArray);
			gc.dispose();
			maskgc.dispose();

			ImageData data = viewMenu.getImageData();
			data.transparentPixel = data.getPixel(0, 0);

			viewMenuImage = new Image(d, viewMenu.getImageData(), viewMenuMask
					.getImageData());
			viewMenu.dispose();
			viewMenuMask.dispose();
		}
		return viewMenuImage;
	}
}
