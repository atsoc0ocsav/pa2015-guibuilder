package pt.iscte.pidesco.guibuilder.extensions;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.internal.GeneratorCode;

public interface WidgetInterface {

	public String getWidgetName();

	public void setWidgetName(String name);

	public void createWidget(Canvas canvas);

	public Control getWidget();

	public String[] generateCodeWidget(GeneratorCode.selectTarget target);

}
