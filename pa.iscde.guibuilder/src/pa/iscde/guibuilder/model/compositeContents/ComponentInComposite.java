package pa.iscde.guibuilder.model.compositeContents;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import pa.iscde.guibuilder.model.ObjectInComposite;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;
import pa.iscde.guibuilder.ui.ObjectMoverResizer;

public interface ComponentInComposite extends ObjectInComposite {
	public GUIBuilderComponent getComponentType();

	public Control getControl();

	public ObjectMoverResizer getObjectMoverResizer();

	public void setObjectMoverResizer(ObjectMoverResizer mr);

	public void setBackgroundColor(Color color);

	public Color getBackgroundColor();

	public void setForegroundColor(Color color);

	public Color getForegroundColor();

	public void setText(String str);

	public String getText();

	public ComponentInCompositeImpl setTextAndReturnObject(String str);

	public void setEnabled(boolean enabled);

	public boolean isEnabled();
}
