package pt.iscte.pidesco.guibuilder.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Menu;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator;
import pt.iscte.pidesco.guibuilder.extensions.ContextMenuExtensionPoint.OBJECT_FAMILY;
import pt.iscte.pidesco.guibuilder.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class ContextMenuExtensionPointData {
	private List<ContextMenuExtensionPoint> contextMenuItems;

	public ContextMenuExtensionPointData() {
		contextMenuItems = new ArrayList<ContextMenuExtensionPoint>();

		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.contextMenu");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					contextMenuItems.add((ContextMenuExtensionPoint) c.createExecutableExtension("class"));
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void addContextMenuItems(Menu menu, ObjectInCompositeContainer obj, GuiBuilderView guiBuilderView) {
		OBJECT_FAMILY f;
		switch (obj.getObjectInComposite().getObjectFamily()) {
		case COMPONENTS:
			f = OBJECT_FAMILY.COMPONENTS;
			break;
		case CONTAINERS:
			f = OBJECT_FAMILY.CONTAINERS;
			break;
		case CANVAS:
			f = OBJECT_FAMILY.CANVAS;
			break;
		case LAYOUTS:
		default:
			throw new IllegalArgumentException("Switch case not defined!");
		}

		for (ContextMenuExtensionPoint c : contextMenuItems) {
			if (c.acceptsType(f)) {
				c.generateMenuItem(menu, obj, guiBuilderView);
			}
		}
	}

	public List<String> getCodeForObject(CodeGenerator.CodeTarget target, ObjectInCompositeContainer obj,
			String variableName) {
		List<String> code = new ArrayList<String>();
		for (ContextMenuExtensionPoint c : contextMenuItems) {
			List<String> s = null;

			try {
				s = c.generateCode(target, obj, variableName);
			} catch (UnsupportedOperationException e) {
			}

			if (s != null) {
				code.addAll(s);
			}
		}

		return code;
	}
}
