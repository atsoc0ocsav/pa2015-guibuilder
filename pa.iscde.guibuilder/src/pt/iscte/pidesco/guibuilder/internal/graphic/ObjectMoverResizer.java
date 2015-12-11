package pt.iscte.pidesco.guibuilder.internal.graphic;

import java.util.ArrayList;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.internal.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public abstract class ObjectMoverResizer {
	protected static final int CORNER = 10;

	public enum Handle {
		TOP_LEFT {
			@Override
			Rectangle getNewPosition(Rectangle bounds, Dimension offset) {
				return new Rectangle(bounds.x + offset.width, bounds.y + offset.height, bounds.width - offset.width,
						bounds.height - offset.height);
			}

			@Override
			boolean match(int x, int y, Rectangle bounds) {
				return match(x, y, CORNER, bounds);
			}

			@Override
			boolean match(int x, int y, int corner, Rectangle bounds) {
				return x < CORNER && y < CORNER;
			}
		},
		TOP_RIGHT {
			@Override
			Rectangle getNewPosition(Rectangle bounds, Dimension offset) {
				return new Rectangle(bounds.x, bounds.y + offset.height, bounds.width + offset.width,
						bounds.height - offset.height);
			}

			@Override
			boolean match(int x, int y, Rectangle bounds) {
				return match(x, y, CORNER, bounds);
			}

			@Override
			boolean match(int x, int y, int corner, Rectangle bounds) {
				return x > bounds.width - corner && y < corner;
			}
		},
		BOT_LEFT {
			@Override
			Rectangle getNewPosition(Rectangle bounds, Dimension offset) {
				return new Rectangle(bounds.x + offset.width, bounds.y, bounds.width - offset.width,
						bounds.height + offset.height);
			}

			@Override
			boolean match(int x, int y, Rectangle bounds) {
				return match(x, y, CORNER, bounds);
			}

			@Override
			boolean match(int x, int y, int corner, Rectangle bounds) {
				return x < corner && y > bounds.height - corner;
			}
		},
		BOT_RIGHT {
			@Override
			Rectangle getNewPosition(Rectangle bounds, Dimension offset) {
				return new Rectangle(bounds.x, bounds.y, bounds.width + offset.width, bounds.height + offset.height);
			}

			@Override
			boolean match(int x, int y, Rectangle bounds) {
				return match(x, y, CORNER, bounds);
			}

			@Override
			boolean match(int x, int y, int corner, Rectangle bounds) {
				return x > bounds.width - corner && y > bounds.height - corner;
			}
		};

		abstract Rectangle getNewPosition(Rectangle bounds, Dimension offset);

		abstract boolean match(int x, int y, Rectangle bounds);

		abstract boolean match(int x, int y, int corner, Rectangle bounds);

		static Handle getHandle(int x, int y, Rectangle bounds) {
			return getHandle(x, y, CORNER, bounds);
		}

		static Handle getHandle(int x, int y, int corner, Rectangle bounds) {
			for (Handle h : values())
				if (h.match(x, y, bounds))
					return h;

			return null;
		}
	}

	protected boolean moveable;
	protected ArrayList<Handle> handlers;
	protected Point location;
	protected Handle handle;
	protected GuiBuilderView guiBuilderView;
	protected ObjectInCompositeContainer objectInCompositeContainer;

	protected Control control;
	protected Canvas canvas;

	public Control getControl() {
		return control;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Point getLocation() {
		return location;
	}
	
	public abstract Figure getFigure();

	public void setObjectInComposite(ObjectInCompositeContainer objectInCompositeContainer) {
		this.objectInCompositeContainer=objectInCompositeContainer;
	}
	
	public ObjectInCompositeContainer getObjectInCompositeContainer(){
		return objectInCompositeContainer;
	}
}
