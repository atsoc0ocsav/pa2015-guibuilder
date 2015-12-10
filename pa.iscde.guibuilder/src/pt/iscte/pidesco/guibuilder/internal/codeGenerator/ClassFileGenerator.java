package pt.iscte.pidesco.guibuilder.internal.codeGenerator;

import java.io.File;
import java.io.IOException;

import pt.iscte.pidesco.guibuilder.internal.GuiBuilderActivator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ClassFileGenerator {
	public static File createFile(String filename) {
		final ProjectBrowserServices browser = GuiBuilderActivator.getInstance().getBrowserServices();

		String path = browser.getRootPackage().getFile().toString();
		File file = new File(path, filename+".java");
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}

	public static void writeToFile(File file, String text) {
		final JavaEditorServices editor = GuiBuilderActivator.getInstance().getJavaEditorServices();

		editor.setText(file, text);
		editor.saveFile(file);
	}

	public static void openFileInEditor(File file) {
		final JavaEditorServices editor = GuiBuilderActivator.getInstance().getJavaEditorServices();

		editor.openFile(file);
	}
}
