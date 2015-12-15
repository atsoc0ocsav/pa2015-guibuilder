package pa.iscde.guibuilder.extensions;

import java.util.List;

import org.eclipse.swt.widgets.Menu;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.ui.GuiBuilderView;

/**
 * Service containing the operations to be implemented by an extension to the
 * context menu
 */
public interface ContextMenuExtensionElement {
	/**
	 * Families of objects that can constitute the window to be built
	 */
	public enum OBJECT_FAMILY {
		COMPONENTS, CONTAINERS, CANVAS;
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
	public void generateMenuItem(Menu menu, ObjectInCompositeContainer obj, GuiBuilderView guiBuilderView);

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
	public List<String> generateCodeForObject(CodeTarget target, ObjectInCompositeContainer object, String objectName)
			throws UnsupportedOperationException;

	public List<String> generateCommonCode(CodeTarget target) throws UnsupportedOperationException;
}
