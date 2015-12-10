package pt.iscte.pidesco.guibuilder.internal.codeGenerator;

public interface CodeGeneratorInterface {

	public String generateImports();

	public String generateStartClass();

	public String generateEndClass();

	public String generateStartConstructorClass();

	public String generateEndConstructorClass();

	public String generateFrame(String[] parameters);

	public String generateButton(String[] parameters);

	public String generateLabel(String[] parameters);

	public String generateTextField(String[] parameters);

	public String generateCheckBox(String[] parameters);

	public String generateAction(String[] parameters);
}
