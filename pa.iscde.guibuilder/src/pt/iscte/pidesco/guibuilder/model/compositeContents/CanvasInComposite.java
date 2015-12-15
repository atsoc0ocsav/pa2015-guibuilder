package pt.iscte.pidesco.guibuilder.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pt.iscte.pidesco.guibuilder.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.ui.CanvasResizer;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;
import pt.iscte.pidesco.guibuilder.ui.ObjectMoverResizer;

public class CanvasInComposite extends ObjectInComposite {
	private ObjectMoverResizer canvasResizer;
	private String label;
	public GUIBuilderLayout activeLayout;

	public CanvasInComposite(Figure figure, ObjectMoverResizer canvasResizer) {
		this(figure,canvasResizer,null,null,"");
	}

	public CanvasInComposite(Figure figure, ObjectMoverResizer canvasResizer, Point location, Point size) {
		this(figure,canvasResizer,location,size,"");
	}

	public CanvasInComposite(Figure figure, ObjectMoverResizer canvasResizer, Point location, Point size,
			String label) {
		super(GUIBuilderObjectFamily.CANVAS, new ContextMenuItem[] { ContextMenuItem.CHANGE_NAME,
				ContextMenuItem.GENERATE_CODE, ContextMenuItem.PLUGIN });
		this.figure = figure;
		this.canvasResizer = canvasResizer;
		this.location = location;
		this.size = size;
		this.label = label;

		activeLayout = GUIBuilderLayout.ABSOLUTE;
	}

	public void setLabel(String label) {
		this.label = label;

		if (canvasResizer instanceof CanvasResizer) {
			((CanvasResizer) canvasResizer).setText(label);
		}
	}

	public String getLabel() {
		return label;
	}

	public ObjectMoverResizer getCanvasResizer() {
		return canvasResizer;
	}

	public GuiLabels.GUIBuilderLayout getActiveLayout() {
		return activeLayout;
	}

	public void setActiveLayout(GUIBuilderLayout activeLayout) {
		this.activeLayout = activeLayout;
	}

	public CanvasResizer getObjectMoverResizer() {
		// TODO Auto-generated method stub
		return null;
	}
}
