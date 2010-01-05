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
package org.eclipse.e4.workbench.ui.renderers.swt;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.services.IContributionFactory;
import org.eclipse.e4.core.services.Logger;
import org.eclipse.e4.core.services.annotations.PostConstruct;
import org.eclipse.e4.core.services.annotations.PreDestroy;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.ContextInjectionFactory;
import org.eclipse.e4.ui.bindings.EBindingService;
import org.eclipse.e4.ui.bindings.TriggerSequence;
import org.eclipse.e4.ui.model.application.MContribution;
import org.eclipse.e4.ui.model.application.MHandledItem;
import org.eclipse.e4.ui.model.application.MParameter;
import org.eclipse.e4.ui.model.application.MToolItem;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.model.application.MUILabel;
import org.eclipse.e4.ui.services.events.IEventBroker;
import org.eclipse.e4.workbench.ui.UIEvents;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Create a contribute part.
 */
public class ToolItemRenderer extends SWTPartRenderer {

	@Inject
	Logger logger;
	@Inject
	IEventBroker eventBroker;
	private EventHandler itemUpdater;

	@PostConstruct
	public void init() {
		itemUpdater = new EventHandler() {
			public void handleEvent(Event event) {
				// Ensure that this event is for a MToolItem
				if (!(event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MToolItem))
					return;

				MToolItem itemModel = (MToolItem) event
						.getProperty(UIEvents.EventTags.ELEMENT);
				ToolItem toolItem = (ToolItem) itemModel.getWidget();

				// No widget == nothing to update
				if (toolItem == null)
					return;

				String attName = (String) event
						.getProperty(UIEvents.EventTags.ATTNAME);
				if (UIEvents.UILabel.LABEL.equals(attName)) {
					setItemText(itemModel, toolItem);
				} else if (UIEvents.UILabel.ICONURI.equals(attName)) {
					toolItem.setImage(getImage(itemModel));
				} else if (UIEvents.UILabel.TOOLTIP.equals(attName)) {
					toolItem.setToolTipText(itemModel.getTooltip());
					toolItem.setImage(getImage(itemModel));
				}
			}
		};

		eventBroker.subscribe(UIEvents.buildTopic(UIEvents.UILabel.TOPIC),
				itemUpdater);
	}

	@PreDestroy
	public void contextDisposed() {
		eventBroker.unsubscribe(itemUpdater);
	}

	private ParameterizedCommand generateParameterizedCommand(
			final MHandledItem item, final IEclipseContext lclContext) {
		ECommandService cmdService = (ECommandService) lclContext
				.get(ECommandService.class.getName());
		Map<String, Object> parameters = null;
		EList<MParameter> modelParms = item.getParameters();
		if (modelParms != null && !modelParms.isEmpty()) {
			parameters = new HashMap<String, Object>();
			for (MParameter mParm : modelParms) {
				parameters.put(mParm.getTag(), mParm.getValue());
			}
		}
		ParameterizedCommand cmd = cmdService.createCommand(item.getCommand()
				.getId(), parameters);
		item.setWbCommand(cmd);
		return cmd;
	}

	private void setItemText(MToolItem model, ToolItem item) {
		String text = model.getLabel();
		if (model instanceof MHandledItem) {
			MHandledItem handledItem = (MHandledItem) model;
			IEclipseContext context = getContext(model);
			EBindingService bs = (EBindingService) context
					.get(EBindingService.class.getName());
			ParameterizedCommand cmd = handledItem.getWbCommand();
			if (cmd == null) {
				cmd = generateParameterizedCommand(handledItem, context);
			}
			TriggerSequence sequence = bs.getBestSequenceFor(handledItem
					.getWbCommand());
			if (sequence != null) {
				text = text + '\t' + sequence.format();
			}
			item.setText(text);
		} else {
			if (text == null) {
				text = ""; //$NON-NLS-1$
			}
			item.setText(text);
		}
	}

	public Object createWidget(final MUIElement element, Object parent) {
		if (!(element instanceof MToolItem) || !(parent instanceof ToolBar))
			return null;

		ToolBar tb = (ToolBar) parent;
		MToolItem itemModel = (MToolItem) element;

		if (itemModel.isSeparator()) {
			return new ToolItem(tb, SWT.SEPARATOR);
		}

		int flags = SWT.PUSH;
		if (itemModel.getChildren().size() > 0)
			flags = SWT.DROP_DOWN;
		ToolItem newItem = new ToolItem((ToolBar) parent, flags);
		if (itemModel.getLabel() != null)
			newItem.setText(itemModel.getLabel());

		if (itemModel.getTooltip() != null)
			newItem.setToolTipText(itemModel.getTooltip());

		newItem.setImage(getImage((MUILabel) element));

		newItem.setEnabled(itemModel.isEnabled());
		newItem.setSelection(itemModel.isSelected());

		return newItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.workbench.ui.renderers.swt.SWTPartRenderer#hookControllerLogic
	 * (org.eclipse.e4.ui.model.application.MUIElement)
	 */
	@Override
	public void hookControllerLogic(MUIElement me) {
		if (me instanceof MContribution
				&& ((MContribution) me).getURI() != null) {
			final MContribution contrib = (MContribution) me;
			final IEclipseContext lclContext = getContext(me);
			ToolItem ti = (ToolItem) me.getWidget();
			ti.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					if (contrib.getObject() == null) {
						IContributionFactory cf = (IContributionFactory) lclContext
								.get(IContributionFactory.class.getName());
						contrib.setObject(cf.create(contrib.getURI(),
								lclContext));
					}
					try {
						ContextInjectionFactory.invoke(contrib.getObject(),
								"execute", lclContext); //$NON-NLS-1$
					} catch (InvocationTargetException e1) {
						if (logger != null)
							logger.error(e1);
					} catch (CoreException e1) {
						if (logger != null)
							logger.error(e1);
					}
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		} else if (me instanceof MHandledItem) {
			final MHandledItem item = (MHandledItem) me;
			final IEclipseContext lclContext = getContext(me);
			ToolItem ti = (ToolItem) me.getWidget();
			ti.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					EHandlerService service = (EHandlerService) lclContext
							.get(EHandlerService.class.getName());
					ParameterizedCommand cmd = item.getWbCommand();
					if (cmd == null) {
						cmd = generateParameterizedCommand(item, lclContext);
					}
					service.executeHandler(cmd);
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
	}
}
