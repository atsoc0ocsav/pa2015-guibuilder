package pt.iscte.pidesco.guibuilder.model.compositeContents;

import org.eclipse.draw2d.Figure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class ComponentInCompositeImpl extends ObjectInComposite implements ComponentInComposite {
	private final GUIBuilderComponent componentType;
	private ObjectMoverResizer objectMoverResizer;
	private Control control;

	private Color backgroundColor;
	private Color foregroundColor;
	private String text;
	private boolean enabled;

	public ComponentInCompositeImpl(GUIBuilderComponent componentType, Control control, Figure figure,
			ObjectMoverResizer objectMoverResizer) {
		super(GUIBuilderObjectFamily.COMPONENTS, new ContextMenuItem[] { ContextMenuItem.CHANGE_NAME,
				ContextMenuItem.SET_COLOR, ContextMenuItem.PLUGIN });

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

	public GUIBuilderComponent getComponentType() {
		return componentType;
	}

	public ObjectMoverResizer getObjectMoverResizer() {
		return objectMoverResizer;
	}

	public void setObjectMoverResizer(ObjectMoverResizer objectMoverResizer) {
		this.objectMoverResizer = objectMoverResizer;
		;
	}

	public Control getControl() {
		return control;
	}

	public Figure getFigure() {
		return figure;
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
		control.setBackground(color);
		figure.setBackgroundColor(color);
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
