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
package org.eclipse.e4.ui.tests.application;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.e4.core.services.IDisposable;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MDirtyable;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MPartSashContainer;
import org.eclipse.e4.ui.model.application.MPartStack;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.workbench.swt.internal.E4Application;
import org.eclipse.e4.workbench.modeling.EModelService;

public class EModelServiceFindTest extends TestCase {

	private IEclipseContext applicationContext;

	MApplication app = null;

	@Override
	protected void setUp() throws Exception {
		applicationContext = E4Application.createDefaultContext();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		if (applicationContext instanceof IDisposable) {
			((IDisposable) applicationContext).dispose();
		}
	}

	private MApplication createApplication() {
		MApplication app = MApplicationFactory.eINSTANCE.createApplication();
		app.setContext(applicationContext);
		MWindow window = MApplicationFactory.eINSTANCE.createWindow();
		window.setId("singleValidId");
		app.getChildren().add(window);

		MPartSashContainer psc = MApplicationFactory.eINSTANCE
				.createPartSashContainer();
		psc.setId("twoValidIds");
		psc.getTags().add("oneValidTag");
		window.getChildren().add(psc);

		MPartStack stack = MApplicationFactory.eINSTANCE.createPartStack();
		stack.getTags().add("twoValidTags");
		psc.getChildren().add(stack);

		MPart part1 = MApplicationFactory.eINSTANCE.createPart();
		part1.setId("twoValidIds");
		stack.getChildren().add(part1);

		MPart part2 = MApplicationFactory.eINSTANCE.createPart();
		part2.getTags().add("twoValidTags");
		part2.getTags().add("secondTag");
		stack.getChildren().add(part2);

		MPart part3 = MApplicationFactory.eINSTANCE.createPart();
		psc.getChildren().add(part3);

		return app;
	}

	public void testFindElementsIdOnly() {
		MApplication application = createApplication();

		EModelService modelService = (EModelService) application.getContext()
				.get(EModelService.class.getName());
		assertNotNull(modelService);

		List<MUIElement> elements1 = modelService.findElements(application,
				"singleValidId", null, null);
		assertEquals(elements1.size(), 1);

		List<MUIElement> elements2 = modelService.findElements(application,
				"twoValidIds", null, null);
		assertEquals(elements2.size(), 2);

		List<MUIElement> elements3 = modelService.findElements(application,
				"invalidId", null, null);
		assertEquals(elements3.size(), 0);
	}

	public void testFindElementsTypeOnly() {
		MApplication application = createApplication();

		EModelService modelService = (EModelService) application.getContext()
				.get(EModelService.class.getName());
		assertNotNull(modelService);

		List<MPart> parts = modelService.findElements(application, null,
				MPart.class, null);
		assertEquals(parts.size(), 3);

		List<MPartStack> stacks = modelService.findElements(application, null,
				MPartStack.class, null);
		assertEquals(stacks.size(), 1);

		List<MDirtyable> dirtyableElements = modelService.findElements(
				application, null, MDirtyable.class, null);
		assertEquals(dirtyableElements.size(), 3);

		// Should find all the elements
		List<MUIElement> uiElements = modelService.findElements(application,
				null, null, null);
		assertEquals(uiElements.size(), 7);

		// Should match 0 since String is not an MUIElement
		List<String> strings = modelService.findElements(application, null,
				String.class, null);
		assertEquals(strings.size(), 0);
	}

	public void testFindElementsTagsOnly() {
		MApplication application = createApplication();

		EModelService modelService = (EModelService) application.getContext()
				.get(EModelService.class.getName());
		assertNotNull(modelService);

		List<String> tags = new ArrayList<String>();
		tags.add("oneValidTag");

		List<MUIElement> oneTags = modelService.findElements(application, null,
				null, tags);
		assertEquals(oneTags.size(), 1);

		tags.clear();
		tags.add("twoValidTags");
		List<MUIElement> twoTags = modelService.findElements(application, null,
				null, tags);
		assertEquals(twoTags.size(), 2);

		tags.clear();
		tags.add("invalidTag");
		List<MUIElement> invalidTags = modelService.findElements(application,
				null, null, tags);
		assertEquals(invalidTags.size(), 0);

		tags.clear();
		tags.add("twoValidTags");
		tags.add("secondTag");
		List<MUIElement> combinedTags = modelService.findElements(application,
				null, null, tags);
		assertEquals(combinedTags.size(), 1);

		tags.clear();
		tags.add("oneValidTag");
		tags.add("secondTag");
		List<MUIElement> unmatchedTags = modelService.findElements(application,
				null, null, tags);
		assertEquals(unmatchedTags.size(), 0);
	}

	public void testFindElementsCombinations() {
		MApplication application = createApplication();

		EModelService modelService = (EModelService) application.getContext()
				.get(EModelService.class.getName());
		assertNotNull(modelService);

		List<String> tags = new ArrayList<String>();
		tags.add("oneValidTag");

		List<MPartSashContainer> idAndType = modelService.findElements(
				application, "twoValidIds", MPartSashContainer.class, tags);
		assertEquals(idAndType.size(), 1);

		List<MPartSashContainer> typeAndTag = modelService.findElements(
				application, null, MPartSashContainer.class, tags);
		assertEquals(typeAndTag.size(), 1);

		List<MUIElement> idAndTag = modelService.findElements(application,
				"twoValidIds", null, tags);
		assertEquals(idAndTag.size(), 1);

		List<MPartSashContainer> idAndTypeAndTags = modelService.findElements(
				application, "twoValidIds", MPartSashContainer.class, null);
		assertEquals(idAndTypeAndTags.size(), 1);

		List<MPartSashContainer> badIdAndTypeAndTags = modelService
				.findElements(application, "invalidId",
						MPartSashContainer.class, null);
		assertEquals(badIdAndTypeAndTags.size(), 0);
	}
}
