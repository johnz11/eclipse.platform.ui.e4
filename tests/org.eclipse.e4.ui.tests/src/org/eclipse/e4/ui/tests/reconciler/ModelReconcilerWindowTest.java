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

package org.eclipse.e4.ui.tests.reconciler;

import java.util.Collection;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MMenu;
import org.eclipse.e4.ui.model.application.MMenuItem;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.workbench.modeling.ModelDelta;
import org.eclipse.e4.workbench.modeling.ModelReconciler;

public abstract class ModelReconcilerWindowTest extends ModelReconcilerTest {

	public void testWindow_X() {
		MApplication application = createApplication();

		MWindow window = createWindow(application);
		window.setX(100);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		window.setX(200);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(100, window.getX());

		applyAll(deltas);

		assertEquals(200, window.getX());
	}

	public void testWindow_Y() {
		MApplication application = createApplication();

		MWindow window = createWindow(application);
		window.setY(100);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		window.setY(200);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(100, window.getY());

		applyAll(deltas);

		assertEquals(200, window.getY());
	}

	public void testWindow_Width() {
		MApplication application = createApplication();

		MWindow window = createWindow(application);
		window.setWidth(100);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		window.setWidth(200);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(100, window.getWidth());

		applyAll(deltas);

		assertEquals(200, window.getWidth());
	}

	public void testWindow_Height() {
		MApplication application = createApplication();

		MWindow window = createWindow(application);
		window.setHeight(100);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		window.setHeight(200);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(100, window.getHeight());

		applyAll(deltas);

		assertEquals(200, window.getHeight());
	}

	public void testWindow_Menu_Set() {
		MApplication application = createApplication();

		MWindow window = createWindow(application);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		MMenu menu = MApplicationFactory.eINSTANCE.createMenu();
		window.setMainMenu(menu);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertNull(window.getMainMenu());

		applyAll(deltas);

		menu = window.getMainMenu();
		assertNotNull(menu);
	}

	public void testWindow_Menu_Unset() {
		MApplication application = createApplication();

		MWindow window = createWindow(application);

		MMenu menu = MApplicationFactory.eINSTANCE.createMenu();
		window.setMainMenu(menu);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		window.setMainMenu(null);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);
		menu = window.getMainMenu();

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(menu, window.getMainMenu());

		applyAll(deltas);

		assertNull(window.getMainMenu());
	}

	private void testWindow_Menu_Visible(boolean before, boolean after) {
		MApplication application = createApplication();

		MWindow window = createWindow(application);

		MMenu menu = MApplicationFactory.eINSTANCE.createMenu();
		menu.setToBeRendered(before);
		window.setMainMenu(menu);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		menu.setToBeRendered(after);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);
		menu = window.getMainMenu();

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(before, menu.isToBeRendered());

		applyAll(deltas);

		assertEquals(after, menu.isToBeRendered());
	}

	public void testWindow_Menu_Children_Add() {
		MApplication application = createApplication();
		MWindow window = createWindow(application);
		MMenu menu = MApplicationFactory.eINSTANCE.createMenu();
		window.setMainMenu(menu);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		MMenuItem menuItem = MApplicationFactory.eINSTANCE.createMenuItem();
		menuItem.setLabel("File");
		menu.getChildren().add(menuItem);

		Object state = reconciler.serialize();

		application = createApplication();
		window = application.getChildren().get(0);
		menu = window.getMainMenu();

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(1, application.getChildren().size());
		assertEquals(window, application.getChildren().get(0));
		assertEquals(menu, window.getMainMenu());

		assertEquals(0, menu.getChildren().size());

		applyAll(deltas);

		assertEquals(1, application.getChildren().size());
		assertEquals(window, application.getChildren().get(0));
		assertEquals(menu, window.getMainMenu());

		assertEquals(1, menu.getChildren().size());
		assertNotNull(menu.getChildren().get(0));
		assertEquals("File", menu.getChildren().get(0).getLabel());
	}

	public void testWindow_Menu_Children_Remove() {
		MApplication application = createApplication();
		MWindow window = createWindow(application);
		MMenu menu = MApplicationFactory.eINSTANCE.createMenu();
		window.setMainMenu(menu);

		MMenuItem menuItem = MApplicationFactory.eINSTANCE.createMenuItem();
		menuItem.setLabel("File");
		menu.getChildren().add(menuItem);

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		menu.getChildren().remove(0);

		Object state = reconciler.serialize();
		print(state);

		application = createApplication();
		window = application.getChildren().get(0);
		menu = window.getMainMenu();

		Collection<ModelDelta> deltas = constructDeltas(application, state);

		assertEquals(1, application.getChildren().size());
		assertEquals(window, application.getChildren().get(0));
		assertEquals(menu, window.getMainMenu());

		assertEquals(1, menu.getChildren().size());
		assertNotNull(menu.getChildren().get(0));
		assertEquals("File", menu.getChildren().get(0).getLabel());

		applyAll(deltas);

		assertEquals(1, application.getChildren().size());
		assertEquals(window, application.getChildren().get(0));
		assertEquals(menu, window.getMainMenu());

		assertEquals(0, menu.getChildren().size());
	}

	public void testWindow_Menu_Visible_TrueTrue() {
		testWindow_Menu_Visible(true, true);
	}

	public void testWindow_Menu_Visible_TrueFalse() {
		testWindow_Menu_Visible(true, false);
	}

	public void testWindow_Menu_Visible_FalseTrue() {
		testWindow_Menu_Visible(false, true);
	}

	public void testWindow_Menu_Visible_FalseFalse() {
		testWindow_Menu_Visible(false, false);
	}
}
