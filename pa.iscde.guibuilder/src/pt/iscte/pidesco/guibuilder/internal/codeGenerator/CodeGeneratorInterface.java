package pt.iscte.pidesco.guibuilder.internal.codeGenerator;

import java.util.List;

public interface CodeGeneratorInterface {

	public List<String> generateImports();

	public List<String> generateClassBegin();

	public List<String> generateClassEnd();

	public List<String> generateConstructorBegin();

	public List<String> generateConstructorEnd();

	public List<String> generateInitialization(String[] parameters);

	public List<String> generateButton(String[] parameters);

	public List<String> generateLabel(String[] parameters);

	public List<String> generateTextField(String[] parameters);

	public List<String> generateCheckBox(String[] parameters);

	public List<String> generateAction(String[] parameters);

	public List<String> generateContainer(String[] parameters);
	
	public int getComponentCount();
	
	public void increaseComponentCount();
}
