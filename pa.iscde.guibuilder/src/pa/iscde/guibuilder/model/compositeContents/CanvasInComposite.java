package pa.iscde.guibuilder.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pa.iscde.guibuilder.model.ObjectInCompositeImpl;
import pa.iscde.guibuilder.ui.CanvasResizer;
import pa.iscde.guibuilder.ui.GuiLabels;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;
import pa.iscde.guibuilder.ui.ObjectMoverResizer;

public class CanvasInComposite extends ObjectInCompositeImpl{
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
