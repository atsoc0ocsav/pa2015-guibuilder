package pt.iscte.pidesco.guibuilder.internal;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.extensions.ExtensionPointsData;
import pt.iscte.pidesco.guibuilder.internal.codeGenerator.GenerateObjectsInterface;
import pt.iscte.pidesco.guibuilder.internal.graphic.GuiBuilderObjFactory;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class GeneratorCode {

	private StringBuffer code = new StringBuffer("");

	public GeneratorCode(GeneratorCode.selectTarget target, String titleFrame, ArrayList<ObjectInComposite> components,
			GuiBuilderView guiBuilderView) {

		// TODO refactoring.....
		System.err.println("I need refactoring!!!!!!!!!!");

		SwingOjects swingObjects = new SwingOjects();
		SWTObjects swtObjects = new SWTObjects();

		////////////////////// ADD CODE BY
		////////////////////// DEFAULT//////////////////////////////////////////
		if (target.equals(selectTarget.SWING)) {
			code.append(swingObjects.generateImports());

			code.append(swingObjects.generateStartClass()).append(swingObjects.generateStartConstructorClass());

			code.append(swingObjects.generateFrame(
					new String[] { titleFrame, String.valueOf(guiBuilderView.getCanvasSize().width),
							String.valueOf(guiBuilderView.getCanvasSize().height) }));
		} else if (target.equals(selectTarget.SWT)) {
			code.append(swtObjects.generateImports());

			code.append(swtObjects.generateStartClass()).append(swtObjects.generateStartConstructorClass());

			code.append(swtObjects.generateFrame(
					new String[] { titleFrame, String.valueOf(guiBuilderView.getCanvasSize().width),
							String.valueOf(guiBuilderView.getCanvasSize().height) }));
		}
		
		///////////////////////////////////// ADD
		///////////////////////////////////// COMPONENTS/////////////////////////////////////////////////////
		for (ObjectInComposite objectInComposite : components) {

			if (objectInComposite.getId().toLowerCase().contains("widget")) {

				ExtensionPointsData extensionPointsData = new ExtensionPointsData(guiBuilderView);

				for (int i = 1; i < extensionPointsData.getCodeWidget().length; i++) {
					code.append("\t \t " + String.format(extensionPointsData.getCodeWidget()[i], "shell") + "\n");
				}
				if (target.equals(selectTarget.SWING)) {
					code.append("\t \t frame.add(" + extensionPointsData.getCodeWidget()[0] + "); \n");
				}
			}

			else if (objectInComposite.getId().toLowerCase().contains("button")) {
				Button btn = (Button) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects
							.generateButton(new String[] { btn.getText(), String.valueOf(btn.getLocation().x),
									String.valueOf(btn.getLocation().y), String.valueOf(btn.getSize().x),
									String.valueOf(btn.getSize().y), String.valueOf(btn.isEnabled()) }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(
							swtObjects.generateButton(new String[] { btn.getText(), String.valueOf(btn.getLocation().x),
									String.valueOf(btn.getLocation().y), String.valueOf(btn.getSize().x),
									String.valueOf(btn.getSize().y), String.valueOf(btn.isEnabled()) }));
				}
			} else if (objectInComposite.getId().toLowerCase().contains("label")) {
				Label label = (Label) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects
							.generateLabel(new String[] { label.getText(), String.valueOf(label.getLocation().x),
									String.valueOf(label.getLocation().y), String.valueOf(label.getSize().x),
									String.valueOf(label.getSize().y), String.valueOf(label.isEnabled()) }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects
							.generateLabel(new String[] { label.getText(), String.valueOf(label.getLocation().x),
									String.valueOf(label.getLocation().y), String.valueOf(label.getSize().x),
									String.valueOf(label.getSize().y), String.valueOf(label.isEnabled()) }));
				}
			} else if (objectInComposite.getId().toLowerCase().contains("text field")) {
				Text textField = (Text) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects.generateTextField(new String[] { textField.getText(),
							String.valueOf(textField.getLocation().x), String.valueOf(textField.getLocation().y),
							String.valueOf(textField.getSize().x), String.valueOf(textField.getSize().y),
							String.valueOf(textField.isEnabled()), String.valueOf(textField.getEditable()) }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects.generateTextField(new String[] { textField.getText(),
							String.valueOf(textField.getLocation().x), String.valueOf(textField.getLocation().y),
							String.valueOf(textField.getSize().x), String.valueOf(textField.getSize().y),
							String.valueOf(textField.isEnabled()), String.valueOf(textField.getEditable()) }));
				}
			} else if (objectInComposite.getId().toLowerCase().contains("check box")) {
				Button checkBox = (Button) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects.generateCheckBox(
							new String[] { checkBox.getText(), String.valueOf(checkBox.getLocation().x),
									String.valueOf(checkBox.getLocation().y), String.valueOf(checkBox.getSize().x),
									String.valueOf(checkBox.getSize().y), String.valueOf(checkBox.isEnabled()) }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects.generateCheckBox(
							new String[] { checkBox.getText(), String.valueOf(checkBox.getLocation().x),
									String.valueOf(checkBox.getLocation().y), String.valueOf(checkBox.getSize().x),
									String.valueOf(checkBox.getSize().y), String.valueOf(checkBox.isEnabled()) }));
				}
			}

		}

		if (target.equals(selectTarget.SWING)) {
			code.append(swingObjects.generateEndConstructorClass()).append(swingObjects.generateEndClass());
		} else if (target.equals(selectTarget.SWT)) {
			code.append(swtObjects.generateEndConstructorClass()).append(swtObjects.generateEndClass());
		}

		System.out.println(code);
	}

	public static enum selectTarget {

		SWING("swing"), SWT("swt");

		private String target;

		private selectTarget(String target) {
			this.target = target;
		}

		public String getTarget() {
			return target;
		}
	}

	private class SwingOjects implements GenerateObjectsInterface {
		private int appendNameComponent = -1;

		@Override
		public String generateFrame(String[] parameters) {

			return new String("\n \t \t JFrame frame = new JFrame(\"" + parameters[0] + "\"); \n"
					+ "\t \t frame.setSize(" + parameters[1] + "," + parameters[2] + "); \n"
					+ "\t \t frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); \n"
					+ "\t \t frame.setVisible(true); \n \n");
		}

		@Override
		public String generateButton(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t JButton jbutton" + appendNameComponent + " = new JButton(\"" + parameters[0]
					+ "\"); \n" + "\t \t jbutton" + appendNameComponent + ".setLocation(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t jbutton" + appendNameComponent + ".setSize(" + parameters[3]
					+ "," + parameters[4] + "); \n" + "\t \t jbutton" + appendNameComponent + ".setEnable("
					+ parameters[5] + "); \n" + "\t \t frame.add(jbutton" + appendNameComponent + "); \n \n");
		}

		@Override
		public String generateLabel(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t JLabel jlabel" + appendNameComponent + " = new JLabel(\"" + parameters[0]
					+ "\"); \n" + "\t \t jlabel" + appendNameComponent + ".setLocation(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t jlabel" + appendNameComponent + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t jlabel" + appendNameComponent + ".setEnable(" + parameters[5]
					+ "); \n" + "\t \t frame.add(jlabel" + appendNameComponent + "); \n \n");
		}

		@Override
		public String generateTextField(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t JTextField jTextField" + appendNameComponent + " = new JTextField(\""
					+ parameters[0] + "\"); \n" + "\t \t jTextField" + appendNameComponent + ".setLocation("
					+ parameters[1] + "," + parameters[2] + "); \n" + "\t \t jTextField" + appendNameComponent
					+ ".setSize(" + parameters[3] + "," + parameters[4] + "); \n" + "\t \t jTextField"
					+ appendNameComponent + ".setEnable(" + parameters[5] + "); \n" + "\t \t jTextField"
					+ appendNameComponent + ".setEditable(" + parameters[6] + "); \n" + "\t \t frame.add(jTextField"
					+ appendNameComponent + "); \n \n");
		}

		@Override
		public String generateCheckBox(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t JCheckBox jcheckBox" + appendNameComponent + " = new JCheckBox(\""
					+ parameters[0] + "\"); \n" + "\t \t jcheckBox" + appendNameComponent + ".setLocation("
					+ parameters[1] + "," + parameters[2] + "); \n" + "\t \t jcheckBox" + appendNameComponent
					+ ".setSize(" + parameters[3] + "," + parameters[4] + "); \n" + "\t \t jcheckBox"
					+ appendNameComponent + ".setEnable(" + parameters[5] + "); \n" + "\t \t frame.add(jcheckBox"
					+ appendNameComponent + "); \n \n");
		}

		@Override
		public String generateAction(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateStartClass() {
			return new String("public class Window { \n");
		}

		@Override
		public String generateEndClass() {
			return new String("\n }");
		}

		@Override
		public String generateStartConstructorClass() {
			return new String("\n \t public Window(){ \n");
		}

		@Override
		public String generateEndConstructorClass() {
			return new String("\t }");
		}

		@Override
		public String generateImports() {
			return new String("import javax.swing.*; \n \n");
		}

	}

	private class SWTObjects implements GenerateObjectsInterface {
		private int appendNameComponent = -1;

		@Override
		public String generateFrame(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t Display display = new Display(); \n"
					+ "\t \t Shell shell = new Shell(display); \n" + "\t \t shell.setSize(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t shell.setText(\"" + parameters[0] + "\"); \n"
					+ "\t \t shell.open(); \n \n" + "\t \t while (!shell.isDisposed()) { \n"
					+ "\t \t \t if (!display.readAndDispatch()){ \n" + "\t \t \t \t display.sleep(); \n"
					+ "\t \t \t } \n" + "\t \t \t display.dispose(); \n" + "\t \t } \n \n");

		}

		@Override
		public String generateButton(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t Button button" + appendNameComponent + " = new Button(shell,SWT.BORDER); \n"
					+ "\t \t button" + appendNameComponent + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t button"
					+ appendNameComponent + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n"
					+ "\t \t button" + appendNameComponent + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n"
					+ "\t \t button" + appendNameComponent + ".setEnable(" + parameters[5] + "); \n \n");
		}

		@Override
		public String generateLabel(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t Label label" + appendNameComponent + " = new Label(shell,SWT.BORDER); \n"
					+ "\t \t label" + appendNameComponent + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t label"
					+ appendNameComponent + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n"
					+ "\t \t label" + appendNameComponent + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n"
					+ "\t \t label" + appendNameComponent + ".setEnable(" + parameters[5] + "); \n \n");
		}

		@Override
		public String generateTextField(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t Text textfield" + appendNameComponent + " = new Text(shell,SWT.BORDER); \n"
					+ "\t \t textfield" + appendNameComponent + ".setText(\"" + parameters[0] + "\"); \n"
					+ "\t \t textfield" + appendNameComponent + ".setLocation(" + parameters[1] + "," + parameters[2]
					+ "); \n" + "\t \t textfield" + appendNameComponent + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t textfield" + appendNameComponent + ".setEnable(" + parameters[5]
					+ "); \n" + "\t \t textfield" + appendNameComponent + ".setEditable(" + parameters[6] + "); \n \n");
		}

		@Override
		public String generateCheckBox(String[] parameters) {
			appendNameComponent++;
			return new String("\n \t \t Button checkBox" + appendNameComponent + " = new Button(shell,SWT.CHECK); \n"
					+ "\t \t checkBox" + appendNameComponent + ".setText(\"" + parameters[0] + "\"); \n"
					+ "\t \t checkBox" + appendNameComponent + ".setLocation(" + parameters[1] + "," + parameters[2]
					+ "); \n" + "\t \t checkBox" + appendNameComponent + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t checkBox" + appendNameComponent + ".setEnable(" + parameters[5]
					+ "); \n \n");
		}

		@Override
		public String generateAction(String[] parameters) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String generateStartClass() {
			return new String("public class Window { \n");
		}

		@Override
		public String generateEndClass() {
			return new String("\n }");
		}

		@Override
		public String generateStartConstructorClass() {
			return "\n \t public Window(){ \n";
		}

		@Override
		public String generateEndConstructorClass() {
			return "\t }";
		}

		@Override
		public String generateImports() {
			return new String("import org.eclipse.swt.*; \n \n");
		}

	}

}
