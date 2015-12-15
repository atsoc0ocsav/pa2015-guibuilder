package pa.iscde.guibuilder.atsoc0ocsav.radioButtonGroup;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.ContextMenuExtensionElement;
import pa.iscde.guibuilder.extensions.ContextMenuExtensionElement.OBJECT_FAMILY;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.model.compositeContents.ComponentInComposite;
import pa.iscde.guibuilder.model.compositeContents.ComponentInCompositeImpl;
import pa.iscde.guibuilder.ui.GuiBuilderView;
import pa.iscde.guibuilder.ui.GuiLabels;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class RadioButtonGroupContextMenuItem implements ContextMenuExtensionElement {
	private final OBJECT_FAMILY[] accepts = {/* OBJECT_FAMILY.COMPONENTS */};
	private final String DELETED_OBJECT_MSG = "Added %s to radio button %s!";
	private final String MENU_ITEM_TEXT = "Add to group...";

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
	public void generateMenuItem(Menu menu, final ObjectInCompositeContainer obj, final GuiBuilderView guiBuilderView) {
		if (((ComponentInComposite) obj.getObjectInComposite()).getComponentType() == GUIBuilderComponent.RADIO_BTN) {
			MenuItem addToRadioButtonGroup = new MenuItem(menu, SWT.NONE);
			addToRadioButtonGroup.setText(MENU_ITEM_TEXT);
			addToRadioButtonGroup.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

//					if (obj.hasChilds()) {
//						if (!MessageDialog.openConfirm(guiBuilderView.getTopCanvas().getShell(),
//								GuiLabels.DialogMenuLabel.DELETE_OBJECT.str(),
//								GuiLabels.DialogMenuLabel.DELETE_OBJECT_CONFIRM_MSG.str())) {
//							return;
//						}
//					}

					ObjectInCompositeContainer objectParent = obj.getParent();

					if (obj.getObjectInComposite().getObjectFamily() == GUIBuilderObjectFamily.COMPONENTS) {
						((ComponentInCompositeImpl) obj.getObjectInComposite()).getControl().dispose();
					}

					obj.getObjectInComposite().getFigure().setVisible(false);
					objectParent.removeChild(obj);

					guiBuilderView.getTopCanvas().update();
					guiBuilderView.getTopCanvas().redraw();
					guiBuilderView.getTopCanvas().layout();

					guiBuilderView.setMessage(DELETED_OBJECT_MSG, obj.getId().split("\t")[0]);
				}
			});
		}
	}

	@Override
	public List<String> generateCodeForObject(CodeTarget target, ObjectInCompositeContainer object, String objectName)
			throws UnsupportedOperationException {
		// List<String> code = new ArrayList<String>();
		//
		// switch(target){
		// case SWING:
		// code.add(.add(birdButton);
		// group.add(catButton);
		// group.add(dogButton);
		// group.add(rabbitButton);
		// group.add(pigButton);
		//
		// case SWT:
		//
		// default:
		// throw new IllegalArgumentException("Switch case not defined!");
		// }
		return null;
	}

	@Override
	public List<String> generateCommonCode(CodeTarget target) throws UnsupportedOperationException {
		// List<String> code = new ArrayList<String>();
		//
		// switch(target){
		// case SWING:
		// ButtonGroup group = new ButtonGroup();
		// group.add(birdButton);
		// group.add(catButton);
		// group.add(dogButton);
		// group.add(rabbitButton);
		// group.add(pigButton);
		// default:
		// throw new IllegalArgumentException("Switch case not defined!");
		// }
		return null;
	}
}