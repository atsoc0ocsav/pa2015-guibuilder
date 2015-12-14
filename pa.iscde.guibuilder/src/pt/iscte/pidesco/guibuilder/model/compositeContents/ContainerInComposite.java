package pt.iscte.pidesco.guibuilder.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderContainer;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class ContainerInComposite extends ObjectInComposite {
	private final GUIBuilderContainer containerType;
	private ObjectMoverResizer objectMoverResizer;
	public GUIBuilderLayout activeLayout;

	public ContainerInComposite(GUIBuilderContainer containerType, Figure figure,
			ObjectMoverResizer objectMoverResizer) {
		super(GUIBuilderObjectFamily.CONTAINERS, new ContextMenuItem[] { ContextMenuItem.PLUGIN });
		this.containerType = containerType;
		this.figure = figure;
		this.objectMoverResizer = objectMoverResizer;

		super.size = new Point(figure.getSize().width, figure.getSize().height);
		super.location = new Point(figure.getLocation().x, figure.getLocation().y);

		activeLayout = GUIBuilderLayout.ABSOLUTE;
	}

	public ObjectMoverResizer getObjectMoverResizer() {
		return objectMoverResizer;
	}

	public GUIBuilderContainer getContainerType() {
		return containerType;
	}

	public GuiLabels.GUIBuilderLayout getActiveLayout() {
		return activeLayout;
	}

	public void setActiveLayout(GUIBuilderLayout activeLayout) {
		this.activeLayout = activeLayout;
	}
}
