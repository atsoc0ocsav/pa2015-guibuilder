package pt.iscte.pidesco.guibuilder.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.MenuItem;

import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;

public class ContextMenuExtensionPointData {
	private GuiBuilderView guiBuilderView;
	private List<ContextMenuExtensionPoint> contextMenuItems;

	public ContextMenuExtensionPointData(GuiBuilderView guiBuilderView) {
		this.guiBuilderView = guiBuilderView;

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

	public List<MenuItem> getContextMenuItems(GuiLabels.GUIBuilderObjectFamily target) {
		List<MenuItem> labels = new ArrayList<MenuItem>();

		for (ContextMenuExtensionPoint c : contextMenuItems) {
			if (c.getFilter().contains(target)) {
				labels.add(c.getItem());
			}
		}

		return labels;
	}
}
