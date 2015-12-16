package pa.iscde.guibuilder.atsoc0ocsav.radioButtonGroup;

import java.util.ArrayList;
import java.util.List;

import pa.iscde.guibuilder.model.ObjectInCompositeContainer;

public class RadioButtonGroupsContainer {
	private List<RadioButtonGroupModel> radioButtonGroups;
	private int idCount = 0;

	public RadioButtonGroupsContainer() {
		radioButtonGroups = new ArrayList<RadioButtonGroupModel>();
	}

	public List<RadioButtonGroupModel> getRadioButtonGroups() {
		return radioButtonGroups;
	}

	public RadioButtonGroupModel createNewGroup() {
		RadioButtonGroupModel model = new RadioButtonGroupModel(idCount++);
		radioButtonGroups.add(model);
		return model;
	}

	public boolean removeGroup(int id) {
		RadioButtonGroupModel group = null;
		for (RadioButtonGroupModel r : radioButtonGroups) {
			if (r.getID() == id) {
				group = r;
			}
		}

		return group != null && radioButtonGroups.remove(group);
	}

	public RadioButtonGroupModel getGroupByID(int id) {
		for (RadioButtonGroupModel r : radioButtonGroups) {
			if (r.getID() == id) {
				return r;
			}
		}

		return null;
	}

	public RadioButtonGroupModel getGroupByRadioButton(ObjectInCompositeContainer obj) {
		for (RadioButtonGroupModel r : radioButtonGroups) {
			if (r.hasRadioButton(obj)) {
				return r;
			}
		}
		return null;
	}

	public int getGroupIDByRadioButton(ObjectInCompositeContainer obj) {
		return (getGroupByRadioButton(obj) == null) ? -1 : getGroupByRadioButton(obj).getID();
	}

	public void printGroups() {
		for (RadioButtonGroupModel r : radioButtonGroups) {
			System.out.println("Group " + r.getID() + ": " + r.getChildCount());
		}
	}
}
