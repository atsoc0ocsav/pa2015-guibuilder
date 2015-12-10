package pt.iscte.pidesco.guibuilder.internal.codeGenerator;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class SWTCodeGenerator implements CodeGeneratorInterface {
	public enum Element {
		BUTTON("Button", "button", "SWT.BORDER"), LABEL("Label", "label", "SWT.BORDER"), TEXT_FIELD("Text", "text",
				"SWT.BORDER"), CHECK_BOX("Button", "checkBox", "SWT.CHECK");

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
		
		public String getStyle(){
			return style;
		}
	}

	private final String[] IMPORTS = { "org.eclipse.swt.*;" };
	public final String CLASS_NAME = "SWTGUIWindow";
	public final String SHELL_NAME = "shelly";
	public int appendNameComponent = -1;

	@Override
	public String generateFrame(String[] parameters) {
		Point dimension = new Point(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));

		return generateFrame(parameters[0], dimension);
	}

	public String generateFrame(String name, Point dimension) {
		return new String(
				  "\t\tDisplay display = new Display();\n" 
				+ "\t\tShell " + SHELL_NAME + " = new Shell(display);\n"
				+ "\t\t" + SHELL_NAME + ".setSize(" + dimension.x + "," + dimension.y + ");\n" 
				+ "\t\t" + SHELL_NAME + ".setText(\"" + name + "\");\n" 
				+ "\t\t" + SHELL_NAME + ".open();\n\n" 
				+ "\t\twhile (!" + SHELL_NAME + ".isDisposed()) {\n"
				+ "\t\t\tif (!display.readAndDispatch()) {\n" 
				+ "\t\t\t\tdisplay.sleep();\n" 
				+ "\t\t\t}\n"
				+ "\t\t\tdisplay.dispose();\n" 
				+ "\t\t}\n\n");
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
				  "\t\t" + element.getCode() + " " + elementName + " = new " + element.getCode() + "(" + SHELL_NAME + "," + element.getStyle() + ");\n"
				+ "\t\t" + elementName + ".setText(\"" + text + "\");\n" 
				+ "\t\t" + elementName + ".setLocation(" + location.x + "," + location.y + ");\n"
				+ "\t\t" + elementName + ".setSize(" + size.x + "," + size.y + ");\n"
				+ "\t\t" + elementName + ".setEnable(" + isEnabled + "); \n" 
				+ "\t\t" + elementName + ".setBackground(new Color(Display.getCurrent()," + backgroundColor.getRed() + "," + backgroundColor.getGreen() + "," + backgroundColor.getBlue() + "));\n"
				+ "\t\t" + elementName + ".setForeground(new Color(Display.getCurrent()," + foregroundColor.getRed() + "," + foregroundColor.getGreen() + "," + foregroundColor.getBlue() + "));\n");
	}
}