/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.tests.application;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.Category;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.services.IContributionFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MApplicationPackage;
import org.eclipse.e4.ui.model.application.MCommand;
import org.eclipse.e4.ui.model.application.MContext;
import org.eclipse.e4.ui.model.application.MElementContainer;
import org.eclipse.e4.ui.model.application.MPSCElement;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.events.IEventBroker;
import org.eclipse.e4.workbench.ui.IPresentationEngine;
import org.eclipse.e4.workbench.ui.internal.IUIEvents;
import org.eclipse.e4.workbench.ui.internal.Workbench;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public abstract class HeadlessApplicationTest extends
		HeadlessApplicationElementTest {

	protected MApplication application;

	protected IPresentationEngine renderer;

	private EventHandler eventHandler = new EventHandler() {
		public void handleEvent(Event event) {
			if (event.getProperty(IUIEvents.EventTags.AttName).equals(
					IUIEvents.ElementContainer.ActiveChild)) {
				Object oldPart = event
						.getProperty(IUIEvents.EventTags.OldValue);
				Object newPart = event
						.getProperty(IUIEvents.EventTags.NewValue);
				if (oldPart instanceof MContext) {
					IEclipseContext context = (IEclipseContext) ((MContext) oldPart)
							.getContext().get(IContextConstants.PARENT);
					context.set(IContextConstants.ACTIVE_CHILD,
							newPart == null ? null : ((MContext) newPart)
									.getContext());
				} else if (newPart instanceof MContext) {
					IEclipseContext context = (IEclipseContext) ((MContext) newPart)
							.getContext().get(IContextConstants.PARENT);
					context.set(IContextConstants.ACTIVE_CHILD,
							((MContext) newPart).getContext());
				}
			}
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		application = (MApplication) applicationElement;

		if (needsActiveChildEventHandling()) {
			addActiveChildEventHandling();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		IEventBroker eventBroker = (IEventBroker) application.getContext().get(
				IEventBroker.class.getName());
		eventBroker.unsubscribe(eventHandler);

		super.tearDown();

		for (MWindow window : application.getChildren()) {
			renderer.removeGui(window);
		}
	}

	protected boolean needsActiveChildEventHandling() {
		return true;
	}

	private void addActiveChildEventHandling() {
		IEventBroker eventBroker = (IEventBroker) application.getContext().get(
				IEventBroker.class.getName());
		eventBroker.subscribe(IUIEvents.ElementContainer.Topic, null,
				eventHandler, true);
	}

	public void testGet_ActiveContexts() throws Exception {
		IEclipseContext context = application.getContext();

		assertNotNull(context.get(IServiceConstants.ACTIVE_CONTEXTS));
	}

	public void testGet_Selection() throws Exception {
		IEclipseContext context = application.getContext();

		assertNull(context.get(IServiceConstants.SELECTION));
	}

	public void testGet_ActiveChild() throws Exception {
		IEclipseContext context = application.getContext();

		assertNull(context.get(IContextConstants.ACTIVE_CHILD));
	}

	public void testGet_ActivePart() throws Exception {
		IEclipseContext context = application.getContext();

		assertNull(context.get(IServiceConstants.ACTIVE_PART));
	}

	public void testGet_Input() throws Exception {
		IEclipseContext context = application.getContext();

		assertNull(context.get(IServiceConstants.INPUT));
	}

	public void testGet_PersistedState() throws Exception {
		IEclipseContext context = application.getContext();

		assertNull(context.get(IServiceConstants.PERSISTED_STATE));
	}

	public void testGet_ActivePartId() throws Exception {
		IEclipseContext context = application.getContext();
		assertNull(context.get(IServiceConstants.ACTIVE_PART_ID));
	}

	// public void test_SwitchActiveChildInContext() {
	// IEclipseContext context = application.getContext();
	//
	// MPart[] parts = getTwoParts();
	//
	// parts[0].getParent().setActiveChild(parts[0]);
	//
	// IEclipseContext activeChildContext = (IEclipseContext) context
	// .get(IContextConstants.ACTIVE_CHILD);
	// while (activeChildContext != null) {
	// if (parts[0].getContext().equals(activeChildContext)) {
	// break;
	// }
	//
	// activeChildContext = (IEclipseContext) activeChildContext
	// .get(IContextConstants.ACTIVE_CHILD);
	// }
	//
	// assertEquals(parts[0].getContext(), activeChildContext);
	//
	// // the OSGi context should not have been affected by the recursion
	// assertEquals(null, osgiContext.get(IContextConstants.ACTIVE_CHILD));
	//
	// parts[1].getParent().setActiveChild(parts[1]);
	//
	// activeChildContext = (IEclipseContext) context
	// .get(IContextConstants.ACTIVE_CHILD);
	// while (activeChildContext != null) {
	// if (parts[1].getContext().equals(activeChildContext)) {
	// break;
	// }
	//
	// activeChildContext = (IEclipseContext) activeChildContext
	// .get(IContextConstants.ACTIVE_CHILD);
	// }
	//
	// assertEquals(parts[1].getContext(), activeChildContext);
	//
	// // the OSGi context should not have been affected by the recursion
	// assertEquals(null, osgiContext.get(IContextConstants.ACTIVE_CHILD));
	// }

	public void test_SwitchActivePartsInContext() throws Exception {
		IEclipseContext context = application.getContext();

		MPart[] parts = getTwoParts();

		context.set(IServiceConstants.ACTIVE_PART, parts[0]);
		assertEquals(parts[0].getId(), context
				.get(IServiceConstants.ACTIVE_PART_ID));

		// the OSGi context should not have been affected by the recursion
		assertNull(osgiContext.get(IServiceConstants.ACTIVE_PART));
		assertNull(osgiContext.get(IServiceConstants.ACTIVE_PART_ID));

		context.set(IServiceConstants.ACTIVE_PART, parts[1]);
		assertEquals(parts[1].getId(), context
				.get(IServiceConstants.ACTIVE_PART_ID));

		// the OSGi context should not have been affected by the recursion
		assertNull(osgiContext.get(IServiceConstants.ACTIVE_PART));
		assertNull(osgiContext.get(IServiceConstants.ACTIVE_PART_ID));
	}

	private void test_GetContext(MContext context) {
		assertNotNull(context.getContext());
	}

	public void testGetFirstPart_GetContext() {
		// set the active part to ensure that it's actually been rendered
		getFirstPart().getParent().setActiveChild(getFirstPart());
		test_GetContext(getFirstPart());
	}

	public void testGetSecondPart_GetContext() {
		// set the active part to ensure that it's actually been rendered
		getSecondPart().getParent().setActiveChild(getSecondPart());
		test_GetContext(getSecondPart());
	}

	private void testModify(MContext mcontext) {
		Set<String> variables = getVariables(mcontext, new HashSet<String>());
		IEclipseContext context = mcontext.getContext();

		for (String variable : variables) {
			Object newValue = new Object();
			context.modify(variable, newValue);
			assertEquals(newValue, context.get(variable));
		}
	}

	public void testModify() {
		testGetFirstPart_GetContext();
		testModify(getFirstPart());
	}

	public void testModify2() {
		testGetSecondPart_GetContext();
		testModify(getSecondPart());
	}

	private static Set<String> getVariables(MContext context,
			Set<String> variables) {
		variables.addAll(context.getVariables());

		if (context instanceof MUIElement) {
			MElementContainer<?> parent = ((MUIElement) context).getParent();
			while (parent != null) {
				if (parent instanceof MContext) {
					getVariables((MContext) parent, variables);
				}
				parent = parent.getParent();
			}
		}

		return variables;
	}

	protected MPart[] getTwoParts() {
		MPart firstPart = getFirstPart();
		assertNotNull(firstPart);

		MPart secondPart = getSecondPart();
		assertNotNull(secondPart);

		assertFalse(firstPart.equals(secondPart));

		return new MPart[] { firstPart, secondPart };
	}

	protected abstract MPart getFirstPart();

	protected abstract MPart getSecondPart();

	protected void createGUI(MUIElement uiRoot) {
		renderer.createGui(uiRoot);
	}

	@Override
	protected MApplicationElement createApplicationElement(
			IEclipseContext appContext) throws Exception {
		return createApplication(appContext, getURI());
	}

	protected abstract String getURI();

	protected IPresentationEngine createPresentationEngine(
			String renderingEngineURI) throws Exception {
		IContributionFactory contributionFactory = (IContributionFactory) applicationContext
				.get(IContributionFactory.class.getName());
		Object newEngine = contributionFactory.create(renderingEngineURI,
				applicationContext);
		return (IPresentationEngine) newEngine;
	}

	private MApplication createApplication(IEclipseContext appContext,
			String appURI) throws Exception {
		URI initialWorkbenchDefinitionInstance = URI.createPlatformPluginURI(
				appURI, true);

		ResourceSet set = new ResourceSetImpl();
		set.getPackageRegistry().put("http://MApplicationPackage/",
				MApplicationPackage.eINSTANCE);

		Resource resource = set.getResource(initialWorkbenchDefinitionInstance,
				true);

		MApplication application = (MApplication) resource.getContents().get(0);
		appContext.set(MApplication.class.getName(), application);
		application.setContext(appContext);

		ECommandService cs = (ECommandService) appContext
				.get(ECommandService.class.getName());
		Category cat = cs.defineCategory(MApplication.class.getName(),
				"Application Category", null); //$NON-NLS-1$
		EList<MCommand> commands = application.getCommands();
		for (MCommand cmd : commands) {
			String id = cmd.getId();
			String name = cmd.getCommandName();
			cs.defineCommand(id, name, null, cat, null);
		}

		// take care of generating the contexts.
		EList<MWindow> windows = application.getChildren();
		for (MWindow window : windows) {
			Workbench.initializeContext(appContext, window);
		}

		Workbench.processHierarchy(application);

		processPartContributions(application.getContext(), resource);

		renderer = createPresentationEngine(getEngineURI());

		for (MWindow wbw : windows) {
			createGUI(wbw);
		}

		return application;
	}

	protected String getEngineURI() {
		return "platform:/plugin/org.eclipse.e4.ui.tests/org.eclipse.e4.ui.tests.application.HeadlessContextPresentationEngine"; //$NON-NLS-1$
	}

	private void processPartContributions(IEclipseContext context,
			Resource resource) {
		IExtensionRegistry registry = (IExtensionRegistry) context
				.get(IExtensionRegistry.class.getName());
		String extId = "org.eclipse.e4.workbench.parts"; //$NON-NLS-1$
		IConfigurationElement[] parts = registry
				.getConfigurationElementsFor(extId);

		for (int i = 0; i < parts.length; i++) {
			MPart part = MApplicationFactory.eINSTANCE.createPart();
			part.setName(parts[i].getAttribute("label")); //$NON-NLS-1$
			part.setIconURI("platform:/plugin/" //$NON-NLS-1$
					+ parts[i].getContributor().getName() + "/" //$NON-NLS-1$
					+ parts[i].getAttribute("icon")); //$NON-NLS-1$
			part.setURI("platform:/plugin/" //$NON-NLS-1$
					+ parts[i].getContributor().getName() + "/" //$NON-NLS-1$
					+ parts[i].getAttribute("class")); //$NON-NLS-1$
			String parentId = parts[i].getAttribute("parentId"); //$NON-NLS-1$

			Object parent = findObject(resource.getAllContents(), parentId);
			if (parent instanceof MElementContainer<?>) {
				((MElementContainer<MPSCElement>) parent).getChildren().add(
						part);
			}
		}

	}

	private EObject findObject(TreeIterator<EObject> it, String id) {
		while (it.hasNext()) {
			EObject el = it.next();
			if (el instanceof MApplicationElement) {
				if (el.eResource().getURIFragment(el).equals(id)) {
					return el;
				}
			}
		}

		return null;
	}

	protected MApplicationElement findElement(String id) {
		return findElement(application, id);
	}

	private MApplicationElement findElement(MElementContainer<?> container,
			String id) {
		if (id.equals(container.getId())) {
			return container;
		}

		EList<?> children = container.getChildren();
		for (Object child : children) {
			MApplicationElement element = (MApplicationElement) child;
			if (element instanceof MElementContainer<?>) {
				MApplicationElement found = findElement(
						(MElementContainer<?>) element, id);
				if (found != null) {
					return found;
				}
			} else if (id.equals(element.getId())) {
				return element;
			}
		}
		return null;
	}

}
