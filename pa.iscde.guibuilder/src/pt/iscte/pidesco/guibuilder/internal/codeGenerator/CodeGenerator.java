package pt.iscte.pidesco.guibuilder.internal.codeGenerator;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.extensions.ExtensionPointsData;
import pt.iscte.pidesco.guibuilder.internal.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;

public class CodeGenerator {
	public static enum CodeTarget {
		SWING("Swing"), SWT("SWT");

		private String target;

		private CodeTarget(String target) {
			this.target = target;
		}

		public String getTarget() {
			return target;
		}
	}

	private StringBuffer code = new StringBuffer("");
	private CodeGeneratorInterface codeGenerator;
	private GuiBuilderView guiBuilderView;
	private CodeTarget target;
	private ArrayList<ObjectInComposite> components;
	private String frameTitle;

	public CodeGenerator(CodeGenerator.CodeTarget target, String frameTitle, ArrayList<ObjectInComposite> components,
			GuiBuilderView guiBuilderView) {
		this.target = target;
		this.frameTitle = frameTitle;
		this.components = components;
		this.guiBuilderView = guiBuilderView;

		switch (target) {
		case SWING:
			codeGenerator = new SwingCodeGenerator();
			break;
		case SWT:
			codeGenerator = new SWTCodeGenerator();
			break;
		}
	}

	public void generateCode() {
		// add default code (imports, class start, definition of frame,....)
		code.append(codeGenerator.generateImports());
		code.append("\n");

		code.append(codeGenerator.generateStartClass()).append(codeGenerator.generateStartConstructorClass());
		code.append(codeGenerator
				.generateFrame(new String[] { frameTitle, String.valueOf(guiBuilderView.getCanvasSize().width),
						String.valueOf(guiBuilderView.getCanvasSize().height) }));

		File classFile = null;

		switch (target) {
		case SWING:
			code.append(generateSwingComponents());
			code.append(codeGenerator.generateEndConstructorClass()).append(codeGenerator.generateEndClass());

			classFile = ClassFileGenerator.createFile(((SwingCodeGenerator) codeGenerator).CLASS_NAME);
			break;
		case SWT:
			code.append(generateSwtComponents());
			code.append(codeGenerator.generateEndConstructorClass()).append(codeGenerator.generateEndClass());

			classFile = ClassFileGenerator.createFile(((SWTCodeGenerator) codeGenerator).CLASS_NAME);
			break;
		}

		ClassFileGenerator.writeToFile(classFile, code.toString());
		//ClassFileGenerator.openFileInEditor(classFile);
		
		// System.out.printf("Generated Code:\n%s\n", code.toString());
	}

	private String generateSwingComponents() {
		if (codeGenerator instanceof SwingCodeGenerator) {
			int appendNameComponent = 0;
			SwingCodeGenerator generator = ((SwingCodeGenerator) codeGenerator);
			StringBuffer buffer = new StringBuffer();

			for (ObjectInComposite objectInComposite : components) {
				String objectName = objectInComposite.getId().toLowerCase();

				if (objectName.contains(GuiLabels.GUIBuilderComponent.OTHER.str().toLowerCase())) {
					ExtensionPointsData extensionPointsData = new ExtensionPointsData(guiBuilderView);
					String[] widgetCode = extensionPointsData.getWidgetCode(target);

					buffer.append("\n");

					for (int i = 1; i < widgetCode.length; i++) {
						if (widgetCode[i].contains(widgetCode[0])) {
							String temp = ("\t\t" + String.format(widgetCode[i], "shell") + "\n")
									.replaceAll(widgetCode[0], widgetCode[0] + appendNameComponent);
							buffer.append(temp);
						} else {
							buffer.append("\t\t" + String.format(widgetCode[i], "shell") + "\n");
						}

					}

					buffer.append(
							"\t\t" + generator.JFRAME_NAME + ".add(" + widgetCode[0] + appendNameComponent + ");\n");
					appendNameComponent++;
				} else {
					boolean generateCode = false;
					String text = null;
					Control control = objectInComposite.getObject();
					SwingCodeGenerator.Element element = null;

					if (objectName.contains(GuiLabels.GUIBuilderComponent.BTN.str().toLowerCase())) {
						text = ((Button) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SwingCodeGenerator.Element.BUTTON;
					} else if (objectName.contains(GuiLabels.GUIBuilderComponent.LABEL.str().toLowerCase())) {
						text = ((Label) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SwingCodeGenerator.Element.LABEL;
					} else if (objectName.contains(GuiLabels.GUIBuilderComponent.TEXTFIELD.str().toLowerCase())) {
						text = ((Text) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SwingCodeGenerator.Element.TEXT_FIELD;
					} else if (objectName.contains(GuiLabels.GUIBuilderComponent.CHK_BOX.str().toLowerCase())) {
						text = ((Button) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SwingCodeGenerator.Element.CHECK_BOX;
					}

					if (generateCode) {
						buffer.append("\n");
						buffer.append(
								generator.generateElementCode(element, text, control.getLocation(), control.getSize(),
										control.isEnabled(), control.getBackground(), control.getForeground()));
					}
				}
			}
			return buffer.toString();
		} else {
			return null;
		}
	}

	private String generateSwtComponents() {
		if (codeGenerator instanceof SWTCodeGenerator) {
			int appendNameComponent = 0;
			SWTCodeGenerator generator = ((SWTCodeGenerator) codeGenerator);
			StringBuffer buffer = new StringBuffer();

			for (ObjectInComposite objectInComposite : components) {
				String objectName = objectInComposite.getId().toLowerCase();

				if (objectName.contains(GuiLabels.GUIBuilderComponent.OTHER.str().toLowerCase())) {
					ExtensionPointsData extensionPointsData = new ExtensionPointsData(guiBuilderView);
					String[] widgetCode = extensionPointsData.getWidgetCode(target);

					buffer.append("\n");

					for (int i = 1; i < widgetCode.length; i++) {
						if (widgetCode[i].contains(widgetCode[0])) {
							String temp = ("\t\t" + String.format(widgetCode[i], "shell") + "\n")
									.replaceAll(widgetCode[0], widgetCode[0] + appendNameComponent);
							buffer.append(temp);
						} else {
							buffer.append("\t\t" + String.format(widgetCode[i], "shell") + "\n");
						}

					}

					appendNameComponent++;
				} else {
					boolean generateCode = false;
					String text = null;
					Control control = objectInComposite.getObject();
					SWTCodeGenerator.Element element = null;

					if (objectName.contains(GuiLabels.GUIBuilderComponent.BTN.str().toLowerCase())) {
						text = ((Button) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SWTCodeGenerator.Element.BUTTON;
					} else if (objectName.contains(GuiLabels.GUIBuilderComponent.LABEL.str().toLowerCase())) {
						text = ((Label) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SWTCodeGenerator.Element.LABEL;
					} else if (objectName.contains(GuiLabels.GUIBuilderComponent.TEXTFIELD.str().toLowerCase())) {
						text = ((Text) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SWTCodeGenerator.Element.TEXT_FIELD;
					} else if (objectName.contains(GuiLabels.GUIBuilderComponent.CHK_BOX.str().toLowerCase())) {
						text = ((Button) objectInComposite.getObject()).getText();
						generateCode = true;
						element = SWTCodeGenerator.Element.CHECK_BOX;
					}

					if (generateCode) {
						buffer.append("\n");
						buffer.append(
								generator.generateElementCode(element, text, control.getLocation(), control.getSize(),
										control.isEnabled(), control.getBackground(), control.getForeground()));
					}

				}
			}
			return buffer.toString();
		} else {
			return null;
		}
	}
}
