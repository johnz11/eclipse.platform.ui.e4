/**
 * <copyright>
 * </copyright>
 *
 * $Id: TestHarnessImpl.java,v 1.1.2.1 2010/04/16 12:24:24 tschindl Exp $
 */
package org.eclipse.e4.ui.tests.model.test.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MContribution;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsPackageImpl;
import org.eclipse.e4.ui.model.application.impl.ApplicationElementImpl;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.e4.ui.model.application.impl.StringToStringMapImpl;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MInput;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.impl.UiPackageImpl;
import org.eclipse.e4.ui.tests.model.test.MTestHarness;
import org.eclipse.e4.ui.tests.model.test.MTestPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Harness</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getCommandName
 * <em>Command Name</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getDescription
 * <em>Description</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getParameters
 * <em>Parameters</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getContext
 * <em>Context</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getVariables
 * <em>Variables</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getProperties
 * <em>Properties</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getContributionURI
 * <em>Contribution URI</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getObject
 * <em>Object</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getPersistedState
 * <em>Persisted State</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getWidget
 * <em>Widget</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getRenderer
 * <em>Renderer</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#isToBeRendered
 * <em>To Be Rendered</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#isOnTop
 * <em>On Top</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#isVisible
 * <em>Visible</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getParent
 * <em>Parent</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getContainerData
 * <em>Container Data</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getChildren
 * <em>Children</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getSelectedElement
 * <em>Selected Element</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getName
 * <em>Name</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getValue
 * <em>Value</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getInputURI
 * <em>Input URI</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getLabel
 * <em>Label</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getIconURI
 * <em>Icon URI</em>}</li>
 * <li>
 * {@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#getTooltip
 * <em>Tooltip</em>}</li>
 * <li>{@link org.eclipse.e4.ui.tests.model.test.impl.TestHarnessImpl#isDirty
 * <em>Dirty</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TestHarnessImpl extends ApplicationElementImpl implements
		MTestHarness {
	/**
	 * The default value of the '{@link #getCommandName() <em>Command Name</em>}
	 * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCommandName()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMAND_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getCommandName() <em>Command Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCommandName()
	 * @generated
	 * @ordered
	 */
	protected String commandName = COMMAND_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;
	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<MCommandParameter> parameters;
	/**
	 * The default value of the '{@link #getContext() <em>Context</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected static final IEclipseContext CONTEXT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getContext() <em>Context</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected IEclipseContext context = CONTEXT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getVariables() <em>Variables</em>}'
	 * attribute list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
	protected EList<String> variables;
	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}'
	 * map. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> properties;
	/**
	 * The default value of the '{@link #getContributionURI()
	 * <em>Contribution URI</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getContributionURI()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTRIBUTION_URI_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getContributionURI()
	 * <em>Contribution URI</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getContributionURI()
	 * @generated
	 * @ordered
	 */
	protected String contributionURI = CONTRIBUTION_URI_EDEFAULT;
	/**
	 * The default value of the '{@link #getObject() <em>Object</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object OBJECT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getObject() <em>Object</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
	protected Object object = OBJECT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getPersistedState()
	 * <em>Persisted State</em>}' map. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getPersistedState()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> persistedState;
	/**
	 * The default value of the '{@link #getWidget() <em>Widget</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWidget()
	 * @generated
	 * @ordered
	 */
	protected static final Object WIDGET_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getWidget() <em>Widget</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWidget()
	 * @generated
	 * @ordered
	 */
	protected Object widget = WIDGET_EDEFAULT;
	/**
	 * The default value of the '{@link #getRenderer() <em>Renderer</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRenderer()
	 * @generated
	 * @ordered
	 */
	protected static final Object RENDERER_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getRenderer() <em>Renderer</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRenderer()
	 * @generated
	 * @ordered
	 */
	protected Object renderer = RENDERER_EDEFAULT;
	/**
	 * The default value of the '{@link #isToBeRendered()
	 * <em>To Be Rendered</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isToBeRendered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TO_BE_RENDERED_EDEFAULT = true;
	/**
	 * The cached value of the '{@link #isToBeRendered()
	 * <em>To Be Rendered</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isToBeRendered()
	 * @generated
	 * @ordered
	 */
	protected boolean toBeRendered = TO_BE_RENDERED_EDEFAULT;
	/**
	 * The default value of the '{@link #isOnTop() <em>On Top</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isOnTop()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ON_TOP_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isOnTop() <em>On Top</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isOnTop()
	 * @generated
	 * @ordered
	 */
	protected boolean onTop = ON_TOP_EDEFAULT;
	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;
	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;
	/**
	 * The default value of the '{@link #getContainerData()
	 * <em>Container Data</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getContainerData()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTAINER_DATA_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getContainerData()
	 * <em>Container Data</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getContainerData()
	 * @generated
	 * @ordered
	 */
	protected String containerData = CONTAINER_DATA_EDEFAULT;
	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<MUIElement> children;
	/**
	 * The cached value of the '{@link #getSelectedElement()
	 * <em>Selected Element</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getSelectedElement()
	 * @generated
	 * @ordered
	 */
	protected MUIElement selectedElement;
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;
	/**
	 * The default value of the '{@link #getInputURI() <em>Input URI</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInputURI()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_URI_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getInputURI() <em>Input URI</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getInputURI()
	 * @generated
	 * @ordered
	 */
	protected String inputURI = INPUT_URI_EDEFAULT;
	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;
	/**
	 * The default value of the '{@link #getIconURI() <em>Icon URI</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getIconURI()
	 * @generated
	 * @ordered
	 */
	protected static final String ICON_URI_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getIconURI() <em>Icon URI</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getIconURI()
	 * @generated
	 * @ordered
	 */
	protected String iconURI = ICON_URI_EDEFAULT;
	/**
	 * The default value of the '{@link #getTooltip() <em>Tooltip</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTooltip()
	 * @generated
	 * @ordered
	 */
	protected static final String TOOLTIP_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getTooltip() <em>Tooltip</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTooltip()
	 * @generated
	 * @ordered
	 */
	protected String tooltip = TOOLTIP_EDEFAULT;
	/**
	 * The default value of the '{@link #isDirty() <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isDirty()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DIRTY_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isDirty() <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isDirty()
	 * @generated
	 * @ordered
	 */
	protected boolean dirty = DIRTY_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TestHarnessImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MTestPackage.Literals.TEST_HARNESS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getCommandName() {
		return commandName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCommandName(String newCommandName) {
		String oldCommandName = commandName;
		commandName = newCommandName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__COMMAND_NAME, oldCommandName,
					commandName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__DESCRIPTION, oldDescription,
					description));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List<MCommandParameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList<MCommandParameter>(
					MCommandParameter.class, this,
					MTestPackage.TEST_HARNESS__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IEclipseContext getContext() {
		return context;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setContext(IEclipseContext newContext) {
		IEclipseContext oldContext = context;
		context = newContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__CONTEXT, oldContext, context));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List<String> getVariables() {
		if (variables == null) {
			variables = new EDataTypeUniqueEList<String>(String.class, this,
					MTestPackage.TEST_HARNESS__VARIABLES);
		}
		return variables;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = new EcoreEMap<String, String>(
					ApplicationPackageImpl.Literals.STRING_TO_STRING_MAP,
					StringToStringMapImpl.class, this,
					MTestPackage.TEST_HARNESS__PROPERTIES);
		}
		return properties.map();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getContributionURI() {
		return contributionURI;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setContributionURI(String newContributionURI) {
		String oldContributionURI = contributionURI;
		contributionURI = newContributionURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__CONTRIBUTION_URI,
					oldContributionURI, contributionURI));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setObject(Object newObject) {
		Object oldObject = object;
		object = newObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__OBJECT, oldObject, object));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Map<String, String> getPersistedState() {
		if (persistedState == null) {
			persistedState = new EcoreEMap<String, String>(
					ApplicationPackageImpl.Literals.STRING_TO_STRING_MAP,
					StringToStringMapImpl.class, this,
					MTestPackage.TEST_HARNESS__PERSISTED_STATE);
		}
		return persistedState.map();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getWidget() {
		return widget;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setWidget(Object newWidget) {
		Object oldWidget = widget;
		widget = newWidget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__WIDGET, oldWidget, widget));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getRenderer() {
		return renderer;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRenderer(Object newRenderer) {
		Object oldRenderer = renderer;
		renderer = newRenderer;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__RENDERER, oldRenderer, renderer));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isToBeRendered() {
		return toBeRendered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setToBeRendered(boolean newToBeRendered) {
		boolean oldToBeRendered = toBeRendered;
		toBeRendered = newToBeRendered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__TO_BE_RENDERED, oldToBeRendered,
					toBeRendered));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isOnTop() {
		return onTop;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOnTop(boolean newOnTop) {
		boolean oldOnTop = onTop;
		onTop = newOnTop;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__ON_TOP, oldOnTop, onTop));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__VISIBLE, oldVisible, visible));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public MElementContainer<MUIElement> getParent() {
		if (eContainerFeatureID() != MTestPackage.TEST_HARNESS__PARENT)
			return null;
		return (MElementContainer<MUIElement>) eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetParent(
			MElementContainer<MUIElement> newParent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newParent,
				MTestPackage.TEST_HARNESS__PARENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setParent(MElementContainer<MUIElement> newParent) {
		if (newParent != eInternalContainer()
				|| (eContainerFeatureID() != MTestPackage.TEST_HARNESS__PARENT && newParent != null)) {
			if (EcoreUtil.isAncestor(this, (EObject) newParent))
				throw new IllegalArgumentException(
						"Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParent != null)
				msgs = ((InternalEObject) newParent).eInverseAdd(this,
						UiPackageImpl.ELEMENT_CONTAINER__CHILDREN,
						MElementContainer.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getContainerData() {
		return containerData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setContainerData(String newContainerData) {
		String oldContainerData = containerData;
		containerData = newContainerData;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__CONTAINER_DATA,
					oldContainerData, containerData));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List<MUIElement> getChildren() {
		if (children == null) {
			children = new EObjectContainmentWithInverseEList<MUIElement>(
					MUIElement.class, this,
					MTestPackage.TEST_HARNESS__CHILDREN,
					UiPackageImpl.UI_ELEMENT__PARENT);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MUIElement getSelectedElement() {
		if (selectedElement != null && ((EObject) selectedElement).eIsProxy()) {
			InternalEObject oldSelectedElement = (InternalEObject) selectedElement;
			selectedElement = (MUIElement) eResolveProxy(oldSelectedElement);
			if (selectedElement != oldSelectedElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							MTestPackage.TEST_HARNESS__SELECTED_ELEMENT,
							oldSelectedElement, selectedElement));
			}
		}
		return selectedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MUIElement basicGetSelectedElement() {
		return selectedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelectedElement(MUIElement newSelectedElement) {
		MUIElement oldSelectedElement = selectedElement;
		selectedElement = newSelectedElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__SELECTED_ELEMENT,
					oldSelectedElement, selectedElement));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getInputURI() {
		return inputURI;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setInputURI(String newInputURI) {
		String oldInputURI = inputURI;
		inputURI = newInputURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__INPUT_URI, oldInputURI, inputURI));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getIconURI() {
		return iconURI;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIconURI(String newIconURI) {
		String oldIconURI = iconURI;
		iconURI = newIconURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__ICON_URI, oldIconURI, iconURI));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTooltip(String newTooltip) {
		String oldTooltip = tooltip;
		tooltip = newTooltip;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__TOOLTIP, oldTooltip, tooltip));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDirty(boolean newDirty) {
		boolean oldDirty = dirty;
		dirty = newDirty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MTestPackage.TEST_HARNESS__DIRTY, oldDirty, dirty));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MTestPackage.TEST_HARNESS__PARENT:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetParent((MElementContainer<MUIElement>) otherEnd,
					msgs);
		case MTestPackage.TEST_HARNESS__CHILDREN:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) getChildren())
					.basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case MTestPackage.TEST_HARNESS__PARAMETERS:
			return ((InternalEList<?>) getParameters()).basicRemove(otherEnd,
					msgs);
		case MTestPackage.TEST_HARNESS__PROPERTIES:
			return ((InternalEList<?>) ((EMap.InternalMapView<String, String>) getProperties())
					.eMap()).basicRemove(otherEnd, msgs);
		case MTestPackage.TEST_HARNESS__PERSISTED_STATE:
			return ((InternalEList<?>) ((EMap.InternalMapView<String, String>) getPersistedState())
					.eMap()).basicRemove(otherEnd, msgs);
		case MTestPackage.TEST_HARNESS__PARENT:
			return basicSetParent(null, msgs);
		case MTestPackage.TEST_HARNESS__CHILDREN:
			return ((InternalEList<?>) getChildren()).basicRemove(otherEnd,
					msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(
			NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case MTestPackage.TEST_HARNESS__PARENT:
			return eInternalContainer().eInverseRemove(this,
					UiPackageImpl.ELEMENT_CONTAINER__CHILDREN,
					MElementContainer.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MTestPackage.TEST_HARNESS__COMMAND_NAME:
			return getCommandName();
		case MTestPackage.TEST_HARNESS__DESCRIPTION:
			return getDescription();
		case MTestPackage.TEST_HARNESS__PARAMETERS:
			return getParameters();
		case MTestPackage.TEST_HARNESS__CONTEXT:
			return getContext();
		case MTestPackage.TEST_HARNESS__VARIABLES:
			return getVariables();
		case MTestPackage.TEST_HARNESS__PROPERTIES:
			if (coreType)
				return ((EMap.InternalMapView<String, String>) getProperties())
						.eMap();
			else
				return getProperties();
		case MTestPackage.TEST_HARNESS__CONTRIBUTION_URI:
			return getContributionURI();
		case MTestPackage.TEST_HARNESS__OBJECT:
			return getObject();
		case MTestPackage.TEST_HARNESS__PERSISTED_STATE:
			if (coreType)
				return ((EMap.InternalMapView<String, String>) getPersistedState())
						.eMap();
			else
				return getPersistedState();
		case MTestPackage.TEST_HARNESS__WIDGET:
			return getWidget();
		case MTestPackage.TEST_HARNESS__RENDERER:
			return getRenderer();
		case MTestPackage.TEST_HARNESS__TO_BE_RENDERED:
			return isToBeRendered();
		case MTestPackage.TEST_HARNESS__ON_TOP:
			return isOnTop();
		case MTestPackage.TEST_HARNESS__VISIBLE:
			return isVisible();
		case MTestPackage.TEST_HARNESS__PARENT:
			return getParent();
		case MTestPackage.TEST_HARNESS__CONTAINER_DATA:
			return getContainerData();
		case MTestPackage.TEST_HARNESS__CHILDREN:
			return getChildren();
		case MTestPackage.TEST_HARNESS__SELECTED_ELEMENT:
			if (resolve)
				return getSelectedElement();
			return basicGetSelectedElement();
		case MTestPackage.TEST_HARNESS__NAME:
			return getName();
		case MTestPackage.TEST_HARNESS__VALUE:
			return getValue();
		case MTestPackage.TEST_HARNESS__INPUT_URI:
			return getInputURI();
		case MTestPackage.TEST_HARNESS__LABEL:
			return getLabel();
		case MTestPackage.TEST_HARNESS__ICON_URI:
			return getIconURI();
		case MTestPackage.TEST_HARNESS__TOOLTIP:
			return getTooltip();
		case MTestPackage.TEST_HARNESS__DIRTY:
			return isDirty();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MTestPackage.TEST_HARNESS__COMMAND_NAME:
			setCommandName((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__DESCRIPTION:
			setDescription((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__PARAMETERS:
			getParameters().clear();
			getParameters().addAll(
					(Collection<? extends MCommandParameter>) newValue);
			return;
		case MTestPackage.TEST_HARNESS__CONTEXT:
			setContext((IEclipseContext) newValue);
			return;
		case MTestPackage.TEST_HARNESS__VARIABLES:
			getVariables().clear();
			getVariables().addAll((Collection<? extends String>) newValue);
			return;
		case MTestPackage.TEST_HARNESS__PROPERTIES:
			((EStructuralFeature.Setting) ((EMap.InternalMapView<String, String>) getProperties())
					.eMap()).set(newValue);
			return;
		case MTestPackage.TEST_HARNESS__CONTRIBUTION_URI:
			setContributionURI((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__OBJECT:
			setObject(newValue);
			return;
		case MTestPackage.TEST_HARNESS__PERSISTED_STATE:
			((EStructuralFeature.Setting) ((EMap.InternalMapView<String, String>) getPersistedState())
					.eMap()).set(newValue);
			return;
		case MTestPackage.TEST_HARNESS__WIDGET:
			setWidget(newValue);
			return;
		case MTestPackage.TEST_HARNESS__RENDERER:
			setRenderer(newValue);
			return;
		case MTestPackage.TEST_HARNESS__TO_BE_RENDERED:
			setToBeRendered((Boolean) newValue);
			return;
		case MTestPackage.TEST_HARNESS__ON_TOP:
			setOnTop((Boolean) newValue);
			return;
		case MTestPackage.TEST_HARNESS__VISIBLE:
			setVisible((Boolean) newValue);
			return;
		case MTestPackage.TEST_HARNESS__PARENT:
			setParent((MElementContainer<MUIElement>) newValue);
			return;
		case MTestPackage.TEST_HARNESS__CONTAINER_DATA:
			setContainerData((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__CHILDREN:
			getChildren().clear();
			getChildren().addAll((Collection<? extends MUIElement>) newValue);
			return;
		case MTestPackage.TEST_HARNESS__SELECTED_ELEMENT:
			setSelectedElement((MUIElement) newValue);
			return;
		case MTestPackage.TEST_HARNESS__NAME:
			setName((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__VALUE:
			setValue((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__INPUT_URI:
			setInputURI((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__LABEL:
			setLabel((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__ICON_URI:
			setIconURI((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__TOOLTIP:
			setTooltip((String) newValue);
			return;
		case MTestPackage.TEST_HARNESS__DIRTY:
			setDirty((Boolean) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case MTestPackage.TEST_HARNESS__COMMAND_NAME:
			setCommandName(COMMAND_NAME_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__DESCRIPTION:
			setDescription(DESCRIPTION_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__PARAMETERS:
			getParameters().clear();
			return;
		case MTestPackage.TEST_HARNESS__CONTEXT:
			setContext(CONTEXT_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__VARIABLES:
			getVariables().clear();
			return;
		case MTestPackage.TEST_HARNESS__PROPERTIES:
			getProperties().clear();
			return;
		case MTestPackage.TEST_HARNESS__CONTRIBUTION_URI:
			setContributionURI(CONTRIBUTION_URI_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__OBJECT:
			setObject(OBJECT_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__PERSISTED_STATE:
			getPersistedState().clear();
			return;
		case MTestPackage.TEST_HARNESS__WIDGET:
			setWidget(WIDGET_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__RENDERER:
			setRenderer(RENDERER_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__TO_BE_RENDERED:
			setToBeRendered(TO_BE_RENDERED_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__ON_TOP:
			setOnTop(ON_TOP_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__VISIBLE:
			setVisible(VISIBLE_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__PARENT:
			setParent((MElementContainer<MUIElement>) null);
			return;
		case MTestPackage.TEST_HARNESS__CONTAINER_DATA:
			setContainerData(CONTAINER_DATA_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__CHILDREN:
			getChildren().clear();
			return;
		case MTestPackage.TEST_HARNESS__SELECTED_ELEMENT:
			setSelectedElement((MUIElement) null);
			return;
		case MTestPackage.TEST_HARNESS__NAME:
			setName(NAME_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__VALUE:
			setValue(VALUE_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__INPUT_URI:
			setInputURI(INPUT_URI_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__LABEL:
			setLabel(LABEL_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__ICON_URI:
			setIconURI(ICON_URI_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__TOOLTIP:
			setTooltip(TOOLTIP_EDEFAULT);
			return;
		case MTestPackage.TEST_HARNESS__DIRTY:
			setDirty(DIRTY_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case MTestPackage.TEST_HARNESS__COMMAND_NAME:
			return COMMAND_NAME_EDEFAULT == null ? commandName != null
					: !COMMAND_NAME_EDEFAULT.equals(commandName);
		case MTestPackage.TEST_HARNESS__DESCRIPTION:
			return DESCRIPTION_EDEFAULT == null ? description != null
					: !DESCRIPTION_EDEFAULT.equals(description);
		case MTestPackage.TEST_HARNESS__PARAMETERS:
			return parameters != null && !parameters.isEmpty();
		case MTestPackage.TEST_HARNESS__CONTEXT:
			return CONTEXT_EDEFAULT == null ? context != null
					: !CONTEXT_EDEFAULT.equals(context);
		case MTestPackage.TEST_HARNESS__VARIABLES:
			return variables != null && !variables.isEmpty();
		case MTestPackage.TEST_HARNESS__PROPERTIES:
			return properties != null && !properties.isEmpty();
		case MTestPackage.TEST_HARNESS__CONTRIBUTION_URI:
			return CONTRIBUTION_URI_EDEFAULT == null ? contributionURI != null
					: !CONTRIBUTION_URI_EDEFAULT.equals(contributionURI);
		case MTestPackage.TEST_HARNESS__OBJECT:
			return OBJECT_EDEFAULT == null ? object != null : !OBJECT_EDEFAULT
					.equals(object);
		case MTestPackage.TEST_HARNESS__PERSISTED_STATE:
			return persistedState != null && !persistedState.isEmpty();
		case MTestPackage.TEST_HARNESS__WIDGET:
			return WIDGET_EDEFAULT == null ? widget != null : !WIDGET_EDEFAULT
					.equals(widget);
		case MTestPackage.TEST_HARNESS__RENDERER:
			return RENDERER_EDEFAULT == null ? renderer != null
					: !RENDERER_EDEFAULT.equals(renderer);
		case MTestPackage.TEST_HARNESS__TO_BE_RENDERED:
			return toBeRendered != TO_BE_RENDERED_EDEFAULT;
		case MTestPackage.TEST_HARNESS__ON_TOP:
			return onTop != ON_TOP_EDEFAULT;
		case MTestPackage.TEST_HARNESS__VISIBLE:
			return visible != VISIBLE_EDEFAULT;
		case MTestPackage.TEST_HARNESS__PARENT:
			return getParent() != null;
		case MTestPackage.TEST_HARNESS__CONTAINER_DATA:
			return CONTAINER_DATA_EDEFAULT == null ? containerData != null
					: !CONTAINER_DATA_EDEFAULT.equals(containerData);
		case MTestPackage.TEST_HARNESS__CHILDREN:
			return children != null && !children.isEmpty();
		case MTestPackage.TEST_HARNESS__SELECTED_ELEMENT:
			return selectedElement != null;
		case MTestPackage.TEST_HARNESS__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case MTestPackage.TEST_HARNESS__VALUE:
			return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT
					.equals(value);
		case MTestPackage.TEST_HARNESS__INPUT_URI:
			return INPUT_URI_EDEFAULT == null ? inputURI != null
					: !INPUT_URI_EDEFAULT.equals(inputURI);
		case MTestPackage.TEST_HARNESS__LABEL:
			return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT
					.equals(label);
		case MTestPackage.TEST_HARNESS__ICON_URI:
			return ICON_URI_EDEFAULT == null ? iconURI != null
					: !ICON_URI_EDEFAULT.equals(iconURI);
		case MTestPackage.TEST_HARNESS__TOOLTIP:
			return TOOLTIP_EDEFAULT == null ? tooltip != null
					: !TOOLTIP_EDEFAULT.equals(tooltip);
		case MTestPackage.TEST_HARNESS__DIRTY:
			return dirty != DIRTY_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == MCommand.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__COMMAND_NAME:
				return CommandsPackageImpl.COMMAND__COMMAND_NAME;
			case MTestPackage.TEST_HARNESS__DESCRIPTION:
				return CommandsPackageImpl.COMMAND__DESCRIPTION;
			case MTestPackage.TEST_HARNESS__PARAMETERS:
				return CommandsPackageImpl.COMMAND__PARAMETERS;
			default:
				return -1;
			}
		}
		if (baseClass == MContext.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__CONTEXT:
				return UiPackageImpl.CONTEXT__CONTEXT;
			case MTestPackage.TEST_HARNESS__VARIABLES:
				return UiPackageImpl.CONTEXT__VARIABLES;
			case MTestPackage.TEST_HARNESS__PROPERTIES:
				return UiPackageImpl.CONTEXT__PROPERTIES;
			default:
				return -1;
			}
		}
		if (baseClass == MContribution.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__CONTRIBUTION_URI:
				return ApplicationPackageImpl.CONTRIBUTION__CONTRIBUTION_URI;
			case MTestPackage.TEST_HARNESS__OBJECT:
				return ApplicationPackageImpl.CONTRIBUTION__OBJECT;
			case MTestPackage.TEST_HARNESS__PERSISTED_STATE:
				return ApplicationPackageImpl.CONTRIBUTION__PERSISTED_STATE;
			default:
				return -1;
			}
		}
		if (baseClass == MUIElement.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__WIDGET:
				return UiPackageImpl.UI_ELEMENT__WIDGET;
			case MTestPackage.TEST_HARNESS__RENDERER:
				return UiPackageImpl.UI_ELEMENT__RENDERER;
			case MTestPackage.TEST_HARNESS__TO_BE_RENDERED:
				return UiPackageImpl.UI_ELEMENT__TO_BE_RENDERED;
			case MTestPackage.TEST_HARNESS__ON_TOP:
				return UiPackageImpl.UI_ELEMENT__ON_TOP;
			case MTestPackage.TEST_HARNESS__VISIBLE:
				return UiPackageImpl.UI_ELEMENT__VISIBLE;
			case MTestPackage.TEST_HARNESS__PARENT:
				return UiPackageImpl.UI_ELEMENT__PARENT;
			case MTestPackage.TEST_HARNESS__CONTAINER_DATA:
				return UiPackageImpl.UI_ELEMENT__CONTAINER_DATA;
			default:
				return -1;
			}
		}
		if (baseClass == MElementContainer.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__CHILDREN:
				return UiPackageImpl.ELEMENT_CONTAINER__CHILDREN;
			case MTestPackage.TEST_HARNESS__SELECTED_ELEMENT:
				return UiPackageImpl.ELEMENT_CONTAINER__SELECTED_ELEMENT;
			default:
				return -1;
			}
		}
		if (baseClass == MParameter.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__NAME:
				return CommandsPackageImpl.PARAMETER__NAME;
			case MTestPackage.TEST_HARNESS__VALUE:
				return CommandsPackageImpl.PARAMETER__VALUE;
			default:
				return -1;
			}
		}
		if (baseClass == MInput.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__INPUT_URI:
				return UiPackageImpl.INPUT__INPUT_URI;
			default:
				return -1;
			}
		}
		if (baseClass == MUILabel.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__LABEL:
				return UiPackageImpl.UI_LABEL__LABEL;
			case MTestPackage.TEST_HARNESS__ICON_URI:
				return UiPackageImpl.UI_LABEL__ICON_URI;
			case MTestPackage.TEST_HARNESS__TOOLTIP:
				return UiPackageImpl.UI_LABEL__TOOLTIP;
			default:
				return -1;
			}
		}
		if (baseClass == MDirtyable.class) {
			switch (derivedFeatureID) {
			case MTestPackage.TEST_HARNESS__DIRTY:
				return UiPackageImpl.DIRTYABLE__DIRTY;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == MCommand.class) {
			switch (baseFeatureID) {
			case CommandsPackageImpl.COMMAND__COMMAND_NAME:
				return MTestPackage.TEST_HARNESS__COMMAND_NAME;
			case CommandsPackageImpl.COMMAND__DESCRIPTION:
				return MTestPackage.TEST_HARNESS__DESCRIPTION;
			case CommandsPackageImpl.COMMAND__PARAMETERS:
				return MTestPackage.TEST_HARNESS__PARAMETERS;
			default:
				return -1;
			}
		}
		if (baseClass == MContext.class) {
			switch (baseFeatureID) {
			case UiPackageImpl.CONTEXT__CONTEXT:
				return MTestPackage.TEST_HARNESS__CONTEXT;
			case UiPackageImpl.CONTEXT__VARIABLES:
				return MTestPackage.TEST_HARNESS__VARIABLES;
			case UiPackageImpl.CONTEXT__PROPERTIES:
				return MTestPackage.TEST_HARNESS__PROPERTIES;
			default:
				return -1;
			}
		}
		if (baseClass == MContribution.class) {
			switch (baseFeatureID) {
			case ApplicationPackageImpl.CONTRIBUTION__CONTRIBUTION_URI:
				return MTestPackage.TEST_HARNESS__CONTRIBUTION_URI;
			case ApplicationPackageImpl.CONTRIBUTION__OBJECT:
				return MTestPackage.TEST_HARNESS__OBJECT;
			case ApplicationPackageImpl.CONTRIBUTION__PERSISTED_STATE:
				return MTestPackage.TEST_HARNESS__PERSISTED_STATE;
			default:
				return -1;
			}
		}
		if (baseClass == MUIElement.class) {
			switch (baseFeatureID) {
			case UiPackageImpl.UI_ELEMENT__WIDGET:
				return MTestPackage.TEST_HARNESS__WIDGET;
			case UiPackageImpl.UI_ELEMENT__RENDERER:
				return MTestPackage.TEST_HARNESS__RENDERER;
			case UiPackageImpl.UI_ELEMENT__TO_BE_RENDERED:
				return MTestPackage.TEST_HARNESS__TO_BE_RENDERED;
			case UiPackageImpl.UI_ELEMENT__ON_TOP:
				return MTestPackage.TEST_HARNESS__ON_TOP;
			case UiPackageImpl.UI_ELEMENT__VISIBLE:
				return MTestPackage.TEST_HARNESS__VISIBLE;
			case UiPackageImpl.UI_ELEMENT__PARENT:
				return MTestPackage.TEST_HARNESS__PARENT;
			case UiPackageImpl.UI_ELEMENT__CONTAINER_DATA:
				return MTestPackage.TEST_HARNESS__CONTAINER_DATA;
			default:
				return -1;
			}
		}
		if (baseClass == MElementContainer.class) {
			switch (baseFeatureID) {
			case UiPackageImpl.ELEMENT_CONTAINER__CHILDREN:
				return MTestPackage.TEST_HARNESS__CHILDREN;
			case UiPackageImpl.ELEMENT_CONTAINER__SELECTED_ELEMENT:
				return MTestPackage.TEST_HARNESS__SELECTED_ELEMENT;
			default:
				return -1;
			}
		}
		if (baseClass == MParameter.class) {
			switch (baseFeatureID) {
			case CommandsPackageImpl.PARAMETER__NAME:
				return MTestPackage.TEST_HARNESS__NAME;
			case CommandsPackageImpl.PARAMETER__VALUE:
				return MTestPackage.TEST_HARNESS__VALUE;
			default:
				return -1;
			}
		}
		if (baseClass == MInput.class) {
			switch (baseFeatureID) {
			case UiPackageImpl.INPUT__INPUT_URI:
				return MTestPackage.TEST_HARNESS__INPUT_URI;
			default:
				return -1;
			}
		}
		if (baseClass == MUILabel.class) {
			switch (baseFeatureID) {
			case UiPackageImpl.UI_LABEL__LABEL:
				return MTestPackage.TEST_HARNESS__LABEL;
			case UiPackageImpl.UI_LABEL__ICON_URI:
				return MTestPackage.TEST_HARNESS__ICON_URI;
			case UiPackageImpl.UI_LABEL__TOOLTIP:
				return MTestPackage.TEST_HARNESS__TOOLTIP;
			default:
				return -1;
			}
		}
		if (baseClass == MDirtyable.class) {
			switch (baseFeatureID) {
			case UiPackageImpl.DIRTYABLE__DIRTY:
				return MTestPackage.TEST_HARNESS__DIRTY;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (commandName: ");
		result.append(commandName);
		result.append(", description: ");
		result.append(description);
		result.append(", context: ");
		result.append(context);
		result.append(", variables: ");
		result.append(variables);
		result.append(", contributionURI: ");
		result.append(contributionURI);
		result.append(", object: ");
		result.append(object);
		result.append(", widget: ");
		result.append(widget);
		result.append(", renderer: ");
		result.append(renderer);
		result.append(", toBeRendered: ");
		result.append(toBeRendered);
		result.append(", onTop: ");
		result.append(onTop);
		result.append(", visible: ");
		result.append(visible);
		result.append(", containerData: ");
		result.append(containerData);
		result.append(", name: ");
		result.append(name);
		result.append(", value: ");
		result.append(value);
		result.append(", inputURI: ");
		result.append(inputURI);
		result.append(", label: ");
		result.append(label);
		result.append(", iconURI: ");
		result.append(iconURI);
		result.append(", tooltip: ");
		result.append(tooltip);
		result.append(", dirty: ");
		result.append(dirty);
		result.append(')');
		return result.toString();
	}

} // TestHarnessImpl
