package pt.iscte.pidesco.guibuilder.extensions;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator;

public interface WidgetInCompositeInterface {

	public String getWidgetName();

	public void setWidgetName(String name);

	public void createWidget(Canvas canvas, Point location, Point size);

	// The first line of generated code has to have the name of the widget
	// variable!!!!!!!!!
	public List<String> generateWidgetCode(CodeGenerator.CodeTarget target, String containerName, int count);
}
