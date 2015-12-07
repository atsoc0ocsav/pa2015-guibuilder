package pt.iscte.pidesco.guibuilder.internal;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.widgets.Control;

public class ObjectInComposite {

	private String id;
	private Figure figure;
	private ObjectMoverResizer mr;
	private Control object;

	public ObjectInComposite(String id, Figure figure, ObjectMoverResizer fmr, Control object) {
		this.id = id;
		this.figure = figure;
		this.mr = fmr;
		this.object = object;
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

}
