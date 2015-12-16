package pa.iscde.guibuilder.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Menu;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator;
import pa.iscde.guibuilder.extensions.ContextMenuElement.OBJECT_FAMILY;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.ui.GuiBuilderView;

public class ContextMenuExtensionPointData {
	private List<ContextMenuElement> contextMenuItems;

	public ContextMenuExtensionPointData() {
		contextMenuItems = new ArrayList<ContextMenuElement>();

		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.contextMenu");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					contextMenuItems.add((ContextMenuElement) c.createExecutableExtension("class"));
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

		for (ContextMenuElement c : contextMenuItems) {
			if (c.acceptsType(f)) {
				c.generateMenuItem(menu, obj, guiBuilderView);
			}
		}
	}

	public List<String> generateCodeForObject(CodeGenerator.CodeTarget target, ObjectInCompositeContainer obj,
			String containerName, String variableName) {
		List<String> code = new ArrayList<String>();
		for (ContextMenuElement c : contextMenuItems) {
			List<String> s = null;

			try {
				s = c.generateCodeForObject(target, obj, containerName, variableName);
			} catch (UnsupportedOperationException e) {
			}

			if (s != null && !s.isEmpty()) {
				if (code.size() != 0) {
					code.add("");
				}
				code.addAll(s);
			}
		}

		return code;
	}

	public List<String> generateCommonCodeBegin(CodeGenerator.CodeTarget target, String containerName) {
		List<String> code = new ArrayList<String>();
		for (ContextMenuElement c : contextMenuItems) {
			List<String> s = null;

			try {
				s = c.generateCommonCodeBegin(target, containerName);
			} catch (UnsupportedOperationException e) {
			}

			if (s != null && !s.isEmpty()) {
				if (code.size() != 0) {
					code.add("");
				}
				code.addAll(s);
			}
		}

		return code;
	}

	public List<String> generateCommonCodeEnd(CodeGenerator.CodeTarget target, String containerName) {
		List<String> code = new ArrayList<String>();
		for (ContextMenuElement c : contextMenuItems) {
			List<String> s = null;

			try {
				s = c.generateCommonCodeEnd(target, containerName);
			} catch (UnsupportedOperationException e) {
			}

			if (s != null && !s.isEmpty()) {
				if (code.size() != 0) {
					code.add("");
				}
				code.addAll(s);
			}
		}

		return code;
	}

}
