/*******************************************************************************
 * Copyright (c) 2010 Tom Schindl and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.css.swt.internal.theme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.core.engine.CSSErrorHandler;
import org.eclipse.e4.ui.css.core.util.resources.IResourceLocator;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class ThemeEngine implements IThemeEngine {
	private List<Theme> themes = new ArrayList<Theme>();

	private CSSEngine engine;
	private ITheme currentTheme;
	private Display display;

	private List<String> globalStyles = new ArrayList<String>();

	private HashMap<String, List<String>> stylesheets = new HashMap<String, List<String>>();
	private HashMap<String, List<IResourceLocator>> sourceLocators = new HashMap<String, List<IResourceLocator>>();

	public ThemeEngine(Display display) {
		this.engine = new CSSSWTEngineImpl(display, true);
		this.display = display;
		this.engine.setErrorHandler(new CSSErrorHandler() {

			public void error(Exception e) {
				// TODO Use the logger
				e.printStackTrace();
			}
		});
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint extPoint = registry
				.getExtensionPoint("org.eclipse.e4.ui.css.swt.theme");

		for (IExtension e : extPoint.getExtensions()) {
			for (IConfigurationElement ce : e.getConfigurationElements()) {
				if (ce.getName().equals("theme")) {
					registerTheme(ce.getAttribute("id"), ce
							.getAttribute("label"), "platform:/plugin/"
							+ ce.getContributor().getName() + "/" + ce
							.getAttribute("basestylesheeturi"));
				}
			}
		}

		for (IExtension e : extPoint.getExtensions()) {
			for (IConfigurationElement ce : e.getConfigurationElements()) {
				if (ce.getName().equals("stylesheet")) {
					IConfigurationElement[] cces = ce.getChildren("themeid");
					if (cces.length == 0) {
						registerStylsheet("platform:/plugin/"
								+ ce.getContributor().getName() + "/"
								+ ce.getAttribute("uri"));
					} else {
						String[] themes = new String[cces.length];
						for (int i = 0; i < cces.length; i++) {
							themes[i] = cces[i].getAttribute("refid");
						}
						String uri = "platform:/plugin/"
								+ ce.getContributor().getName() + "/"
								+ ce.getAttribute("uri");
						registerStylsheet(uri, themes);
					}
				}
			}
		}

		display.setData("org.eclipse.e4.ui.css.core.engine", engine);
	}

	public synchronized ITheme registerTheme(String id, String label,
			String basestylesheetURI) {
		for (Theme t : themes) {
			if (t.getId().equals(id)) {
				throw new IllegalArgumentException("A theme with the id '" + id
						+ "' is already registered");
			}
		}
		Theme theme = new Theme(id, label);
		themes.add(theme);
		registerStyle(id, basestylesheetURI);

		return theme;
	}

	public synchronized void registerStylsheet(String uri, String... themes) {
		if (themes.length == 0) {
			globalStyles.add(uri);
		} else {
			for (String t : themes) {
				registerStyle(t, uri);
			}
		}
	}

	public synchronized void registerResourceLocator(IResourceLocator locator,
			String... themes) {
		if (themes.length == 0) {
			engine.getResourcesLocatorManager()
					.registerResourceLocator(locator);
		} else {

		}
	}

	private void registerStyle(String id, String stylesheet) {
		List<String> s = stylesheets.get(id);
		if (s == null) {
			s = new ArrayList<String>();
			stylesheets.put(id, s);
		}
		s.add(stylesheet);
	}

	private List<String> getAllStyles(String id) {
		List<String> s = stylesheets.get(id);
		if (s == null) {
			s = Collections.emptyList();
		}

		s = new ArrayList<String>(s);
		s.addAll(globalStyles);
		return s;
	}

	private List<IResourceLocator> getResourceLocators(String id) {
		List<IResourceLocator> s = sourceLocators.get(id);
		if (s == null) {
			s = Collections.emptyList();
		}

		return s;
	}

	public void setTheme(String themeId) {
		for (Theme t : themes) {
			if (t.getId().equals(themeId)) {
				setTheme(t);
				break;
			}
		}
	}

	public void setTheme(ITheme theme) {
		Assert.isNotNull(theme, "The theme must not be null");

		if (this.currentTheme != theme) {
			if (currentTheme != null) {
				for (IResourceLocator l : getResourceLocators(currentTheme
						.getId())) {
					engine.getResourcesLocatorManager()
							.unregisterResourceLocator(l);
				}
			}

			this.currentTheme = theme;
			engine.reset();

			for (IResourceLocator l : getResourceLocators(theme.getId())) {
				engine.getResourcesLocatorManager()
						.registerResourceLocator(l);
			}

			for (String stylesheet : getAllStyles(theme.getId())) {
				URL url;
				InputStream stream = null;
				try {
					url = FileLocator.resolve(new URL(stylesheet.toString()));
					stream = url.openStream();
					engine.parseStyleSheet(stream);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
			Shell[] shells = display.getShells();
			for (Shell s : shells) {
				try {
					s.setRedraw(false);
					s.reskin(SWT.ALL);
					applyStyles(s, true);
				} catch(Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					s.setRedraw(true);
				}
			}
		}
	}

	public synchronized List<ITheme> getThemes() {
		return Collections.unmodifiableList(new ArrayList<ITheme>(themes));
	}

	public void applyStyles(Widget widget, boolean applyStylesToChildNodes) {
		engine.applyStyles(widget, applyStylesToChildNodes);
	}
}