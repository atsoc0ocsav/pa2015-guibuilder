package pt.iscte.pidesco.guibuilder.extensions;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator;
import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.model.compositeContents.ComponentInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public interface WidgetInterface extends ComponentInComposite{
	
	public String getWidgetName();

	public void setWidgetName(String name);

	public void createWidget(Canvas canvas);

	public Control getWidget();

	// The first line of generated code has to have the name of the widget
	// variable!!!!!!!!!
	public String[] generateWidgetCode(CodeGenerator.CodeTarget target, String containerName,int count);

}
