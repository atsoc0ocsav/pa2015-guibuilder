package pt.iscte.pidesco.guibuilder.internal.model;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.CanvasInComposite;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ComponentInCompositeImpl;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ContainerInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;

public class ObjectInCompositeContainer {
	private String id;
	private boolean isRoot;
	private boolean canBeParent;
	private ObjectInCompositeContainer parent;
	private ObjectInComposite object;

	private List<ObjectInCompositeContainer> childs;

	public ObjectInCompositeContainer(String id, ObjectInComposite object, ObjectInCompositeContainer parent) {
		this.id = id;
		this.object = object;
		this.parent = parent;

		childs = new ArrayList<ObjectInCompositeContainer>();

		if (object instanceof CanvasInComposite) {
			((CanvasInComposite) object).getCanvasResizer().setObjectInComposite(this);
		} else if (object instanceof ComponentInCompositeImpl) {
			((ComponentInCompositeImpl) object).getObjectMoverResizer().setObjectInComposite(this);
		} else if (object instanceof ContainerInComposite) {
			((ContainerInComposite) object).getObjectMoverResizer().setObjectInComposite(this);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public ObjectInCompositeContainer getParent() {
		return parent;
	}

	public void setParent(ObjectInCompositeContainer parent) {
		this.parent = parent;
	}

	public boolean hasChilds() {
		return !childs.isEmpty();
	}

	public void setCanBeParent(boolean canBeParent) {
		this.canBeParent = canBeParent;
	}

	public boolean canBeParent() {
		return canBeParent;
	}

	public boolean removeChild(ObjectInCompositeContainer child) {
		return childs.remove(child);
	}

	public ObjectInComposite getObjectInComposite() {
		return object;
	}

	public ObjectInCompositeContainer getChildById(String id) {
		if (hasChilds()) {
			for (ObjectInCompositeContainer child : childs) {
				if (child.getId().equals(id)) {
					return child;
				} else {
					ObjectInCompositeContainer c = child.getChildById(id);
					if (c != null) {
						return c;
					}
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public void addChild(ObjectInCompositeContainer child) {
		if (child != null) {
			if (canBeParent) {
				childs.add(child);
			} else {
				throw new IllegalAccessError("This object can not have childs");
			}
		} else {
			throw new NullPointerException("Null child");
		}
	}

	/**
	 * This method providesa list with all the childs for this object
	 * 
	 * @return list of the first degree (direct) childs
	 */
	public List<ObjectInCompositeContainer> getChilds() {
		return childs;
	}

	public List<ObjectInCompositeContainer> getAllChildsByType(GuiLabels.GUIBuilderObjectFamily objectFamily) {
		List<ObjectInCompositeContainer> allChilds = new ArrayList<ObjectInCompositeContainer>();

		for (ObjectInCompositeContainer c : childs) {
			if (c.getObjectInComposite().getObjectFamily() == objectFamily) {
				allChilds.add(c);
			}

			allChilds.addAll(c.getAllChildsByType(objectFamily));
		}
		return allChilds;
	}

	/**
	 * This method provides a list with all the childs and sub childs for this
	 * object
	 * 
	 * @return list of the all possible direct and indirect childs of this
	 *         object
	 */
	public List<ObjectInCompositeContainer> getAllSubChilds() {
		List<ObjectInCompositeContainer> allChilds = new ArrayList<ObjectInCompositeContainer>();

		for (ObjectInCompositeContainer c : childs) {
			allChilds.add(c);
			allChilds.addAll(c.getAllSubChilds());
		}

		return allChilds;
	}
}