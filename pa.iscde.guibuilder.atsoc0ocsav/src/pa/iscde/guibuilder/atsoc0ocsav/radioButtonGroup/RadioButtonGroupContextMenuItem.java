package pa.iscde.guibuilder.atsoc0ocsav.radioButtonGroup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.ContextMenuElement;
import pa.iscde.guibuilder.extensions.ContextMenuElementCodeGenerator;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.model.compositeContents.ComponentInComposite;
import pa.iscde.guibuilder.ui.GuiBuilderView;
import pa.iscde.guibuilder.ui.GuiLabels.GUIBuilderComponent;

public class RadioButtonGroupContextMenuItem implements ContextMenuElement, ContextMenuElementCodeGenerator {
	private final OBJECT_FAMILY[] accepts = { OBJECT_FAMILY.COMPONENTS };
	private final String MENU_ITEM_TEXT = "Add to radio button group...";
	private final String VAR_NAME = "radioButtonGroup";
	private RadioButtonGroupsContainer radioButtonGroups;

	public RadioButtonGroupContextMenuItem() {
		radioButtonGroups = new RadioButtonGroupsContainer();
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
					new GUI(guiBuilderView, radioButtonGroups, obj).open();
				}
			});
		}
	}

	@Override
	public List<String> generateCodeForObject(CodeTarget target, ObjectInCompositeContainer object,
			String containerName, String objectName) {
		List<String> code = new ArrayList<String>();
		if (radioButtonGroups.getGroupByRadioButton(object) != null) {

			String groupName = VAR_NAME + radioButtonGroups.getGroupIDByRadioButton(object);
			switch (target) {
			case SWING:
				code.add(groupName + ".add(" + objectName + ");");
				break;
			case SWT:
				code.add(objectName + ".setParent(" + groupName + ");");
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}

		}
		return code;
	}

	@Override
	public List<String> generateCommonCodeBegin(CodeTarget target, String containerName) {
		List<String> code = new ArrayList<String>();
		String elementName;

		switch (target) {
		case SWING:
			for (RadioButtonGroupModel radioButtonGroup : radioButtonGroups.getRadioButtonGroups()) {
				if (radioButtonGroup.hasChilds()) {
					elementName = VAR_NAME + radioButtonGroup.getID();
					code.add("ButtonGroup " + elementName + " = new ButtonGroup();");
				}
			}
			break;
		case SWT:
			for (RadioButtonGroupModel radioButtonGroup : radioButtonGroups.getRadioButtonGroups()) {
				if (radioButtonGroup.hasChilds()) {
					elementName = VAR_NAME + radioButtonGroup.getID();
					code.add("Group " + elementName + " = new Group(" + containerName + ", SWT.NO_RADIO_GROUP);");
					code.add(elementName + ".setText(\"" + radioButtonGroup.getText() + "\");");
					code.add(elementName + ".setLayout(new RowLayout(SWT.VERTICAL));");
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Switch case not defined!");
		}
		return code;
	}

	@Override
	public List<String> generateCommonCodeEnd(CodeTarget target, String containerName) {
		return null;
	}
}