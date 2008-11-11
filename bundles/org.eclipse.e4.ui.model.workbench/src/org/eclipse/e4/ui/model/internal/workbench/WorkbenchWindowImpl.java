/**
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      IBM Corporation - initial API and implementation
 *
 * $Id$
 */
package org.eclipse.e4.ui.model.internal.workbench;

import java.util.Collection;

import org.eclipse.e4.ui.model.application.Part;

import org.eclipse.e4.ui.model.internal.application.WindowImpl;

import org.eclipse.e4.ui.model.workbench.Perspective;
import org.eclipse.e4.ui.model.workbench.WorkbenchPackage;
import org.eclipse.e4.ui.model.workbench.WorkbenchWindow;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Window</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.ui.model.internal.workbench.WorkbenchWindowImpl#getSharedParts <em>Shared Parts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WorkbenchWindowImpl extends WindowImpl<Perspective<?>> implements WorkbenchWindow {
	/**
	 * The cached value of the '{@link #getSharedParts() <em>Shared Parts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSharedParts()
	 * @generated
	 * @ordered
	 */
	protected EList<Part<?>> sharedParts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WorkbenchWindowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WorkbenchPackage.Literals.WORKBENCH_WINDOW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Part<?>> getSharedParts() {
		if (sharedParts == null) {
			sharedParts = new EObjectContainmentEList<Part<?>>(Part.class, this, WorkbenchPackage.WORKBENCH_WINDOW__SHARED_PARTS);
		}
		return sharedParts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WorkbenchPackage.WORKBENCH_WINDOW__SHARED_PARTS:
				return ((InternalEList<?>)getSharedParts()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WorkbenchPackage.WORKBENCH_WINDOW__SHARED_PARTS:
				return getSharedParts();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WorkbenchPackage.WORKBENCH_WINDOW__SHARED_PARTS:
				getSharedParts().clear();
				getSharedParts().addAll((Collection<? extends Part<?>>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case WorkbenchPackage.WORKBENCH_WINDOW__SHARED_PARTS:
				getSharedParts().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WorkbenchPackage.WORKBENCH_WINDOW__SHARED_PARTS:
				return sharedParts != null && !sharedParts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //WorkbenchWindowImpl
