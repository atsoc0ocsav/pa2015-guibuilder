package pa.iscde.guibuilder.codeGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;

import pa.iscde.guibuilder.extensions.WidgetInComposite;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.model.compositeContents.CanvasInComposite;
import pa.iscde.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pa.iscde.guibuilder.model.compositeContents.ContainerInComposite;
import pa.iscde.guibuilder.ui.GuiBuilderView;
import pa.iscde.guibuilder.ui.GuiLabels;

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
		default:
			throw new IllegalArgumentException("Switch case not defined!");
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

		List<String> stringsInitialization = codeGenerator
				.generateInitialization(new String[] { canvas.getLabel(), String.valueOf(canvas.getSize().x),
						String.valueOf(canvas.getSize().y), canvas.getActiveLayout().str() });
		for (int i = 1; i < stringsInitialization.size(); i++) {
			code.add(generateDepthSpace() + stringsInitialization.get(i));
		}

		List<String> list = guiBuilderView.getContextMenuExtensionPointData().generateCommonCodeBegin(target,
				stringsInitialization.get(0));
		if (list.size() > 0) {
			code.add("");
			code.add(generateDepthSpace() + "// Context Menu Plug-In code");
			for (int i = 0; i < list.size(); i++) {
				code.add(generateDepthSpace() + list.get(i));
			}
		}

		List<String> objectsCode = generateObjects(rootContainer, stringsInitialization.get(0), target);
		if (objectsCode != null)
			code.addAll(objectsCode);

		List<String> stringFinalization = guiBuilderView.getContextMenuExtensionPointData()
				.generateCommonCodeEnd(target, stringsInitialization.get(0));
		if (stringFinalization.size() > 0) {
			code.add("");
			code.add(generateDepthSpace() + "// Context Menu Plug-In code");
			for (int i = 0; i < stringFinalization.size(); i++) {
				code.add(generateDepthSpace() + stringFinalization.get(i));
			}
		}

		depth--;
		for (String s : codeGenerator.generateConstructorEnd()) {
			code.add(generateDepthSpace() + s);
		}

		depth--;
		for (String s : codeGenerator.generateClassEnd()) {
			code.add(generateDepthSpace() + s);
		}

		File classFile = ClassFileGenerator.createFile(stringsClassBegin.get(0));
		ClassFileGenerator.writeToFile(classFile, code);

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
			List<String> strings;

			switch (o.getObjectInComposite().getObjectFamily()) {
			case CANVAS:
				throw new IllegalArgumentException("Double canvas definition in object tree!");
			case COMPONENTS:
				switch (target) {
				case SWING:
					strings = generateSwingComponent(o, containerName);
					break;
				case SWT:
					strings = generateSWTComponent(o, containerName);
					break;
				default:
					throw new IllegalArgumentException("Switch case not defined!");
				}
				break;
			case CONTAINERS:
				switch (target) {
				case SWING:
					strings = generateSwingContainer(o, containerName);
					break;
				case SWT:
					strings = generateSWTContainer(o, containerName);
					break;
				default:
					throw new IllegalArgumentException("Switch case not defined!");
				}
				break;
			case LAYOUTS:
				throw new IllegalArgumentException("Layout should be set on canvas/ container object!");
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}

			if (strings.size() > 0) {
				for (int i = 1; i < strings.size(); i++) {
					buffer.add(strings.get(i));
				}

				List<String> l = guiBuilderView.getContextMenuExtensionPointData().generateCodeForObject(target, o,
						containerName, strings.get(0));
				if (l.size() > 0) {
					buffer.add("");
					buffer.add(generateDepthSpace() + "// Context Menu Plug-In code");
					for (String s : l) {
						buffer.add(generateDepthSpace() + s);
					}
				}
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
			List<String> widgetCode = ((WidgetInComposite) object.getObjectInComposite()).generateWidgetCode(target,
					containerName, generator.getAndIncreaseComponentCount());

			buffer.add(widgetCode.get(0));
			buffer.add("");
			buffer.add(generateDepthSpace() + "// Widget Plug-In code");
			for (int i = 1; i < widgetCode.size(); i++) {
				buffer.add(generateDepthSpace() + widgetCode.get(i));
			}
			buffer.add(generateDepthSpace() + containerName + ".add(" + widgetCode.get(0) + ");");
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

		List<String> containerCode = generator.generateContainer(component.getLocation(), component.getSize(),
				component.getActiveLayout());
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
			List<String> widgetCode = ((WidgetInComposite) object.getObjectInComposite()).generateWidgetCode(target,
					containerName, generator.getAndIncreaseComponentCount());

			buffer.add(widgetCode.get(0));
			buffer.add("");
			buffer.add(generateDepthSpace() + "// Widget Plug-In code");
			for (int i = 1; i < widgetCode.size(); i++) {
				buffer.add(generateDepthSpace() + widgetCode.get(i));
			}
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
				component.getSize(), component.getActiveLayout());
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
