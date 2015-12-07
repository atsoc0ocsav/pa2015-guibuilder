package pt.iscte.pidesco.guibuilder.extensions;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;

public interface WidgetInterface {

	public String[] getWidgetNames();

	public void createWidgets(Canvas canvas);

	public Control[] getWidgets();
}
