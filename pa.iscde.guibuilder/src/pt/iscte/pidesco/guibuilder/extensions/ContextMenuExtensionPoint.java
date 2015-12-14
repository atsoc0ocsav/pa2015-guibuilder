package pt.iscte.pidesco.guibuilder.extensions;

import java.util.List;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Menu;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pt.iscte.pidesco.guibuilder.model.ObjectInCompositeContainer;

/**
 * Service containing the operations to be implemented by an extension to the
 * context menu
 */
public interface ContextMenuExtensionPoint {
	/**
	 * Families of objects that can constitute the window to be built
	 */
	public enum OBJECT_FAMILY {
		COMPONENTS, CONTAINERS;
	}

	/**
	 * This method indicated in which types of object this extension will be
	 * called
	 * 
	 * @return a list of object families
	 */
	public List<OBJECT_FAMILY> getFilter();
	
	public boolean acceptsType(OBJECT_FAMILY o);

	// TODO
	/**
	 * Provides the GUIBuilder with the text item to be displayed in the context
	 * menu of the selected objects. This item should already implement the
	 * selection adapter, which is called when the item is selected in the
	 * context menu
	 * 
	 * @param the
	 *            menu to which the item should be added
	 * @param obj
	 */
	public void generateMenuItem(Menu menu, ObjectInCompositeContainer obj, Canvas canvas);

	/**
	 * This method is called on final code generation and has the objective of
	 * providing the code generator with the lines of code necessary generated
	 * by this extension
	 * 
	 * @param the
	 *            code target for which the code should be generated
	 * @param the
	 *            object from which the code is generated
	 * @param the
	 *            variable which the generated code should reference
	 * @return a list with the lines of code that this functionality requires to
	 *         be included in the generated code
	 * @throws UnsupportedOperationException
	 *             in case the methods does not applies to this extension
	 */
	public List<String> generateCode(CodeTarget target, ObjectInCompositeContainer object, String objectName)
			throws UnsupportedOperationException;
}
