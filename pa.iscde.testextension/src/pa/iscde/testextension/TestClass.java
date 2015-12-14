package pa.iscde.testextension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Spinner;

import pa.iscde.test.ExtensionTestInterface;
import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pt.iscte.pidesco.guibuilder.extensions.WidgetInterface;
import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public class TestClass implements ExtensionTestInterface, WidgetInterface {

	private Spinner spinner;
	private String nameSpinner = "Spinner Super";

	public TestClass() {

	}

	@Override
	public String getHelloWorld() {
		return "Hello World from TestClass";
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
	public void createWidget(Canvas canvas) {

		spinner = new Spinner(canvas, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(1000);
		spinner.setSelection(500);
		spinner.setIncrement(1);
		spinner.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		spinner.setForeground(new Color(Display.getCurrent(), 255, 255, 255));

	}

	@Override
	public Control getWidget() {
		return spinner;
	}

	@Override
	public String[] generateWidgetCode(CodeTarget target, String containerName) {
		String[] code = null;
		switch (target) {
		case SWING:
			code = new String[] { "spinner", "SpinnerModel spinnerModel = new SpinnerNumberModel(500,0,1000,1);",
					"JSpinner spinner = new JSpinner(spinnerModel);", "spinner.setBackground(new Color(255,255,255));",
					"spinner.setForeground(new Color(255,255,255));" };
			break;
		case SWT:
			code = new String[] { "spinner", "Spinner spinner = new Spinner(" + containerName + ", SWT.BORDER);",
					"spinner.setMinimum(0);", "spinner.setMaximum(1000);", "spinner.setSelection(500);",
					"spinner.setIncrement(1);", "spinner.setBackground(new Color(Display.getCurrent(),255,255,255));",
					"spinner.setForeground(new Color(Display.getCurrent(),255,255,255));" };
			break;
		default:
			break;
		}
		return code;
	}

	@Override
	public GUIBuilderComponent getComponentType() {

		return GUIBuilderComponent.WIDGET;
	}

	@Override
	public void setBackgroundColor(Color color) {
		spinner.setBackground(color);

	}

	@Override
	public Color getBackgroundColor() {

		return spinner.getBackground();
	}

	@Override
	public void setForegroundColor(Color color) {
		spinner.setForeground(color);

	}

	@Override
	public Color getForegroundColor() {

		return spinner.getForeground();
	}

	@Override
	public void setEnabled(boolean enabled) {
		spinner.setEnabled(enabled);

	}

	@Override
	public boolean isEnabled() {
		return spinner.isEnabled();
	}

}
