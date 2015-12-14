package pt.iscte.pidesco.guibuilder.codeGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.extensions.WidgetExtensionPointsData;
import pt.iscte.pidesco.guibuilder.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.model.compositeContents.CanvasInComposite;
import pt.iscte.pidesco.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pt.iscte.pidesco.guibuilder.model.compositeContents.ContainerInComposite;
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

	private CodeGeneratorInterface codeGenerator;
	private CodeTarget target;
	private ObjectInCompositeContainer rootContainer;
	private GuiBuilderView guiBuilderView;
	private int depth;

	public CodeGenerator(CodeGenerator.CodeTarget target, ObjectInCompositeContainer rootContainer,
			GuiBuilderView guiBuilderView) {
		this.target = target;
		this.rootContainer = rootContainer;
		this.guiBuilderView = guiBuilderView;

		switch (target) {
		case SWING:
			codeGenerator = new SwingCodeGenerator();
			break;
		case SWT:
			codeGenerator = new SWTCodeGenerator();
			break;
		}

		depth = 0;
	}

	public void generateCode() {
		List<String> code = new ArrayList<String>();
		CanvasInComposite canvas = (CanvasInComposite) rootContainer.getObjectInComposite();

		// add default code (imports, class start, definition of frame,....)
		for (String s : codeGenerator.generateImports()) {
			code.add(generateDepthSpace() + s);
		}

		code.add("");

		List<String> stringsClassBegin = codeGenerator.generateClassBegin();
		for (int i = 1; i < stringsClassBegin.size(); i++) {
			code.add(generateDepthSpace() + stringsClassBegin.get(i));
		}

		depth++;

		for (String s : codeGenerator.generateConstructorBegin()) {
			code.add(generateDepthSpace() + s);
		}

		depth++;

		List<String> stringsInitialization = codeGenerator.generateInitialization(new String[] { canvas.getLabel(),
				String.valueOf(canvas.getSize().x), String.valueOf(canvas.getSize().y) });
		for (int i = 1; i < stringsInitialization.size(); i++) {
			code.add(generateDepthSpace() + stringsInitialization.get(i));
		}

		List<String> objectsCode = generateObjects(rootContainer, stringsInitialization.get(0), target);
		if (objectsCode != null)
			code.addAll(objectsCode);

		depth--;
		for (String s : codeGenerator.generateConstructorEnd()) {
			code.add(generateDepthSpace() + s);
		}

		depth--;
		for (String s : codeGenerator.generateClassEnd()) {
			code.add(generateDepthSpace() + s);
		}

		// TODO
		File classFile = ClassFileGenerator.createFile(stringsClassBegin.get(0));
		ClassFileGenerator.writeToFile(classFile, code);
		// ClassFileGenerator.openFileInEditor(classFile);

		guiBuilderView.setMessage(guiBuilderView.GENERATED_CODE_FOR_TARGET, target.getTarget());
	}

	private String generateDepthSpace() {
		String s = "";

		for (int i = 0; i < depth; i++) {
			s += "\t";
		}

		return s;
	}

	private List<String> generateObjects(ObjectInCompositeContainer object, String containerName, CodeTarget target) {
		ArrayList<String> buffer = new ArrayList<String>();

		for (ObjectInCompositeContainer o : object.getChilds()) {
			switch (o.getObjectInComposite().getObjectFamily()) {
			case CANVAS:
				throw new IllegalArgumentException("Double canvas definition in object tree!");
			case COMPONENTS:
				List<String> strings1;

				switch (target) {
				case SWING:
					strings1 = generateSwingComponent(o, containerName);
					break;
				case SWT:
					strings1 = generateSWTComponent(o, containerName);
					break;
				default:
					throw new IllegalArgumentException("Switch case not defined!");
				}

				if (strings1.size() > 0) {
					for (int i = 1; i < strings1.size(); i++) {
						buffer.add(strings1.get(i));
					}
				}
				break;
			case CONTAINERS:
				List<String> strings2;

				switch (target) {
				case SWING:
					strings2 = generateSwingContainer(o, containerName);
					break;
				case SWT:
					strings2 = generateSWTContainer(o, containerName);
					break;
				default:
					throw new IllegalArgumentException("Switch case not defined!");
				}

				if (strings2.size() > 0) {
					for (int i = 1; i < strings2.size(); i++) {
						buffer.add(strings2.get(i));
					}
				}
				break;
			case LAYOUTS:
				throw new IllegalArgumentException("Layout should be set on canvas object!");
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}
		}

		return buffer;
	}

	/*
	 * Swing Stuff
	 */
	private List<String> generateSwingComponent(ObjectInCompositeContainer object, String containerName) {
		SwingCodeGenerator generator = ((SwingCodeGenerator) codeGenerator);
		List<String> buffer = new ArrayList<String>();
		ComponentInCompositeImpl component = (ComponentInCompositeImpl) object.getObjectInComposite();

		if (component.getComponentType() == GuiLabels.GUIBuilderComponent.WIDGET) {
			WidgetExtensionPointsData extensionPointsData = new WidgetExtensionPointsData(guiBuilderView);
			String[] widgetCode = extensionPointsData.getWidgetCode(target, containerName);

			buffer.add(widgetCode[0]);
			buffer.add("");

			for (int i = 1; i < widgetCode.length; i++) {
				buffer.add(generateDepthSpace() + (String.format(widgetCode[i], "shell")));
			}

			buffer.add(generateDepthSpace() + containerName + ".add(" + widgetCode[0] + ");");
			generator.increaseComponentCount();
		} else {
			String labelText = component.getText();
			Control control = component.getControl();
			SwingCodeGenerator.Element element = null;

			switch (component.getComponentType()) {
			case BTN:
				element = SwingCodeGenerator.Element.BTN;
				break;
			case LABEL:
				element = SwingCodeGenerator.Element.LABEL;
				break;
			case TXTFIELD:
				element = SwingCodeGenerator.Element.TXT_FIELD;
				break;
			case RADIO_BTN:
				element = SwingCodeGenerator.Element.RADIO_BTN;
				break;
			case CHK_BOX:
				element = SwingCodeGenerator.Element.CHECK_BOX;
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}

			List<String> strings = generator.generateComponentCode(element, labelText, control.getLocation(),
					control.getSize(), control.isEnabled(), control.getBackground(), control.getForeground());
			buffer.add(strings.get(0));
			buffer.add("");

			for (int i = 1; i < strings.size(); i++) {
				buffer.add(generateDepthSpace() + strings.get(i));
			}

			buffer.add(generateDepthSpace() + containerName + ".add(" + strings.get(0) + ");");
		}
		return buffer;
	}

	private List<String> generateSwingContainer(ObjectInCompositeContainer object, String containerName) {
		SwingCodeGenerator generator = ((SwingCodeGenerator) codeGenerator);
		List<String> buffer = new ArrayList<String>();
		ContainerInComposite component = (ContainerInComposite) object.getObjectInComposite();

		List<String> containerCode = generator.generateContainer(component.getLocation(), component.getSize());
		buffer.add(containerCode.get(0));
		buffer.add("");
		for (int i = 1; i < containerCode.size(); i++) {
			buffer.add(generateDepthSpace() + containerCode.get(i));
		}

		buffer.add(generateDepthSpace() + containerName + ".add(" + containerCode.get(0) + ");");

		if (object.hasChilds()) {
			List<String> code = generateObjects(object, containerCode.get(0), CodeTarget.SWING);
			buffer.add("");
			for (int i = 1; i < code.size(); i++) {
				buffer.add(code.get(i));
			}
		}

		return buffer;
	}

	/*
	 * SWT Stuff
	 */
	private List<String> generateSWTComponent(ObjectInCompositeContainer object, String containerName) {
		SWTCodeGenerator generator = ((SWTCodeGenerator) codeGenerator);
		List<String> buffer = new ArrayList<String>();
		ComponentInCompositeImpl component = (ComponentInCompositeImpl) object.getObjectInComposite();

		if (component.getComponentType() == GuiLabels.GUIBuilderComponent.WIDGET) {
			WidgetExtensionPointsData extensionPointsData = new WidgetExtensionPointsData(guiBuilderView);
			String[] widgetCode = extensionPointsData.getWidgetCode(target, containerName);

			buffer.add(widgetCode[0]);
			buffer.add("");

			for (int i = 1; i < widgetCode.length; i++) {
				if (widgetCode[i].contains(widgetCode[0])) {
					buffer.add(generateDepthSpace() + String.format(widgetCode[i], "shell"));
				} else {
					buffer.add(generateDepthSpace() + String.format(widgetCode[i], "shell"));
				}

			}

			generator.increaseComponentCount();
		} else {
			String labelText = component.getText();
			Control control = component.getControl();
			SWTCodeGenerator.Element element = null;

			switch (component.getComponentType()) {
			case BTN:
				element = SWTCodeGenerator.Element.BTN;
				break;
			case LABEL:
				element = SWTCodeGenerator.Element.LABEL;
				break;
			case TXTFIELD:
				element = SWTCodeGenerator.Element.TXT_FIELD;
				break;
			case RADIO_BTN:
				element = SWTCodeGenerator.Element.RADIO_BTN;
				break;
			case CHK_BOX:
				element = SWTCodeGenerator.Element.CHECK_BOX;
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}

			List<String> strings = generator.generateComponentCode(element, containerName, labelText,
					control.getLocation(), control.getSize(), control.isEnabled(), control.getBackground(),
					control.getForeground());
			buffer.add(strings.get(0));
			buffer.add("");

			for (int i = 1; i < strings.size(); i++) {
				buffer.add(generateDepthSpace() + strings.get(i));
			}
		}
		return buffer;
	}

	private List<String> generateSWTContainer(ObjectInCompositeContainer object, String containerName) {
		SWTCodeGenerator generator = ((SWTCodeGenerator) codeGenerator);
		List<String> buffer = new ArrayList<String>();
		ContainerInComposite component = (ContainerInComposite) object.getObjectInComposite();

		List<String> containerCode = generator.generateContainer(containerName, component.getLocation(),
				component.getSize());
		buffer.add(containerCode.get(0));
		buffer.add("");
		for (int i = 1; i < containerCode.size(); i++) {
			buffer.add(generateDepthSpace() + containerCode.get(i));
		}

		buffer.add(generateDepthSpace() + containerName + ".add(" + containerCode.get(0) + ");");

		if (object.hasChilds()) {
			List<String> code = generateObjects(object, containerCode.get(0), CodeTarget.SWT);
			buffer.add("");
			for (int i = 1; i < code.size(); i++) {
				buffer.add(code.get(i));
			}
		}

		return buffer;
	}
}
