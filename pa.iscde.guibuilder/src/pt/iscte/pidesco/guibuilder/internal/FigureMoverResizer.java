package pt.iscte.pidesco.guibuilder.internal;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class FigureMoverResizer extends ObjectMoverResizer implements MouseListener, MouseMotionListener {
	protected static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);
	private final IFigure figure;
	private String text;
	private boolean enableText = true;
	private Dimension controlMargin = new Dimension(0,0);

	public FigureMoverResizer(IFigure figure, GuiBuilderView guiBuilderView, Canvas canvas, String text,
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

	public FigureMoverResizer(IFigure figure, GuiBuilderView guiBuilderView, Control control, Canvas canvas,
			String text, boolean moveable, Handle... handlers) {
		this(figure, guiBuilderView, canvas, text, moveable, handlers);
		this.control = control;
	}

	public FigureMoverResizer(IFigure figure, GuiBuilderView guiBuilderView, Control control, Canvas canvas,
			boolean moveable, Handle... handlers) {
		this(figure, guiBuilderView, control, canvas, null, moveable, handlers);
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

	public void updateText(final IFigure figure, final Canvas canvas, final String text) {
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
		}else{
			canvas.layout(true);
			canvas.redraw();
			canvas.update();
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.button == 1) { // Left Button
			Dimension d=event.getLocation().getDifference(figure.getBounds().getLocation());

			Handle tmpHandle = Handle.getHandle(d.width, d.height, figure.getBounds());

			if (handlers.contains(tmpHandle)) {
				handle = tmpHandle;
			}

			location = event.getLocation();
			event.consume();

		} else if (event.button == 3) { // Right Button
			guiBuilderView.openDialogMenu(this, event.x, event.y);
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
					control.setLocation(figure.getBounds().x + controlMargin.width / 2,
							figure.getBounds().y + controlMargin.height / 2);
				}
			}
		}

		if (enableText)
			updateText(figure,canvas,text);

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

	public IFigure getFigure() {
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

		if (enableText)
			updateText();
	}

	public void setForegroundColor(Color color) {
		figure.setForegroundColor(color);

		if (enableText)
			updateText();
	}
	
	public void setControlMargin(Dimension controlMargin) {
		this.controlMargin = controlMargin;
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