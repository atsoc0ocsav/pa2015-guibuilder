package pa.iscde.guibuilder.atsoc0ocsav.radioButtonGroup;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.ContextMenuElement;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.model.compositeContents.ComponentInComposite;
import pa.iscde.guibuilder.ui.GuiBuilderView;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public class RadioButtonGroupContextMenuItem implements ContextMenuElement {
	private final OBJECT_FAMILY[] accepts = {/* OBJECT_FAMILY.COMPONENTS */};
	private final String ADDED_TO_BUTTON_GROUP = "Added %s to radio button group %s!";
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
		System.out.println("Was called!");
		if (((ComponentInComposite) obj.getObjectInComposite()).getComponentType() == GUIBuilderComponent.RADIO_BTN) {
			MenuItem addToRadioButtonGroup = new MenuItem(menu, SWT.NONE);
			addToRadioButtonGroup.setText(MENU_ITEM_TEXT);
			addToRadioButtonGroup.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final Display display = new Display();
			        final Shell shell = new Shell(display);
			        shell.setLayout(new GridLayout(1, false));

			        Label label = new Label(shell, SWT.NONE);
					label.setText("teste");
					

			        shell.pack();
			        shell.open();
			        while (!shell.isDisposed())
			        {
			            if ( !display.readAndDispatch() )
			                display.sleep();
			        }
			        display.dispose();
					
//					ObjectInCompositeContainer objectParent = obj.getParent();
//
//					if (obj.getObjectInComposite().getObjectFamily() == GUIBuilderObjectFamily.COMPONENTS) {
//						((ComponentInCompositeImpl) obj.getObjectInComposite()).getControl().dispose();
//					}
//
//					obj.getObjectInComposite().getFigure().setVisible(false);
//					objectParent.removeChild(obj);
//
//					guiBuilderView.getTopCanvas().update();
//					guiBuilderView.getTopCanvas().redraw();
//					guiBuilderView.getTopCanvas().layout();
//
//					// TODO -> se removermos um botão de todos os grupos
//					guiBuilderView.setMessage(ADDED_TO_BUTTON_GROUP, obj.getId().split("\t")[0]);
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