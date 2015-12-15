package pt.iscte.pidesco.guibuilder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class GuiBuilderActivator implements BundleActivator {
	private static GuiBuilderActivator instance;
	private ProjectBrowserServices browserServices;
	private JavaEditorServices editor;
	private PidescoServices pidescoServices;

	@Override
	public void start(BundleContext context) throws Exception {
		instance = this;
		
		final ServiceReference<ProjectBrowserServices> ref1 = context.getServiceReference(ProjectBrowserServices.class);
		browserServices = context.getService(ref1);

		final ServiceReference<JavaEditorServices> ref2 = context.getServiceReference(JavaEditorServices.class);
		editor = context.getService(ref2);
		
		final ServiceReference<PidescoServices> ref3 = context.getServiceReference(PidescoServices.class);
		pidescoServices= context.getService(ref3);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
	}

	public static GuiBuilderActivator getInstance() {
		return instance;
	}

	public ProjectBrowserServices getBrowserServices() {
		return browserServices;
	}

	public JavaEditorServices getJavaEditorServices() {
		return editor;
	}

	public PidescoServices getPidescoServices() {
		return pidescoServices;
	}
}