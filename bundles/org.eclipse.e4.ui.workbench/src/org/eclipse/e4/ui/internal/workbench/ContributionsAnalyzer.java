/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.internal.workbench;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.model.application.ui.menu.MTrimContribution;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public final class ContributionsAnalyzer {

	private static boolean DEBUG = false;

	private static void trace(String msg, Object menu, Object menuModel) {
		System.err.println(msg + ": " + menu + ": " + menuModel); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static void addMenuContributions(final MMenu menuModel,
			final ArrayList<MMenuContribution> toContribute,
			final ArrayList<MMenuElement> menuContributionsToRemove) {

		HashSet<String> existingMenuIds = new HashSet<String>();
		HashSet<String> existingSeparatorNames = new HashSet<String>();
		for (MMenuElement child : menuModel.getChildren()) {
			String elementId = child.getElementId();
			if (child instanceof MMenu && elementId != null) {
				existingMenuIds.add(elementId);
			} else if (child instanceof MMenuSeparator && elementId != null) {
				existingSeparatorNames.add(elementId);
			}
		}

		boolean done = toContribute.size() == 0;
		while (!done) {
			ArrayList<MMenuContribution> curList = new ArrayList<MMenuContribution>(toContribute);
			int retryCount = toContribute.size();
			toContribute.clear();

			for (MMenuContribution menuContribution : curList) {
				if (!processAddition(menuModel, menuContributionsToRemove, menuContribution,
						existingMenuIds, existingSeparatorNames)) {
					toContribute.add(menuContribution);
				}
			}
			// We're done if the retryList is now empty (everything done) or
			// if the list hasn't changed at all (no hope)
			done = (toContribute.size() == 0) || (toContribute.size() == retryCount);
		}
	}

	private static boolean processAddition(final MMenu menuModel,
			final ArrayList<MMenuElement> menuContributionsToRemove,
			MMenuContribution menuContribution, final HashSet<String> existingMenuIds,
			HashSet<String> existingSeparatorNames) {
		int idx = getIndex(menuModel, menuContribution.getPositionInParent());
		if (idx == -1) {
			return false;
		}
		for (MMenuElement item : menuContribution.getChildren()) {
			if (item instanceof MMenu && existingMenuIds.contains(item.getElementId())) {
				// skip this, it's already there
				continue;
			} else if (item instanceof MMenuSeparator
					&& existingSeparatorNames.contains(item.getElementId())) {
				// skip this, it's already there
				continue;
			}
			MMenuElement copy = (MMenuElement) EcoreUtil.copy((EObject) item);
			if (DEBUG) {
				trace("addMenuContribution " + copy, menuModel.getWidget(), menuModel); //$NON-NLS-1$
			}
			menuContributionsToRemove.add(copy);
			menuModel.getChildren().add(idx++, copy);
			if (copy instanceof MMenu) {
				existingMenuIds.add(copy.getElementId());
			}
		}
		return true;
	}

	public static List<MToolBarElement> addToolBarContributions(final MToolBar menuModel,
			final List<MToolBarContribution> toContribute) {
		List<MToolBarElement> contributions = new ArrayList<MToolBarElement>();
		boolean done = toContribute.size() == 0;
		while (!done) {
			ArrayList<MToolBarContribution> curList = new ArrayList<MToolBarContribution>(
					toContribute);
			int retryCount = toContribute.size();
			toContribute.clear();

			for (MToolBarContribution menuContribution : curList) {
				if (!processAddition(menuModel, menuContribution, contributions)) {
					toContribute.add(menuContribution);
				}
			}
			// We're done if the retryList is now empty (everything done) or
			// if the list hasn't changed at all (no hope)
			done = (toContribute.size() == 0) || (toContribute.size() == retryCount);
		}
		return contributions;
	}

	private static boolean processAddition(final MToolBar toolBarModel,
			MToolBarContribution toolBarContribution, List<MToolBarElement> contributions) {
		int idx = getIndex(toolBarModel, toolBarContribution.getPositionInParent());
		if (idx == -1) {
			return false;
		}
		for (MToolBarElement item : toolBarContribution.getChildren()) {
			MToolBarElement copy = (MToolBarElement) EcoreUtil.copy((EObject) item);
			if (DEBUG) {
				trace("addToolBarContribution " + copy, toolBarModel.getWidget(), toolBarModel); //$NON-NLS-1$
			}
			toolBarModel.getChildren().add(idx++, copy);
			contributions.add(copy);
		}
		return true;
	}

	public static List<MUIElement> addTrimBarContributions(final MTrimBar trimBar,
			final List<MTrimContribution> toContribute) {
		List<MUIElement> contributions = new ArrayList<MUIElement>();
		boolean done = toContribute.size() == 0;
		while (!done) {
			ArrayList<MTrimContribution> curList = new ArrayList<MTrimContribution>(toContribute);
			int retryCount = toContribute.size();
			toContribute.clear();

			for (MTrimContribution menuContribution : curList) {
				if (!processAddition(trimBar, menuContribution, contributions)) {
					toContribute.add(menuContribution);
				}
			}
			// We're done if the retryList is now empty (everything done) or
			// if the list hasn't changed at all (no hope)
			done = (toContribute.size() == 0) || (toContribute.size() == retryCount);
		}
		return contributions;
	}

	private static boolean processAddition(final MTrimBar trimBar,
			MTrimContribution toolBarContribution, List<MUIElement> contributions) {
		String positionInParent = toolBarContribution.getPositionInParent();
		if (shouldContribute(trimBar, positionInParent)) {
			int idx = getIndex(trimBar, positionInParent);
			if (idx == -1) {
				String id = null;
				String modifier = null;
				if (positionInParent != null && positionInParent.length() > 0) {
					String[] array = positionInParent.split("="); //$NON-NLS-1$
					modifier = array[0];
					id = array[1];
				}

				for (int i = 0; i < trimBar.getChildren().size(); i++) {
					MTrimElement element = trimBar.getChildren().get(i);
					if (element instanceof MToolBar) {
						MToolBar toolBar = (MToolBar) element;
						boolean match = false;
						int insertionIdx = 0;
						for (MToolBarElement tbe : toolBar.getChildren()) {
							if (id.equals(tbe.getElementId())) {
								if ("after".equals(modifier)) { //$NON-NLS-1$
									insertionIdx++;
								}
								match = true;
								break;
							}
							insertionIdx++;
						}

						if (match) {
							for (MTrimElement item : toolBarContribution.getChildren()) {
								MTrimElement copy = (MTrimElement) EcoreUtil.copy((EObject) item);
								if (copy instanceof MToolControl) {
									trimBar.getChildren().add(i++, copy);
									contributions.add(copy);
								} else if (copy instanceof MToolBar) {
									MToolBar toolBarCopy = (MToolBar) copy;
									for (int j = 0; j < toolBarContribution.getChildren().size(); j++) {
										toolBar.getChildren().add(insertionIdx++,
												toolBarCopy.getChildren().get(j));
									}
									copy.setToBeRendered(true);
									contributions.add(copy);
								}
							}
							return true;
						}
					}
				}
				return false;
			}

			if (idx == trimBar.getChildren().size()) {
				idx = getIndex(trimBar, "after=additions"); //$NON-NLS-1$
				for (MTrimElement item : toolBarContribution.getChildren()) {
					MTrimElement copy = (MTrimElement) EcoreUtil.copy((EObject) item);
					if (DEBUG) {
						trace("addTrimContribution " + copy, trimBar.getWidget(), trimBar); //$NON-NLS-1$
					}
					trimBar.getChildren().add(idx++, copy);
					contributions.add(copy);
				}
				return true;
			}

			MTrimElement element = trimBar.getChildren().get(idx);
			if (element instanceof MToolControl) {
				for (MTrimElement item : toolBarContribution.getChildren()) {
					MTrimElement copy = (MTrimElement) EcoreUtil.copy((EObject) item);
					if (DEBUG) {
						trace("addTrimContribution " + copy, trimBar.getWidget(), trimBar); //$NON-NLS-1$
					}
					trimBar.getChildren().add(idx++, copy);
					contributions.add(copy);
				}
			} else if (element instanceof MToolBar) {
				MToolBar bar = (MToolBar) element;
				for (MTrimElement item : toolBarContribution.getChildren()) {
					if (item instanceof MToolBar) {
						MToolBar contributedToolBar = (MToolBar) item;
						for (int i = 0; i < contributedToolBar.getChildren().size(); i++) {
							MToolBarElement tbe = contributedToolBar.getChildren().get(i);
							MToolBarElement copy = (MToolBarElement) EcoreUtil.copy((EObject) tbe);
							if (DEBUG) {
								trace("addTrimContribution " + copy, trimBar.getWidget(), trimBar); //$NON-NLS-1$
							}
							bar.setToBeRendered(true);
							bar.getChildren().add(copy);
							contributions.add(copy);
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private static boolean shouldContribute(MTrimBar trimBar, String positionInParent) {
		String id = null;
		if (positionInParent != null && positionInParent.length() > 0) {
			String[] array = positionInParent.split("="); //$NON-NLS-1$
			id = array[1];
		}
		if (id == null || id.equals("additions")) { //$NON-NLS-1$
			return true;
		}

		for (MTrimElement element : trimBar.getChildren()) {
			if (id.equals(element.getElementId())) {
				return true;
			}

			if (element instanceof MToolBar) {
				for (MToolBarElement e : ((MToolBar) element).getChildren()) {
					if (id.equals(e.getElementId())) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static int getIndex(MElementContainer<?> menuModel, String positionInParent) {
		String id = null;
		String modifier = null;
		if (positionInParent != null && positionInParent.length() > 0) {
			String[] array = positionInParent.split("="); //$NON-NLS-1$
			modifier = array[0];
			id = array[1];
		}
		if (id == null) {
			return menuModel.getChildren().size();
		}

		int idx = 0;
		int size = menuModel.getChildren().size();
		while (idx < size) {
			if (id.equals(menuModel.getChildren().get(idx).getElementId())) {
				if ("after".equals(modifier)) { //$NON-NLS-1$
					idx++;
				}
				return idx;
			}
			idx++;
		}
		return id.equals("additions") ? menuModel.getChildren().size() : -1; //$NON-NLS-1$
	}

}
