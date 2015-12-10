package pt.iscte.pidesco.guibuilder.internal.codeGenerator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class SwingCodeGenerator implements CodeGeneratorInterface {
	public enum Element {
		BUTTON("JButton", "jButton"), LABEL("JLabel", "jLabel"), TEXT_FIELD("JTextField",
				"jTextField"), CHECK_BOX("JCheckBox", "jCheckBox");

		public String code;
		public String prefix;

		private Element(String code, String prefix) {
			this.code = code;
			this.prefix = prefix;
		}

		public String getCode() {
			return code;
		}

		public String getPrefix() {
			return prefix;
		}
	}

	private final String[] IMPORTS = { "javax.swing.*;", "java.awt.Color;" };
	public final String CLASS_NAME = "SwingGUIWindow";
	public final String JFRAME_NAME = "frame";
	public int appendNameComponent = -1;

	/*
	 * Interface methods
	 */
	// Elements code generation
	@Override
	public String generateFrame(String[] parameters) {
		Point dimension = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));

		return generateFrame(parameters[0], dimension);
	}

	public String generateFrame(String name, Point dimension) {
		return new String(
				  "\t\tJFrame " + JFRAME_NAME + " = new JFrame(\"" + name + "\");\n" 
				+ "\t\t" + JFRAME_NAME + ".setSize(" + dimension.x + "," + dimension.y + ");\n" 
				+ "\t\t" + JFRAME_NAME + ".setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" 
				+ "\t\t" + JFRAME_NAME + ".setVisible(true);\n");
	}

	@Override
	public String generateButton(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateElementCode(Element.BUTTON, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public String generateLabel(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateElementCode(Element.LABEL, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public String generateTextField(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateElementCode(Element.TEXT_FIELD, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public String generateCheckBox(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateElementCode(Element.CHECK_BOX, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	// Class start and end, constructor start and end, action listeners
	@Override
	public String generateStartClass() {
		return new String("public class " + CLASS_NAME + " {\n");
	}

	@Override
	public String generateEndClass() {
		return new String("\n}");
	}

	@Override
	public String generateStartConstructorClass() {
		return new String("\tpublic " + CLASS_NAME + "(){\n");
	}

	@Override
	public String generateEndConstructorClass() {
		return new String("\t}");
	}

	@Override
	public String generateImports() {
		String str = "";

		for (String s : IMPORTS) {
			str += "import " + s + "\n";
		}

		return str;
	}

	@Override
	public String generateAction(String[] parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Elements code generators
	 */
	public String generateButton(String text, Point location, Point size, boolean isEnabled, Color backgroundColor,
			Color foregroundColor) {
		return generateElementCode(Element.BUTTON, text, location, size, isEnabled, backgroundColor, foregroundColor);
	}

	public String generateLabel(String text, Point location, Point size, boolean isEnabled, Color backgroundColor,
			Color foregroundColor) {
		return generateElementCode(Element.LABEL, text, location, size, isEnabled, backgroundColor, foregroundColor);
	}

	public String generateTextField(String text, Point location, Point size, boolean isEnabled, Color backgroundColor,
			Color foregroundColor) {
		return generateElementCode(Element.TEXT_FIELD, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public String generateCheckBox(String text, Point location, Point size, boolean isEnabled, Color backgroundColor,
			Color foregroundColor) {
		return generateElementCode(Element.CHECK_BOX, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public String generateElementCode(Element element, String text, Point location, Point size, boolean isEnabled,
			Color backgroundColor, Color foregroundColor) {
		String elementName = element.getPrefix() + (++appendNameComponent);

		return new String(
				  "\t\t" + element.getCode() + " " + elementName + " = new " + element.getCode() + "(\"" + text + "\");\n" 
				+ "\t\t" + elementName + ".setLocation(" + location.x + "," + location.y + ");\n" 
				+ "\t\t" + elementName + ".setSize(" + size.x + "," + size.y + ");\n" 
				+ "\t\t" + elementName + ".setEnabled("	+ isEnabled + ");\n" 
				+ "\t\t" + elementName + ".setBackground(new Color("+ backgroundColor.getRed() + "," + backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));\n" 
				+ "\t\t" + elementName + ".setForeground(new Color("+ foregroundColor.getRed() + "," + foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));\n" 
				+ "\t\t" + JFRAME_NAME + ".add("+ elementName + ");\n");
	}
}