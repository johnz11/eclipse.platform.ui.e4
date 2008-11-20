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
package org.eclipse.e4.ui.css.swt.helpers;

import org.eclipse.e4.ui.css.core.css2.CSS2ColorHelper;
import org.eclipse.e4.ui.css.core.css2.CSS2RGBColorImpl;
import org.eclipse.e4.ui.css.core.dom.properties.Gradient;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.RGBColor;

public class CSSSWTColorHelper {

	/*--------------- SWT Color Helper -----------------*/

	public static Color getSWTColor(RGBColor rgbColor, Display display) {
		RGB rgb = getRGB(rgbColor);
		return new Color(display, rgb);
	}

	public static Color getSWTColor(CSSValue value, Display display) {
		if (value.getCssValueType() != CSSValue.CSS_PRIMITIVE_VALUE)
			return null;
		RGB rgb = getRGB((CSSPrimitiveValue) value);
		if (rgb == null)
			return null;
		Color color = new Color(display, rgb.red, rgb.green, rgb.blue);
		return color;
	}

	public static RGB getRGB(String name) {
		RGBColor color = CSS2ColorHelper.getRGBColor(name);
		if (color != null) {
			return getRGB(color);
		}
		return null;
	}

	public static RGB getRGB(RGBColor color) {
		return new RGB((int) color.getRed().getFloatValue(
				CSSPrimitiveValue.CSS_NUMBER), (int) color.getGreen()
				.getFloatValue(CSSPrimitiveValue.CSS_NUMBER), (int) color
				.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
	}

	public static RGB getRGB(CSSValue value) {
		if (value.getCssValueType() != CSSValue.CSS_PRIMITIVE_VALUE)
			return null;
		return getRGB((CSSPrimitiveValue) value);
	}

	public static RGB getRGB(CSSPrimitiveValue value) {
		RGB rgb = null;
		switch (value.getPrimitiveType()) {
		case CSSPrimitiveValue.CSS_IDENT:
		case CSSPrimitiveValue.CSS_STRING:
			String string = value.getStringValue();
			rgb = getRGB(string);
			break;
		case CSSPrimitiveValue.CSS_RGBCOLOR:
			RGBColor rgbColor = value.getRGBColorValue();
			rgb = getRGB(rgbColor);
			break;
		}
		return rgb;
	}

	public static Integer getPercent(CSSPrimitiveValue value) {
		int percent = 0;
		switch (value.getPrimitiveType()) {
		case CSSPrimitiveValue.CSS_PERCENTAGE:
			percent = (int) value
					.getFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE);
		}
		return new Integer(percent);
	}

	public static Gradient getGradient(CSSValueList list) {
		Gradient gradient = new Gradient();
		for (int i = 0; i < list.getLength(); i++) {
			CSSValue value = list.item(i);
			if (value.getCssText().equals("gradient"))
				continue;
			if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
				switch (((CSSPrimitiveValue) value).getPrimitiveType()) {
				case CSSPrimitiveValue.CSS_IDENT:
				case CSSPrimitiveValue.CSS_STRING:
				case CSSPrimitiveValue.CSS_RGBCOLOR:
					RGB rgb = getRGB((CSSPrimitiveValue) value);
					if (rgb != null)
						gradient.addRGB(rgb);
					break;
				case CSSPrimitiveValue.CSS_PERCENTAGE:
					gradient.addPercent(getPercent((CSSPrimitiveValue) value));
					break;
				}
			}
		}
		return gradient;
	}

	public static RGBColor getRGBColor(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		return new CSS2RGBColorImpl(red, green, blue);
	}

	public static RGBColor getRGBColor(RGB color) {
		int red = color.red;
		int green = color.green;
		int blue = color.blue;
		return new CSS2RGBColorImpl(red, green, blue);
	}
}
