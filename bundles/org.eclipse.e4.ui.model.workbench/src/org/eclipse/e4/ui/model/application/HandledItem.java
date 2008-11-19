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
 * $Id: HandledItem.java,v 1.1 2008/11/11 18:19:12 bbokowski Exp $
 */
package org.eclipse.e4.ui.model.application;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Handled Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.e4.ui.model.application.HandledItem#getHandler <em>Handler</em>}</li>
 *   <li>{@link org.eclipse.e4.ui.model.application.HandledItem#getMenu <em>Menu</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.e4.ui.model.application.ApplicationPackage#getHandledItem()
 * @model
 * @generated
 */
public interface HandledItem extends Item {
	/**
	 * Returns the value of the '<em><b>Handler</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Handler</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Handler</em>' reference.
	 * @see #setHandler(Handler)
	 * @see org.eclipse.e4.ui.model.application.ApplicationPackage#getHandledItem_Handler()
	 * @model
	 * @generated
	 */
	Handler getHandler();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.ui.model.application.HandledItem#getHandler <em>Handler</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Handler</em>' reference.
	 * @see #getHandler()
	 * @generated
	 */
	void setHandler(Handler value);

	/**
	 * Returns the value of the '<em><b>Menu</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Menu</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Menu</em>' containment reference.
	 * @see #setMenu(Menu)
	 * @see org.eclipse.e4.ui.model.application.ApplicationPackage#getHandledItem_Menu()
	 * @model containment="true"
	 * @generated
	 */
	Menu getMenu();

	/**
	 * Sets the value of the '{@link org.eclipse.e4.ui.model.application.HandledItem#getMenu <em>Menu</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Menu</em>' containment reference.
	 * @see #getMenu()
	 * @generated
	 */
	void setMenu(Menu value);

} // HandledItem
