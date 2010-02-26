/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.e4.examples.services.snippets;

import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.core.internal.services.EclipseAdapter;
import org.eclipse.e4.core.services.Adapter;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextInjectionFactory;

@SuppressWarnings("restriction")
public class SnippetSetup  {
	private static IEclipseContext context;
	
	public static void initializeServices() {
		
		// Create a context representing OSGi services
		IEclipseContext osgiContext = EclipseContextFactory.getServiceContext(SnippetActivator.bundleContext);
		Assert.isNotNull(osgiContext);

		// Now create a child context that represents Eclipse/e4 services
		context = EclipseContextFactory.create(osgiContext, null);
		Assert.isNotNull(context);
		
		// Set up the e4 services into the context
		context.set(Adapter.class.getName(), ContextInjectionFactory.inject(
				new EclipseAdapter(), context));
	}
	
	public static void setup(Object toBeSetup) {
		ContextInjectionFactory.inject(toBeSetup, context);
	}
	
	public static void dispose() {
		context = null;
	}
}