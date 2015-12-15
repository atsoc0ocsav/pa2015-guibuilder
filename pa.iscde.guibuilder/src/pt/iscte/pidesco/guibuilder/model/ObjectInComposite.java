package pt.iscte.pidesco.guibuilder.model;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public abstract class ObjectInComposite {
	public enum ContextMenuItem {
		CHANGE_NAME, SET_COLOR, ALL, PLUGIN, GENERATE_CODE;
	}

	protected GuiLabels.GUIBuilderObjectFamily objectFamily;
	protected ContextMenuItem[] contextMenuItems;
	protected Point location;
	protected Point size;
	protected Figure figure;

	public ObjectInComposite(GuiLabels.GUIBuilderObjectFamily objectFamily, ContextMenuItem[] contextMenuItems) {
		this.contextMenuItems = contextMenuItems;
		this.objectFamily = objectFamily;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public void setLocation(int x, int y) {
		this.location = new Point(x, y);
	}

	public Point getSize() {
		return size;
	}

	public void setSize(Point size) {
		this.size = size;
	}

	public void setSize(int x, int y) {
		this.size = new Point(x, y);
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	public GUIBuilderObjectFamily getObjectFamily() {
		return objectFamily;
	}
	
	public ContextMenuItem[] getContextMenuItems() {
		return contextMenuItems;
	}

	public boolean acceptsMenuItem(ContextMenuItem item) {
		if (contextMenuItems[0].equals(ContextMenuItem.ALL)) {
			return true;
		} else {
			for (ContextMenuItem i : contextMenuItems) {
				if (i.equals(item)) {
					return true;
				}
			}
		}

		return false;
	}
}
