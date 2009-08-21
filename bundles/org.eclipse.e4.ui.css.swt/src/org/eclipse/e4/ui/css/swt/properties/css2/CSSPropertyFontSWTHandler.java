/*******************************************************************************
 * Copyright (c) 2008, 2009 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *     Remy Chi Jian Suen <remy.suen@gmail.com>
 *******************************************************************************/
package org.eclipse.e4.ui.css.swt.properties.css2;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler2;
import org.eclipse.e4.ui.css.core.dom.properties.css2.AbstractCSSPropertyFontHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.CSS2FontProperties;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyFontHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.helpers.CSSSWTFontHelper;
import org.eclipse.e4.ui.css.swt.helpers.SWTElementHelpers;
import org.eclipse.e4.ui.css.swt.properties.custom.CTabETabHelper;
import org.eclipse.e4.ui.widgets.ETabFolder;
import org.eclipse.e4.ui.widgets.ETabItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

public class CSSPropertyFontSWTHandler extends AbstractCSSPropertyFontHandler
		implements ICSSPropertyHandler2 {

	public final static ICSSPropertyFontHandler INSTANCE = new CSSPropertyFontSWTHandler();

	/**
	 * The key for the SWT event listener that will be attached to a CTabFolder
	 * for applying fonts to a CTabItem.
	 */
	private static final String CSS_CTABITEM_SELECTED_FONT_LISTENER_KEY = "CSS_CTABFOLDER_SELECTED_FONT_LISTENER_KEY"; //$NON-NLS-1$

	private static void setFont(Widget widget, Font font) {
		if (widget instanceof CTabItem) {
			((CTabItem) widget).setFont(font);
		} else if (widget instanceof ETabItem) {
			((ETabItem) widget).setFont(font);
		} else if (widget instanceof Control) {
			((Control) widget).setFont(font);
		}
	}

	public boolean applyCSSProperty(Object element, String property,
			CSSValue value, String pseudo, CSSEngine engine) throws Exception {
		Widget widget = SWTElementHelpers.getWidget(element);
		if (widget != null) {
			CSS2FontProperties fontProperties = CSSSWTFontHelper
					.getCSS2FontProperties(widget, engine
							.getCSSElementContext(widget));
			if (fontProperties != null) {
				super.applyCSSProperty(fontProperties, property, value, pseudo,
						engine);
				if (widget instanceof CTabItem || widget instanceof ETabItem) {
					Control parent = CTabETabHelper.getParent(widget);
					FontSelectionListener listener = (FontSelectionListener) parent.getData(
									CSS_CTABITEM_SELECTED_FONT_LISTENER_KEY);
					if (listener == null) {
						listener = new FontSelectionListener(engine);
						parent.addListener(SWT.Paint, listener);
						parent.setData(
								CSS_CTABITEM_SELECTED_FONT_LISTENER_KEY,
								listener);
					} else {
						// update our engine
						listener.setEngine(engine);
					}
					listener.setShouldStyle(true);
				}
			}
			return true;
		} else {
			if (element instanceof CSS2FontProperties) {
				super
						.applyCSSProperty(element, property, value, pseudo,
								engine);
				return true;
			}
		}
		return false;
	}

	public String retrieveCSSProperty(Object element, String property,
			String pseudo, CSSEngine engine) throws Exception {
		Widget widget = SWTElementHelpers.getWidget(element);
		if (widget != null) {
			return super.retrieveCSSProperty(widget, property, pseudo, engine);
		}
		return null;
	}

	public String retrieveCSSPropertyFontAdjust(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		return null;
	}

	public String retrieveCSSPropertyFontFamily(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		Widget widget = (Widget) element;
		return CSSSWTFontHelper.getFontFamily(widget);
	}

	public String retrieveCSSPropertyFontSize(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		Widget widget = (Widget) element;
		return CSSSWTFontHelper.getFontSize(widget);
	}

	public String retrieveCSSPropertyFontStretch(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		return null;
	}

	public String retrieveCSSPropertyFontStyle(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		Widget widget = (Widget) element;
		return CSSSWTFontHelper.getFontStyle(widget);

	}

	public String retrieveCSSPropertyFontVariant(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		return null;
	}

	public String retrieveCSSPropertyFontWeight(Object element, String pseudo,
			CSSEngine engine) throws Exception {
		Widget widget = (Widget) element;
		return CSSSWTFontHelper.getFontWeight(widget);
	}

	public void onAllCSSPropertiesApplyed(Object element, CSSEngine engine)
			throws Exception {
		final Widget widget = SWTElementHelpers.getWidget(element);
		if (widget == null || widget instanceof CTabItem || widget instanceof ETabItem)
			return;
		CSS2FontProperties fontProperties = CSSSWTFontHelper
				.getCSS2FontProperties(widget, engine
						.getCSSElementContext(widget));
		if (fontProperties == null)
			return;
		Font font = (Font) engine.convert(fontProperties, Font.class, widget);
		setFont(widget, font);
	}

	private class FontSelectionListener implements Listener {

		/**
		 * The font attributes that we currently "support" and that should be
		 * retrieved from the style sheet. This list must be updated if
		 * AbstractCSSPropertyFontHandler's listing changes.
		 */
		private String[] fontAttributes = { "font", "font-family", "font-size",
				"font-adjust", "font-stretch", "font-style", "font-variant",
				"font-weight" };

		private CSSEngine engine;

		private Item selection;
		
		private boolean shouldStyle;

		public FontSelectionListener(CSSEngine engine) {
			this.engine = engine;
		}

		public void setEngine(CSSEngine engine) {
			this.engine = engine;
		}
		
		public void setShouldStyle(boolean shouldStyle) {
			this.shouldStyle = shouldStyle;
		}

		private void applyStyles(CSSStyleDeclaration styleDeclaration,
				String pseudo, Item item) {
			CSS2FontProperties fontProperties = CSSSWTFontHelper
					.getCSS2FontProperties(item, engine
							.getCSSElementContext(item));
			if (fontProperties != null) {
				// reset ourselves to prevent the stacking of properties
				reset(fontProperties);

				for (int j = 0; j < fontAttributes.length; j++) {
					CSSValue value = styleDeclaration
							.getPropertyCSSValue(fontAttributes[j]);
					if (value != null) {
						try {
							// we have a value, so apply it to the properties
							CSSPropertyFontSWTHandler.super.applyCSSProperty(
									fontProperties, fontAttributes[j], value,
									pseudo, engine);
						} catch (Exception e) {
							engine.handleExceptions(e);
						}
					}
				}

				try {
					// set the font
					Font font = (Font) engine.convert(fontProperties,
							Font.class, item);
					setFont(item, font);
				} catch (Exception e) {
					engine.handleExceptions(e);
				}
			}
		}

		private void styleUnselected(Item[] items) {
			for (int i = 0; i < items.length; i++) {
				CSSStyleDeclaration unselectedStyle = engine.getViewCSS()
						.getComputedStyle(engine.getElement(items[i]), null);
				if (unselectedStyle == null) {
					setFont(items[i], null);
				} else {
					applyStyles(unselectedStyle, null, items[i]);
				}
			}
		}

		private boolean styleSelected(Item selection) {
			CSSStyleDeclaration selectedStyle = engine.getViewCSS()
					.getComputedStyle(engine.getElement(selection), "selected");
			if (selectedStyle == null) {
				return false;
			}

			applyStyles(selectedStyle, "selected", selection);
			return true;
		}

		private void reset(CSS2FontProperties properties) {
			properties.setFamily(null);
			properties.setSize(null);
			properties.setSizeAdjust(null);
			properties.setWeight(null);
			properties.setStyle(null);
			properties.setVariant(null);
			properties.setStretch(null);
		}

		public void handleEvent(Event e) {
			if (e.widget instanceof CTabFolder || e.widget instanceof ETabFolder) {
				Item[] items;
				Item selection;
				if (e.widget instanceof CTabFolder) {
					CTabFolder folder = (CTabFolder) e.widget;
					selection = folder.getSelection();
					items = folder.getItems();
				} else {
					ETabFolder folder = (ETabFolder) e.widget;
					selection = (ETabItem) folder.getSelection();
					items = folder.getItems();
					// only style if the selection has changed
				}
				if (!shouldStyle && this.selection == selection) {
					return;	
				}			
				// style individual items
				styleUnselected(items);
	
				if (selection != null && !styleSelected(selection)) {
					setFont(selection, null);
				}
				this.selection = selection;
				shouldStyle = false;
			}
		}
	}
}
