package pt.iscte.pidesco.guibuilder.internal.model;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public abstract class ObjectInComposite {
	protected GuiLabels.GUIBuilderObjectFamily objectFamily;
	protected Point location;
	protected Point size;
	protected Figure figure;
	
	public Point getLocation() {
		return location;
	}
	
	public Point getSize() {
		return size;
	}
	
	public Figure getFigure() {
		return figure;
	}
	
	public GUIBuilderObjectFamily getObjectFamily(){
		return objectFamily;
	}
}
