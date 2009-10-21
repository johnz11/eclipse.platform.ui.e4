package org.eclipse.e4.core.commands.tests;

import junit.framework.TestCase;

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.e4.core.commands.ContextUtil;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.services.IDisposable;
import org.eclipse.e4.core.services.context.EclipseContextFactory;
import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.core.services.context.spi.IContextConstants;

public class DefineCommandsTest extends TestCase {

	private static final String TEST_ID2 = "test.id2";
	private static final String TEST_ID1 = "test.id1";
	private static final String TEST_CAT1 = "test.cat1";

	public void testCreateCommands() throws Exception {
		ECommandService cs = (ECommandService) workbenchContext
				.get(ECommandService.class.getName());
		assertNotNull(cs);
		assertNotNull(cs.defineCategory(TEST_CAT1, "CAT1", null));
		Category category = cs.getCategory(TEST_CAT1);
		assertNotNull("need category", category);
		assertNotNull("command1", cs.defineCommand(TEST_ID1, "ID1", null,
				category, null));
		assertNotNull("command2", cs.defineCommand(TEST_ID2, "ID2", null,
				category, null));

		Command cmd1 = cs.getCommand(TEST_ID1);
		assertNotNull("get command1", cmd1);
		assertEquals("ID1", cmd1.getName());
		assertNotNull("get command2", cs.getCommand(TEST_ID2));
		
		assertNotNull("parameterized command", cs.createCommand(TEST_ID1, null));
	}

	public void testCreateWithSecondContexts() throws Exception {
		IEclipseContext localContext = EclipseContextFactory.create(
				workbenchContext, null);
		ECommandService cs = (ECommandService) localContext
				.get(ECommandService.class.getName());
		assertNotNull(cs);
		assertNotNull(cs.defineCategory(TEST_CAT1, "CAT1", null));
		Category category = cs.getCategory(TEST_CAT1);
		assertNotNull("need category", category);
		assertNotNull("command1", cs.defineCommand(TEST_ID1, "ID1", null,
				category, null));
		assertNotNull("command2", cs.defineCommand(TEST_ID2, "ID2", null,
				category, null));

		Command cmd1 = cs.getCommand(TEST_ID1);
		assertNotNull("get command1", cmd1);
		assertEquals("ID1", cmd1.getName());
		assertNotNull("get command2", cs.getCommand(TEST_ID2));
	}

	public void testCreateWithTwoContexts() throws Exception {
		IEclipseContext localContext = TestUtil.createContext(workbenchContext, "Level1");
		ECommandService cs = (ECommandService) localContext
				.get(ECommandService.class.getName());
		assertNotNull(cs);
		assertNotNull(cs.defineCategory(TEST_CAT1, "CAT1", null));
		Category category = cs.getCategory(TEST_CAT1);
		assertNotNull("need category", category);
		assertNotNull("command1", cs.defineCommand(TEST_ID1, "ID1", null,
				category, null));
		assertNotNull("command2", cs.defineCommand(TEST_ID2, "ID2", null,
				category, null));

		cs = (ECommandService) workbenchContext.get(ECommandService.class
				.getName());
		Command cmd1 = cs.getCommand(TEST_ID1);
		assertNotNull("get command1", cmd1);
		assertEquals("ID1", cmd1.getName());
		assertNotNull("get command2", cs.getCommand(TEST_ID2));
	}

	private IEclipseContext workbenchContext;

	@Override
	protected void setUp() throws Exception {
		workbenchContext = createWorkbenchContext(TestActivator.getDefault()
				.getGlobalContext());
	}

	@Override
	protected void tearDown() throws Exception {
		if (workbenchContext instanceof IDisposable) {
			((IDisposable) workbenchContext).dispose();
		}
		workbenchContext = null;
	}

	private IEclipseContext createWorkbenchContext(IEclipseContext globalContext) {
		IEclipseContext wb = TestUtil.createContext(globalContext, "workbenchContext");
		ContextUtil.commandSetup(wb);
		return wb;
	}
}
