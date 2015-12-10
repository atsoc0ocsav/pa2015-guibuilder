package pt.iscte.pidesco.guibuilder.internal.model;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;

public class ObjectInComposite {

	private String id;
	private Figure figure;
	private ObjectMoverResizer mr;
	private Control object;
	private boolean isRoot;
	private ObjectInComposite parent;

	public ObjectInComposite(String id, Figure figure, ObjectMoverResizer fmr, Control object) {
		this.id = id;
		this.figure = figure;
		this.mr = fmr;
		this.object = object;
	}
	
	public ObjectInComposite(String id, Figure figure, ObjectMoverResizer fmr, Control object, ObjectInComposite parent) {
		this.id = id;
		this.figure = figure;
		this.mr = fmr;
		this.object = object;
		this.parent= parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	public ObjectMoverResizer getFmr() {
		return mr;
	}

	public void setMr(ObjectMoverResizer mr) {
		this.mr = mr;
	}

	public Control getObject() {
		return object;
	}

	public void setRoot(boolean isRoot){
		this.isRoot=isRoot;
	}
	
	public boolean isRoot(){
		return isRoot;
	}
	
	public ObjectInComposite getParent(){
		return parent;
	}
}
