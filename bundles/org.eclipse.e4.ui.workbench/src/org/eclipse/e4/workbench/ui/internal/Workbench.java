/*******************************************************************************
 * Copyright (c) 2008 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.workbench.ui.internal;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.services.AbstractServiceFactory;
import org.eclipse.e4.core.services.IContributionFactory;
import org.eclipse.e4.core.services.IContributionFactorySpi;
import org.eclipse.e4.core.services.IServiceLocator;
import org.eclipse.e4.ui.model.application.Application;
import org.eclipse.e4.ui.model.application.ApplicationElement;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.ContributedPart;
import org.eclipse.e4.ui.model.application.Menu;
import org.eclipse.e4.ui.model.application.Part;
import org.eclipse.e4.ui.model.application.Window;
import org.eclipse.e4.ui.model.workbench.Perspective;
import org.eclipse.e4.ui.model.workbench.WorkbenchFactory;
import org.eclipse.e4.ui.model.workbench.WorkbenchPackage;
import org.eclipse.e4.ui.model.workbench.WorkbenchWindow;
import org.eclipse.e4.workbench.modeling.ModelService;
import org.eclipse.e4.workbench.ui.IExceptionHandler;
import org.eclipse.e4.workbench.ui.ILegacyHook;
import org.eclipse.e4.workbench.ui.IWorkbench;
import org.eclipse.e4.workbench.ui.renderers.swt.ContributedPartFactory;
import org.eclipse.e4.workbench.ui.renderers.swt.PartFactory;
import org.eclipse.e4.workbench.ui.renderers.swt.PartRenderer;
import org.eclipse.e4.workbench.ui.utils.ResourceUtility;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.PackageAdmin;

public class Workbench implements IWorkbench, IServiceLocator,
		IContributionFactory {
	public static final String ID = "org.eclipse.e4.workbench.fakedWBWindow";
	private Application<WorkbenchWindow> workbench;
	private ResourceUtility resourceUtility;
	private static final boolean saveAndRestore = true;
	private File workbenchData;
	private Shell appWindow;
	private final IExtensionRegistry registry;
	private final PackageAdmin packageAdmin;
	private ResourceSetImpl resourceSet;
	private ModelService modelService;
	private IServiceLocator serviceLocator;

	private ILegacyHook legacyHook;

	// UI Construction...
	private PartRenderer renderer;
	private int rv;
	private Map<String,Object> languages;
	private ExceptionHandler exceptionHandler;

	public Workbench(Location instanceLocation, IExtensionRegistry registry,
			PackageAdmin packageAdmin, URI workbenchXmiURI) {

		exceptionHandler = new ExceptionHandler();
		this.registry = registry;
		this.packageAdmin = packageAdmin;
		workbenchData = null;
		try {
			workbenchData = new File(
					new File(instanceLocation.getURL().toURI()),
					".metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		resourceSet = new ResourceSetImpl();

		// Register the appropriate resource factory to handle all file
		// extensions.
		//
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());

		// Register the package to ensure it is available during loading.
		//
		resourceSet.getPackageRegistry().put(WorkbenchPackage.eNS_URI,
				WorkbenchPackage.eINSTANCE);

		processLanguages();
		serviceLocator = createServiceLocator();
		if (workbenchData != null && workbenchData.exists() && saveAndRestore) {
			createWorkbenchModel(workbenchData.getAbsolutePath(),
					workbenchXmiURI);
		} else {
			createWorkbenchModel(null, workbenchXmiURI);
		}
	}

	private IServiceLocator createServiceLocator() {
		final Map<Class<?>, AbstractServiceFactory> serviceFactories = new HashMap<Class<?>, AbstractServiceFactory>();
		IConfigurationElement[] contributions = registry
				.getConfigurationElementsFor("org.eclipse.e4.services");
		for (IConfigurationElement contribution : contributions) {
			IContributor contributor = contribution.getContributor();
			Bundle bundle = getBundleForName(contributor.getName());
			try {
				AbstractServiceFactory factory = (AbstractServiceFactory) contribution
						.createExecutableExtension("class");
				for (IConfigurationElement serviceElement : contribution
						.getChildren("service")) {
					String apiClassname = serviceElement.getAttribute("api");
					Class<?> apiClass;
					apiClass = bundle.loadClass(apiClassname);
					serviceFactories.put(apiClass, factory);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final IServiceLocator parentLocator = new IServiceLocator() {
			public Object getService(Class<?> api) {
				if (IWorkbench.class.equals(api)) {
					return (IWorkbench) Workbench.this;
				} else if (IExceptionHandler.class.equals(api)) {
					return (IExceptionHandler) exceptionHandler;
				} else if (ResourceUtility.class.equals(api)) {
					return (ResourceUtility) resourceUtility;
				}
				return null;
			}

			public boolean hasService(Class<?> api) {
				return IWorkbench.class.equals(api)
						|| IExceptionHandler.class.equals(api)
						|| ResourceUtility.class.equals(api);
			}
		};
		return new IServiceLocator() {
			Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();

			public Object getService(Class<?> api) {
				Object result = services.get(api);
				if (result == null) {
					AbstractServiceFactory factory = serviceFactories.get(api);
					if (factory != null) {
						result = factory.create(api, parentLocator, this);
						services.put(api, result);
					}
				}
				if (result == null) {
					result = parentLocator.getService(api);
				}
				return result;
			}

			public boolean hasService(Class<?> api) {
				return serviceFactories.containsKey(api)
						|| parentLocator.hasService(api);
			}
		};
	}

	private Application<WorkbenchWindow> createWorkbenchModel(String restoreFile,
			URI workbenchDefinitionInstance) {
		boolean restore = false;// restoreFile != null;

		URI uri = null;
		Resource resource = null;
		if (!restore) {
			resource = new XMIResourceImpl();
			workbench = ApplicationFactory.eINSTANCE.createApplication();
			resource.getContents().add((EObject) workbench);

			// Should set up such things as initial perspective id here...
			String initialPerspectiveId = "org.eclipse.e4.compatibility.testPerspective";
			populateWBModel(workbench, workbenchDefinitionInstance,
					initialPerspectiveId);
		} else {
			uri = URI.createFileURI(restoreFile);
			try {
				resource = new ResourceSetImpl().getResource(uri, true);
				workbench = (Application<WorkbenchWindow>) resource.getContents().get(0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!restore) {
			resource.setURI(URI.createFileURI(workbenchData.getAbsolutePath()));
		}

		init(workbench);

		return workbench;
	}

	private void populateWBModel(Application<WorkbenchWindow> wb,
			URI initialWorkbenchDefinitionInstance, String initialPerspectiveId) {

		// Install any registered legacy hook
		installLegacyHook();

		WorkbenchWindow wbw;

		if (legacyHook != null) {
			wbw = WorkbenchFactory.eINSTANCE.createWorkbenchWindow();
			wbw.setName("E4 Workbench Window [Java, Debug]");
			wbw.setTrim(ApplicationFactory.eINSTANCE.createTrim());
			Part<Part<?>> cp = ApplicationFactory.eINSTANCE.createPart();
			wbw.getTrim().setTopTrim(cp);
			cp = ApplicationFactory.eINSTANCE.createPart();
			wbw.getTrim().setBottomTrim(cp);
			cp = ApplicationFactory.eINSTANCE.createPart();
			wbw.getTrim().setLeftTrim(cp);
			cp = ApplicationFactory.eINSTANCE.createPart();
			wbw.getTrim().setRightTrim(cp);

			Menu mainMenu = ApplicationFactory.eINSTANCE.createMenu();
			legacyHook.loadMenu(mainMenu);
			wbw.setMenu(mainMenu);

			Perspective<?> persp = WorkbenchFactory.eINSTANCE.createPerspective();
			persp.setName("Java Perspective");
			legacyHook.loadPerspective(persp);
			wbw.getChildren().add(persp);
			wbw.setActiveChild(persp);
		} else {
			Resource resource = new ResourceSetImpl().getResource(
					initialWorkbenchDefinitionInstance, true);
			Application<Window<Part<?>>> app = (Application<Window<Part<?>>>) resource.getContents().get(0);
			
			// temporary code - we are reading a new model but the code still assumes
			// a WorkbenchWindow with a Perspective, so we need to copy the parts of the
			// window into a perspective.
			wbw = WorkbenchFactory.eINSTANCE.createWorkbenchWindow();
			wbw.setWidth(app.getWindows().get(0).getWidth());
			wbw.setHeight(app.getWindows().get(0).getHeight());
			wbw.setX(app.getWindows().get(0).getX());
			wbw.setY(app.getWindows().get(0).getY());
			wbw.setTrim(ApplicationFactory.eINSTANCE.createTrim());
			Perspective<Part<?>> perspective = WorkbenchFactory.eINSTANCE.createPerspective();
			wbw.getChildren().add(perspective);
			perspective.getChildren().addAll(app.getWindows().get(0).getChildren());

			processPartContributions(resource, wbw);
		}

		wb.getWindows().add(wbw);
	}

	private void processLanguages() {
		languages = new HashMap<String,Object>();
		IExtensionRegistry registry = InternalPlatform.getDefault()
				.getRegistry();
		String extId = "org.eclipse.e4.languages";
		IConfigurationElement[] languageElements = registry
				.getConfigurationElementsFor(extId);
		for (int i = 0; i < languageElements.length; i++) {
			IConfigurationElement languageElement = languageElements[i];
			try {
				languages
						.put(
								languageElement.getAttribute("name"),
								languageElement
										.createExecutableExtension("contributionFactory"));
			} catch (InvalidRegistryObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void processPartContributions(Resource resource, WorkbenchWindow wbw) {
		IExtensionRegistry registry = InternalPlatform.getDefault()
				.getRegistry();
		String extId = "org.eclipse.e4.workbench.parts";
		IConfigurationElement[] parts = registry
				.getConfigurationElementsFor(extId);

		for (int i = 0; i < parts.length; i++) {
			ContributedPart<?> part = ApplicationFactory.eINSTANCE
					.createContributedPart();
			part.setName(parts[i].getAttribute("label"));
			part.setIconURI("platform:/plugin/"
					+ parts[i].getContributor().getName() + "/"
					+ parts[i].getAttribute("icon"));
			part.setURI("platform:/plugin/"
					+ parts[i].getContributor().getName() + "/"
					+ parts[i].getAttribute("class"));
			String parentId = parts[i].getAttribute("parentId");

			Part parent = (Part) findObject(resource.getAllContents(), parentId);
			if (parent != null) {
				parent.getChildren().add(part);
			}
		}

	}

	private void installLegacyHook() {
		IExtensionRegistry registry = InternalPlatform.getDefault()
				.getRegistry();
		String extId = "org.eclipse.e4.workbench.legacy";
		IConfigurationElement[] hooks = registry
				.getConfigurationElementsFor(extId);

		ILegacyHook impl = null;
		if (hooks.length > 0) {
			try {
				impl = (ILegacyHook) hooks[0]
						.createExecutableExtension("class");
				legacyHook = impl;
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

	}

	private EObject findObject(TreeIterator<EObject> it, String id) {
		while (it.hasNext()) {
			EObject el = it.next();
			if (el instanceof ApplicationElement) {
				if (el.eResource().getURIFragment(el).equals(id)) {
					return el;
				}
			}
		}

		return null;
	}

	private void init(Application<WorkbenchWindow> workbench) {
		// workbench.addAdapter(new EContentAdapter() {
		//
		// @Override
		// public void notifyChanged(Notification notification) {
		// super.notifyChanged(notification);
		//
		// if (notification.getEventType() == Notification.ADD
		// && notification.getNewValue() instanceof EclipseElement) {
		// WorkbenchPart<?> part = findPart((EclipseElement) notification
		// .getNewValue());
		// WorkbenchPart<?> newParent = findPart((EclipseElement) notification
		// .getNotifier());
		// if (part != null && newParent != null)
		// part.setParent(newParent);
		// }
		// }
		//
		// });

		// Initialize Services
		modelService = new ModelService(Platform.getAdapterManager());

		resourceUtility = new ResourceUtility(packageAdmin);

		// HACK!! test the modelService and imported functionality
		String[] propIds = modelService.getPropIds(workbench);
	}

	public int run() {
		// appWindow = new WorkbenchWindowPart(this,
		// workbench.getWindows().get(0));
		// appWindow.createPartControl(null, new IServiceLocator() {
		// public Object getService(Class api) {
		// if (api == ResourceUtility.class) {
		// return resourceUtility;
		// }
		// return serviceLocator.getService(api);
		// }
		//
		// public boolean hasService(Class api) {
		// if (api == ResourceUtility.class) {
		// return true;
		// }
		// return serviceLocator.hasService(api);
		// }
		// });

		// appWindow.createPartControl(null);
		WorkbenchWindow wbw = workbench.getWindows().get(0);
		createGUI(wbw);

		rv = 0;
		Platform.endSplash();
		appWindow.open();
		// A position of 0 is not possible on OS-X because then the title-bar is
		// hidden
		// below the Menu-Bar
		// TODO is there a better method to find out the height of the title bar
		int y = wbw.getY();
		if (y == 0 && SWT.getPlatform().equals("carbon")) {
			y = 20;
		}
		appWindow.getShell().setBounds(wbw.getX(), y, wbw.getWidth(),
				wbw.getHeight());

		((Composite) wbw.getWidget()).layout(true);

		Display display = appWindow.getDisplay();
		while (appWindow != null && !appWindow.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		display.update();

		if (workbenchData != null && saveAndRestore && workbench != null) {
			try {
				System.err.println("Saving workbench: "
						+ ((EObject) workbench).eResource().getURI());
				// workbenchData.getParentFile().mkdirs();
				// workbenchData.createNewFile();
				// FileOutputStream fos = new FileOutputStream(workbenchData);
				// ((EObject)workbench).eResource().save(fos, null);
				// fos.close();
				((EObject) workbench).eResource().save(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return rv;
	}

	private void createGUI(WorkbenchWindow workbenchWindow) {
		if (renderer == null) {
			renderer = new PartRenderer((IContributionFactory) this,
					serviceLocator);

			// add the factories from the extension point, sort by dependency
			// * Need to make the EP more declarative to avoid aggressive
			// loading
			IConfigurationElement[] factories = registry
					.getConfigurationElementsFor("org.eclipse.e4.workbench.partfactory");

			// Sort the factories based on their dependence
			// This is a hack, should be based on plug-in dependencies
			int offset = 0;
			for (int i = 0; i < factories.length; i++) {
				String clsSpec = factories[i].getAttribute("class");
				if (clsSpec.indexOf("Palette") >= 0
						|| clsSpec.indexOf("PartSash") >= 0) {
					IConfigurationElement tmp = factories[offset];
					factories[offset++] = factories[i];
					factories[i] = tmp;
				}
			}

			for (int i = 0; i < factories.length; i++) {
				PartFactory factory = null;
				try {
					factory = (PartFactory) factories[i]
							.createExecutableExtension("class");
				} catch (CoreException e) {
					e.printStackTrace();
				}
				if (factory != null) {
					factory.init(renderer, serviceLocator,
							(IContributionFactory) this);
					renderer.addPartFactory(factory);

					// Hack!! initialize the ContributedPartFactory
					if (factory instanceof ContributedPartFactory) {
						ContributedPartFactory cpf = (ContributedPartFactory) factory;
						cpf
								.setContributionFactory(((IContributionFactory) this));
					}
				}
			}
		}

		renderer.createGui(workbenchWindow);
		appWindow = (Shell) workbenchWindow.getWidget();
	}

	public void close() {
		appWindow.dispose();
	}

	private Bundle getBundleForName(String bundleName) {
		Bundle[] bundles = packageAdmin.getBundles(bundleName, null);
		if (bundles == null)
			return null;
		// Return the first bundle that is not installed or uninstalled
		for (int i = 0; i < bundles.length; i++) {
			if ((bundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) {
				return bundles[i];
			}
		}
		return null;
	}

	private Bundle getBundle(URI platformURI) {
		return getBundleForName(platformURI.segment(1));
	}

	public Object createObject(Class<?> targetClass, IServiceLocator serviceLocator) {

		Constructor<?> targetConstructor = null;

		Constructor<?>[] constructors = targetClass.getConstructors();

		// Optimization: if there's only one constructor, use it.
		if (constructors.length == 1) {
			targetConstructor = constructors[0];
		} else {
			ArrayList<Constructor<?>> toSort = new ArrayList<Constructor<?>>();

			for (int i = 0; i < constructors.length; i++) {
				Constructor<?> constructor = constructors[i];

				// Filter out non-public constructors
				if ((constructor.getModifiers() & Modifier.PUBLIC) != 0) {
					toSort.add(constructor);
				}
			}

			// Sort the constructors by descending number of constructor
			// arguments
			Collections.sort(toSort, new Comparator<Constructor<?>>() {
				public int compare(Constructor<?> c1, Constructor<?> c2) {

					int l1 = c1.getParameterTypes().length;
					int l2 = c2.getParameterTypes().length;

					return l1 - l2;
				}
			});

			// Find the first satisfiable constructor
			for (Constructor<?> next: toSort) {
				boolean satisfiable = true;

				Class<?>[] params = next.getParameterTypes();
				for (int i = 0; i < params.length && satisfiable; i++) {
					Class<?> clazz = params[i];

					if (!serviceLocator.hasService(clazz)) {
						satisfiable = false;
					}
				}

				if (satisfiable) {
					targetConstructor = next;
				}
			}
		}

		if (targetConstructor == null) {
			throw new RuntimeException(
					"could not find satisfiable constructor in class " + targetClass); //$NON-NLS-1$//$NON-NLS-2$
		}

		Class<?>[] paramKeys = targetConstructor.getParameterTypes();

		try {
			Object[] params = new Object[paramKeys.length];
			for (int i = 0; i < params.length; i++) {
				params[i] = serviceLocator.getService(paramKeys[i]);
			}

			return targetConstructor.newInstance(params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ResourceUtility getResourceUtility() {
		return resourceUtility;
	}

	public Application<WorkbenchWindow> getModelElement() {
		return workbench;
	}

	public Object getService(Class<?> api) {
		if (api == ModelService.class) {
			return modelService;
		}
		if (api == ResourceUtility.class) {
			return resourceUtility;
		}
		return null;
	}

	public boolean hasService(Class<?> api) {
		if (api == ModelService.class
				|| api == ResourceUtility.class) {
			return true;
		}
		return false;
	}

	public Object create(String uriString,
			IServiceLocator serviceLocator) {
		URI uri = URI.createURI(uriString);
		Bundle bundle = getBundle(uri);
		if (bundle != null) {
			String className = uri.segment(2);
			// hack to get JavaScript support
			int indexOfColon = className.indexOf(":");
			if (indexOfColon != -1) {
				String prefix = className.substring(0, indexOfColon);
				IContributionFactorySpi factory = (IContributionFactorySpi) languages
						.get(prefix);
				return factory.create(bundle, className
						.substring(indexOfColon + 1), serviceLocator);
			}
			try {
				Class targetClass = bundle.loadClass(className);
				return createObject(targetClass, serviceLocator);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Object call(Object object, String uriString, String methodName,
			IServiceLocator serviceLocator, Object defaultValue) {
		URI uri = URI.createURI(uriString);
		// hack to get JavaScript support
		String className = uri.segment(1);
		int indexOfColon = className.indexOf(":");
		if (indexOfColon != -1) {
			String prefix = className.substring(0, indexOfColon);
			IContributionFactorySpi factory = (IContributionFactorySpi) languages
					.get(prefix);
			return factory.call(object, methodName, serviceLocator);
		}

		Method targetMethod = null;

		Method[] methods = object.getClass().getMethods();

		// Optimization: if there's only one method, use it.
		if (methods.length == 1) {
			targetMethod = methods[0];
		} else {
			ArrayList toSort = new ArrayList();

			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];

				// Filter out non-public constructors
				if ((method.getModifiers() & Modifier.PUBLIC) != 0
						&& method.getName().equals(methodName)) {
					toSort.add(method);
				}
			}

			// Sort the methods by descending number of method
			// arguments
			Collections.sort(toSort, new Comparator() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.Comparator#compare(java.lang.Object,
				 * java.lang.Object)
				 */
				public int compare(Object arg0, Object arg1) {
					Constructor c1 = (Constructor) arg0;
					Constructor c2 = (Constructor) arg1;

					int l1 = c1.getParameterTypes().length;
					int l2 = c2.getParameterTypes().length;

					return l1 - l2;
				}
			});

			// Find the first satisfiable method
			for (Iterator iter = toSort.iterator(); iter.hasNext()
					&& targetMethod == null;) {
				Method next = (Method) iter.next();

				boolean satisfiable = true;

				Class[] params = next.getParameterTypes();
				for (int i = 0; i < params.length && satisfiable; i++) {
					Class clazz = params[i];

					if (!serviceLocator.hasService(clazz)) {
						satisfiable = false;
					}
				}

				if (satisfiable) {
					targetMethod = next;
				}
			}
		}

		if (targetMethod == null) {
			if (defaultValue != null) {
				return defaultValue;
			}
			throw new RuntimeException(
					"could not find satisfiable method " + methodName + " in class " + object.getClass()); //$NON-NLS-1$//$NON-NLS-2$
		}

		Class[] paramKeys = targetMethod.getParameterTypes();

		try {
			Object[] params = new Object[paramKeys.length];
			for (int i = 0; i < params.length; i++) {
				params[i] = serviceLocator.getService(paramKeys[i]);
			}

			return targetMethod.invoke(object, params);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
