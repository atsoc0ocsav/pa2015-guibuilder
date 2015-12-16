package pa.iscde.guibuilder.atsoc0ocsav.radioButtonGroup;

import java.util.ArrayList;
import java.util.List;

import pa.iscde.guibuilder.model.ObjectInCompositeContainer;

public class RadioButtonGroupModel {
	private List<ObjectInCompositeContainer> radioButtons;
	private int id;
	private String text;

	public RadioButtonGroupModel(int id) {
		this.id = id;
		radioButtons = new ArrayList<ObjectInCompositeContainer>();
	}

	public void addChild(ObjectInCompositeContainer child) {
		radioButtons.add(child);
	}

	public boolean removeChild(ObjectInCompositeContainer child) {
		return radioButtons.remove(child);
	}

	public List<ObjectInCompositeContainer> getChilds() {
		return radioButtons;
	}

	public int getChildCount() {
		return radioButtons.size();
	}

	public int getID() {
		return id;
	}

	public boolean hasChilds() {
		return !radioButtons.isEmpty();
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public boolean hasRadioButton(ObjectInCompositeContainer obj) {
		for (ObjectInCompositeContainer o : radioButtons) {
			if (o.equals(obj)) {
				return true;
			}
		}
		return false;
	}
}
