package pa.iscde.guibuilder.extensions;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator;
import pa.iscde.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public abstract class WidgetInComposite extends ComponentInCompositeImpl {
	
	
	public WidgetInComposite(ContextMenuItem[] contextMenuItems) {
		super(GUIBuilderComponent.WIDGET, contextMenuItems);
	}
/**
 * 
 * @return
 */
	public abstract String getWidgetName();

	public abstract void setWidgetName(String widgetName);

	public abstract void createWidget(Canvas canvas, Point location, Point size);

	// The first line of generated code has to have the name of the widget
	// variable!!!!!!!!!
	public abstract ArrayList<String> generateWidgetCode(CodeGenerator.CodeTarget target, String containerName,
			int count);
}
