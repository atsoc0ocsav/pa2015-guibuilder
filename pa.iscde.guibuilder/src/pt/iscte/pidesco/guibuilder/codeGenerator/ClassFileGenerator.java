package pt.iscte.pidesco.guibuilder.codeGenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pt.iscte.pidesco.guibuilder.internal.GuiBuilderActivator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ClassFileGenerator {
	public static File createFile(String filename) {
		final ProjectBrowserServices browser = GuiBuilderActivator.getInstance().getBrowserServices();

		String path = browser.getRootPackage().getFile().toString();
		File file = new File(path, filename + ".java");

		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

	public static void writeToFile(File file, List<String> code) {
		final JavaEditorServices editor = GuiBuilderActivator.getInstance().getJavaEditorServices();

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < code.size(); i++) {
			buffer.append(code.get(i));

			if (i != (code.size() - 1)) {
				buffer.append("\n");
			}
		}

		editor.setText(file, buffer.toString());
		editor.saveFile(file);

		GuiBuilderActivator.getInstance().getPidescoServices().runTool("pt.iscte.pidesco.projectbrowser.refresh", true);
	}

	public static void openFileInEditor(File file) {
		final JavaEditorServices editor = GuiBuilderActivator.getInstance().getJavaEditorServices();

		editor.openFile(file);
	}
}
