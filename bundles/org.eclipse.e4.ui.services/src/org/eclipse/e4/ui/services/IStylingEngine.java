/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.services;


public interface IStylingEngine {
	/**
	 * The service name for a styling engine. This name can be
	 * used to obtain instances of the service from a context or service registry.
	 */
	public static final String SERVICE_NAME = IStylingEngine.class.getName();

	public void setClassname(Object widget, String classname);
	public void setId(Object widget, String id);
	public void style(Object widget);
}
