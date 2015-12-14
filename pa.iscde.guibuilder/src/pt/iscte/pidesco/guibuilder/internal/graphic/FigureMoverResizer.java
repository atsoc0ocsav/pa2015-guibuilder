package pt.iscte.pidesco.guibuilder.internal.graphic;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.model.compositeContents.ContainerInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class FigureMoverResizer extends ObjectMoverResizer implements MouseListener, MouseMotionListener {
	private static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);
	private Figure figure;
	private String text;
	private boolean enableText = true;
	private Dimension controlMargin = new Dimension(0, 0);
	private GuiLabels.GUIBuilderComponent type;

	public FigureMoverResizer(Figure figure, GuiBuilderView guiBuilderView, Canvas canvas, String text,
			boolean moveable, Handle... handlers) {
		if (figure == null)
			throw new NullPointerException();

		this.figure = figure;
		this.canvas = canvas;
		this.text = text;
		this.moveable = moveable;
		this.handlers = new ArrayList<Handle>(Arrays.asList(handlers));
		this.guiBuilderView = guiBuilderView;
		this.control = null;
		figure.addMouseListener(this);
		figure.addMouseMotionListener(this);

		if (enableText)
			setText(text);
	}

	public FigureMoverResizer(Figure figure, GuiBuilderView guiBuilderView, Control control,
			GuiLabels.GUIBuilderComponent type, Canvas canvas, String text, boolean moveable, Handle... handlers) {
		this(figure, guiBuilderView, canvas, text, moveable, handlers);
		this.control = control;
		this.type = type;
	}

	public FigureMoverResizer(Figure figure, GuiBuilderView guiBuilderView, Control control,
			GuiLabels.GUIBuilderComponent type, Canvas canvas, boolean moveable, Handle... handlers) {
		this(figure, guiBuilderView, control, type, canvas, null, moveable, handlers);
		enableText = false;
	}

	public void updateText() {
		if (enableText) {
			updateText(figure, canvas, text);

			canvas.layout(true);
			canvas.redraw();
			canvas.update();
		}
	}

	public void updateText(final Figure figure, final Canvas canvas, final String text) {
		if (canvas != null && text != null) {
			canvas.addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					GC gc = e.gc;
					FontMetrics fm = gc.getFontMetrics();
					gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					gc.drawText(text,
							figure.getClientArea().getCenter().x - (fm.getAverageCharWidth() * text.length()) / 2,
							figure.getClientArea().getCenter().y - fm.getHeight() / 2, true);
				}
			});
		}
	}

	public void enableText(boolean enable) {
		enableText = enable;

		if (enable) {
			if (text == null) {
				text = "";
			}

			updateText();
		} else {
			canvas.layout(true);
			canvas.redraw();
			canvas.update();
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.button == 1) { // Left Button
			Dimension d = event.getLocation().getDifference(figure.getBounds().getLocation());

			Handle tmpHandle = Handle.getHandle(d.width, d.height, figure.getBounds());

			if (handlers.contains(tmpHandle)) {
				handle = tmpHandle;
			}

			location = event.getLocation();
			event.consume();
		} else if (event.button == 3) { // Right Button
			guiBuilderView.openDialogMenu(objectInCompositeContainer.getObjectInComposite().getObjectFamily(), this,
					event.x, event.y);
		}
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

			if (newPos.getSize().height >= MIN_DIM.height && newPos.getSize().width >= MIN_DIM.width
					&& guiBuilderView.isInsideCanvas(newPos.getLocation().x, newPos.getLocation().y,
							newPos.getSize().width, newPos.getSize().height)) {
				layoutMgr.setConstraint(figure, newPos);
				updateMgr.addDirtyRegion(figure.getParent(), newPos);
				layoutMgr.layout(figure.getParent());

				if (objectInCompositeContainer.getObjectInComposite()
						.getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS) {
					((ContainerInComposite) objectInCompositeContainer.getObjectInComposite())
							.setSize(newPos.getSize().width, newPos.getSize().height);
					((ContainerInComposite) objectInCompositeContainer.getObjectInComposite())
							.setLocation(newPos.getLocation().x, newPos.getLocation().y);
				} else if (control != null) {
					control.setSize(newPos.getSize().width - controlMargin.width,
							newPos.getSize().height - controlMargin.height);

					if (handle != Handle.BOT_RIGHT) {
						control.setLocation(figure.getBounds().x + controlMargin.width / 2,
								figure.getBounds().y + controlMargin.height / 2);
					}
				}
			} else if (!guiBuilderView.isInsideCanvas(newPos.getLocation().x, newPos.getLocation().y,
					newPos.getSize().width, newPos.getSize().height)) {
				if (objectInCompositeContainer.getObjectInComposite()
						.getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS) {
					guiBuilderView.setMessage(guiBuilderView.TOO_BIG_OBJECT,
							objectInCompositeContainer.getId().split("\t")[0]);
				} else {
					if (control != null) {
						guiBuilderView.setMessage(guiBuilderView.TOO_BIG_OBJECT, control);
					} else {
						guiBuilderView.setMessage(guiBuilderView.TOO_BIG_OBJECT, figure);
					}
				}
			}
		} else { // move
			Point pos = new Point(bounds.getCopy().translate(offset.width, offset.height).x,
					bounds.getCopy().translate(offset.width, offset.height).y);

			if (moveable && guiBuilderView.isInsideCanvas(pos.x, pos.y, figure.getSize().width, figure.getSize().height)
					&& !guiBuilderView.isOverObject(pos.x, pos.y, figure.getSize().width, figure.getSize().height,
							objectInCompositeContainer.getObjectInComposite(), false)) {
				if (guiBuilderView.isOverObject(pos.x, pos.y, figure.getSize().width, figure.getSize().height,
						objectInCompositeContainer.getObjectInComposite(), true)) {
					ObjectInCompositeContainer c = guiBuilderView.getContainerInPosition(pos.x, pos.y);
					if (objectInCompositeContainer.getParent() != c) {
						objectInCompositeContainer.getParent().removeChild(objectInCompositeContainer);
						objectInCompositeContainer.setParent(c);
						c.addChild(objectInCompositeContainer);
					}
				}
				bounds = bounds.getCopy().translate(offset.width, offset.height);
				layoutMgr.setConstraint(figure, bounds);
				figure.translate(offset.width, offset.height);
				updateMgr.addDirtyRegion(figure.getParent(), bounds);

				if (objectInCompositeContainer.getObjectInComposite()
						.getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS) {
					((ContainerInComposite) objectInCompositeContainer.getObjectInComposite())
							.setSize(bounds.getSize().width, bounds.getSize().height);
					((ContainerInComposite) objectInCompositeContainer.getObjectInComposite()).setLocation(pos.x,
							pos.y);
				} else if (control != null) {
					control.setLocation(figure.getBounds().x + controlMargin.width / 2,
							figure.getBounds().y + controlMargin.height / 2);
				}

				// canvas.layout();
				// canvas.update();
				// canvas.redraw();
			} else if (!guiBuilderView.isInsideCanvas(pos.x, pos.y, figure.getSize().width, figure.getSize().height)) {
				if (objectInCompositeContainer.getObjectInComposite()
						.getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS) {
					guiBuilderView.setMessage(guiBuilderView.OUT_OF_BOUNDS_OBJECT_MSG,
							objectInCompositeContainer.getId().split("\t")[0]);
				} else {
					if (control != null) {
						guiBuilderView.setMessage(guiBuilderView.OUT_OF_BOUNDS_OBJECT_MSG, control);
					} else {
						guiBuilderView.setMessage(guiBuilderView.OUT_OF_BOUNDS_OBJECT_MSG, figure);
					}
				}
			} else if (guiBuilderView.isOverObject(pos.x, pos.y, figure.getSize().width, figure.getSize().height,
					objectInCompositeContainer.getObjectInComposite(), false)) {
				if (objectInCompositeContainer.getObjectInComposite()
						.getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS) {
					guiBuilderView.setMessage(guiBuilderView.OVER_OBJECT_MSG,
							objectInCompositeContainer.getId().split("\t")[0]);
				} else {
					if (control != null) {
						guiBuilderView.setMessage(guiBuilderView.OVER_OBJECT_MSG, control);
					} else {
						guiBuilderView.setMessage(guiBuilderView.OVER_OBJECT_MSG, figure);
					}
				}
			}
		}

		if (enableText)
			updateText(figure, canvas, text);

		event.consume();
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (location == null)
			return;
		location = null;
		handle = null;
		event.consume();

		if (enableText)
			updateText();
	}

	@Override
	public Figure getFigure() {
		return figure;
	}

	public Control getControl() {
		return control;
	}

	public void setText(String text) {
		this.text = text;

		if (enableText)
			updateText();
	}

	public void setBackgroundColor(Color color) {
		figure.setBackgroundColor(color);

		if (control != null) {
			control.setBackground(color);
			control.redraw();
		}

		if (enableText)
			updateText();
	}

	public void setForegroundColor(Color color) {
		figure.setForegroundColor(color);

		if (control != null) {
			control.setForeground(color);
			control.redraw();
		}

		if (enableText)
			updateText();
	}

	public void setControlMargin(Dimension controlMargin) {
		this.controlMargin = controlMargin;
	}

	public void renameControl(String str) {
		if (control != null) {
			switch (type) {
			case BTN:
				((Button) control).setText(str);
				break;
			case CHK_BOX:
				((Button) control).setText(str);
				break;
			case LABEL:
				((Label) control).setText(str);
				break;
			case TXTFIELD:
				((Text) control).setText(str);
				break;
			case RADIO_BTN:
				((Button) control).setText(str);
			case WIDGET:
				// ((WidgetInterface) control).setWidgetName(str);
				System.out.println("uncoment me!");
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}
		}
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
	}
}