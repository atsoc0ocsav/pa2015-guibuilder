package pa.iscde.guibuilder.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Point;

import pa.iscde.guibuilder.model.ObjectInCompositeImpl;
import pa.iscde.guibuilder.ui.GuiLabels;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderContainer;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;
import pa.iscde.guibuilder.ui.ObjectMoverResizer;

public class ContainerInComposite extends ObjectInCompositeImpl {
	private final GUIBuilderContainer containerType;
	private ObjectMoverResizer objectMoverResizer;
	public GUIBuilderLayout activeLayout;
	private String text;

	public ContainerInComposite(GUIBuilderContainer containerType, Figure figure,
			ObjectMoverResizer objectMoverResizer) {
		super(GUIBuilderObjectFamily.CONTAINERS, new ContextMenuItem[] { ContextMenuItem.PLUGIN });
		this.containerType = containerType;
		this.figure = figure;
		this.objectMoverResizer = objectMoverResizer;

		super.size = new Point(figure.getSize().width, figure.getSize().height);
		super.location = new Point(figure.getLocation().x, figure.getLocation().y);

		activeLayout = GUIBuilderLayout.ABSOLUTE;
		text = "";
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

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
