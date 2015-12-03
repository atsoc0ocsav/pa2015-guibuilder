package pt.iscte.pidesco.guibuilder.ui;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;

public class FigureMoverResizer implements MouseListener, MouseMotionListener {
	protected static final int CORNER = 10;
	protected static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);
	private boolean moveable;
	protected Canvas canvas;
	protected String text;

	public enum Handle {
		TOP_LEFT {
			@Override
			Rectangle getNewPosition(Rectangle bounds, Dimension offset) {
				return new Rectangle(bounds.x + offset.width, bounds.y + offset.height, bounds.width - offset.width,
						bounds.height - offset.height);
			}

			@Override
			boolean match(int x, int y, Rectangle bounds) {
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
				return x > bounds.width - CORNER && y < CORNER;
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
				return x < CORNER && y > bounds.height - CORNER;
			}
		},
		BOT_RIGHT {
			@Override
			Rectangle getNewPosition(Rectangle bounds, Dimension offset) {
				return new Rectangle(bounds.x, bounds.y, bounds.width + offset.width, bounds.height + offset.height);
			}

			@Override
			boolean match(int x, int y, Rectangle bounds) {
				return x > bounds.width - CORNER && y > bounds.height - CORNER;
			}
		};

		abstract Rectangle getNewPosition(Rectangle bounds, Dimension offset);

		abstract boolean match(int x, int y, Rectangle bounds);

		static Handle getHandle(int x, int y, Rectangle bounds) {
			for (Handle h : values())
				if (h.match(x, y, bounds))
					return h;

			return null;
		}
	}

	protected final IFigure figure;
	protected Point location;
	protected Handle handle;
	protected ArrayList<Handle> handlers;
	private Control control;

	public FigureMoverResizer(IFigure figure, Canvas canvas, String text, boolean moveable, Handle... handlers) {
		if (figure == null)
			throw new NullPointerException();

		this.figure = figure;
		this.canvas = canvas;
		this.text = text;
		this.moveable = moveable;
		this.handlers = new ArrayList<Handle>(Arrays.asList(handlers));
		figure.addMouseListener(this);
		figure.addMouseMotionListener(this);

		updateText(figure, canvas, text);
		this.control = null;
	}

	public FigureMoverResizer(IFigure figure, Control control, Canvas canvas, String text, boolean moveable,
			Handle... handlers) {
		this(figure, canvas, text, moveable, handlers);
		this.control = control;
	}

	public void updateText(final IFigure figure, Canvas canvas, final String text) {
		if (canvas != null) {
			canvas.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					e.gc.drawText(text, figure.getClientArea().getCenter().x - (text.length() * 3),
							figure.getClientArea().getCenter().y);
				}
			});
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		Dimension d = event.getLocation().getDifference(figure.getBounds().getLocation());
		Handle tmpHandle = Handle.getHandle(d.width, d.height, figure.getBounds());

		if (handlers.contains(tmpHandle)) {
			handle = tmpHandle;
		}

		location = event.getLocation();
		event.consume();
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (location == null)
			return;

		Point newLocation = event.getLocation();
		if (newLocation == null)
			return;

		final Dimension offset = newLocation.getDifference(location);
		if (offset.width == 0 && offset.height == 0)
			return;

		location = newLocation;

		UpdateManager updateMgr = figure.getUpdateManager();
		LayoutManager layoutMgr = figure.getParent().getLayoutManager();
		Rectangle bounds = figure.getBounds();
		updateMgr.addDirtyRegion(figure.getParent(), bounds);

		if (handle != null) { // resize
			Rectangle newPos = handle.getNewPosition(bounds, offset);

			if (newPos.getSize().height >= MIN_DIM.height && newPos.getSize().width >= MIN_DIM.width) {
				layoutMgr.setConstraint(figure, newPos);
				updateMgr.addDirtyRegion(figure.getParent(), newPos);
				layoutMgr.layout(figure.getParent());

				if (control != null) {
					control.setSize(newPos.getSize().width, newPos.getSize().height);
				}
			}
		} else { // move
			if (moveable) {
				bounds = bounds.getCopy().translate(offset.width, offset.height);
				layoutMgr.setConstraint(figure, bounds);
				figure.translate(offset.width, offset.height);
				updateMgr.addDirtyRegion(figure.getParent(), bounds);

				if (control != null) {
					control.setLocation(figure.getBounds().x, figure.getBounds().y);
				}
			}
		}

		updateText(figure, canvas, text);
		event.consume();
	}

	public IFigure getFigure() {
		return figure;
	}

	public void setText(String text) {
		this.text = text;

		// o texto está a ficar por cima do outro texto
		// necessario resolver este bug

		updateText(figure, canvas, text);

		canvas.redraw();
		canvas.update();

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (location == null)
			return;
		location = null;
		handle = null;
		event.consume();
	}

	/*
	 * Not used for now
	 */
	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mouseHover(MouseEvent me) {
	}

	@Override
	public void mouseMoved(MouseEvent me) {
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
		// TODO use to change text for instance, in the objects with text inside
	}
}