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

import java.util.Iterator;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MElementContainer;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.model.application.MUILabel;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.e4.ui.workbench.swt.internal.AbstractPartRenderer;
import org.eclipse.e4.ui.workbench.swt.util.ISWTResourceUtiltities;
import org.eclipse.e4.workbench.ui.IPresentationEngine;
import org.eclipse.e4.workbench.ui.IResourceUtiltities;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public abstract class SWTPartRenderer extends AbstractPartRenderer {

	public void processContents(MElementContainer<MUIElement> container) {
		// EMF gives us null lists if empty
		if (container == null)
			return;

		// Process any contents of the newly created ME
		EList<MUIElement> parts = container.getChildren();
		if (parts != null) {
			for (Iterator<MUIElement> childIter = parts.iterator(); childIter
					.hasNext();) {
				MUIElement childME = childIter.next();
				IPresentationEngine renderer = (IPresentationEngine) context
						.get(IPresentationEngine.class.getName());
				renderer.createGui(childME);
			}
		}
	}

	public void bindWidget(MUIElement me, Object widget) {
		me.setWidget(widget);
		((Widget) widget).setData(OWNING_ME, me);
		me.setRenderer(this);

		final IStylingEngine engine = (IStylingEngine) getContext(me).get(
				IStylingEngine.SERVICE_NAME);
		engine.setId(widget, me.getId()); // also triggers style()

		Widget swtWidget = (Widget) widget;
		swtWidget.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				MUIElement element = (MUIElement) e.widget.getData(OWNING_ME);
				if (element != null)
					unbindWidget(element);
			}
		});
	}

	public Object unbindWidget(MUIElement me) {
		Widget widget = (Widget) me.getWidget();
		if (widget != null) {
			me.setWidget(null);
			if (!widget.isDisposed())
				widget.setData(OWNING_ME, null);
		}

		// Clear the factory reference
		me.setRenderer(null);

		return widget;
	}

	protected Widget getParentWidget(MUIElement element) {
		return (Widget) element.getParent().getWidget();
	}

	public void disposeWidget(MUIElement element) {
		Widget curWidget = (Widget) element.getWidget();

		if (curWidget != null && !curWidget.isDisposed()) {
			unbindWidget(element);
			curWidget.dispose();
		}
		element.setWidget(null);
	}

	public void hookControllerLogic(final MUIElement me) {
		Widget widget = (Widget) me.getWidget();

		// Clean up if the widget is disposed
		widget.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				MUIElement model = (MUIElement) e.widget.getData(OWNING_ME);
				if (model != null)
					model.setWidget(null);
			}
		});

		// add an accessibility listener (not sure if this is in the wrong place
		// (factory?)
		if (widget instanceof Control && me instanceof MUILabel) {
			((Control) widget).getAccessible().addAccessibleListener(
					new AccessibleAdapter() {
						public void getName(AccessibleEvent e) {
							e.result = ((MUILabel) me).getLabel();
						}
					});
		}
	}

	protected Image getImage(MUILabel element) {
		IEclipseContext localContext = context;
		String iconURI = element.getIconURI();
		if (iconURI != null && iconURI.length() > 0) {
			ISWTResourceUtiltities resUtils = (ISWTResourceUtiltities) localContext
					.get(IResourceUtiltities.class.getName());
			ImageDescriptor desc = resUtils.imageDescriptorFromURI(URI
					.createURI(iconURI));
			if (desc != null)
				return desc.createImage();
		}
		return null;
	}

	/**
	 * Calculates the index of the element in terms of the other <b>rendered</b>
	 * elements. This is useful when 'inserting' elements in the middle of
	 * existing, rendered parents.
	 * 
	 * @param element
	 *            The element to get the index for
	 * @return The visible index or -1 if the element is not a child of the
	 *         parent
	 */
	protected int calcVisibleIndex(MUIElement element) {
		MElementContainer<MUIElement> parent = element.getParent();

		int curIndex = 0;
		for (MUIElement child : parent.getChildren()) {
			if (child == element) {
				return curIndex;
			}

			if (child.getWidget() != null)
				curIndex++;
		}
		return -1;
	}

	/*
	 * HACK: Create a wrapper composite with appropriate layout for the purpose
	 * of styling margins. See bug #280632
	 */
	protected Composite createWrapperForStyling(Composite parentWidget,
			IEclipseContext context) {
		Composite layoutHolder = new Composite(parentWidget, SWT.NONE);
		addLayoutForStyling(layoutHolder);
		layoutHolder.setData("org.eclipse.e4.ui.css.swt.marginWrapper", true); //$NON-NLS-1$
		final IStylingEngine engine = (IStylingEngine) context
				.get(IStylingEngine.SERVICE_NAME);
		engine.setClassname(layoutHolder, "marginWrapper"); //$NON-NLS-1$
		return layoutHolder;
	}

	/*
	 * HACK: Add layout information to the composite for the purpose of styling
	 * margins. See bug #280632
	 */
	protected void addLayoutForStyling(Composite composite) {
		GridLayout gl = new GridLayout(1, true);
		composite.setLayout(gl);
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
	}

	/*
	 * HACK: Prep the control with layout information for the purpose of styling
	 * margins. See bug #280632
	 */
	protected void configureForStyling(Control control) {
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.workbench.ui.renderers.AbstractPartRenderer#childRendered
	 * (org.eclipse.e4.ui.model.application.MElementContainer,
	 * org.eclipse.e4.ui.model.application.MUIElement)
	 */
	@Override
	public void childRendered(MElementContainer<MUIElement> parentElement,
			MUIElement element) {
	}
}
