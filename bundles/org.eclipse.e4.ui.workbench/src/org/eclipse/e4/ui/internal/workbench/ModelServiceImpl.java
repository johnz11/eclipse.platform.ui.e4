/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.internal.workbench;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MGenericTile;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.advanced.MAdvancedFactory;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.emf.ecore.EObject;

/**
 *
 */
public class ModelServiceImpl implements EModelService {
	/**
	 * Determine if the element passes the matching test for all non-null parameters.
	 * 
	 * @param element
	 *            The element to test
	 * @param id
	 *            The Id
	 * @param clazz
	 *            The class that element must be an instance of
	 * @param tagsToMatch
	 *            The tags to check, <b>all</b> the specified rags must be in the element's tags
	 * @return <code>true</code> iff all the tests pass
	 */
	private boolean match(MUIElement element, String id, Class clazz, List<String> tagsToMatch) {
		if (id != null && !id.equals(element.getElementId()))
			return false;

		if (clazz != null && !(clazz.isInstance(element)))
			return false;

		if (tagsToMatch != null) {
			List<String> elementTags = element.getTags();
			for (String tag : tagsToMatch) {
				if (!elementTags.contains(tag))
					return false;
			}
		}

		return true;
	}

	private <T> void findElementsRecursive(MUIElement searchRoot, String id,
			Class<? extends T> type, List<String> tagsToMatch, List<T> elements) {
		// are *we* a match ?
		if (match(searchRoot, id, type, tagsToMatch)) {
			if (!elements.contains((T) searchRoot))
				elements.add((T) searchRoot);
		}

		// Check regular containers
		if (searchRoot instanceof MElementContainer<?>) {
			MElementContainer<MUIElement> container = (MElementContainer<MUIElement>) searchRoot;
			List<MUIElement> children = container.getChildren();
			for (MUIElement child : children) {
				findElementsRecursive(child, id, type, tagsToMatch, elements);
			}
		}

		// Search Trim
		if (searchRoot instanceof MTrimmedWindow) {
			MTrimmedWindow tw = (MTrimmedWindow) searchRoot;
			List<MTrimBar> bars = tw.getTrimBars();
			for (MTrimBar bar : bars) {
				findElementsRecursive(bar, id, type, tagsToMatch, elements);
			}
		}

		// Search Detached Windows
		if (searchRoot instanceof MWindow) {
			MWindow window = (MWindow) searchRoot;
			for (MWindow dw : window.getWindows()) {
				findElementsRecursive(dw, id, type, tagsToMatch, elements);
			}
		}
		if (searchRoot instanceof MPerspective) {
			MPerspective persp = (MPerspective) searchRoot;
			for (MWindow dw : persp.getWindows()) {
				findElementsRecursive(dw, id, type, tagsToMatch, elements);
			}
		}
		// Search shared elements
		if (searchRoot instanceof MPlaceholder) {
			MPlaceholder ph = (MPlaceholder) searchRoot;
			findElementsRecursive(ph.getRef(), id, type, tagsToMatch, elements);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#getAllElements(org.eclipse.e4.ui.model.
	 * application.MUIElement, java.lang.String, java.lang.Class, java.util.List)
	 */
	public <T> List<T> findElements(MUIElement searchRoot, String id, Class<T> clazz,
			List<String> tagsToMatch) {
		List<T> elements = new ArrayList<T>();
		findElementsRecursive(searchRoot, id, clazz, tagsToMatch, elements);
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.workbench.modeling.EModelService#find(java.lang.String,
	 * org.eclipse.e4.ui.model.application.MElementContainer)
	 */
	public MUIElement find(String id, MUIElement searchRoot) {
		if (id == null || id.length() == 0)
			return null;

		List elements = findElements(searchRoot, id, null, null);
		if (elements.size() > 0)
			return (MUIElement) elements.get(0);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#getContainingContext(org.eclipse.e4.ui
	 * .model .application.MUIElement)
	 */
	public IEclipseContext getContainingContext(MUIElement element) {
		MUIElement curParent = null;
		if (element.getCurSharedRef() != null)
			curParent = element.getCurSharedRef().getParent();
		else
			curParent = (MUIElement) ((EObject) element).eContainer();

		while (curParent != null) {
			if (curParent instanceof MContext) {
				return ((MContext) curParent).getContext();
			}

			if (curParent.getCurSharedRef() != null)
				curParent = curParent.getCurSharedRef().getParent();
			else
				curParent = (MUIElement) ((EObject) curParent).eContainer();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#bringToTop(org.eclipse.e4.ui.model.application
	 * .ui.basic.MWindow, org.eclipse.e4.ui.model.application.ui.MUIElement)
	 */
	public void bringToTop(MWindow window, MUIElement element) {
		if (element instanceof MApplication)
			return;

		if (element instanceof MPartStack && !element.isVisible()) {
			MWindow window1 = getTopLevelWindowFor(element);
			String trimId = element.getElementId() + "(minimized)"; //$NON-NLS-1$
			MPerspective persp = getPerspectiveFor(element);
			if (persp != null)
				trimId = element.getElementId() + '(' + persp.getElementId() + ')';
			MToolControl trimCtrl = (MToolControl) find(trimId, window1);
			if (trimCtrl != null) {
				IEclipseContext ctxt = EclipseContextFactory.create();
				ctxt.set("show", true); //$NON-NLS-1$
				ContextInjectionFactory.invoke(trimCtrl.getObject(), Execute.class, ctxt);
			}
		}

		MUIElement parent = element.getParent();
		if (parent == null) {
			MPlaceholder ph = findPlaceholderFor(window, element);
			if (ph != null) {
				element = ph;
				parent = element.getParent();
			}
		}

		if (parent != null) {
			// Force the element to be rendered
			if (!element.isToBeRendered())
				element.setToBeRendered(true);

			((MElementContainer<MUIElement>) parent).setSelectedElement(element);
			bringToTop(window, parent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#findPlaceholderFor(org.eclipse.e4.ui.model
	 * .application.ui.basic.MWindow, org.eclipse.e4.ui.model.application.ui.MUIElement)
	 */
	public MPlaceholder findPlaceholderFor(MWindow window, MUIElement element) {
		List<MPerspectiveStack> psList = findElements(window, null, MPerspectiveStack.class, null);
		if (psList.size() != 1)
			return null;
		MPerspectiveStack pStack = psList.get(0);
		MPerspective persp = pStack.getSelectedElement();
		if (persp == null)
			return null;

		List<MPlaceholder> phList = findElements(persp, null, MPlaceholder.class, null);
		for (MPlaceholder ph : phList) {
			if (ph.getRef() == element)
				return ph;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#move(org.eclipse.e4.ui.model.application.
	 * MUIElement, org.eclipse.e4.ui.model.application.MElementContainer)
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent) {
		move(element, newParent, -1, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#move(org.eclipse.e4.ui.model.application.
	 * MUIElement, org.eclipse.e4.ui.model.application.MElementContainer, boolean)
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent,
			boolean leavePlaceholder) {
		move(element, newParent, -1, leavePlaceholder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#move(org.eclipse.e4.ui.model.application.
	 * MUIElement, org.eclipse.e4.ui.model.application.MElementContainer, int)
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent, int index) {
		move(element, newParent, index, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#move(org.eclipse.e4.ui.model.application.
	 * MUIElement, org.eclipse.e4.ui.model.application.MElementContainer, int, boolean)
	 */
	public void move(MUIElement element, MElementContainer<MUIElement> newParent, int index,
			boolean leavePlaceholder) {
		// Cache where we were
		MElementContainer<MUIElement> curParent = element.getParent();
		int curIndex = curParent.getChildren().indexOf(element);

		// Move the model element
		newParent.getChildren().add(index, element);

		if (leavePlaceholder) {
			MPlaceholder ph = MAdvancedFactory.INSTANCE.createPlaceholder();
			ph.setRef(element);
			curParent.getChildren().add(curIndex, ph);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#swap(org.eclipse.e4.ui.model.application.
	 * MUIElement, org.eclipse.e4.ui.model.application.MPlaceholder)
	 */
	public void swap(MPlaceholder placeholder) {
		MUIElement element = placeholder.getRef();

		MElementContainer<MUIElement> elementParent = element.getParent();
		int elementIndex = elementParent.getChildren().indexOf(element);
		MElementContainer<MUIElement> phParent = placeholder.getParent();
		int phIndex = phParent.getChildren().indexOf(placeholder);

		// Remove the two elements from their respective parents
		elementParent.getChildren().remove(element);
		phParent.getChildren().remove(placeholder);

		// swap over the UIElement info
		boolean onTop = element.isOnTop();
		boolean vis = element.isVisible();
		boolean tbr = element.isToBeRendered();

		element.setOnTop(placeholder.isOnTop());
		element.setVisible(placeholder.isVisible());
		element.setToBeRendered(placeholder.isToBeRendered());

		placeholder.setOnTop(onTop);
		placeholder.setVisible(vis);
		placeholder.setToBeRendered(tbr);

		// Add the elements back into the new parents
		elementParent.getChildren().add(elementIndex, placeholder);
		phParent.getChildren().add(phIndex, element);

	}

	private void combine(MPartSashContainerElement toInsert, MPartSashContainerElement relTo,
			MPartSashContainer newSash, boolean newFirst, int ratio) {
		MElementContainer<MUIElement> curParent = relTo.getParent();
		int index = curParent.getChildren().indexOf(relTo);
		curParent.getChildren().remove(relTo);
		if (newFirst) {
			newSash.getChildren().add(toInsert);
			newSash.getChildren().add(relTo);
		} else {
			newSash.getChildren().add(relTo);
			newSash.getChildren().add(toInsert);
		}

		// Set up the container data before adding the new sash to the model
		toInsert.setContainerData(Integer.toString(ratio));
		relTo.setContainerData(Integer.toString(100 - ratio));

		// add the new sash at the same location
		curParent.getChildren().add(index, newSash);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#insert(org.eclipse.e4.ui.model.application
	 * .MPartSashContainerElement, org.eclipse.e4.ui.model.application.MPartSashContainerElement,
	 * int, int)
	 */
	public void insert(MPartSashContainerElement toInsert, MPartSashContainerElement relTo,
			int where, int ratio) {
		if (toInsert == null || relTo == null)
			return;

		// Ensure the ratio is sane
		if (ratio == 0)
			ratio = 10;
		if (ratio > 100)
			ratio = 90;

		// determine insertion order
		boolean newFirst = where == ABOVE || where == LEFT_OF;

		// The only thing we can add sashes to is an MPartSashContainer or an MWindow so
		// find the correct place to start the insertion
		MUIElement insertRoot = relTo.getParent();
		while (insertRoot != null && !(insertRoot instanceof MWindow)
				&& !(insertRoot instanceof MPartSashContainer)) {
			relTo = (MPartSashContainerElement) insertRoot;
			insertRoot = insertRoot.getParent();
		}

		if (insertRoot instanceof MWindow) {
			// OK, we're certainly going to need a new sash
			MPartSashContainer newSash = BasicFactoryImpl.eINSTANCE.createPartSashContainer();
			newSash.setHorizontal(where == LEFT_OF || where == RIGHT_OF);
			combine(toInsert, relTo, newSash, newFirst, ratio);
		} else if (insertRoot instanceof MGenericTile<?>) {
			MGenericTile<MUIElement> curTile = (MGenericTile<MUIElement>) insertRoot;

			// do we need a new sash or can we extend the existing one?
			if (curTile.isHorizontal() && (where == ABOVE || where == BELOW)) {
				MPartSashContainer newSash = BasicFactoryImpl.eINSTANCE.createPartSashContainer();
				newSash.setHorizontal(false);
				newSash.setContainerData(relTo.getContainerData());
				combine(toInsert, relTo, newSash, newFirst, ratio);
			} else if (!curTile.isHorizontal() && (where == LEFT_OF || where == RIGHT_OF)) {
				MPartSashContainer newSash = BasicFactoryImpl.eINSTANCE.createPartSashContainer();
				newSash.setHorizontal(true);
				newSash.setContainerData(relTo.getContainerData());
				combine(toInsert, relTo, newSash, newFirst, ratio);
			} else {
				// We just need to add to the existing sash
				int relToIndex = relTo.getParent().getChildren().indexOf(relTo);
				if (newFirst) {
					curTile.getChildren().add(relToIndex, toInsert);
				} else {
					curTile.getChildren().add(relToIndex + 1, toInsert);
				}

				// Adjust the sash weights by taking the ratio
				int relToWeight = 100;
				if (relTo.getContainerData() != null) {
					try {
						relToWeight = Integer.parseInt(relTo.getContainerData());
					} catch (NumberFormatException e) {
					}
				}
				int toInsertWeight = (int) ((ratio / 100.0) * relToWeight + 0.5);
				relToWeight = relToWeight - toInsertWeight;
				relTo.setContainerData(Integer.toString(relToWeight));
				toInsert.setContainerData(Integer.toString(toInsertWeight));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#detach(org.eclipse.e4.ui.model.application
	 * .MPartSashContainerElement)
	 */
	public void detach(MPartSashContainerElement element, int x, int y, int width, int height) {
		// Determine the correct parent for the new window
		MUIElement curParent = element.getParent();
		while (curParent != null && !(curParent instanceof MPerspective)
				&& !(curParent instanceof MWindow))
			curParent = curParent.getParent();

		if (curParent == null)
			return; // log??

		MTrimmedWindow newWindow = BasicFactoryImpl.INSTANCE.createTrimmedWindow();

		// HACK! should either be args or should be computed from the control being detached
		newWindow.setX(x);
		newWindow.setY(y);
		newWindow.setWidth(width);
		newWindow.setHeight(height);

		element.getParent().getChildren().remove(element);
		MWindowElement uiRoot = wrapElementForWindow(element);
		newWindow.getChildren().add(uiRoot);

		if (curParent instanceof MPerspective) {
			MPerspective persp = (MPerspective) curParent;
			persp.getWindows().add(newWindow);

			MWindow window = getTopLevelWindowFor(persp);
			IPresentationEngine renderingEngine = persp.getContext().get(IPresentationEngine.class);
			Object foo = renderingEngine.createGui(newWindow, window.getWidget(), persp
					.getContext());
			if (foo != null) {
				// System.out.println(foo.toString());
			}
		} else if (curParent instanceof MWindow) {
			((MWindow) curParent).getWindows().add(newWindow);
		}
	}

	/**
	 * @param element
	 * @return
	 */
	private MWindowElement wrapElementForWindow(MPartSashContainerElement element) {
		if (element instanceof MWindowElement)
			return (MWindowElement) element;

		if (element instanceof MPlaceholder) {
			MUIElement ref = ((MPlaceholder) element).getRef();
			if (ref instanceof MPart) {
				MPartStack newPS = MBasicFactory.INSTANCE.createPartStack();
				newPS.getChildren().add((MPlaceholder) element);
				return newPS;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#getTrim(org.eclipse.e4.ui.model.application
	 * .ui.basic.MTrimmedWindow, org.eclipse.e4.ui.model.application.ui.SideValue)
	 */
	public MTrimBar getTrim(MTrimmedWindow window, SideValue sv) {
		List<MTrimBar> bars = window.getTrimBars();
		for (MTrimBar bar : bars) {
			if (bar.getSide() == sv)
				return bar;
		}

		// Didn't find a trim bar for the side, make one
		MTrimBar newBar = BasicFactoryImpl.eINSTANCE.createTrimBar();
		newBar.setSide(sv);
		window.getTrimBars().add(newBar);
		return newBar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#getTopLevelWindowFor(org.eclipse.e4.ui
	 * .model .application.ui.MUIElement)
	 */
	public MWindow getTopLevelWindowFor(MUIElement element) {
		EObject eObj = (EObject) element;
		while (eObj != null && !(eObj.eContainer() instanceof MApplication))
			eObj = (EObject) eObj.eContainer();

		if (eObj instanceof MWindow)
			return (MWindow) eObj;

		return null; // Ooops!
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#getPerspectiveFor(org.eclipse.e4.ui.model
	 * .application.ui.MUIElement)
	 */
	public MPerspective getPerspectiveFor(MUIElement element) {

		while (true) {
			// if we have a placeholder, reassign ourselves
			MPlaceholder placeholder = element.getCurSharedRef();
			if (placeholder != null) {
				element = placeholder;
			}
			EObject container = ((EObject) element).eContainer();
			if (container == null || container instanceof MApplication) {
				// climbed to the top and found nothing, return null
				return null;
			} else if (container instanceof MPerspectiveStack) {
				// parent is a perspective stack, we ourselves should be a perspective
				return (MPerspective) element;
			}

			// climb up
			element = (MUIElement) container;
		}
	}

	public void removePerspectiveModel(MPerspective persp, MWindow window) {
		if (persp == null)
			return;

		for (MWindow win : persp.getWindows()) {
			win.setToBeRendered(false);
		}

		// Remove any minimized stacks for this perspective
		List<MTrimBar> bars = findElements(window, null, MTrimBar.class, null);
		List<MToolControl> toRemove = new ArrayList<MToolControl>();
		for (MTrimBar bar : bars) {
			for (MUIElement barKid : bar.getChildren()) {
				if (!(barKid instanceof MToolControl))
					continue;
				String id = barKid.getElementId();
				if (id != null && id.contains(persp.getElementId())) {
					toRemove.add((MToolControl) barKid);
				}
			}
		}

		for (MToolControl toolControl : toRemove) {
			// Close any open fast view
			if (toolControl.getObject() != null
					&& toolControl.getObject().getClass().getName().contains("TrimStack")) { //$NON-NLS-1$
				IEclipseContext ctxt = EclipseContextFactory.create();
				ctxt.set("show", false); //$NON-NLS-1$
				ContextInjectionFactory.invoke(toolControl.getObject(), Execute.class, ctxt);
			}

			toolControl.setToBeRendered(false);
			toolControl.getParent().getChildren().remove(toolControl);
		}

		// find the editor area
		//		MUIElement editorArea = find("org.eclipse.ui.editorss", persp); //$NON-NLS-1$
		// if (editorArea instanceof MPlaceholder)
		// ((MPlaceholder) editorArea).setRef(null);
		// editorArea.getParent().getChildren().remove(editorArea);
		persp.setToBeRendered(false);

		MUIElement psElement = persp.getParent();
		MPerspectiveStack ps = (MPerspectiveStack) psElement;
		ps.getChildren().remove(persp);

		if (ps.getSelectedElement() == persp) {
			for (MPerspective p : ps.getChildren()) {
				if (p != persp && p.isToBeRendered()) {
					ps.setSelectedElement(p);
					return;
				}
			}
			ps.setSelectedElement(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.workbench.modeling.EModelService#getActivePerspective(org.eclipse.e4.ui
	 * .model.application.ui.basic.MWindow)
	 */
	public MPerspective getActivePerspective(MWindow window) {
		List<MPerspectiveStack> pStacks = findElements(window, null, MPerspectiveStack.class, null);
		if (pStacks.size() == 1)
			return pStacks.get(0).getSelectedElement();

		return null;
	}
}
