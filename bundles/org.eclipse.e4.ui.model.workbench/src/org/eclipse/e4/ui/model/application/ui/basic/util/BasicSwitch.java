/**
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      IBM Corporation - initial API and implementation
 */
package org.eclipse.e4.ui.model.application.ui.basic.util;

import java.util.List;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.MContribution;
import org.eclipse.e4.ui.model.application.commands.MBindings;
import org.eclipse.e4.ui.model.application.commands.MHandlerContainer;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MGenericStack;
import org.eclipse.e4.ui.model.application.ui.MGenericTile;
import org.eclipse.e4.ui.model.application.ui.MGenericTrimContainer;
import org.eclipse.e4.ui.model.application.ui.MInput;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl
 * @generated
 */
public class BasicSwitch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static BasicPackageImpl modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BasicSwitch() {
		if (modelPackage == null) {
			modelPackage = BasicPackageImpl.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T1 doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T1 doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case BasicPackageImpl.PART: {
				MPart part = (MPart)theEObject;
				T1 result = casePart(part);
				if (result == null) result = casePartSashContainerElement(part);
				if (result == null) result = caseStackElement(part);
				if (result == null) result = caseContribution(part);
				if (result == null) result = caseContext(part);
				if (result == null) result = caseUILabel(part);
				if (result == null) result = caseHandlerContainer(part);
				if (result == null) result = caseDirtyable(part);
				if (result == null) result = caseBindings(part);
				if (result == null) result = caseWindowElement(part);
				if (result == null) result = caseUIElement(part);
				if (result == null) result = caseApplicationElement(part);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.INPUT_PART: {
				MInputPart inputPart = (MInputPart)theEObject;
				T1 result = caseInputPart(inputPart);
				if (result == null) result = casePart(inputPart);
				if (result == null) result = caseInput(inputPart);
				if (result == null) result = casePartSashContainerElement(inputPart);
				if (result == null) result = caseStackElement(inputPart);
				if (result == null) result = caseContribution(inputPart);
				if (result == null) result = caseContext(inputPart);
				if (result == null) result = caseUILabel(inputPart);
				if (result == null) result = caseHandlerContainer(inputPart);
				if (result == null) result = caseDirtyable(inputPart);
				if (result == null) result = caseBindings(inputPart);
				if (result == null) result = caseWindowElement(inputPart);
				if (result == null) result = caseUIElement(inputPart);
				if (result == null) result = caseApplicationElement(inputPart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.PART_STACK: {
				MPartStack partStack = (MPartStack)theEObject;
				T1 result = casePartStack(partStack);
				if (result == null) result = caseGenericStack(partStack);
				if (result == null) result = casePartSashContainerElement(partStack);
				if (result == null) result = caseWindowElement(partStack);
				if (result == null) result = caseElementContainer(partStack);
				if (result == null) result = caseUIElement(partStack);
				if (result == null) result = caseApplicationElement(partStack);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.PART_SASH_CONTAINER: {
				MPartSashContainer partSashContainer = (MPartSashContainer)theEObject;
				T1 result = casePartSashContainer(partSashContainer);
				if (result == null) result = caseGenericTile(partSashContainer);
				if (result == null) result = casePartSashContainerElement(partSashContainer);
				if (result == null) result = caseWindowElement(partSashContainer);
				if (result == null) result = caseElementContainer(partSashContainer);
				if (result == null) result = caseUIElement(partSashContainer);
				if (result == null) result = caseApplicationElement(partSashContainer);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.WINDOW: {
				MWindow window = (MWindow)theEObject;
				T1 result = caseWindow(window);
				if (result == null) result = caseElementContainer(window);
				if (result == null) result = caseUILabel(window);
				if (result == null) result = caseContext(window);
				if (result == null) result = caseHandlerContainer(window);
				if (result == null) result = caseBindings(window);
				if (result == null) result = caseUIElement(window);
				if (result == null) result = caseApplicationElement(window);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.TRIMMED_WINDOW: {
				MTrimmedWindow trimmedWindow = (MTrimmedWindow)theEObject;
				T1 result = caseTrimmedWindow(trimmedWindow);
				if (result == null) result = caseWindow(trimmedWindow);
				if (result == null) result = caseElementContainer(trimmedWindow);
				if (result == null) result = caseUILabel(trimmedWindow);
				if (result == null) result = caseContext(trimmedWindow);
				if (result == null) result = caseHandlerContainer(trimmedWindow);
				if (result == null) result = caseBindings(trimmedWindow);
				if (result == null) result = caseUIElement(trimmedWindow);
				if (result == null) result = caseApplicationElement(trimmedWindow);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.TRIM_ELEMENT: {
				MTrimElement trimElement = (MTrimElement)theEObject;
				T1 result = caseTrimElement(trimElement);
				if (result == null) result = caseUIElement(trimElement);
				if (result == null) result = caseApplicationElement(trimElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.PART_SASH_CONTAINER_ELEMENT: {
				MPartSashContainerElement partSashContainerElement = (MPartSashContainerElement)theEObject;
				T1 result = casePartSashContainerElement(partSashContainerElement);
				if (result == null) result = caseUIElement(partSashContainerElement);
				if (result == null) result = caseApplicationElement(partSashContainerElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.WINDOW_ELEMENT: {
				MWindowElement windowElement = (MWindowElement)theEObject;
				T1 result = caseWindowElement(windowElement);
				if (result == null) result = caseUIElement(windowElement);
				if (result == null) result = caseApplicationElement(windowElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.TRIM_BAR: {
				MTrimBar trimBar = (MTrimBar)theEObject;
				T1 result = caseTrimBar(trimBar);
				if (result == null) result = caseGenericTrimContainer(trimBar);
				if (result == null) result = caseElementContainer(trimBar);
				if (result == null) result = caseUIElement(trimBar);
				if (result == null) result = caseApplicationElement(trimBar);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BasicPackageImpl.STACK_ELEMENT: {
				MStackElement stackElement = (MStackElement)theEObject;
				T1 result = caseStackElement(stackElement);
				if (result == null) result = caseUIElement(stackElement);
				if (result == null) result = caseApplicationElement(stackElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Part</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Part</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePart(MPart object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Input Part</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Input Part</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInputPart(MInputPart object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Part Stack</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Part Stack</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePartStack(MPartStack object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Part Sash Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Part Sash Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePartSashContainer(MPartSashContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Window</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Window</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseWindow(MWindow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Trimmed Window</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Trimmed Window</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTrimmedWindow(MTrimmedWindow object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Trim Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Trim Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTrimElement(MTrimElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Part Sash Container Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Part Sash Container Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 casePartSashContainerElement(MPartSashContainerElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Window Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Window Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseWindowElement(MWindowElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Trim Bar</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Trim Bar</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseTrimBar(MTrimBar object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stack Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stack Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseStackElement(MStackElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseApplicationElement(MApplicationElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contribution</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contribution</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseContribution(MContribution object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Context</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Context</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseContext(MContext object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UI Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UI Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseUIElement(MUIElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>UI Label</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>UI Label</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseUILabel(MUILabel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Handler Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Handler Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseHandlerContainer(MHandlerContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dirtyable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dirtyable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDirtyable(MDirtyable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bindings</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bindings</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseBindings(MBindings object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Input</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Input</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseInput(MInput object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends MUIElement> T1 caseElementContainer(MElementContainer<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Stack</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Stack</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends MUIElement> T1 caseGenericStack(MGenericStack<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Tile</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Tile</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends MUIElement> T1 caseGenericTile(MGenericTile<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Trim Container</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Trim Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends MUIElement> T1 caseGenericTrimContainer(MGenericTrimContainer<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T1 defaultCase(EObject object) {
		return null;
	}

} //BasicSwitch
