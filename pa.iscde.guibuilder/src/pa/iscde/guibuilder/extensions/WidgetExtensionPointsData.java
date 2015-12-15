package pa.iscde.guibuilder.extensions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator;
import pa.iscde.guibuilder.ui.GuiBuilderView;

public class WidgetExtensionPointsData {

	private List<WidgetInComposite> widgets;
	private List<String> widgetNames;

	public WidgetExtensionPointsData(GuiBuilderView guiBuilderView) {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.widget");
		IExtension[] extensions = extensionPoint.getExtensions();

		widgets = new ArrayList<WidgetInComposite>();
		widgetNames = new ArrayList<String>();

		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					widgets.add((WidgetInComposite) c.createExecutableExtension("class"));
					widgetNames.add(((WidgetInComposite) c.createExecutableExtension("class")).getWidgetName());
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public List<String> getWidgetsNames() {
		return widgetNames;
	}

	public List<WidgetInComposite> getWidgetsImplementation() {
		return widgets;
	}

	public WidgetInComposite getWidgetByName(String name) {
		for (WidgetInComposite w : widgets) {
			if (w.getWidgetName().contains(name) || name.contains(w.getWidgetName())) {
				return w;
			}
		}

		return null;
	}

	public List<String> getWidgetsCode(CodeGenerator.CodeTarget target, String containerName, int count) {
		List<String> code = new ArrayList<String>();

		for (WidgetInComposite w : widgets) {
			List<String> c = w.generateWidgetCode(target, containerName, count);

			if (c != null && !c.isEmpty()) {
				code.add("");
				code.addAll(c);
			}
		}

		return code;
	}
}
