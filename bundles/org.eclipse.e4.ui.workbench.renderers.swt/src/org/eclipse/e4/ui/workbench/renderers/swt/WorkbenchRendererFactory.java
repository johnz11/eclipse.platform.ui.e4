package org.eclipse.e4.ui.workbench.renderers.swt;

import javax.annotation.PostConstruct;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.workbench.swt.factories.IRendererFactory;

public class WorkbenchRendererFactory implements IRendererFactory {

	private MenuRenderer menuRenderer;
	private HandledMenuItemRenderer handledMenuItemRenderer;
	private ToolBarRenderer toolbarRenderer;
	private ToolItemRenderer toolItemRenderer;
	private SeparatorRenderer separatorRenderer;
	private ContributedPartRenderer contributedPartRenderer;
	private ElementReferenceRenderer elementRefRenderer;
	private PerspectiveStackRenderer perspStackRenderer;
	private PerspectiveRenderer perspRenderer;
	private SashRenderer partSashRenderer;
	private StackRenderer stackRenderer;
	private TrimBarRenderer trimBarRenderer;
	private ToolControlRenderer toolControlRenderer;
	private WBWRenderer wbwRenderer;

	private IEclipseContext context;
	private DirectMenuItemRenderer directMenuItemRenderer;
	private RenderedMenuRenderer renderedMenuRenderer;
	private RenderedMenuItemRenderer renderedMenuItemRenderer;
	private RenderedToolBarRenderer renderedToolbarRenderer;

	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {
		if (uiElement instanceof MPart) {
			if (contributedPartRenderer == null) {
				contributedPartRenderer = new ContributedPartRenderer();
				initRenderer(contributedPartRenderer);
			}
			return contributedPartRenderer;
		} else if (uiElement instanceof MHandledMenuItem) {
			if (handledMenuItemRenderer == null) {
				handledMenuItemRenderer = new HandledMenuItemRenderer();
				initRenderer(handledMenuItemRenderer);
			}
			return handledMenuItemRenderer;
		} else if (uiElement instanceof MDirectMenuItem) {
			if (directMenuItemRenderer == null) {
				directMenuItemRenderer = new DirectMenuItemRenderer();
				initRenderer(directMenuItemRenderer);
			}
			return directMenuItemRenderer;
		} else if (uiElement instanceof MRenderedMenu) {
			if (renderedMenuRenderer == null) {
				renderedMenuRenderer = new RenderedMenuRenderer();
				initRenderer(renderedMenuRenderer);
			}
			return renderedMenuRenderer;
		} else if (uiElement instanceof MRenderedMenuItem) {
			if (renderedMenuItemRenderer == null) {
				renderedMenuItemRenderer = new RenderedMenuItemRenderer();
				initRenderer(renderedMenuItemRenderer);
			}
			return renderedMenuItemRenderer;
		} else if (uiElement instanceof MMenu) {
			if (menuRenderer == null) {
				menuRenderer = new MenuRenderer();
				initRenderer(menuRenderer);
			}
			return menuRenderer;
		} else if (uiElement instanceof MRenderedToolBar) {
			if (renderedToolbarRenderer == null) {
				renderedToolbarRenderer = new RenderedToolBarRenderer();
				initRenderer(renderedToolbarRenderer);
			}
			return renderedToolbarRenderer;
		} else if (uiElement instanceof MToolBar) {
			if (toolbarRenderer == null) {
				toolbarRenderer = new ToolBarRenderer();
				initRenderer(toolbarRenderer);
			}
			return toolbarRenderer;
		} else if (uiElement instanceof MToolItem) {
			if (toolItemRenderer == null) {
				toolItemRenderer = new ToolItemRenderer();
				initRenderer(toolItemRenderer);
			}
			return toolItemRenderer;
		} else if (uiElement instanceof MMenuSeparator
				|| uiElement instanceof MToolBarSeparator) {
			if (separatorRenderer == null) {
				separatorRenderer = new SeparatorRenderer();
				initRenderer(separatorRenderer);
			}
			return separatorRenderer;
		} else if (uiElement instanceof MPlaceholder) {
			if (elementRefRenderer == null) {
				elementRefRenderer = new ElementReferenceRenderer();
				initRenderer(elementRefRenderer);
			}
			return elementRefRenderer;
		} else if (uiElement instanceof MPerspective) {
			if (perspRenderer == null) {
				perspRenderer = new PerspectiveRenderer();
				initRenderer(perspRenderer);
			}
			return perspRenderer;
		} else if (uiElement instanceof MPerspectiveStack) {
			if (perspStackRenderer == null) {
				perspStackRenderer = new PerspectiveStackRenderer();
				initRenderer(perspStackRenderer);
			}
			return perspStackRenderer;
		} else if (uiElement instanceof MPartSashContainer) {
			if (partSashRenderer == null) {
				partSashRenderer = new SashRenderer();
				initRenderer(partSashRenderer);
			}
			return partSashRenderer;
		} else if (uiElement instanceof MPartStack) {
			if (stackRenderer == null) {
				stackRenderer = new StackRenderer();
				initRenderer(stackRenderer);
			}
			return stackRenderer;
		} else if (uiElement instanceof MTrimBar) {
			if (trimBarRenderer == null) {
				trimBarRenderer = new TrimBarRenderer();
				initRenderer(trimBarRenderer);
			}
			return trimBarRenderer;
		} else if (uiElement instanceof MToolControl) {
			if (toolControlRenderer == null) {
				toolControlRenderer = new ToolControlRenderer();
				initRenderer(toolControlRenderer);
			}
			return toolControlRenderer;
		} else if (uiElement instanceof MWindow) {
			if (wbwRenderer == null) {
				wbwRenderer = new WBWRenderer();
				initRenderer(wbwRenderer);
			}
			return wbwRenderer;
		}

		// We could return an 'no renderer' renderer here ??
		return null;
	}

	protected void initRenderer(AbstractPartRenderer renderer) {
		renderer.init(context);
		ContextInjectionFactory.inject(renderer, context);
	}

	@PostConstruct
	public void init(IEclipseContext context) {
		this.context = context;
	}

}
