package pt.iscte.pidesco.guibuilder.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Menu;

import pt.iscte.pidesco.guibuilder.extensions.ContextMenuExtensionPoint.OBJECT_FAMILY;
import pt.iscte.pidesco.guibuilder.model.ObjectInCompositeContainer;

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

	public void addContextMenuItems(Menu menu, ObjectInCompositeContainer obj, Canvas canvas) {
		OBJECT_FAMILY f;
		switch (obj.getObjectInComposite().getObjectFamily()) {
		case COMPONENTS:
			f = OBJECT_FAMILY.COMPONENTS;
			break;
		case CONTAINERS:
			f = OBJECT_FAMILY.CONTAINERS;
			break;
		case CANVAS:
		case LAYOUTS:
		default:
			throw new IllegalArgumentException("Switch case not defined!");
		}

		for (ContextMenuExtensionPoint c : contextMenuItems) {
			if (c.acceptsType(f)) {
				c.generateMenuItem(menu, obj, canvas);
			}
		}
	}
}
