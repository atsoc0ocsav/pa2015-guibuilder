package pt.iscte.pidesco.guibuilder.internal;

public interface GenerateObjectsInterface {

	public String generateStartClass();

	public String generateEndClass();

	public String generateStartConstructorClass();

	public String generateEndConstructorClass();

	public String generateFrame(String[] parameters);

	public String generateButton(String[] parameters);

	public String generateLabel(String[] parameters);

	public String generateTextField(String[] parameters);

	public String generateCheckBox(String[] parameters);

	public String generateWidget(String[] parameters);

	public String generateAction(String[] parameters);
}
