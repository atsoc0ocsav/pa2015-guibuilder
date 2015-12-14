package pa.iscde.guibuilder.contextMenuExtension;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pt.iscte.pidesco.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pt.iscte.pidesco.guibuilder.extensions.ContextMenuExtensionPoint;
import pt.iscte.pidesco.guibuilder.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class DeleteContextMenuExtensionPointImplementation implements ContextMenuExtensionPoint {
	private OBJECT_FAMILY[] accepts = { OBJECT_FAMILY.COMPONENTS };

	@Override
	public List<OBJECT_FAMILY> getFilter() {
		return Arrays.asList(accepts);
	}

	public boolean acceptsType(OBJECT_FAMILY o) {
		for (OBJECT_FAMILY object : accepts) {
			if (object.equals(o)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void generateMenuItem(Menu menu, final ObjectInCompositeContainer obj, final Canvas canvas) {
		MenuItem deleteItem = new MenuItem(menu, SWT.NONE);
		deleteItem.setText(GuiLabels.DialogMenuLabel.DELETE_OBJECT.str());
		deleteItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (obj.hasChilds()) {
					if (!MessageDialog.openConfirm(canvas.getShell(), GuiLabels.DialogMenuLabel.DELETE_OBJECT.str(),
							GuiLabels.DialogMenuLabel.DELETE_OBJECT_CONFIRM_MSG.str())) {
						return;
					}
				}

				ObjectInCompositeContainer objectParent = obj.getParent();

				if (obj.getObjectInComposite().getObjectFamily() == GUIBuilderObjectFamily.COMPONENTS) {
					((ComponentInCompositeImpl) obj.getObjectInComposite()).getControl().dispose();
				}

				obj.getObjectInComposite().getFigure().setVisible(false);
				objectParent.removeChild(obj);

				canvas.update();
				canvas.redraw();
				canvas.layout();
			}
		});
	}

	@Override
	public List<String> generateCode(CodeTarget target, ObjectInCompositeContainer object, String objectName)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

}
