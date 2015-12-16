package pa.iscde.guibuilder.atsoc0ocsav.radioButtonGroup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.ui.GuiBuilderView;
import pa.iscde.guibuilder.ui.InputDialog;

public class GUI extends Dialog {
	private final String ADDED_TO_BUTTON_GROUP = "Added %s to radio button group %d!";
	private final String REMOVED_BUTTON_FROM_GROUP = "Removed %s from radio button group %d!";
	private final String MOVED_TO_GROUP = "Moved %s to radio button group %d!";

	private RadioButtonGroupsContainer radioButtonGroups;
	private ObjectInCompositeContainer object;
	private Combo comboBox;
	private GuiBuilderView guiBuilderView;

	public GUI(GuiBuilderView guiBuilderView, RadioButtonGroupsContainer radioButtonGroups,
			ObjectInCompositeContainer object) {
		super(guiBuilderView.getShell());
		this.radioButtonGroups = radioButtonGroups;
		this.object = object;
		this.guiBuilderView = guiBuilderView;
	}

	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;

		new Label(container, SWT.NONE).setText("Select group to include radio button:");

		comboBox = new Combo(container, SWT.READ_ONLY);
		List<String> items = new ArrayList<String>();
		items.add("None");

		RadioButtonGroupModel group = radioButtonGroups.getGroupByRadioButton(object);
		int index = 0;
		for (int i = 0; i < radioButtonGroups.getRadioButtonGroups().size(); i++) {
			RadioButtonGroupModel g = radioButtonGroups.getRadioButtonGroups().get(i);
			items.add("Group " + g.getID());

			if (group != null && g.equals(group)) {
				index = i;
			}
		}

		comboBox.setItems(items.toArray(new String[items.size()]));
		comboBox.select(index);
		comboBox.setLayoutData(gridData);

		Composite bottomButtons = new Composite(container, SWT.NONE);
		bottomButtons.setLayoutData(gridData);
		bottomButtons.setLayout(new FillLayout());

		final Button setTitle = new Button(bottomButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
		setTitle.setText("Set group title");
		setTitle.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Point position = e.display.map(parent, null, new Point(e.x, e.y));
				String name = new InputDialog(position.x, position.y, container.getShell(), SWT.BAR).open();

				if (name != null) {
					int selectedIndex = comboBox.getSelectionIndex();
					String selectedItem = comboBox.getItem(selectedIndex);
					radioButtonGroups.getGroupByID(Integer.parseInt(selectedItem.split(" ")[1])).setText(name);
					
					List<String> items = new ArrayList<String>();
					items.add("None");
					for (int i = 0; i < radioButtonGroups.getRadioButtonGroups().size(); i++) {
						RadioButtonGroupModel g = radioButtonGroups.getRadioButtonGroups().get(i);
						items.add("Group " + g.getID() + (g.getText() != null ? " - " + g.getText() : ""));
					}

					comboBox.setItems(items.toArray(new String[items.size()]));
					comboBox.select(selectedIndex);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		setTitle.setEnabled(false);
		
		Button addGroup = new Button(bottomButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
		addGroup.setText("Add group");
		addGroup.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				radioButtonGroups.createNewGroup();

				List<String> items = new ArrayList<String>();
				items.add("None");
				for (int i = 0; i < radioButtonGroups.getRadioButtonGroups().size(); i++) {
					RadioButtonGroupModel g = radioButtonGroups.getRadioButtonGroups().get(i);
					items.add("Group " + g.getID() + (g.getText() != null ? " - " + g.getText() : ""));
				}

				comboBox.setItems(items.toArray(new String[items.size()]));
				comboBox.select(items.size() - 1);
				setTitle.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Button removeGroup = new Button(bottomButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
		removeGroup.setText("Remove group");
		removeGroup.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedItem = comboBox.getItem(comboBox.getSelectionIndex());
				radioButtonGroups.removeGroup(Integer.parseInt(selectedItem.split(" ")[1]));

				List<String> items = new ArrayList<String>();
				items.add("None");
				for (int i = 0; i < radioButtonGroups.getRadioButtonGroups().size(); i++) {
					RadioButtonGroupModel g = radioButtonGroups.getRadioButtonGroups().get(i);
					items.add("Group " + g.getID() + (g.getText() != null ? " - " + g.getText() : ""));
				}

				comboBox.setItems(items.toArray(new String[items.size()]));
				
				if(items.size()==1){
					setTitle.setEnabled(false);
				}
				comboBox.select(items.size() - 1);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		comboBox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboBox.getSelectionIndex() == 0) {
					setTitle.setEnabled(false);
				} else {
					setTitle.setEnabled(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		return container;
	}

	@Override
	protected void okPressed() {
		RadioButtonGroupModel o = radioButtonGroups.getGroupByRadioButton(object);
		int index = comboBox.getSelectionIndex();

		if (o != null && index == 0) { // remove
			o.removeChild(object);
			guiBuilderView.setMessage(REMOVED_BUTTON_FROM_GROUP, object.getId().split("\t")[0], o.getID());

		} else if (o == null && index != 0) { // add
			String selectedItem = comboBox.getItem(index);
			RadioButtonGroupModel whereToAdd = radioButtonGroups
					.getGroupByID(Integer.parseInt(selectedItem.split(" ")[1]));

			whereToAdd.addChild(object);
			guiBuilderView.setMessage(ADDED_TO_BUTTON_GROUP, object.getId().split("\t")[0], whereToAdd.getID());
		} else if (o != null && index != 0) { // change or not?
			String selectedItem = comboBox.getItem(index);
			RadioButtonGroupModel whereToAdd = radioButtonGroups
					.getGroupByID(Integer.parseInt(selectedItem.split(" ")[1]));

			if (!o.equals(whereToAdd)) {
				o.removeChild(object);
				whereToAdd.addChild(object);
				guiBuilderView.setMessage(MOVED_TO_GROUP, object.getId().split("\t")[0], whereToAdd.getID());
			}
		}

		super.okPressed();
	}
}
