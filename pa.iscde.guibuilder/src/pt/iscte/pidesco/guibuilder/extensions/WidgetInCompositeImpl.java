package pt.iscte.pidesco.guibuilder.extensions;

import pt.iscte.pidesco.guibuilder.model.compositeContents.ComponentInComposite;
import pt.iscte.pidesco.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public abstract class WidgetInCompositeImpl extends ComponentInCompositeImpl
		implements WidgetInCompositeInterface, ComponentInComposite {
	public WidgetInCompositeImpl(ContextMenuItem[] contextMenuItems) {
		super(GUIBuilderComponent.WIDGET, contextMenuItems);
	}

}
