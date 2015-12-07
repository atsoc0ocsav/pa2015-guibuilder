package pa.iscde.testextension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import pa.iscde.test.ExtensionTestInterface;
import pt.iscte.pidesco.guibuilder.extensions.WidgetInterface;
import pt.iscte.pidesco.guibuilder.internal.GeneratorCode.selectTarget;

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

	}

	@Override
	public Control getWidget() {
		return spinner;
	}

	@Override
	public String[] generateCodeWidget(selectTarget target) {
		
		String[] code = null;
		switch (target) {
		case SWING:
			code = new String[] { "spinner", "SpinnerModel spinnerModel = new SpinnerNumberModel(500,0,1000,1);",
					"JSpinner spinner = new JSpinner(spinnerModel);" };
			break;
		case SWT:
			code = new String[] { "spinner", "Spinner spinner = new Spinner(canvas, SWT.BORDER);",
					"spinner.setMinimum(0);", "spinner.setMaximum(1000);", "spinner.setSelection(500);",
					"spinner.setIncrement(1);" };
			break;
		default:
			break;
		}
		return code;
	}

}
