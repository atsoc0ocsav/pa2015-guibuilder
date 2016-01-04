package pa.iscde.guibuilder.extensions;

import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator;
import pa.iscde.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;

/**
 * Service containing the operations to be implemented by an extension to the
 * widget extension point
 * Two examples of use of this class can be found here: https://goo.gl/RNSFna
 */
public abstract class WidgetInComposite extends ComponentInCompositeImpl {

	/**
	 * Constructor for the widget. It receives an array containing all the
	 * options that this widget accepts on a context menu. It is demanding for
	 * this class childs to call super constructor with the indication of which
	 * context menu elements need/ have to be available
	 * 
	 * @param contextMenuItems is an array containing an indication of all context
	 *            menu items to display in this widget
	 */

	public WidgetInComposite(ContextMenuItem[] contextMenuItems) {
		super(GUIBuilderComponent.WIDGET, contextMenuItems);
	}

	/**
	 * Returns the name that is being displayed in tab bar to refer to this
	 * widget
	 * 
	 * @return displayed widget name
	 */
	public abstract String getWidgetName();

	/**
	 * Set the widget name displayed in the tab bar
	 * 
	 * @param widgetName
	 *            to be displayed
	 */

	public abstract void setWidgetName(String widgetName);

	/**
	 * Upon the call of this method, it should create the widget in the canvas
	 * and in the indicated position. Don't forget to keep a copy of the
	 * variables in the model (i.e. not only in the GUI component), being
	 * therefore necessary to update super class values such as control,
	 * location, size, backgroundColor, foreground color and enabled.
	 * 
	 * @param canvas
	 *            where widget will be created
	 * @param location of the widget in canvas
	 * @param size
	 *            of the widget
	 */

	public abstract void createWidget(Canvas canvas, Point location, Point size);

	/**
	 * When GUIBuilder code generator is invoked, this method is therefore
	 * called. It should generate the code lines for this widget, according to
	 * the indicated code target. It is also important to take in note that some
	 * code target may need for the widget to refer to a parent component, which
	 * is also passed in this method. Finally, in the generated code for this
	 * widget, the variables names should be composed of a static prefix plus
	 * the count. In the first line of generated code is then returned the used
	 * variable name.
	 * 
	 * @param target for where to generate the code
	 * @param containerName is the parent
	 *            container to which the widget may refer, if needed
	 * @param count
	 *            value to be added to the used variables names
	 * @return list with the widget variable name in first place followed by the
	 *         code lines
	 */

	public abstract List<String> generateWidgetCode(CodeGenerator.CodeTarget target, String containerName, int count);
}
