package pt.iscte.pidesco.guibuilder.codeGenerator;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class SwingCodeGenerator implements CodeGeneratorInterface {
	public enum Element {
		BTN("JButton", "jButton"), LABEL("JLabel", "jLabel"), TXT_FIELD("JTextField", "jTextField"), CHECK_BOX(
				"JCheckBox", "jCheckBox"), RADIO_BTN("JRadioButton", "jRadioButton"), PANEL("JPanel", "jPanel");

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

	private final String[] IMPORTS = { "javax.swing.*", "java.awt.Color" };
	public final String CLASS_NAME = "SwingGUIWindow";
	public final String JFRAME_NAME = "frame";
	public int componentCount = -1;

	@Override
	public int getComponentCount() {
		return componentCount;
	}

	@Override
	public void increaseComponentCount() {
		componentCount++;
	}

	/*
	 * Interface methods
	 */
	// Elements code generation
	@Override
	public ArrayList<String> generateInitialization(String[] parameters) {
		Point dimension = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		return generateFrame(parameters[0], dimension);
	}

	private ArrayList<String> generateFrame(String name, Point dimension) {
		ArrayList<String> strings = new ArrayList<String>();

		// TODO Layout
		strings.add(JFRAME_NAME);
		strings.add("JFrame " + JFRAME_NAME + " = new JFrame(\"" + name + "\");");
		strings.add(JFRAME_NAME + ".setSize(" + dimension.x + "," + dimension.y + ");");
		strings.add(JFRAME_NAME + ".setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);");

		return strings;
	}

	@Override
	public ArrayList<String> generateButton(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateComponentCode(Element.BTN, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateLabel(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateComponentCode(Element.LABEL, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateTextField(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateComponentCode(Element.TXT_FIELD, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateCheckBox(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point dimension = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[6]),
				Integer.parseInt(parameters[7]), Integer.parseInt(parameters[8]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[9]),
				Integer.parseInt(parameters[10]), Integer.parseInt(parameters[11]));

		return generateComponentCode(Element.CHECK_BOX, parameters[0], location, dimension,
				Boolean.parseBoolean(parameters[5]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateContainer(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
		Point size = new Point(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));

		// TODO Layout

		return generateContainer(location, size);
	}

	public ArrayList<String> generateContainer(Point location, Point size) {
		ArrayList<String> strings = new ArrayList<String>();
		String elementName = Element.PANEL.getPrefix() + (++componentCount);

		// TODO Layout

		strings.add(elementName);
		strings.add(Element.PANEL.getCode() + " " + elementName + " = new " + Element.PANEL.getCode() + "();");
		strings.add(elementName + ".setLocation(" + location.x + "," + location.y + ");");
		strings.add(elementName + ".setSize(" + size.x + "," + size.y + ");");
		return strings;
	}

	// Class start and end, constructor start and end, action listeners
	@Override
	public ArrayList<String> generateClassBegin() {
		ArrayList<String> strings = new ArrayList<String>();
		strings.add(CLASS_NAME);
		strings.add("public class " + CLASS_NAME + " {");
		return strings;
	}

	@Override
	public ArrayList<String> generateClassEnd() {
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("}");
		return strings;
	}

	@Override
	public ArrayList<String> generateConstructorBegin() {
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("public " + CLASS_NAME + "() {");
		return strings;
	}

	@Override
	public ArrayList<String> generateConstructorEnd() {
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("");
		strings.add("\t" + JFRAME_NAME + ".setVisible(true);");
		strings.add("}");
		return strings;
	}

	@Override
	public ArrayList<String> generateImports() {
		ArrayList<String> strings = new ArrayList<String>();

		for (String s : IMPORTS) {
			strings.add("import " + s + ";");
		}

		return strings;
	}

	@Override
	public ArrayList<String> generateAction(String[] parameters) {
		// TODO
		return null;
	}

	/*
	 * Elements code generators
	 */
	public ArrayList<String> generateButton(String text, Point location, Point size, boolean isEnabled,
			Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.BTN, text, location, size, isEnabled, backgroundColor, foregroundColor);
	}

	public ArrayList<String> generateLabel(String text, Point location, Point size, boolean isEnabled,
			Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.LABEL, text, location, size, isEnabled, backgroundColor, foregroundColor);
	}

	public ArrayList<String> generateTextField(String text, Point location, Point size, boolean isEnabled,
			Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.TXT_FIELD, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public ArrayList<String> generateCheckBox(String text, Point location, Point size, boolean isEnabled,
			Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.CHECK_BOX, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public ArrayList<String> generateComponentCode(Element element, String text, Point location, Point size,
			boolean isEnabled, Color backgroundColor, Color foregroundColor) {
		String elementName = element.getPrefix() + (++componentCount);
		ArrayList<String> strings = new ArrayList<String>();

		strings.add(elementName);
		strings.add(element.getCode() + " " + elementName + " = new " + element.getCode() + "(\"" + text + "\");");
		strings.add(elementName + ".setLocation(" + location.x + "," + location.y + ");");
		strings.add(elementName + ".setSize(" + size.x + "," + size.y + ");");
		strings.add(elementName + ".setEnabled(" + isEnabled + ");");
		strings.add(elementName + ".setBackground(new Color(" + backgroundColor.getRed() + ","
				+ backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));");
		strings.add(elementName + ".setForeground(new Color(" + foregroundColor.getRed() + ","
				+ foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));");

		return strings;
	}
}