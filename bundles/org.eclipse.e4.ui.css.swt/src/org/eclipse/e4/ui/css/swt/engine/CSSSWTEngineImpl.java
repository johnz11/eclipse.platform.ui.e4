/*******************************************************************************
 * Copyright (c) 2008 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.css.swt.engine;

import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyBackgroundHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyBorderHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyClassificationHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyFontHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyTextHandler;
import org.eclipse.e4.ui.css.swt.properties.css2.CSSPropertyBackgroundSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.css2.CSSPropertyBorderSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.css2.CSSPropertyClassificationSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.css2.CSSPropertyFontSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.css2.CSSPropertyTextSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyAlignmentSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyBorderVisibleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMaximizeVisibleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMaximizedSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMinimizeVisibleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMinimizedSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMruVisibleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyShowCloseSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertySimpleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertySingleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyUnselectedCloseVisibleSWTHandler;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyUnselectedImageVisibleSWTHandler;
import org.eclipse.e4.ui.css.xml.properties.css2.CSSPropertyBackgroundXMLHandler;
import org.eclipse.e4.ui.css.xml.properties.css2.CSSPropertyFontXMLHandler;
import org.eclipse.e4.ui.css.xml.properties.css2.CSSPropertyTextXMLHandler;
import org.eclipse.swt.widgets.Display;

/**
 * CSS SWT Engine implementation which configure CSSEngineImpl to apply styles
 * to SWT widgets with static handler strategy.
 */
public class CSSSWTEngineImpl extends AbstractCSSSWTEngineImpl {

	public CSSSWTEngineImpl(Display display) {
		super(display);
	}

	public CSSSWTEngineImpl(Display display, boolean lazyApplyingStyles) {
		super(display, lazyApplyingStyles);
	}

	protected void initializeCSSPropertyHandlers() {
		// Register SWT CSS Property Background Handler
		super.registerCSSPropertyHandler(ICSSPropertyBackgroundHandler.class,
				CSSPropertyBackgroundSWTHandler.INSTANCE);
		// Register SWT CSS Property Border Handler
		super.registerCSSPropertyHandler(ICSSPropertyBorderHandler.class,
				CSSPropertyBorderSWTHandler.INSTANCE);
		// Register SWT CSS Property Classification Handler
		super.registerCSSPropertyHandler(
				ICSSPropertyClassificationHandler.class,
				CSSPropertyClassificationSWTHandler.INSTANCE);
		// Register SWT CSS Property Text Handler
		super.registerCSSPropertyHandler(ICSSPropertyTextHandler.class,
				CSSPropertyTextSWTHandler.INSTANCE);
		// Register SWT CSS Property Font Handler
		super.registerCSSPropertyHandler(ICSSPropertyFontHandler.class,
				CSSPropertyFontSWTHandler.INSTANCE);

		// Register XML CSS Property Background Handler
		super.registerCSSPropertyHandler(ICSSPropertyBackgroundHandler.class,
				CSSPropertyBackgroundXMLHandler.INSTANCE);
		// Register XML CSS Property Text Handler
		super.registerCSSPropertyHandler(ICSSPropertyTextHandler.class,
				CSSPropertyTextXMLHandler.INSTANCE);
		// Register XML CSS Property Font Handler
		super.registerCSSPropertyHandler(ICSSPropertyFontHandler.class,
				CSSPropertyFontXMLHandler.INSTANCE);
		//Register SWT CSS Property BorderVisible
		super.registerCSSProperty("border-visible", CSSPropertyBorderVisibleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyBorderVisibleSWTHandler.class,
				CSSPropertyBorderVisibleSWTHandler.INSTANCE);
		//Register SWT CSS Property Simple
		super.registerCSSProperty("simple", CSSPropertySimpleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertySimpleSWTHandler.class,
				CSSPropertySimpleSWTHandler.INSTANCE);
		//Register SWT CSS Property MaximizeVisible
		super.registerCSSProperty("maximize-visible", CSSPropertyMaximizeVisibleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyMaximizeVisibleSWTHandler.class,
				CSSPropertyMaximizeVisibleSWTHandler.INSTANCE);
		//Register SWT CSS Property MinimizeVisible
		super.registerCSSProperty("minimize-visible", CSSPropertyMinimizeVisibleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyMinimizeVisibleSWTHandler.class,
				CSSPropertyMinimizeVisibleSWTHandler.INSTANCE);
		//Register SWT CSS Property ShowClose
		super.registerCSSProperty("show-close", CSSPropertyShowCloseSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyShowCloseSWTHandler.class,
				CSSPropertyShowCloseSWTHandler.INSTANCE);
		//Register SWT CSS Property mruVisible
		super.registerCSSProperty("mru-visible", CSSPropertyMruVisibleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyMruVisibleSWTHandler.class,
				CSSPropertyMruVisibleSWTHandler.INSTANCE);
		//Register SWT CSS Property Maximized
		super.registerCSSProperty("maximized", CSSPropertyMaximizedSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyMaximizedSWTHandler.class,
				CSSPropertyMaximizedSWTHandler.INSTANCE);
		//Register SWT CSS Property Minimized
		super.registerCSSProperty("minimized", CSSPropertyMinimizedSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyMinimizedSWTHandler.class,
				CSSPropertyMinimizedSWTHandler.INSTANCE);
		//Register SWT CSS Property Single
		super.registerCSSProperty("single", CSSPropertySingleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertySingleSWTHandler.class,
				CSSPropertySingleSWTHandler.INSTANCE);
		//Register SWT CSS Property UnselectedCloseVisible
		super.registerCSSProperty("unselected-close-visible", CSSPropertyUnselectedCloseVisibleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyUnselectedCloseVisibleSWTHandler.class,
				CSSPropertyUnselectedCloseVisibleSWTHandler.INSTANCE);
		//Register SWT CSS Property UnselectedImageVisible
		super.registerCSSProperty("unselected-image-visible", CSSPropertyUnselectedImageVisibleSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyUnselectedImageVisibleSWTHandler.class,
				CSSPropertyUnselectedImageVisibleSWTHandler.INSTANCE);
		//Register SWT CSS Property Alignment
		super.registerCSSProperty("alignment", CSSPropertyAlignmentSWTHandler.class);  
		super.registerCSSPropertyHandler(CSSPropertyAlignmentSWTHandler.class,
				CSSPropertyAlignmentSWTHandler.INSTANCE);
	}

}
