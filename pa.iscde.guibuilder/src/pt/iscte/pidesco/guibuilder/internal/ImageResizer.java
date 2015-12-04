package pt.iscte.pidesco.guibuilder.internal;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public class ImageResizer extends ObjectMoverResizer implements MouseListener, MouseMotionListener {
	protected static final int CORNER = 50;
	protected static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);
	
	private Image imageFile = null;
	private ImageFigure image;

	public ImageResizer(ImageFigure image, Image imageFile, Canvas canvas, boolean moveable, Handle... handlers) {
		if (imageFile == null)
			throw new NullPointerException();

		this.image = image;
		this.imageFile = imageFile;
		this.canvas = canvas;
		this.moveable = moveable;
		this.handlers = new ArrayList<Handle>(Arrays.asList(handlers));
		image.addMouseListener(this);
		image.addMouseMotionListener(this);
	}

	private static Image resizeImage(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.button == 1) {
			Dimension d = event.getLocation().getDifference(image.getBounds().getLocation());

			System.out.println("d: "+d);
			System.out.println("bounds: "+image.getBounds());
			
			Handle tmpHandle = Handle.getHandle(d.width, d.height, CORNER,image.getBounds());
			System.out.println("handle: "+tmpHandle);
			
			if (handlers.contains(tmpHandle)) {
				handle = tmpHandle;
			}

			location = event.getLocation();
			event.consume();
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

		UpdateManager updateMgr = image.getUpdateManager();
		LayoutManager layoutMgr = image.getParent().getLayoutManager();
		Rectangle bounds = image.getBounds();
		updateMgr.addDirtyRegion(image.getParent(), bounds);
		
		if (handle != null) { // resize
			Rectangle newPos = handle.getNewPosition(bounds, offset);

			System.out.println("hAELLO");
			
			if (newPos.getSize().height >= MIN_DIM.height && newPos.getSize().width >= MIN_DIM.width) {
				layoutMgr.setConstraint(image, newPos);
				updateMgr.addDirtyRegion(image.getParent(), newPos);
				layoutMgr.layout(image.getParent());

//				control.setSize(newPos.getSize().width, newPos.getSize().height);
//				Image newImage = resizeImage(imageFile, newPos.getSize().width, newPos.getSize().height);
//				image.setImage(newImage);
				
//				canvasBackground.setBounds(new Rectangle(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x, DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y,
//						DEFAULT_FAKEWINDOW_INIT_DIM.width, DEFAULT_FAKEWINDOW_INIT_DIM.height));
//				img.dispose();
//				return canvasBackground;
			}
		}
		event.consume();
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (location == null)
			return;
		location = null;
		handle = null;
		event.consume();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseHover(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
