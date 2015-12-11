package pt.iscte.pidesco.guibuilder.internal.model.compositeContents;

import org.eclipse.draw2d.Figure;

import pt.iscte.pidesco.guibuilder.internal.graphic.CanvasResizer;
import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.internal.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class CanvasInComposite extends ObjectInComposite {
	private ObjectMoverResizer canvasResizer;
	private String label;
	public GUIBuilderLayout activeLayout;

	public CanvasInComposite(Figure figure, ObjectMoverResizer canvasResizer) {
		super.objectFamily = GUIBuilderObjectFamily.CANVAS;
		this.figure = figure;
		this.canvasResizer = canvasResizer;
	}

	public void setLabel(String label) {
		this.label = label;

		if (canvasResizer instanceof CanvasResizer) {
			((CanvasResizer) canvasResizer).setText(label);
		}

		activeLayout = GUIBuilderLayout.ABSOLUTE;
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
}
