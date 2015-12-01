package pt.iscte.pidesco.guibuilder.ui;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public class ImageResizer extends FigureMoverResizer{
	private Image image;
	
	public ImageResizer(ImageFigure figure, Canvas canvas, String text, boolean moveable, Image image, Handle... handlers) {
		super(figure, canvas, text, moveable, handlers);
		this.image=image;
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		Point newLocation = event.getLocation();
		if (newLocation == null)
			return;

		final Dimension offset = newLocation.getDifference(location);
		if (offset.width == 0 && offset.height == 0)
			return;

		UpdateManager updateMgr = figure.getUpdateManager();
		LayoutManager layoutMgr = figure.getParent().getLayoutManager();
		Rectangle bounds = figure.getBounds();
		updateMgr.addDirtyRegion(figure.getParent(), bounds);

		if (handle != null) { // resize
			Rectangle newPos = handle.getNewPosition(bounds, offset);
			System.out.println("Size: "+newPos.getSize()); 
			// no handle, so not entering here.... why?
			
			if (newPos.getSize().height > MIN_DIM.height && newPos.getSize().width > MIN_DIM.width) {
				((ImageFigure)figure).setImage(resizeImage(image, newPos.width, newPos.height));
				((ImageFigure)figure).repaint();
				
				System.out.println("Entrei "+newPos.getSize());
				
				layoutMgr.setConstraint(figure, newPos);
				updateMgr.addDirtyRegion(figure.getParent(), newPos);
				layoutMgr.layout(figure.getParent());
			}
		}

		updateText(figure, canvas, text);
		event.consume();
		
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
}
