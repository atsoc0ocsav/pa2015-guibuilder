package pt.iscte.pidesco.guibuilder.internal.model;

import java.util.ArrayList;

import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.CanvasInComposite;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ComponentInComposite;

public class ObjectInCompositeContainer {
	private String id;
	private boolean isRoot;
	private ObjectInCompositeContainer parent;
	private ObjectInComposite object;

	private ArrayList<ObjectInCompositeContainer> childs;

	public ObjectInCompositeContainer(String id, ObjectInComposite object, ObjectInCompositeContainer parent) {
		this.id = id;
		this.object = object;
		this.parent = parent;

		childs = new ArrayList<ObjectInCompositeContainer>();

		if (object instanceof CanvasInComposite) {
			((CanvasInComposite) object).getCanvasResizer().setObjectInComposite(this);
		} else if (object instanceof ComponentInComposite) {
			((ComponentInComposite) object).getObjectMoverResizer().setObjectInComposite(this);
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

	/**
	 * This method providesa list with all the childs for this object
	 * 
	 * @return list of the first degree (direct) childs
	 */
	public ArrayList<ObjectInCompositeContainer> getChilds() {
		return childs;
	}

	public void addChild(ObjectInCompositeContainer child) {
		if (child != null) {
			childs.add(child);
		}
	}

	public boolean hasChilds() {
		return childs.isEmpty();
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

	/**
	 * This method provides a list with all the childs and sub childs for this
	 * object
	 * 
	 * @return list of the all possible direct and indirect childs of this
	 *         object
	 */
	public ArrayList<ObjectInCompositeContainer> getAllSubChilds() {
		ArrayList<ObjectInCompositeContainer> allChilds = new ArrayList<ObjectInCompositeContainer>();

		for (ObjectInCompositeContainer c : childs) {
			allChilds.add(c);
			allChilds.addAll(c.getAllSubChilds());
		}

		return allChilds;
	}
}