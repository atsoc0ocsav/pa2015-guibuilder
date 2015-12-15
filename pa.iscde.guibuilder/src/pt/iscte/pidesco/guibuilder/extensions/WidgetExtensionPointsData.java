package pt.iscte.pidesco.guibuilder.extensions;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class WidgetExtensionPointsData {

	private ArrayList<WidgetInCompositeImpl> widgetsImpl = new ArrayList<WidgetInCompositeImpl>();
	private ArrayList<String> widgetsName = new ArrayList<String>();

	public WidgetExtensionPointsData(GuiBuilderView guiBuilderView) {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.widget");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					WidgetInCompositeImpl widgetImpl = (WidgetInCompositeImpl) c.createExecutableExtension("class");
					widgetsName.add(widgetImpl.getWidgetName());
					widgetsImpl.add(widgetImpl);
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public ArrayList<String> getWidgetsName() {
		return widgetsName;
	}

	public ArrayList<WidgetInCompositeImpl> getWidgetsImplementation() {
		return widgetsImpl;
	}

	public ArrayList<ArrayList<String>> getWidgetsCode(CodeGenerator.CodeTarget target, String containerName,
			int count) {
		ArrayList<ArrayList<String>> widgetsCode = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < widgetsImpl.size(); i++) {
			widgetsCode.add(widgetsImpl.get(i).generateWidgetCode(target, containerName, count));
		}
		return widgetsCode;
	}
}
