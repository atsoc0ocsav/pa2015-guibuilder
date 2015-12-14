package pa.iscde.testextension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Spinner;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pt.iscte.pidesco.guibuilder.extensions.WidgetInCompositeImpl;

public class SuperSpinner extends WidgetInCompositeImpl {
	private final String VARIABLE_PREFIX = "spinner";
	private final int DEFAULT_MINIMUM = 0;
	private final int DEFAULT_MAXIMUM = 1000;
	private final int DEFAULT_SELECTION = 500;
	private final int DEFAULT_INCREMET = 1;

	private String nameSpinner = "Spinner Super";

	private int minimum = 0;
	private int maximum = 0;
	private int selection = 0;
	private int increment = 0;

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
		super.createWidget(canvas, location, size);

		control = new Spinner(canvas, SWT.BORDER);
		((Spinner) control).setMinimum(DEFAULT_MINIMUM);
		((Spinner) control).setMaximum(DEFAULT_MAXIMUM);
		((Spinner) control).setSelection(DEFAULT_SELECTION);
		((Spinner) control).setIncrement(DEFAULT_INCREMET);
		((Spinner) control).setLocation(location);
		((Spinner) control).setSize(size);
		((Spinner) control).setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		((Spinner) control).setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));

		minimum = DEFAULT_MINIMUM;
		maximum = DEFAULT_MAXIMUM;
		selection = DEFAULT_SELECTION;
		increment = DEFAULT_INCREMET;

		this.backgroundColor = control.getBackground();
		this.foregroundColor = control.getForeground();
		this.enabled = control.isEnabled();

		System.out.println("Background: " + control.getBackground());
	}

	@Override
	public List<String> generateWidgetCode(CodeTarget target, String containerName, int count) {
		List<String> code = new ArrayList<String>();
		String elementName = VARIABLE_PREFIX + count;

		code.add(elementName);

		System.out.println("Back: " + backgroundColor);

		switch (target) {
		case SWING:
			code.add("SpinnerModel " + elementName + " = new SpinnerNumberModel(" + selection + "," + minimum + ","
					+ maximum + "," + increment + ");");
			code.add("JSpinner " + elementName + " = new JSpinner(spinnerModel" + count + ");");
			code.add(elementName + ".setBackground(new Color(" + backgroundColor.getRed() + ","
					+ backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));");
			code.add(elementName + ".setForeground(new Color(" + foregroundColor.getRed() + ","
					+ foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));");
			break;
		case SWT:
			code.add("Spinner " + elementName + " = new Spinner(" + containerName + ", SWT.BORDER);");
			code.add(elementName + ".setMinimum(" + minimum + ");");
			code.add(elementName + ".setMaximum(" + maximum + ");");
			code.add(elementName + ".setSelection(" + selection + ");");
			code.add(elementName + ".setIncrement(" + increment + ");");
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
