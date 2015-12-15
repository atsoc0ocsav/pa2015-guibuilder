package pa.iscde.testextension;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Slider;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pt.iscte.pidesco.guibuilder.extensions.WidgetInCompositeImpl;

public class SuperSlider extends WidgetInCompositeImpl {

	private final String VARIABLE_PREFIX = "slider";
	private final int DEFAULT_MINIMUM = 0;
	private final int DEFAULT_MAXIMUM = 1000;
	private final int DEFAULT_SELECTION = 500;
	private final int DEFAULT_INCREMET = 1;

	private String widgetName = "Slider Super";

	private int minimum = 0;
	private int maximum = 0;
	private int selection = 0;
	private int increment = 0;

	public SuperSlider() {
		super(new ContextMenuItem[] { ContextMenuItem.SET_COLOR, ContextMenuItem.PLUGIN });
	}

	@Override
	public String getWidgetName() {
		return widgetName;
	}

	@Override
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	@Override
	public void createWidget(Canvas canvas, Point location, Point size) {
		super.createWidget(canvas, location, size);

		control = new Slider(canvas, SWT.BORDER);
		((Slider) control).setMinimum(DEFAULT_MINIMUM);
		((Slider) control).setMaximum(DEFAULT_MAXIMUM);
		((Slider) control).setSelection(DEFAULT_SELECTION);
		((Slider) control).setIncrement(DEFAULT_INCREMET);
		((Slider) control).setLocation(location);
		((Slider) control).setSize(size);
		((Slider) control).setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		((Slider) control).setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		minimum = DEFAULT_MINIMUM;
		maximum = DEFAULT_MAXIMUM;
		selection = DEFAULT_SELECTION;
		increment = DEFAULT_INCREMET;

		this.backgroundColor = control.getBackground();
		this.foregroundColor = control.getForeground();
		this.enabled = control.isEnabled();

	}

	@Override
	public ArrayList<String> generateWidgetCode(CodeTarget target, String containerName, int count) {
		ArrayList<String> code = new ArrayList<String>();
		String elementName = VARIABLE_PREFIX + count;

		return code;
	}

}
