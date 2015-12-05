package pt.iscte.pidesco.guibuilder.internal;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public class ImageResizer extends ObjectMoverResizer implements MouseListener, MouseMotionListener {
	protected static final int CORNER = 50;
	protected static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);

	private ImageData imageData = null;
	private ImageFigure imageFigure;
	private RectangleFigure rectangle;
	private Image image;

	public ImageResizer(ImageFigure imageFigure, ImageData imageData, Image image, RectangleFigure rectangle, Canvas canvas,
			boolean moveable, Handle... handlers) {
		if (imageData == null || image == null)
			throw new NullPointerException();

		this.image = image;
		this.imageFigure=imageFigure;
		this.imageData = imageData;
		this.rectangle = rectangle;
		this.canvas = canvas;
		this.moveable = moveable;
		this.handlers = new ArrayList<Handle>(Arrays.asList(handlers));

		rectangle.addMouseListener(this);
		rectangle.addMouseMotionListener(this);
	}

	private Image resizeImage(Image i, int width, int height) {
		Image scaled = new Image(Display.getCurrent(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(i, 0, 0, i.getBounds().width, i.getBounds().height, 0, 0, width, height);
		gc.dispose();
		//image.dispose(); // don't forget about me!
		System.out.println("Resized");
		return scaled;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.button == 1) {
			Dimension d = event.getLocation().getDifference(rectangle.getBounds().getLocation());

			Handle tmpHandle = Handle.getHandle(d.width, d.height, CORNER, rectangle.getBounds());
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

		UpdateManager updateMgr = rectangle.getUpdateManager();
		LayoutManager layoutMgr = rectangle.getParent().getLayoutManager();
		Rectangle bounds = rectangle.getBounds();
		updateMgr.addDirtyRegion(rectangle.getParent(), bounds);

		if (handle != null) { // resize
			Rectangle newPos = handle.getNewPosition(bounds, offset);

			System.out.println("Hello");

			if (newPos.getSize().height >= MIN_DIM.height && newPos.getSize().width >= MIN_DIM.width) {
				Image img =resizeImage(new Image(canvas.getDisplay(), imageData), newPos.getSize().width,newPos.getSize().height);
				
				System.out.println("Dim: "+img.getBounds());
				imageFigure.erase();
				imageFigure.setImage(img);// 
				imageFigure.setBounds(rectangle.getBounds());
				image=img;

				System.out.println(imageFigure.getParent());
				
				layoutMgr.setConstraint(rectangle, newPos);
				updateMgr.addDirtyRegion(rectangle.getParent(), newPos);
				layoutMgr.layout(rectangle.getParent());
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
