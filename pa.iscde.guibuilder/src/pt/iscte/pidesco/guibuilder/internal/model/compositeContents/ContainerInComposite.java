package pt.iscte.pidesco.guibuilder.internal.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.internal.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderContainer;

public class ContainerInComposite extends ObjectInComposite {
	private final GUIBuilderContainer containerType;
	private ObjectMoverResizer objectMoverResizer;

	public ContainerInComposite(GUIBuilderContainer containerType, Control control, Figure figure,
			ObjectMoverResizer objectMoverResizer) {
		//super.objectFamily = CONTAINER;
		this.containerType = containerType;
		this.figure = figure;
		this.objectMoverResizer = objectMoverResizer;
	}
	
	public ObjectMoverResizer getObjectMoverResizer() {
		return objectMoverResizer;
	}
	
	public GUIBuilderContainer getContainerType() {
		return containerType;
	}
}
