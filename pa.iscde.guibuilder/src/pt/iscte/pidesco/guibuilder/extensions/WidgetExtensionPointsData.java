package pt.iscte.pidesco.guibuilder.extensions;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class WidgetExtensionPointsData {

	private String widgetName = "";
	private WidgetInCompositeImpl widgetImpl;

	public WidgetExtensionPointsData(GuiBuilderView guiBuilderView) {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.widget");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					widgetImpl = (WidgetInCompositeImpl) c.createExecutableExtension("class");
					widgetName = widgetImpl.getWidgetName();
					// System.out.println("Name: " + o.getWidgetNames());
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	public String getWidgetName() {
		return widgetName;
	}

	public WidgetInCompositeImpl getWidgetImplementation() {
		return widgetImpl;
	}

	public List<String> getWidgetCode(CodeGenerator.CodeTarget target, String containerName, int count) {
		return widgetImpl.generateWidgetCode(target, containerName, count);
	}
}
