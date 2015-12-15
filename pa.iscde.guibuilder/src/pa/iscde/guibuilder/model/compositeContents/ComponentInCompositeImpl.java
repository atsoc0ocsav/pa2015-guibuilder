package pa.iscde.guibuilder.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pa.iscde.guibuilder.model.ObjectInCompositeImpl;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;
import pa.iscde.guibuilder.ui.ObjectMoverResizer;

public class ComponentInCompositeImpl extends ObjectInCompositeImpl implements ComponentInComposite {
	protected final GUIBuilderComponent componentType;
	protected ObjectMoverResizer objectMoverResizer;
	protected Control control;

	protected Color backgroundColor;
	protected Color foregroundColor;
	protected String text;
	protected boolean enabled;

	public ComponentInCompositeImpl(GUIBuilderComponent componentType, Control control, Figure figure,
			ObjectMoverResizer objectMoverResizer, GUIBuilderObjectFamily objectFamily,
			ContextMenuItem[] contextMenuItems) {
		super(objectFamily, contextMenuItems);

		this.componentType = componentType;
		this.control = control;
		this.figure = figure;
		this.objectMoverResizer = objectMoverResizer;

		super.location = control.getLocation();
		super.size = control.getSize();
		this.backgroundColor = control.getBackground();
		this.foregroundColor = control.getForeground();
		this.enabled = control.isEnabled();
		this.text = "";
	}

	public ComponentInCompositeImpl(GUIBuilderComponent componentType, Control control, Figure figure,
			ObjectMoverResizer objectMoverResizer) {
		this(componentType, control, figure, objectMoverResizer, GUIBuilderObjectFamily.COMPONENTS,
				new ContextMenuItem[] { ContextMenuItem.CHANGE_NAME, ContextMenuItem.SET_COLOR,
						ContextMenuItem.PLUGIN });
	}

	public ComponentInCompositeImpl(GUIBuilderComponent componentType, ContextMenuItem[] contextMenuItems) {
		super(GUIBuilderObjectFamily.COMPONENTS, contextMenuItems);

		this.componentType = componentType;
	}

	@Override
	public GUIBuilderComponent getComponentType() {
		return componentType;
	}

	@Override
	public ObjectMoverResizer getObjectMoverResizer() {
		return objectMoverResizer;
	}

	@Override
	public void setObjectMoverResizer(ObjectMoverResizer objectMoverResizer) {
		this.objectMoverResizer = objectMoverResizer;
		;
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
		control.setBackground(color);
		figure.setBackgroundColor(color);
	}

	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public void setForegroundColor(Color color) {
		this.foregroundColor = color;
		control.setForeground(color);
		figure.setForegroundColor(color);
	}

	@Override
	public Color getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public void setText(String str) {
		this.text = str;

		if (control instanceof Button) {
			((Button) control).setText(str);
		} else if (control instanceof Label) {
			((Label) control).setText(str);
		} else if (control instanceof Text) {
			((Text) control).setText(str);
		}
	}

	public ComponentInCompositeImpl setTextAndReturnObject(String str) {
		setText(str);
		return this;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		control.setEnabled(enabled);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
