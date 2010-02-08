/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.workbench.ui.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.core.services.Logger;
import org.eclipse.e4.core.services.annotations.Optional;
import org.eclipse.e4.core.services.annotations.PostConstruct;
import org.eclipse.e4.core.services.annotations.PreDestroy;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextInjectionFactory;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MContext;
import org.eclipse.e4.ui.model.application.MElementContainer;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MPartDescriptor;
import org.eclipse.e4.ui.model.application.MPartStack;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.events.IEventBroker;
import org.eclipse.e4.workbench.modeling.EModelService;
import org.eclipse.e4.workbench.modeling.EPartService;
import org.eclipse.e4.workbench.modeling.IPartListener;
import org.eclipse.e4.workbench.modeling.ISaveHandler;
import org.eclipse.e4.workbench.modeling.ISaveHandler.Save;
import org.eclipse.e4.workbench.ui.IPresentationEngine;
import org.eclipse.e4.workbench.ui.UIEvents;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class PartServiceImpl implements EPartService {

	public static void addListener(IEventBroker broker) {
		EventHandler windowHandler = new EventHandler() {
			public void handleEvent(Event event) {
				Object element = event.getProperty(UIEvents.EventTags.ELEMENT);
				if (element instanceof MWindow) {
					MContext contextAware = (MContext) element;
					IEclipseContext context = contextAware.getContext();
					if (context != null) {
						context.set(EPartService.PART_SERVICE_ROOT, element);
					}
				}
			}
		};
		broker.subscribe(UIEvents.buildTopic(UIEvents.Context.TOPIC, UIEvents.Context.CONTEXT),
				windowHandler);
	}

	private EventHandler selectedHandler = new EventHandler() {
		public void handleEvent(Event event) {
			Object selected = event.getProperty(UIEvents.EventTags.NEW_VALUE);
			if (selected instanceof MPart) {
				firePartBroughtToTop((MPart) selected);
			}
		}
	};

	@Inject
	private MApplication application;

	/**
	 * This is the specific implementation. TODO: generalize it
	 */
	@Inject
	@Named(EPartService.PART_SERVICE_ROOT)
	private MElementContainer<MUIElement> rootContainer;

	@Inject
	private IPresentationEngine engine;

	@Inject
	private EModelService modelService;

	@Inject
	private Logger logger;

	@Inject
	private ISaveHandler saveHandler;

	@Inject
	private IEventBroker eventBroker;

	private MPart activePart;

	private ListenerList listeners = new ListenerList();

	private boolean constructed = false;

	@Inject
	void setPart(@Optional @Named(IServiceConstants.ACTIVE_PART) MPart p) {
		activePart = p;

		if (constructed && p != null) {
			firePartActivated(p);
		}
	}

	@PostConstruct
	void postConstruct() {
		eventBroker.subscribe(UIEvents.buildTopic(UIEvents.ElementContainer.TOPIC,
				UIEvents.ElementContainer.SELECTEDELEMENT), selectedHandler);
		constructed = true;
	}

	@PreDestroy
	void preDestroy() {
		constructed = false;
		eventBroker.unsubscribe(selectedHandler);
	}

	private void firePartActivated(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partActivated(part);
		}
	}

	private void firePartBroughtToTop(MPart part) {
		for (Object listener : listeners.getListeners()) {
			((IPartListener) listener).partBroughtToTop(part);
		}
	}

	public void addPartListener(IPartListener listener) {
		listeners.add(listener);
	}

	public void removePartListener(IPartListener listener) {
		listeners.remove(listener);
	}

	private MContext getParentWithContext(MUIElement part) {
		MElementContainer<MUIElement> parent = part.getParent();
		while (parent != null) {
			if (parent instanceof MContext) {
				if (((MContext) parent).getContext() != null)
					return (MContext) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public void bringToTop(MPart part) {
		if (isInContainer(part)) {
			part.setToBeRendered(true);
			internalBringToTop(part);
		}
	}

	private void internalBringToTop(MPart part) {
		MElementContainer<MUIElement> parent = part.getParent();
		MPart oldSelectedElement = (MPart) parent.getSelectedElement();
		if (oldSelectedElement != part) {
			parent.setSelectedElement(part);
			internalFixContext(part, oldSelectedElement);
		}
	}

	private void internalFixContext(MPart part, MPart oldSelectedElement) {
		MContext parentPart = getParentWithContext(oldSelectedElement);
		if (parentPart == null) {
			return;
		}
		IEclipseContext parentContext = parentPart.getContext();
		IEclipseContext oldContext = oldSelectedElement.getContext();
		Object child = parentContext.get(IContextConstants.ACTIVE_CHILD);
		if (child == oldContext) {
			parentContext.set(IContextConstants.ACTIVE_CHILD, part == null ? null : part
					.getContext());
		}
	}

	public MPart findPart(String id) {
		MApplicationElement element = modelService.find(id, rootContainer);
		return element instanceof MPart ? (MPart) element : null;
	}

	public Collection<MPart> getParts() {
		return getParts(new ArrayList<MPart>(), rootContainer);
	}

	private Collection<MPart> getParts(Collection<MPart> parts,
			MElementContainer<?> elementContainer) {
		for (Object child : elementContainer.getChildren()) {
			if (child instanceof MPart) {
				parts.add((MPart) child);
			} else if (child instanceof MElementContainer<?>) {
				getParts(parts, (MElementContainer<?>) child);
			}
		}
		return parts;
	}

	public boolean isPartVisible(MPart part) {
		if (isInContainer(part)) {
			MElementContainer<?> parent = part.getParent();
			if (parent instanceof MPartStack) {
				return parent.getSelectedElement() == part;
			}

			return part.isVisible();
		}
		return false;
	}

	private boolean isInContainer(MPart part) {
		return isInContainer(rootContainer, part);
	}

	private boolean isInContainer(MElementContainer<?> container, MPart part) {
		for (Object object : container.getChildren()) {
			if (object == part) {
				return true;
			} else if (object instanceof MElementContainer<?>) {
				if (isInContainer((MElementContainer<?>) object, part)) {
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.workbench.modeling.EPartService#activate(org.eclipse.e4.ui.model.application
	 * .MPart)
	 */
	public void activate(MPart part) {
		if (!isInContainer(part)) {
			return;
		}
		IEclipseContext curContext = part.getContext();
		MContext pwc = getParentWithContext(part);
		MUIElement curElement = part;
		while (pwc != null) {
			// Ensure that the UI model has the part 'on top'
			while (curElement != pwc) {
				MElementContainer<MUIElement> parent = curElement.getParent();
				curElement.setToBeRendered(true);
				if (parent.getSelectedElement() != curElement) {
					parent.setSelectedElement(curElement);
				}
				curElement = parent;
			}

			if (curContext == null) {
				curContext = part.getContext();
			}

			IEclipseContext parentContext = pwc.getContext();
			if (parentContext != null) {
				parentContext.set(IContextConstants.ACTIVE_CHILD, curContext);
				curContext = parentContext;
			}

			pwc = getParentWithContext((MUIElement) pwc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.workbench.modeling.EPartService#getActivePart()
	 */
	public MPart getActivePart() {
		return activePart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.workbench.modeling.EPartService#deactivate(org.eclipse.e4.ui.model.application
	 * .MPart)
	 */
	public void deactivate(MPart part) {
		MElementContainer<MUIElement> parent = part.getParent();
		MPart oldSelectedElement = (MPart) parent.getSelectedElement();
		if (oldSelectedElement == part) {
			parent.setSelectedElement(null);
			internalFixContext(null, oldSelectedElement);
		}
	}

	private MPartDescriptor findDescriptor(String id) {
		for (MPartDescriptor descriptor : application.getDescriptors()) {
			if (descriptor.getId().equals(id)) {
				return descriptor;
			}
		}
		return null;
	}

	private MPart createPart(MPartDescriptor descriptor) {
		return descriptor == null ? null : (MPart) EcoreUtil.copy((EObject) descriptor);
	}

	public MPart createPart(String id) {
		MPartDescriptor descriptor = findDescriptor(id);
		return createPart(descriptor);
	}

	private MPart addPart(MPart providedPart, MPart localPart) {
		if (providedPart == localPart && isInContainer(providedPart)) {
			return providedPart;
		}

		MPartDescriptor descriptor = findDescriptor(providedPart.getId());
		if (descriptor == null) {
			if (providedPart != localPart) {
				MPartStack stack = MApplicationFactory.eINSTANCE.createPartStack();
				stack.getChildren().add(providedPart);
				rootContainer.getChildren().add(stack);
			}
		} else {
			if (providedPart != localPart && !descriptor.isAllowMultiple()) {
				return localPart;
			}

			String category = descriptor.getCategory();
			MApplicationElement container = modelService.find(category, rootContainer);
			if (container instanceof MElementContainer<?>) {
				((MElementContainer<MPart>) container).getChildren().add(providedPart);
			} else {
				MPartStack stack = MApplicationFactory.eINSTANCE.createPartStack();
				stack.setId(category);
				stack.getChildren().add(providedPart);
				rootContainer.getChildren().add(stack);
			}
		}
		return providedPart;
	}

	private MPart showExistingPart(PartState partState, MPart providedPart, MPart localPart) {
		MPart part = addPart(providedPart, localPart);
		switch (partState) {
		case ACTIVATE:
			activate(part);
			return part;
		case VISIBLE:
			MPart activePart = getActivePart();
			if (activePart == part) {
				part.setToBeRendered(true);
			} else {
				if (activePart.getParent() == part.getParent()) {
					part.setToBeRendered(true);
					engine.createGui(part);
				} else {
					bringToTop(part);
				}
			}
			return part;
		case CREATE:
			part.setToBeRendered(true);
			engine.createGui(part);
			return part;
		}
		return part;
	}

	private MPart showNewPart(MPart part, PartState partState) {
		part = addPart(part, part);

		MPart activePart = getActivePart();
		if (activePart == null) {
			activate(part);
			return part;
		}

		switch (partState) {
		case ACTIVATE:
			activate(part);
			return part;
		case VISIBLE:
			if (activePart.getParent() != part.getParent()) {
				bringToTop(part);
			}
		}
		return part;
	}

	public MPart showPart(String id, PartState partState) {
		Assert.isNotNull(id);
		Assert.isNotNull(partState);

		MPart part = findPart(id);
		if (part != null) {
			return showPart(part, partState);
		}

		MPartDescriptor descriptor = findDescriptor(id);
		part = createPart(descriptor);
		if (part == null) {
			return null;
		}

		return showNewPart(part, partState);
	}

	public MPart showPart(MPart part, PartState partState) {
		Assert.isNotNull(part);
		Assert.isNotNull(partState);

		MPart localPart = findPart(part.getId());
		if (localPart != null) {
			return showExistingPart(partState, part, localPart);
		}
		return showNewPart(part, partState);
	}

	public void hidePart(MPart part) {
		if (isInContainer(part)) {
			part.setToBeRendered(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.workbench.modeling.EPartService#getDirtyParts()
	 */
	public Collection<MPart> getDirtyParts() {
		List<MPart> dirtyParts = new ArrayList<MPart>();
		for (MPart part : getParts()) {
			if (part.isDirty()) {
				dirtyParts.add(part);
			}
		}
		return dirtyParts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.workbench.modeling.EPartService#save(org.eclipse.e4.ui.model.application.
	 * MSaveablePart, boolean)
	 */
	public boolean savePart(MPart part, boolean confirm) {
		if (!part.isDirty()) {
			return true;
		}

		if (confirm && saveHandler != null) {
			switch (saveHandler.promptToSave(part)) {
			case NO:
				return true;
			case CANCEL:
				return false;
			}
		}

		Object client = part.getObject();
		try {
			ContextInjectionFactory.invoke(client, "doSave", part.getContext()); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			logger.error(e.getCause());
			return false;
		} catch (CoreException e) {
			IStatus status = e.getStatus();
			Throwable throwable = status.getException();
			if (throwable == null) {
				logger.error(status.getMessage());
			} else {
				logger.error(throwable);
			}
			return false;
		}
		return true;
	}

	public boolean saveAll(boolean confirm) {
		Collection<MPart> dirtyParts = getDirtyParts();
		if (dirtyParts.isEmpty()) {
			return true;
		}

		if (confirm) {
			List<MPart> dirtyPartsList = Collections.unmodifiableList(new ArrayList<MPart>(
					dirtyParts));
			Save[] decisions = saveHandler.promptToSave(dirtyPartsList);
			for (Save decision : decisions) {
				if (decision == Save.CANCEL) {
					return false;
				}
			}

			boolean success = true;
			for (int i = 0; i < decisions.length; i++) {
				if (decisions[i] == Save.YES) {
					if (!savePart(dirtyPartsList.get(i), false)) {
						return false;
					}
				}
			}
			return success;
		}

		boolean success = true;
		for (MPart dirtyPart : dirtyParts) {
			if (!savePart(dirtyPart, false)) {
				success = false;
			}
		}
		return success;
	}
}
