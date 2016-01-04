package pa.iscde.guibuilder.atsoc0ocsav.deleteItem;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pa.iscde.guibuilder.extensions.ContextMenuElement;
import pa.iscde.guibuilder.model.ObjectInComposite;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.model.compositeContents.ComponentInComposite;
import pa.iscde.guibuilder.model.compositeContents.ContainerInComposite;
import pa.iscde.guibuilder.ui.FigureMoverResizer;
import pa.iscde.guibuilder.ui.GuiBuilderView;

public class DeleteObjectContextMenuItem implements ContextMenuElement {
	private final OBJECT_FAMILY[] accepts = { OBJECT_FAMILY.COMPONENTS, OBJECT_FAMILY.CONTAINERS };
	private final String DELETED_OBJECT_MSG = "Deleted %s from canvas!";
	private final String MENU_ITEM_TEXT = "Delete Object";
	private final String DELETE_OBJECT_CONFIRM_MSG = "This object has other objects inside. Do you want to continue?";

	@Override
	public boolean acceptsType(OBJECT_FAMILY o) {
		for (OBJECT_FAMILY object : accepts) {
			if (object.equals(o)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void generateMenuItem(Menu menu, final ObjectInCompositeContainer obj, final GuiBuilderView guiBuilderView) {
		MenuItem deleteItem = new MenuItem(menu, SWT.NONE);
		deleteItem.setText(MENU_ITEM_TEXT);
		deleteItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ObjectInComposite object = obj.getObjectInComposite();

				if (obj.hasChilds()) {
					if (!MessageDialog.openConfirm(guiBuilderView.getTopCanvas().getShell(), MENU_ITEM_TEXT,
							DELETE_OBJECT_CONFIRM_MSG)) {
						return;
					}
				}

				ObjectInCompositeContainer objectParent = obj.getParent();
				if (object instanceof ComponentInComposite) {
					((ComponentInComposite) object).getControl().dispose();
					((FigureMoverResizer) (((ComponentInComposite) object).getObjectMoverResizer())).getFigure()
							.setVisible(false);
				} else if (object instanceof ContainerInComposite) {
					((FigureMoverResizer) (((ContainerInComposite) object).getObjectMoverResizer())).getFigure()
							.setVisible(false);
				}

				object.getFigure().setVisible(false);
				objectParent.removeChild(obj);

				guiBuilderView.getTopCanvas().update();
				guiBuilderView.getTopCanvas().redraw();
				guiBuilderView.getTopCanvas().layout();

				guiBuilderView.setMessage(DELETED_OBJECT_MSG, obj.getId().split("\t")[0]);
			}
		});
	}
}
