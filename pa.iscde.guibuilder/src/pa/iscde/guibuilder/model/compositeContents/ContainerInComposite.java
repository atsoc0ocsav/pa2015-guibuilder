package pa.iscde.guibuilder.model.compositeContents;

import pa.iscde.guibuilder.model.ObjectInComposite;
import pa.iscde.guibuilder.ui.GuiLabels;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderContainer;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pa.iscde.guibuilder.ui.ObjectMoverResizer;

public interface ContainerInComposite extends ObjectInComposite {
	public ObjectMoverResizer getObjectMoverResizer();

	public GUIBuilderContainer getContainerType();

	public GuiLabels.GUIBuilderLayout getActiveLayout();

	public void setActiveLayout(GUIBuilderLayout activeLayout);

	public void setText(String text);

	public String getText();
}
