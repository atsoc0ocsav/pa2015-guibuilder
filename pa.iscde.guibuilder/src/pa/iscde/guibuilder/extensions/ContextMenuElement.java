package pa.iscde.guibuilder.extensions;

import org.eclipse.swt.widgets.Menu;

import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.ui.GuiBuilderView;

/**
 * Service containing the operations to be implemented by an extension to the
 * context menu extension point
 */
public interface ContextMenuElement {
	/**
	 * Families of objects that can constitute the GUI to be built
	 */
	public enum OBJECT_FAMILY {
		COMPONENTS, CONTAINERS, CANVAS;
	}

	/**
	 * This methods allows for accessing if the context menu's element is
	 * applicable to a specific object belonging to an object family
	 * 
	 * @param the
	 *            object family to test
	 * @return if this context menu element is applicable to the specified
	 *         object family
	 */
	public boolean acceptsType(OBJECT_FAMILY o);

	/**
	 * Generates the menu item in the menu. Special attentions should be taken,
	 * since the action listener for this item should be defined in this method
	 * too
	 * 
	 * @param menu
	 *            where to generate the menu item
	 * @param object
	 *            from where the menu was called
	 * @param GUIBuilder's
	 *            view, allowing access to numerous canvas elements namely the
	 *            canvas and error messages
	 */
	public void generateMenuItem(Menu menu, ObjectInCompositeContainer obj, GuiBuilderView guiBuilderView);
}
