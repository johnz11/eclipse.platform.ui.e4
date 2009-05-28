/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.e4.ui.tests.css.core.parser;

import junit.framework.TestCase;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.core.impl.dom.Measure;
import org.eclipse.e4.ui.tests.css.core.util.ParserTestUtil;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class ValueTest extends TestCase {

	private CSSEngine engine;

	protected void setUp() throws Exception {
		engine = ParserTestUtil.createEngine();
	}
	
	public void testFloat() throws Exception {
		CSSValue value = engine.parsePropertyValue("2.0");
		assertTrue(value instanceof Measure);
		assertEquals( "2.0", value.getCssText() );
	}

	public void testInt() throws Exception {
		CSSValue value = engine.parsePropertyValue("34");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_NUMBER);
		assertEquals( "34", value.getCssText() );
	}

	public void testIdentifier() throws Exception {
		CSSValue value = engine.parsePropertyValue("SomeWord");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_IDENT);
		assertEquals( "SomeWord", value.getCssText() );
	}

	public void testPercent() throws Exception {
		CSSValue value = engine.parsePropertyValue("30%");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_PERCENTAGE);
		assertEquals( "30.0%", value.getCssText() );
	}
	
	public void testPixel() throws Exception {
		CSSValue value = engine.parsePropertyValue("26px");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_PX);
		assertEquals( "26.0px", value.getCssText() );
	}
	
	public void testInch() throws Exception {
		CSSValue value = engine.parsePropertyValue("88in");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_IN);
		assertEquals( "88.0in", value.getCssText() );
	}
	
	public void testEm() throws Exception {
		CSSValue value = engine.parsePropertyValue("75em");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_EMS);
		assertEquals( "75.0em", value.getCssText() );
	}
	
	public void testURI() throws Exception {
		CSSValue value = engine.parsePropertyValue("url(./somepath/picture.gif)");
		assertTrue(value instanceof Measure);
		assertEquals(((Measure) value).getPrimitiveType(), CSSPrimitiveValue.CSS_URI);
		assertEquals( "./somepath/picture.gif", value.getCssText() );
	}
	
	;	
}
