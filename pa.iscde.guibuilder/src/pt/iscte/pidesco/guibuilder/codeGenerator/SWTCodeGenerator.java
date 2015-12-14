package pt.iscte.pidesco.guibuilder.codeGenerator;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class SWTCodeGenerator implements CodeGeneratorInterface {
	public enum Element {
		BTN("Button", "button", "SWT.BORDER"), LABEL("Label", "label", "SWT.BORDER"), TXT_FIELD("Text", "text",
				"SWT.BORDER"), CHECK_BOX("Button", "checkBox", "SWT.CHECK"), RADIO_BTN("Button", "radioButton",
						"SWT.RADIO"), COMPOSITE("Composite", "composite", "SWT.BORDER");

		public String code;
		public String prefix;
		public String style;

		private Element(String code, String prefix, String style) {
			this.code = code;
			this.prefix = prefix;
			this.style = style;
		}

		public String getCode() {
			return code;
		}

		public String getPrefix() {
			return prefix;
		}

		public String getStyle() {
			return style;
		}
	}

	private final String[] IMPORTS = { "org.eclipse.swt.*"};
	public final String CLASS_NAME = "SWTGUIWindow";
	public final String SHELL_NAME = "shell";
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

	public ArrayList<String> generateFrame(String name, Point dimension) {
		ArrayList<String> strings = new ArrayList<String>();

		strings.add(SHELL_NAME);
		strings.add("Display display = new Display();");
		strings.add("Shell " + SHELL_NAME + " = new Shell(display);");
		strings.add(SHELL_NAME + ".setSize(" + dimension.x + "," + dimension.y + ");");
		strings.add(SHELL_NAME + ".setText(\"" + name + "\");");

		return strings;
	}

	@Override
	public ArrayList<String> generateButton(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
		Point dimension = new Point(Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[7]),
				Integer.parseInt(parameters[8]), Integer.parseInt(parameters[9]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[10]),
				Integer.parseInt(parameters[11]), Integer.parseInt(parameters[12]));

		return generateComponentCode(Element.BTN, parameters[0], parameters[1], location, dimension,
				Boolean.parseBoolean(parameters[6]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateLabel(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
		Point dimension = new Point(Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[7]),
				Integer.parseInt(parameters[8]), Integer.parseInt(parameters[9]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[10]),
				Integer.parseInt(parameters[11]), Integer.parseInt(parameters[12]));

		return generateComponentCode(Element.LABEL, parameters[0], parameters[1], location, dimension,
				Boolean.parseBoolean(parameters[6]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateTextField(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
		Point dimension = new Point(Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[7]),
				Integer.parseInt(parameters[8]), Integer.parseInt(parameters[9]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[10]),
				Integer.parseInt(parameters[11]), Integer.parseInt(parameters[12]));

		return generateComponentCode(Element.TXT_FIELD, parameters[0], parameters[1], location, dimension,
				Boolean.parseBoolean(parameters[6]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateCheckBox(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
		Point dimension = new Point(Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]));
		Color backgroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[7]),
				Integer.parseInt(parameters[8]), Integer.parseInt(parameters[9]));
		Color foregroundColor = new Color(Display.getDefault(), Integer.parseInt(parameters[10]),
				Integer.parseInt(parameters[11]), Integer.parseInt(parameters[12]));

		return generateComponentCode(Element.CHECK_BOX, parameters[0], parameters[1], location, dimension,
				Boolean.parseBoolean(parameters[6]), backgroundColor, foregroundColor);
	}

	@Override
	public ArrayList<String> generateContainer(String[] parameters) {
		Point location = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
		Point size = new Point(Integer.parseInt(parameters[3]), Integer.parseInt(parameters[4]));

		// TODO Layout

		return generateContainer(parameters[0], location, size);
	}

	public ArrayList<String> generateContainer(String containerName, Point location, Point size) {
		ArrayList<String> strings = new ArrayList<String>();
		String elementName = Element.COMPOSITE.getPrefix() + (++componentCount);

		// TODO Layout

		strings.add(elementName);
		strings.add(Element.COMPOSITE.getCode() + " " + elementName + " = new " + Element.COMPOSITE.getCode() + "("
				+ containerName + "," + Element.COMPOSITE.style + ");");
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
		strings.add("\t" + SHELL_NAME + ".open();");
		strings.add("\twhile (!" + SHELL_NAME + ".isDisposed()) {");
		strings.add("\t\tif (!display.readAndDispatch()) {");
		strings.add("\t\t\tdisplay.sleep();");
		strings.add("\t\t}");
		strings.add("\t\tdisplay.dispose();");
		strings.add("\t}");
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Elements code generators
	 */
	public ArrayList<String> generateButton(String compositeName, String text, Point location, Point size,
			boolean isEnabled, Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.BTN, compositeName, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public ArrayList<String> generateLabel(String compositeName, String text, Point location, Point size,
			boolean isEnabled, Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.LABEL, compositeName, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public ArrayList<String> generateTextField(String compositeName, String text, Point location, Point size,
			boolean isEnabled, Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.TXT_FIELD, compositeName, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public ArrayList<String> generateCheckBox(String compositeName, String text, Point location, Point size,
			boolean isEnabled, Color backgroundColor, Color foregroundColor) {
		return generateComponentCode(Element.CHECK_BOX, compositeName, text, location, size, isEnabled, backgroundColor,
				foregroundColor);
	}

	public ArrayList<String> generateComponentCode(Element element, String compositeName, String text, Point location,
			Point size, boolean isEnabled, Color backgroundColor, Color foregroundColor) {
		String elementName = element.getPrefix() + (++componentCount);
		ArrayList<String> strings = new ArrayList<String>();

		strings.add(elementName);
		strings.add(element.getCode() + " " + elementName + " = new " + element.getCode() + "(" + compositeName + ","
				+ element.getStyle() + ");");
		strings.add(elementName + ".setText(\"" + text + "\");");
		strings.add(elementName + ".setLocation(" + location.x + "," + location.y + ");");
		strings.add(elementName + ".setSize(" + size.x + "," + size.y + ");");
		strings.add(elementName + ".setEnabled(" + isEnabled + ");");
		strings.add(elementName + ".setBackground(new Color(Display.getCurrent()," + backgroundColor.getRed() + ","
				+ backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));");
		strings.add(elementName + ".setForeground(new Color(Display.getCurrent()," + foregroundColor.getRed() + ","
				+ foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));");

		return strings;
	}
}