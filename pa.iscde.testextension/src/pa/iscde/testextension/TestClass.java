package pa.iscde.testextension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import pa.iscde.test.ExtensionTestInterface;
import pt.iscte.pidesco.guibuilder.extensions.WidgetInterface;

public class TestClass implements ExtensionTestInterface, WidgetInterface {
	private Button button;
	private Text txtField;
	private Spinner spinner;
	private String nameButton = "Button Super";
	private String nameTextfield = "TextField Super";
	private String nameSpinner = "Spinner Super";

	public TestClass() {

	}

	@Override
	public String getHelloWorld() {
		return "Hello World from TestClass";
	}

	@Override
	public String[] getWidgetNames() {
		// TODO Auto-generated method stub
		return new String[] { nameButton, nameTextfield, nameSpinner };
	}

	@Override
	public void createWidgets(Canvas canvas) {
		button = new Button(canvas, SWT.BORDER);
		button.setText(nameButton);
		button.setEnabled(true);

		txtField = new Text(canvas, SWT.BORDER);
		txtField.setText(nameTextfield);

		txtField.setEditable(false);
		txtField.setEnabled(true);

		spinner = new Spinner(canvas, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(1000);
		spinner.setSelection(500);
		spinner.setIncrement(1);
		spinner.setPageIncrement(100);

	}

	@Override
	public Control[] getWidgets() {
		return new Control[] { button, txtField, spinner };

	}
}
