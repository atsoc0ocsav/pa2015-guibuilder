package pt.iscte.pidesco.guibuilder.internal;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.internal.codeGenerator.GenerateObjectsInterface;
import pt.iscte.pidesco.guibuilder.internal.graphic.GuiBuilderObjFactory;

public class GeneratorCode {

	private StringBuffer code = new StringBuffer("");

	public GeneratorCode(GeneratorCode.selectTarget target, String titleFrame,
			ArrayList<ObjectInComposite> components) {

		// TODO refactoring.....
		System.err.println("I need refactoring!!!!!!!!!!");

		SwingOjects swingObjects = new SwingOjects();
		SWTObjects swtObjects = new SWTObjects();

		////////////////////// ADD CODE BY
		////////////////////// DEFAULT//////////////////////////////////////////
		if (target.equals(selectTarget.SWING)) {
			code.append(swingObjects.generateStartClass()).append(swingObjects.generateStartConstructorClass());

			code.append(swingObjects.generateFrame(
					new String[] { titleFrame, String.valueOf(GuiBuilderObjFactory.DEFAULT_CANVAS_INIT_DIM.width),
							String.valueOf(GuiBuilderObjFactory.DEFAULT_CANVAS_INIT_DIM.height) }));
		} else if (target.equals(selectTarget.SWT)) {
			code.append(swtObjects.generateStartClass()).append(swtObjects.generateStartConstructorClass());

			code.append(swtObjects.generateFrame(
					new String[] { titleFrame, String.valueOf(GuiBuilderObjFactory.DEFAULT_CANVAS_INIT_DIM.width),
							String.valueOf(GuiBuilderObjFactory.DEFAULT_CANVAS_INIT_DIM.height) }));
		}

		///////////////////////////////////// ADD
		///////////////////////////////////// COMPONENTS/////////////////////////////////////////////////////
		for (ObjectInComposite objectInComposite : components) {

			if (objectInComposite.getId().toLowerCase().contains("widget")) {
				Control object = (Control) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects
							.generateWidget(new String[] { "New Widget", String.valueOf(object.getLocation().x),
									String.valueOf(object.getLocation().y), String.valueOf(object.getSize().x),
									String.valueOf(object.getSize().y), String.valueOf(object.isEnabled()),
									objectInComposite.getId().toLowerCase().split("\t")[1] }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects
							.generateWidget(new String[] { "New Widget", String.valueOf(object.getLocation().x),
									String.valueOf(object.getLocation().y), String.valueOf(object.getSize().x),
									String.valueOf(object.getSize().y), String.valueOf(object.isEnabled()),
									objectInComposite.getId().toLowerCase().split("\t")[1] }));
				}
			}

			else if (objectInComposite.getId().toLowerCase().contains("button")) {
				Button btn = (Button) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects.generateButton(new String[] { btn.getText(),
							String.valueOf(btn.getLocation().x), String.valueOf(btn.getLocation().y),
							String.valueOf(btn.getSize().x), String.valueOf(btn.getSize().y),
							String.valueOf(btn.isEnabled()), objectInComposite.getId().toLowerCase().split("\t")[1] }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects.generateButton(new String[] { btn.getText(),
							String.valueOf(btn.getLocation().x), String.valueOf(btn.getLocation().y),
							String.valueOf(btn.getSize().x), String.valueOf(btn.getSize().y),
							String.valueOf(btn.isEnabled()), objectInComposite.getId().toLowerCase().split("\t")[1] }));
				}
			} else if (objectInComposite.getId().toLowerCase().contains("label")) {
				Label label = (Label) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects
							.generateLabel(new String[] { label.getText(), String.valueOf(label.getLocation().x),
									String.valueOf(label.getLocation().y), String.valueOf(label.getSize().x),
									String.valueOf(label.getSize().y), String.valueOf(label.isEnabled()),
									objectInComposite.getId().toLowerCase().split("\t")[1] }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects
							.generateLabel(new String[] { label.getText(), String.valueOf(label.getLocation().x),
									String.valueOf(label.getLocation().y), String.valueOf(label.getSize().x),
									String.valueOf(label.getSize().y), String.valueOf(label.isEnabled()),
									objectInComposite.getId().toLowerCase().split("\t")[1] }));
				}
			} else if (objectInComposite.getId().toLowerCase().contains("text field")) {
				Text textField = (Text) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects.generateTextField(new String[] { textField.getText(),
							String.valueOf(textField.getLocation().x), String.valueOf(textField.getLocation().y),
							String.valueOf(textField.getSize().x), String.valueOf(textField.getSize().y),
							String.valueOf(textField.isEnabled()), String.valueOf(textField.getEditable()),
							objectInComposite.getId().toLowerCase().split("\t")[1] }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects.generateTextField(new String[] { textField.getText(),
							String.valueOf(textField.getLocation().x), String.valueOf(textField.getLocation().y),
							String.valueOf(textField.getSize().x), String.valueOf(textField.getSize().y),
							String.valueOf(textField.isEnabled()), String.valueOf(textField.getEditable()),
							objectInComposite.getId().toLowerCase().split("\t")[1] }));
				}
			} else if (objectInComposite.getId().toLowerCase().contains("check box")) {
				Button checkBox = (Button) objectInComposite.getObject();

				if (target.equals(selectTarget.SWING)) {
					code.append(swingObjects.generateCheckBox(
							new String[] { checkBox.getText(), String.valueOf(checkBox.getLocation().x),
									String.valueOf(checkBox.getLocation().y), String.valueOf(checkBox.getSize().x),
									String.valueOf(checkBox.getSize().y), String.valueOf(checkBox.isEnabled()),
									objectInComposite.getId().toLowerCase().split("\t")[1] }));
				} else if (target.equals(selectTarget.SWT)) {
					code.append(swtObjects.generateCheckBox(
							new String[] { checkBox.getText(), String.valueOf(checkBox.getLocation().x),
									String.valueOf(checkBox.getLocation().y), String.valueOf(checkBox.getSize().x),
									String.valueOf(checkBox.getSize().y), String.valueOf(checkBox.isEnabled()),
									objectInComposite.getId().toLowerCase().split("\t")[1] }));
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

	public enum selectTarget {

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

		@Override
		public String generateFrame(String[] parameters) {

			return new String("\n \t \t JFrame frame = new JFrame(\"" + parameters[0] + "\"); \n"
					+ "\t \t frame.setSize(" + parameters[1] + "," + parameters[2] + "); \n"
					+ "\t \t frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); \n"
					+ "\t \t frame.setVisible(true); \n \n");
		}

		@Override
		public String generateButton(String[] parameters) {

			return new String("\n \t \t JButton jbutton" + parameters[6] + " = new JButton(\"" + parameters[0]
					+ "\"); \n" + "\t \t jbutton" + parameters[6] + ".setLocation(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t jbutton" + parameters[6] + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t jbutton" + parameters[6] + ".setEnable(" + parameters[5]
					+ "); \n" + "\t \t frame.add(jbutton" + parameters[6] + "); \n \n");
		}

		@Override
		public String generateLabel(String[] parameters) {
			return new String("\n \t \t JLabel jlabel" + parameters[6] + " = new JLabel(\"" + parameters[0] + "\"); \n"
					+ "\t \t jlabel" + parameters[6] + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n"
					+ "\t \t jlabel" + parameters[6] + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n"
					+ "\t \t jlabel" + parameters[6] + ".setEnable(" + parameters[5] + "); \n"
					+ "\t \t frame.add(jlabel" + parameters[6] + "); \n \n");
		}

		@Override
		public String generateTextField(String[] parameters) {
			return new String("\n \t \t JTextField jTextField" + parameters[7] + " = new JTextField(\"" + parameters[0]
					+ "\"); \n" + "\t \t jTextField" + parameters[7] + ".setLocation(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t jTextField" + parameters[7] + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t jTextField" + parameters[7] + ".setEnable(" + parameters[5]
					+ "); \n" + "\t \t jTextField" + parameters[7] + ".setEditable(" + parameters[6] + "); \n"
					+ "\t \t frame.add(jTextField" + parameters[7] + "); \n \n");
		}

		@Override
		public String generateCheckBox(String[] parameters) {
			return new String("\n \t \t JCheckBox jcheckBox" + parameters[6] + " = new JCheckBox(\"" + parameters[0]
					+ "\"); \n" + "\t \t jcheckBox" + parameters[6] + ".setLocation(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t jcheckBox" + parameters[6] + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t jcheckBox" + parameters[6] + ".setEnable(" + parameters[5]
					+ "); \n" + "\t \t frame.add(jcheckBox" + parameters[6] + "); \n \n");
		}

		@Override
		public String generateWidget(String[] parameters) {
			return new String("\n \t \t JComponent jComponent" + parameters[6] + " = new JComponent(\"" + parameters[0]
					+ "\"); \n" + "\t \t jComponent" + parameters[6] + ".setLocation(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t jComponent" + parameters[6] + ".setSize(" + parameters[3] + ","
					+ parameters[4] + "); \n" + "\t \t jComponent" + parameters[6] + ".setEnable(" + parameters[5]
					+ "); \n" + "\t \t frame.add(jComponent" + parameters[6] + "); \n \n");
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

	}

	private class SWTObjects implements GenerateObjectsInterface {

		@Override
		public String generateFrame(String[] parameters) {
			return new String("\n \t \t Display display = new Display(); \n"
					+ "\t \t Shell shell = new Shell(display); \n" + "\t \t shell.setSize(" + parameters[1] + ","
					+ parameters[2] + "); \n" + "\t \t shell.setText(\"" + parameters[0] + "\"); \n"
					+ "\t \t shell.open(); \n \n" + "\t \t while (!shell.isDisposed()) { \n"
					+ "\t \t \t if (!display.readAndDispatch()){ \n" + "\t \t \t \t display.sleep(); \n"
					+ "\t \t \t } \n" + "\t \t \t display.dispose(); \n" + "\t \t } \n \n");

		}

		@Override
		public String generateButton(String[] parameters) {
			return new String("\n \t \t Button button" + parameters[6] + " = new Button(shell,SWT.BORDER); \n"
					+ "\t \t button" + parameters[6] + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t button"
					+ parameters[6] + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n" + "\t \t button"
					+ parameters[6] + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n" + "\t \t button"
					+ parameters[6] + ".setEnable(" + parameters[5] + "); \n \n");
		}

		@Override
		public String generateLabel(String[] parameters) {
			return new String("\n \t \t Label label" + parameters[6] + " = new Label(shell,SWT.BORDER); \n"
					+ "\t \t label" + parameters[6] + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t label"
					+ parameters[6] + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n" + "\t \t label"
					+ parameters[6] + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n" + "\t \t label"
					+ parameters[6] + ".setEnable(" + parameters[5] + "); \n \n");
		}

		@Override
		public String generateTextField(String[] parameters) {
			return new String("\n \t \t Text textfield" + parameters[7] + " = new Text(shell,SWT.BORDER); \n"
					+ "\t \t textfield" + parameters[7] + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t textfield"
					+ parameters[7] + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n"
					+ "\t \t textfield" + parameters[7] + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n"
					+ "\t \t textfield" + parameters[7] + ".setEnable(" + parameters[5] + "); \n" + "\t \t textfield"
					+ parameters[7] + ".setEditable(" + parameters[6] + "); \n \n");
		}

		@Override
		public String generateCheckBox(String[] parameters) {
			return new String("\n \t \t Button checkBox" + parameters[6] + " = new Button(shell,SWT.CHECK); \n"
					+ "\t \t checkBox" + parameters[6] + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t checkBox"
					+ parameters[6] + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n" + "\t \t checkBox"
					+ parameters[6] + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n" + "\t \t checkBox"
					+ parameters[6] + ".setEnable(" + parameters[5] + "); \n \n");
		}

		@Override
		public String generateWidget(String[] parameters) {
			return new String("\n \t \t Control object" + parameters[6] + " = new Control(shell,SWT.BORDER); \n"
					+ "\t \t object" + parameters[6] + ".setText(\"" + parameters[0] + "\"); \n" + "\t \t object"
					+ parameters[6] + ".setLocation(" + parameters[1] + "," + parameters[2] + "); \n" + "\t \t object"
					+ parameters[6] + ".setSize(" + parameters[3] + "," + parameters[4] + "); \n" + "\t \t object"
					+ parameters[6] + ".setEnable(" + parameters[5] + "); \n \n");
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

	}

}
