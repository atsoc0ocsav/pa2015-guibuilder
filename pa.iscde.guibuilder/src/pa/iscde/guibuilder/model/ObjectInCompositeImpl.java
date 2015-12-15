package pa.iscde.guibuilder.model;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pa.iscde.guibuilder.ui.GuiLabels;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public abstract class ObjectInCompositeImpl implements ObjectInComposite {
	protected GuiLabels.GUIBuilderObjectFamily objectFamily;
	protected ContextMenuItem[] contextMenuItems;
	protected Point location;
	protected Point size;
	protected Figure figure;

	public ObjectInCompositeImpl(GuiLabels.GUIBuilderObjectFamily objectFamily, ContextMenuItem[] contextMenuItems) {
		this.contextMenuItems = contextMenuItems;
		this.objectFamily = objectFamily;
	}

	@Override
	public Point getLocation() {
		return location;
	}

	@Override
	public void setLocation(Point location) {
		this.location = location;
	}

	@Override
	public void setLocation(int x, int y) {
		this.location = new Point(x, y);
	}

	@Override
	public Point getSize() {
		return size;
	}

	@Override
	public void setSize(Point size) {
		this.size = size;
	}

	@Override
	public void setSize(int x, int y) {
		this.size = new Point(x, y);
	}

	@Override
	public Figure getFigure() {
		return figure;
	}

	@Override
	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	@Override
	public GUIBuilderObjectFamily getObjectFamily() {
		return objectFamily;
	}

	@Override
	public ContextMenuItem[] getContextMenuItems() {
		return contextMenuItems;
	}

	@Override
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
