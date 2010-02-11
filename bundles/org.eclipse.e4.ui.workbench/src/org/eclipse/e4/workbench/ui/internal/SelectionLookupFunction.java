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

package org.eclipse.e4.workbench.ui.internal;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextFunction;
import org.eclipse.e4.core.services.context.spi.IContextConstants;

/**
 *
 */
public class SelectionLookupFunction extends ContextFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.core.services.context.spi.ContextFunction#compute(org.eclipse.e4.core.services
	 * .context.IEclipseContext, java.lang.Object[])
	 */
	@Override
	public Object compute(IEclipseContext context, Object[] arguments) {
		IEclipseContext leafContext = context;
		IEclipseContext child = (IEclipseContext) leafContext
				.getLocal(IContextConstants.ACTIVE_CHILD);
		while (child != null) {
			leafContext = child;
			child = (IEclipseContext) leafContext.getLocal(IContextConstants.ACTIVE_CHILD);
		}
		return leafContext.get(SelectionServiceImpl.OUT_SELECTION);
	}

}
