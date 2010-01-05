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

package org.eclipse.e4.ui.tests.workbench;

import junit.framework.TestCase;

import org.eclipse.e4.core.services.IDisposable;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MToolBar;
import org.eclipse.e4.ui.model.application.MToolItem;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.model.application.MWindowTrim;
import org.eclipse.e4.ui.workbench.swt.internal.E4Application;
import org.eclipse.e4.ui.workbench.swt.internal.PartRenderingEngine;
import org.eclipse.e4.workbench.ui.internal.E4Workbench;
import org.eclipse.e4.workbench.ui.renderers.swt.TrimmedPartLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;

public class MToolItemTest extends TestCase {
	protected IEclipseContext appContext;
	protected E4Workbench wb;

	@Override
	protected void setUp() throws Exception {
		appContext = E4Application.createDefaultContext();
		appContext.set(E4Workbench.PRESENTATION_URI_ARG,
				PartRenderingEngine.engineURI);
	}

	@Override
	protected void tearDown() throws Exception {
		if (wb != null) {
			wb.close();
		}

		if (appContext instanceof IDisposable) {
			((IDisposable) appContext).dispose();
		}
	}

	protected Control[] getPresentationControls(Shell shell) {
		TrimmedPartLayout tpl = (TrimmedPartLayout) shell.getLayout();
		return tpl.clientArea.getChildren();
	}

	private void testMToolItem_Text(String before, String beforeExpected,
			String after, String afterExpected) {
		MWindow window = MApplicationFactory.eINSTANCE.createWindow();
		MWindowTrim windowTrim = MApplicationFactory.eINSTANCE
				.createWindowTrim();
		MToolBar toolBar = MApplicationFactory.eINSTANCE.createToolBar();
		MToolItem toolItem = MApplicationFactory.eINSTANCE.createToolItem();

		toolItem.setLabel(before);

		window.getChildren().add(windowTrim);
		toolBar.getChildren().add(toolItem);
		windowTrim.getChildren().add(toolBar);

		wb = new E4Workbench(window, appContext);
		wb.createAndRunUI(window);

		Object widget = toolItem.getWidget();
		assertNotNull(widget);
		assertTrue(widget instanceof ToolItem);

		ToolItem toolItemWidget = (ToolItem) widget;

		assertEquals(beforeExpected, toolItemWidget.getText());

		toolItem.setLabel(after);

		assertEquals(afterExpected, toolItemWidget.getText());
	}

	public void testMToolItem_Text_NullNull() {
		testMToolItem_Text(null, "", null, "");
	}

	public void testMToolItem_Text_NullEmpty() {
		testMToolItem_Text(null, "", "", "");
	}

	public void testMToolItem_Text_NullString() {
		testMToolItem_Text(null, "", "label", "label");
	}

	public void testMToolItem_Text_EmptyNull() {
		testMToolItem_Text("", "", null, "");
	}

	public void testMToolItem_Text_EmptyEmpty() {
		testMToolItem_Text("", "", "", "");
	}

	public void testMToolItem_Text_EmptyString() {
		testMToolItem_Text("", "", "label", "label");
	}

	public void testMToolItem_Text_StringNull() {
		testMToolItem_Text("label", "label", null, "");
	}

	public void testMToolItem_Text_StringEmpty() {
		testMToolItem_Text("label", "label", "", "");
	}

	public void testMToolItem_Text_StringStringUnchanged() {
		testMToolItem_Text("label", "label", "label", "label");
	}

	public void testMToolItem_Text_StringStringChanged() {
		testMToolItem_Text("label", "label", "label2", "label2");
	}

	private void testMToolItem_Tooltip(String before, String beforeExpected,
			String after, String afterExpected) {
		MWindow window = MApplicationFactory.eINSTANCE.createWindow();
		MWindowTrim windowTrim = MApplicationFactory.eINSTANCE
				.createWindowTrim();
		MToolBar toolBar = MApplicationFactory.eINSTANCE.createToolBar();
		MToolItem toolItem = MApplicationFactory.eINSTANCE.createToolItem();

		toolItem.setTooltip(before);

		window.getChildren().add(windowTrim);
		toolBar.getChildren().add(toolItem);
		windowTrim.getChildren().add(toolBar);
		// windowTrim.getChildren().add(toolItem);

		wb = new E4Workbench(window, appContext);
		wb.createAndRunUI(window);

		Object widget = toolItem.getWidget();
		assertNotNull(widget);
		assertTrue(widget instanceof ToolItem);

		ToolItem toolItemWidget = (ToolItem) widget;

		assertEquals(beforeExpected, toolItemWidget.getToolTipText());

		toolItem.setTooltip(after);

		assertEquals(afterExpected, toolItemWidget.getToolTipText());
	}

	public void testMToolItem_Tooltip_NullNull() {
		testMToolItem_Tooltip(null, null, null, null);
	}

	public void testMToolItem_Tooltip_NullEmpty() {
		testMToolItem_Tooltip(null, null, "", "");
	}

	public void testMToolItem_Tooltip_NullString() {
		testMToolItem_Tooltip(null, null, "toolTip", "toolTip");
	}

	public void testMToolItem_Tooltip_EmptyNull() {
		testMToolItem_Tooltip("", "", null, null);
	}

	public void testMToolItem_Tooltip_EmptyEmpty() {
		testMToolItem_Tooltip("", "", "", "");
	}

	public void testMToolItem_Tooltip_EmptyString() {
		testMToolItem_Tooltip("", "", "toolTip", "toolTip");
	}

	public void testMToolItem_Tooltip_StringNull() {
		testMToolItem_Tooltip("toolTip", "toolTip", null, null);
	}

	public void testMToolItem_Tooltip_StringEmpty() {
		testMToolItem_Tooltip("toolTip", "toolTip", "", "");
	}

	public void testMToolItem_Tooltip_StringStringUnchanged() {
		testMToolItem_Tooltip("toolTip", "toolTip", "toolTip", "toolTip");
	}

	public void testMToolItem_Tooltip_StringStringChanged() {
		testMToolItem_Tooltip("toolTip", "toolTip", "toolTip2", "toolTip2");
	}
}
