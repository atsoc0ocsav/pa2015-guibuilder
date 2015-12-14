package pt.iscte.pidesco.guibuilder.model;

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
		this.size=new Point(x, y);
	}
	
	public Figure getFigure() {
		return figure;
	}
	
	public void setFigure(Figure figure) {
		this.figure = figure;
	}
	
	public GUIBuilderObjectFamily getObjectFamily(){
		return objectFamily;
	}
}
