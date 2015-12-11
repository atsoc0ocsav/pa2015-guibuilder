package pt.iscte.pidesco.guibuilder.internal.model.compositeContents;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public interface ComponentInComposite {
	public GUIBuilderComponent getComponentType();
	
	public Control getControl();
	public ObjectMoverResizer getObjectMoverResizer();
	
	public void setBackgroundColor(Color color);
	public Color getBackgroundColor();
	
	public void setForegroundColor(Color color);
	public Color getForegroundColor();
	
	public void setText(String str);
	public String getText();
	
	public void setEnabled(boolean enabled);
	public boolean isEnabled();
}