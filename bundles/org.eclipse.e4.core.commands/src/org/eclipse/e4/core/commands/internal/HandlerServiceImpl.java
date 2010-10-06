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

package org.eclipse.e4.core.commands.internal;

import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

/**
 *
 */
public class HandlerServiceImpl implements EHandlerService {
	public final static String H_ID = "handler::"; //$NON-NLS-1$
	public final static String PARM_MAP = "parmMap::"; //$NON-NLS-1$
	final static String LOOKUP_HANDLER = "handler"; //$NON-NLS-1$

	public static Object lookUpHandler(IEclipseContext context, String commandId) {
		return context.getActiveLeaf().get(H_ID + commandId);
	}

	private IEclipseContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.core.commands.EHandlerService#activateHandler(java.lang.String,
	 * java.lang.Object)
	 */
	public void activateHandler(String commandId, Object handler) {
		String handlerId = H_ID + commandId;
		context.set(handlerId, handler);
	}

	/**
	 * @param command
	 */
	private void addParmsToContext(ParameterizedCommand command) {
		final Map parms = command.getParameterMap();
		Iterator i = parms.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			context.set((String) entry.getKey(), entry.getValue());
		}
		context.set(PARM_MAP, parms);
	}

	private void removeParmsFromContext(ParameterizedCommand command) {
		if (context == null) {
			return;
		}
		final Map<?, ?> parms = command.getParameterMap();
		Iterator<?> i = parms.entrySet().iterator();
		while (i.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) i.next();
			context.set((String) entry.getKey(), null);
		}
		context.set(PARM_MAP, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.core.commands.EHandlerService#canExecute(org.eclipse.core.commands.
	 * ParameterizedCommand)
	 */
	public boolean canExecute(ParameterizedCommand command) {
		String commandId = command.getId();
		Object handler = lookUpHandler(context, commandId);
		if (handler == null) {
			return false;
		}

		try {
			addParmsToContext(command);
			Boolean result = ((Boolean) ContextInjectionFactory.invoke(handler, CanExecute.class,
					context, Boolean.TRUE));
			return result.booleanValue();
		} finally {
			removeParmsFromContext(command);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.core.commands.EHandlerService#deactivateHandler(java.lang.String,
	 * java.lang.Object)
	 */
	public void deactivateHandler(String commandId, Object handler) {
		context.remove(H_ID + commandId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.e4.core.commands.EHandlerService#executeHandler(org.eclipse.core.commands.
	 * ParameterizedCommand)
	 */
	public Object executeHandler(ParameterizedCommand command) {
		String commandId = command.getId();
		Object handler = lookUpHandler(context, commandId);
		if (handler == null) {
			return null;
		}

		try {
			addParmsToContext(command);

			Object rc = ContextInjectionFactory.invoke(handler, CanExecute.class, context,
					Boolean.TRUE);
			if (Boolean.FALSE.equals(rc))
				return null;
			return ContextInjectionFactory.invoke(handler, Execute.class, context, null);
		} finally {
			removeParmsFromContext(command);
		}
	}

	@Inject
	public void setContext(IEclipseContext c) {
		context = c;
	}

	public IEclipseContext getContext() {
		return context;
	}
}
