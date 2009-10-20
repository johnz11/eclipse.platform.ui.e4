package org.eclipse.e4.ui.workbench.swt.factories;

import org.eclipse.e4.core.services.context.IEclipseContext;
import org.eclipse.e4.ui.model.application.MUIElement;
import org.eclipse.e4.ui.workbench.swt.internal.AbstractPartRenderer;

public interface IRendererFactory {
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent);

	public void init(IEclipseContext context);
}
