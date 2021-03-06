package pa.iscde.guibuilder.rudiluis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Spinner;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.WidgetInComposite;

public class SuperSpinner extends WidgetInComposite {
	private final String VARIABLE_PREFIX = "spinner";
	private final int DEFAULT_MINIMUM = 0;
	private final int DEFAULT_MAXIMUM = 1000;
	private final int DEFAULT_SELECTION = 500;
	private final int DEFAULT_INCREMET = 1;

	private String nameSpinner = "Spinner Super";

	private int sliderMinimum = 0;
	private int sliderMaximum = 0;
	private int sliderSelection = 0;
	private int sliderIncrement = 0;

	public SuperSpinner() {
		super(new ContextMenuItem[] { ContextMenuItem.SET_COLOR, ContextMenuItem.PLUGIN });
	}

	@Override
	public String getWidgetName() {
		return nameSpinner;
	}

	@Override
	public void setWidgetName(String nameSpinner) {
		this.nameSpinner = nameSpinner;
	}

	@Override
	public void createWidget(Canvas canvas, Point location, Point size) {
		this.location = location;
		this.size = size;

		Spinner spinner = new Spinner(canvas, SWT.BORDER);
		spinner.setMinimum(DEFAULT_MINIMUM);
		spinner.setMaximum(DEFAULT_MAXIMUM);
		spinner.setSelection(DEFAULT_SELECTION);
		spinner.setIncrement(DEFAULT_INCREMET);
		spinner.setLocation(location);
		spinner.setSize(size);
		spinner.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		spinner.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		sliderMinimum = DEFAULT_MINIMUM;
		sliderMaximum = DEFAULT_MAXIMUM;
		sliderSelection = DEFAULT_SELECTION;
		sliderIncrement = DEFAULT_INCREMET;

		this.control = spinner;
		this.backgroundColor = spinner.getBackground();
		this.foregroundColor = spinner.getForeground();
		this.enabled = spinner.isEnabled();
	}

	@Override
	public List<String> generateWidgetCode(CodeTarget target, String containerName, int count) {
		List<String> code = new ArrayList<String>();
		String elementName = VARIABLE_PREFIX + count;

		code.add(elementName);
		switch (target) {
		case SWING:
			code.add("SpinnerModel spinnerModel"+count+" = new SpinnerNumberModel(" + sliderSelection + ","
					+ sliderMinimum + "," + sliderMaximum + "," + sliderIncrement + ");");
			code.add("JSpinner " + elementName + " = new JSpinner(spinnerModel" + count + ");");
			code.add(elementName + ".setLocation(" + location.x + "," + location.y + ");");
			code.add(elementName + ".setSize(" + size.x + "," + size.y + ");");
			code.add(elementName + ".setBackground(new Color(" + backgroundColor.getRed() + ","
					+ backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));");
			code.add(elementName + ".setForeground(new Color(" + foregroundColor.getRed() + ","
					+ foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));");
			break;
		case SWT:
			code.add("Spinner " + elementName + " = new Spinner(" + containerName + ", SWT.BORDER);");
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

	@Override
	public String getText() {
		throw new UnsupportedOperationException();
	}
}
