package pt.iscte.pidesco.guibuilder.internal;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;

import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class ControlMoverResizer extends ObjectMoverResizer implements MouseListener, MouseMoveListener {
	protected static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);
	private boolean pressed = false;

	public ControlMoverResizer(Control control, GuiBuilderView guiBuilderView, Canvas canvas, boolean moveable,
			Handle... handlers) {
		if (control == null)
			throw new NullPointerException();

		this.canvas = canvas;
		this.moveable = moveable;
		this.handlers = new ArrayList<Handle>(Arrays.asList(handlers));
		this.guiBuilderView = guiBuilderView;
		this.control = control;

		control.addMouseListener(this);
		control.addMouseMoveListener(this);

		location = null;
		;
	}

	public Control getControl() {
		return control;
	}

	@Override
	public void mouseDoubleClick(MouseEvent event) {
	}

	@Override
	public void mouseDown(MouseEvent event) {
		if (event.button == 1) { // Right button
			System.out.println("Down");
			Point eventPosition = new Point(event.x, event.y);

			Point controlSize = new Point(control.getSize().x, control.getSize().y);
			Handle tmpHandle = Handle.getHandle(event.x, event.y,
					new Rectangle(control.getLocation().x, control.getLocation().y, controlSize.x, controlSize.y));

			if (handlers.contains(tmpHandle)) {
				handle = tmpHandle;
			}

			location = eventPosition;
			pressed = true;
		} else if (event.button == 3) { // Left button
			// guiBuilderView.openDialogMenu(this, event.x, event.y);
			System.out.println("Open dialog menu!");
		}
	}

	@Override
	public void mouseUp(MouseEvent event) {
		if (event.button == 1) { // Right button
			if (location == null)
				return;
			location = null;
			handle = null;

			pressed = false;
		}
	}

	@Override
	public void mouseMove(MouseEvent event) {
		if (pressed) {
			if (location == null)
				return;

			Point newLocation = new Point(event.x, event.y);

			Dimension offset = newLocation.getDifference(location);
			if (offset.width == 0 && offset.height == 0)
				return;

			location = newLocation;

			Rectangle bounds = new Rectangle(control.getLocation().x, control.getLocation().y, control.getSize().x,
					control.getSize().y);

			if (handle != null) { // resize
				Rectangle newPos = handle.getNewPosition(bounds, offset);

				if (newPos.getSize().height >= MIN_DIM.height && newPos.getSize().width >= MIN_DIM.width) {
					control.setBounds(newPos.x, newPos.y, newPos.width, newPos.height);
				}
			} else { // move
				if (moveable) {
					bounds = bounds.getCopy().translate(offset.width, offset.height);
					control.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
				}
			}
		}
	}
}