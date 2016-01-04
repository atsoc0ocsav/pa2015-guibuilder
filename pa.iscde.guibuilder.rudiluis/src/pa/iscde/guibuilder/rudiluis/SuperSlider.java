package pa.iscde.guibuilder.rudiluis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Slider;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.WidgetInComposite;

public class SuperSlider extends WidgetInComposite {

	private final String VARIABLE_PREFIX = "slider";
	private final int DEFAULT_MINIMUM = 0;
	private final int DEFAULT_MAXIMUM = 1000;
	private final int DEFAULT_SELECTION = 500;
	private final int DEFAULT_INCREMET = 1;

	private String widgetName = "Slider Super";

	private int sliderMinimum = 0;
	private int sliderMaximum = 0;
	private int sliderSelection = 0;
	private int sliderIncrement = 0;

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
		// super.createWidget(canvas, location, size);
		this.location = location;
		this.size = size;

		control = new Slider(canvas, SWT.BORDER);
		((Slider) control).setMinimum(DEFAULT_MINIMUM);
		((Slider) control).setMaximum(DEFAULT_MAXIMUM);
		((Slider) control).setSelection(DEFAULT_SELECTION);
		((Slider) control).setIncrement(DEFAULT_INCREMET);
		((Slider) control).setLocation(location);
		((Slider) control).setSize(size);
		((Slider) control).setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		((Slider) control).setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		sliderMinimum = DEFAULT_MINIMUM;
		sliderMaximum = DEFAULT_MAXIMUM;
		sliderSelection = DEFAULT_SELECTION;
		sliderIncrement = DEFAULT_INCREMET;

		this.backgroundColor = control.getBackground();
		this.foregroundColor = control.getForeground();
		this.enabled = control.isEnabled();

	}

	@Override
	public List<String> generateWidgetCode(CodeTarget target, String containerName, int count) {
		List<String> code = new ArrayList<String>();
		String elementName = VARIABLE_PREFIX + count;

		code.add(elementName);
		switch (target) {
		case SWING:
			code.add("JSlider " + elementName + " = new JSlider(" + sliderMinimum + "," + sliderMaximum + ","
					+ sliderSelection + ");");
			code.add(elementName + ".setValue(" + sliderSelection + ");");
			code.add(elementName + ".setLocation(" + location.x + "," + location.y + ");");
			code.add(elementName + ".setSize(" + size.x + "," + size.y + ");");
			code.add(elementName + ".setBackground(new Color(" + backgroundColor.getRed() + ","
					+ backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));");
			code.add(elementName + ".setForeground(new Color(" + foregroundColor.getRed() + ","
					+ foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));");
			break;
		case SWT:
			code.add("Slider " + elementName + " = new Slider(" + containerName + ", SWT.BORDER);");
			code.add(elementName + ".setMinimum(" + sliderMinimum + ");");
			code.add(elementName + ".setMaximum(" + sliderMaximum + ");");
			code.add(elementName + ".setSelection(" + sliderSelection + ");");
			code.add(elementName + ".setIncrement(" + sliderIncrement + ");");
			code.add(elementName + ".setLocation(" + location.x + "," + location.y + ");");
			code.add(elementName + ".setSize(" + size.x + "," + size.y + ");");
			code.add(elementName + ".setBackground(new Color(Display.getCurrent()," + backgroundColor.getRed() + ","
					+ backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));");
			code.add(elementName + ".setForeground(new Color(Display.getCurrent()," + foregroundColor.getRed() + ","
					+ foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));");
			break;
		default:
			throw new IllegalArgumentException("Switch case not defined!");
		}
		return code;
	}
}
