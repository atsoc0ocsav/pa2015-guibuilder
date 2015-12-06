package pt.iscte.pidesco.guibuilder.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ExtensionPointsData {

	public ExtensionPointsData() {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.guibuilder.widget");
		
		IExtension[] extensions = extensionPoint.getExtensions();
		
		StringBuffer str = new StringBuffer();
		
		
		for(IExtension e : extensions) {
		    IConfigurationElement[] confElements = e.getConfigurationElements();
		    for(IConfigurationElement c : confElements) {
		        String s = c.getAttribute("name");
		       
		        WidgetInterface o;
		        try {
		        	o = (WidgetInterface) c.createExecutableExtension("class");
		        	System.out.println("Name: "+o.getWidgetName());
		        } catch (CoreException e1) {
		            // TODO Auto-generated catch block
		            e1.printStackTrace();
		        }
		    }
		}
	}
	
}
