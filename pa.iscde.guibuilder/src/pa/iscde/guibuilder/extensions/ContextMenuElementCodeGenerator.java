package pa.iscde.guibuilder.extensions;

import java.util.List;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;

/**
 * Service containing the operations to be implemented by an extension to the
 * context menu extension point that needs to generate source code. It works in
 * conjunction with ContextMenuElement interface and can not be used in separate
 */
public interface ContextMenuElementCodeGenerator {
	/**
	 * This method is called on code generation and has the objective of
	 * providing the code generator with the lines of code necessary generated
	 * by this extension for a specific object. It is also important to take in
	 * note that some code target may need for the widget to refer to a parent
	 * component, which is also passed in this method.
	 * 
	 * @param target
	 *            for which the code should be generated
	 * @param object
	 *            of which the code is generated
	 * @param containerName
	 *            is the parent element that should be referenced, in case the
	 *            selected target used hierarchical scheme (for instance SWT)
	 * @param objectName
	 *            is the variable name to be used in the generated code
	 * @return a list containing the code lines to be added to the generated
	 *         code
	 */
	public List<String> generateCodeForObject(CodeTarget target, ObjectInCompositeContainer object,
			String containerName, String objectName);

	/**
	 * This method is called on code generation and has the objective of
	 * providing the code generator with the necessary general codes of line, to
	 * be included in the beginning of the generated code. It is also important
	 * to take in note that some code target may need for the widget to refer to
	 * a parent component, which is also passed in this method.
	 * 
	 * @param target
	 *            for which the code should be generated
	 * @param containerName
	 *            is the parent element that should be referenced, in case the
	 *            selected target used hierarchical scheme (for instance SWT)
	 * @return a list containing the code lines to be added to the generated
	 *         code
	 */
	public List<String> generateCommonCodeBegin(CodeTarget target, String containerName);

	/**
	 * This method is called on code generation and has the objective of
	 * providing the code generator with the necessary general codes of line, to
	 * be included in the end of the generated code. It is also important to
	 * take in note that some code target may need for the widget to refer to a
	 * parent component, which is also passed in this method.
	 * 
	 * @param target
	 *            for which the code should be generated
	 * @param containerName
	 *            is the parent element that should be referenced, in case the
	 *            selected target used hierarchical scheme (for instance SWT)
	 * @return a list containing the code lines to be added to the generated
	 *         code
	 */
	public List<String> generateCommonCodeEnd(CodeTarget target, String containerName);
}
