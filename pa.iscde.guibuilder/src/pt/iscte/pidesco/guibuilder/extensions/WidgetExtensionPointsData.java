package pt.iscte.pidesco.guibuilder.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class WidgetExtensionPointsData {

	private String widgetName = "";
	private Control widget = null;
	private Color backgroundColor = null;
	private Color foregroundColor = null;
	private WidgetInterface widgetInterface;

	public WidgetExtensionPointsData(GuiBuilderView guiBuilderView) {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.widget");
		IExtension[] extensions = extensionPoint.getExtensions();

		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					widgetInterface = (WidgetInterface) c.createExecutableExtension("class");
					widgetName = widgetInterface.getWidgetName();
					widgetInterface.createWidget(guiBuilderView.getTopCanvas());
					widget = widgetInterface.getWidget();
					backgroundColor = widgetInterface.getBackgroundColor();
					foregroundColor = widgetInterface.getForegroundColor();
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

	public Control getWidget() {
		return widget;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public String[] getWidgetCode(CodeGenerator.CodeTarget target, String containerName, int count) {
		return widgetInterface.generateWidgetCode(target, containerName,count);
	}

}
