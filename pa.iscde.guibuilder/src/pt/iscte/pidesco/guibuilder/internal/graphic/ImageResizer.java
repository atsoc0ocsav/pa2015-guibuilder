package pt.iscte.pidesco.guibuilder.internal.graphic;

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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;

public class ImageResizer extends ObjectMoverResizer implements MouseListener, MouseMotionListener {
	private static final int CORNER = 50;
	private static final Dimension MIN_DIM = new Dimension(CORNER * 3, CORNER * 3);
	private static final int TITLE_LEFTSPACE = 30;

	private GuiBuilderView guiBuilderView;
	private ImageData canvasImageData;
	private ImageData canvasTopbarImageData;
	private ImageFigure canvasBackgroundFigure;
	private ImageFigure canvasTopbarFigure;
	private int topbarHeight;
	private RectangleFigure backgroundRectangle;
	private Label label = null;

	public ImageResizer(GuiBuilderView guiBuilderView, ImageFigure canvasBackgroundFigure,
			ImageFigure canvasTopBarFigure, ImageData canvasBackgroundImageData, ImageData canvasTopbarImageData,
			int topbarHeight, RectangleFigure backgroundRectangle, Canvas canvas, boolean moveable,
			Handle... handlers) {
		if (canvasBackgroundFigure == null || canvasBackgroundImageData == null)
			throw new NullPointerException();

		this.guiBuilderView = guiBuilderView;
		this.canvasBackgroundFigure = canvasBackgroundFigure;
		this.canvasTopbarFigure = canvasTopBarFigure;
		this.canvasImageData = canvasBackgroundImageData;
		this.canvasTopbarImageData = canvasTopbarImageData;
		this.topbarHeight = topbarHeight;

		this.backgroundRectangle = backgroundRectangle;
		this.canvas = canvas;
		this.moveable = moveable;
		this.handlers = new ArrayList<Handle>(Arrays.asList(handlers));

		backgroundRectangle.addMouseListener(this);
		backgroundRectangle.addMouseMotionListener(this);
	}

	/*
	 * Thank to Chris Aniszczyk for providing this lines of code ;)
	 * http://aniszczyk.org/2007/08/09/resizing-images-using-swt/
	 */
	private Image resizeImage(Image i, int srcx, int srcy, int width, int height) {
		Image scaled = new Image(Display.getCurrent(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(i, srcx, srcy, i.getBounds().width - srcx, i.getBounds().height - srcy, 0, 0, width, height);
		gc.dispose();

		return scaled;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.button == 1) {
			Dimension d = event.getLocation().getDifference(backgroundRectangle.getBounds().getLocation());

			Handle tmpHandle = Handle.getHandle(d.width, d.height, CORNER, backgroundRectangle.getBounds());
			if (handlers.contains(tmpHandle)) {
				handle = tmpHandle;
			}

			location = event.getLocation();
			event.consume();
		} else if (event.button == 3) {
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

		UpdateManager updateMgr = backgroundRectangle.getUpdateManager();
		LayoutManager layoutMgr = backgroundRectangle.getParent().getLayoutManager();
		Rectangle bounds = backgroundRectangle.getBounds();
		updateMgr.addDirtyRegion(backgroundRectangle.getParent(), bounds);

		if (handle != null) { // resize
			Rectangle newPos = handle.getNewPosition(bounds, offset);

			if (newPos.getSize().height >= MIN_DIM.height && newPos.getSize().width >= MIN_DIM.width) {
				Image topBarImage = resizeImage(new Image(canvas.getDisplay(), canvasTopbarImageData), 0, 0,
						newPos.getSize().width, topbarHeight);

				Image canvasImage = resizeImage(new Image(canvas.getDisplay(), canvasImageData), 0, 50,
						newPos.getSize().width, newPos.getSize().height - topbarHeight);

				canvasBackgroundFigure.erase();
				canvasTopbarFigure.erase();

				canvasBackgroundFigure.setImage(canvasImage);
				canvasTopbarFigure.setImage(topBarImage);

				Rectangle canvasBounds = new Rectangle(backgroundRectangle.getLocation().x,
						backgroundRectangle.getLocation().y + topbarHeight, backgroundRectangle.getSize().width,
						backgroundRectangle.getSize().height - topbarHeight);
				Rectangle topbarBounds = new Rectangle(backgroundRectangle.getLocation().x,
						backgroundRectangle.getLocation().y, backgroundRectangle.getSize().width, topbarHeight);

				canvasBackgroundFigure.setBounds(canvasBounds);
				canvasTopbarFigure.setBounds(topbarBounds);

				layoutMgr.setConstraint(backgroundRectangle, newPos);
				updateMgr.addDirtyRegion(backgroundRectangle.getParent(), newPos);
				layoutMgr.layout(backgroundRectangle.getParent());
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

	public void setText(String str) {
		if (label == null) {
			label = new Label(canvas, SWT.NONE);
		}

		label.setText(str);

		FontMetrics fm = new GC(canvas).getFontMetrics();
		Point position = new Point(canvasTopbarFigure.getLocation().x + TITLE_LEFTSPACE,
				canvasTopbarFigure.getLocation().y + ((canvasTopbarFigure.getSize().height - fm.getHeight()) / 2));

		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		label.setLocation(position.x, position.y);
		label.setSize((fm.getAverageCharWidth() * str.length()) + 8, fm.getHeight() + 2);

		int color = canvasTopbarImageData.getPixel(position.x, position.y);
		RGB rgb = new RGB(color & 0xFF, (color & 0xFF00)>> 8, (color & 0xFF0000)>>16);

		label.setBackground(new Color(Display.getDefault().getSystemColor(SWT.COLOR_BLACK).getDevice(), rgb));
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
	}
	public String getTitleFrame(){
		if(label!=null){
			return label.getText();
		}
		return null;
	}
	/*
	 * Not used
	 */
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
