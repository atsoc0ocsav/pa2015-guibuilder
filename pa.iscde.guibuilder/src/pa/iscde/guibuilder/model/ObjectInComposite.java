package pa.iscde.guibuilder.model;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public interface ObjectInComposite {
	public enum ContextMenuItem {
		CHANGE_NAME, SET_COLOR, ALL, PLUGIN, GENERATE_CODE;
	}

	public Point getLocation();

	public void setLocation(Point location);

	public void setLocation(int x, int y);

	public Point getSize();

	public void setSize(Point size);

	public void setSize(int x, int y);

	public Figure getFigure();

	public void setFigure(Figure figure);

	public GUIBuilderObjectFamily getObjectFamily();

	public ContextMenuItem[] getContextMenuItems();

	public boolean acceptsMenuItem(ContextMenuItem item);
}
