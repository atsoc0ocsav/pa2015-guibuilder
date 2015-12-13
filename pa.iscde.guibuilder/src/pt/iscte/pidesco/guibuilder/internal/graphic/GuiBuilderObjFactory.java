package pt.iscte.pidesco.guibuilder.internal.graphic;

import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.internal.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.CanvasInComposite;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ComponentInCompositeImpl;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ContainerInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiBuilderView;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderContainer;

public class GuiBuilderObjFactory {
	// Dimensions
	private static final Point DEFAULT_CANVAS_POS_OFFSET = new Point(5, 5);
	private static final Dimension DEFAULT_CANVAS_INIT_DIM = new Dimension(400, 400);
	private static final Dimension DEFAULT_CANVAS_TOPBAR_INIT_DIM = new Dimension(DEFAULT_CANVAS_INIT_DIM.width, 35);
	private static final Dimension DEFAULT_CONTAINER_DIM = new Dimension(100, 100);
	private static final Dimension LABELS_MARGIN = new Dimension(5, 5);
	private static final Dimension BACKGND_MARGIN = new Dimension(14, 14);

	// Default text
	private static final String DEFAULT_FRAME_TITLE_TXT = "New Frame";
	private static final String DEFAULT_BTN_TXT = "New Button";
	private static final String DEFAULT_LABEL_TXT = "New Label";
	private static final String DEFAULT_TXTFIELD_TXT = "New Textfield";
	private static final String DEFAULT_RADIOBTN_TXT = "New choice";
	private static final String DEFAULT_CHCKBOX_TXT = "New checkbox";
	private static final String DEFAULT_WIDGET_TXT = "New widget";

	// Files
	private static final String CANVAS_BACKGND_FILENAME = "fake_window_complete_canvas.png";
	private static final String CANVAS_TOPBAR_FILENAME = "fake_window_topbar.png";

	// Other
	private GuiBuilderView guiBuilderView;
	private ImageFigure canvasBackgnd;
	private ImageFigure canvasTopbar;

	public GuiBuilderObjFactory(GuiBuilderView guiBuilderView) {
		this.guiBuilderView = guiBuilderView;
	}

	public CanvasInComposite createGuiBuilderCanvas(Canvas canvas, Map<String, Image> imageMap) {
		if (imageMap.get(CANVAS_BACKGND_FILENAME) != null && imageMap.get(CANVAS_TOPBAR_FILENAME) != null) {
			Image imgCanvas = imageMap.get(CANVAS_BACKGND_FILENAME);
			Image imgCanvasTopbar = imageMap.get(CANVAS_TOPBAR_FILENAME);

			Image scaledImgCanvas = resizeImage(imgCanvas, 0, 50, DEFAULT_CANVAS_INIT_DIM.width,
					DEFAULT_CANVAS_INIT_DIM.height);
			Image scaledImgCanvasTopbar = resizeImage(imgCanvasTopbar, 0, 0, DEFAULT_CANVAS_TOPBAR_INIT_DIM.width,
					DEFAULT_CANVAS_TOPBAR_INIT_DIM.height);

			canvasBackgnd = new ImageFigure(scaledImgCanvas);
			canvasTopbar = new ImageFigure(scaledImgCanvasTopbar);

			Rectangle canvasBackgndBounds = new Rectangle(DEFAULT_CANVAS_POS_OFFSET.x,
					DEFAULT_CANVAS_POS_OFFSET.y + DEFAULT_CANVAS_TOPBAR_INIT_DIM.height, DEFAULT_CANVAS_INIT_DIM.width,
					DEFAULT_CANVAS_INIT_DIM.height);
			Rectangle canvasTopbarBounds = new Rectangle(DEFAULT_CANVAS_POS_OFFSET.x, DEFAULT_CANVAS_POS_OFFSET.y,
					DEFAULT_CANVAS_TOPBAR_INIT_DIM.width, DEFAULT_CANVAS_TOPBAR_INIT_DIM.height);
			Rectangle rectangleFigureBounds = new Rectangle(DEFAULT_CANVAS_POS_OFFSET.x, DEFAULT_CANVAS_POS_OFFSET.y,
					DEFAULT_CANVAS_INIT_DIM.width,
					DEFAULT_CANVAS_INIT_DIM.height + DEFAULT_CANVAS_TOPBAR_INIT_DIM.height);

			canvasBackgnd.setBounds(canvasBackgndBounds);
			canvasTopbar.setBounds(canvasTopbarBounds);

			RectangleFigure backgroundCanvas = new RectangleFigure();
			backgroundCanvas.setBounds(rectangleFigureBounds);
			backgroundCanvas.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
			backgroundCanvas.add(canvasBackgnd);
			backgroundCanvas.add(canvasTopbar);

			CanvasResizer imageResizer = new CanvasResizer(guiBuilderView, canvasBackgnd, canvasTopbar,
					imgCanvas.getImageData(), imgCanvasTopbar.getImageData(), DEFAULT_CANVAS_TOPBAR_INIT_DIM.height,
					backgroundCanvas, canvas, false, CanvasResizer.Handle.BOT_RIGHT);
			imageResizer.setText(DEFAULT_FRAME_TITLE_TXT);

			imgCanvas.dispose();
			imgCanvasTopbar.dispose();

			return new CanvasInComposite(backgroundCanvas, imageResizer,
					new Point(canvasBackgndBounds.getLocation().x, canvasBackgndBounds.getLocation().y),
					new Point(canvasBackgndBounds.getSize().width, canvasBackgndBounds.getSize().height),
					DEFAULT_FRAME_TITLE_TXT);
		} else {
			RectangleFigure fig = new RectangleFigure();
			fig.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			fig.setBounds(new Rectangle(DEFAULT_CANVAS_POS_OFFSET.x, DEFAULT_CANVAS_POS_OFFSET.y,
					DEFAULT_CANVAS_INIT_DIM.width, DEFAULT_CANVAS_INIT_DIM.height));

			FigureMoverResizer figHandler = new FigureMoverResizer(fig, guiBuilderView, null, "", false,
					FigureMoverResizer.Handle.BOT_RIGHT);
			return new CanvasInComposite(fig, figHandler, new Point(fig.getLocation().x, fig.getLocation().y),
					new Point(fig.getSize().width, fig.getSize().height));
		}
	}

	/*
	 * Thank to Chris Aniszczyk for providing this lines of code ;)
	 * http://aniszczyk.org/2007/08/09/resizing-images-using-swt/
	 */
	private Image resizeImage(Image image, int srcx, int srcy, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, srcx, srcy, image.getBounds().width - srcx, image.getBounds().height - srcy, 0, 0, width,
				height);
		gc.dispose();
		return scaled;
	}

	public ComponentInCompositeImpl createComponentFamilyObject(GuiLabels.GUIBuilderComponent componentType,
			Point position, Canvas canvas, Object... args) {

		String componentLabel;
		if (args.length == 0) {
			switch (componentType) {
			case BTN:
				componentLabel = DEFAULT_BTN_TXT;
				break;
			case LABEL:
				componentLabel = DEFAULT_LABEL_TXT;
				break;
			case TXTFIELD:
				componentLabel = DEFAULT_TXTFIELD_TXT;
				break;
			case RADIO_BTN:
				componentLabel = DEFAULT_RADIOBTN_TXT;
				break;
			case CHK_BOX:
				componentLabel = DEFAULT_CHCKBOX_TXT;
				break;
			case WIDGET:
				componentLabel = DEFAULT_WIDGET_TXT;
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}
		} else if (args.length == 1 && componentType != GUIBuilderComponent.WIDGET) {
			componentLabel = DEFAULT_WIDGET_TXT;
		} else {
			componentLabel = ((String) args[0]);
		}

		FontMetrics fm = new GC(canvas).getFontMetrics();
		Point componentSize = new Point((fm.getAverageCharWidth() * componentLabel.length()) + LABELS_MARGIN.width + 23,
				fm.getHeight() + LABELS_MARGIN.height);

		if (isInsideCanvas(position.x, position.y, componentSize.x, componentSize.y)) {
			Control widget = null;

			RectangleFigure componentBackground = new RectangleFigure();
			if (componentType == GUIBuilderComponent.WIDGET) {
				if (args.length == 1) {
					widget = ((Control) args[0]);
				} else {
					widget = ((Control) args[1]);
				}
				componentBackground.setBounds(new Rectangle(position.x, position.y,
						widget.getSize().x + BACKGND_MARGIN.width, widget.getSize().y + BACKGND_MARGIN.height));
			} else {
				componentBackground.setBounds(new Rectangle(position.x, position.y,
						componentSize.x + BACKGND_MARGIN.width, componentSize.y + BACKGND_MARGIN.height));
			}

			componentBackground.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));

			switch (componentType) {
			case BTN:
				widget = new Button(canvas, SWT.BORDER);
				((Button) widget).setText(componentLabel);
				break;
			case LABEL:
				widget = new Label(canvas, SWT.BORDER);
				((Label) widget).setText(componentLabel);
				break;
			case TXTFIELD:
				widget = new Text(canvas, SWT.BORDER);
				((Text) widget).setText(componentLabel);
				((Text) widget).setEditable(false);
				break;
			case RADIO_BTN:
				widget = new Button(canvas, SWT.RADIO);
				((Button) widget).setText(componentLabel);
				((Button) widget).setSelection(true);
				break;
			case CHK_BOX:
				widget = new Button(canvas, SWT.CHECK);
				((Button) widget).setText(componentLabel);
				break;
			case WIDGET:
				if (args.length == 1) {
					widget = ((Control) args[0]);
				} else {
					widget = ((Control) args[1]);
				}
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");

			}

			widget.setLocation(position.x + BACKGND_MARGIN.width / 2, position.y + BACKGND_MARGIN.height / 2);
			widget.setSize(componentSize);

			FigureMoverResizer fmr = new FigureMoverResizer(componentBackground, guiBuilderView, widget, componentType,
					canvas, true, FigureMoverResizer.Handle.values());
			fmr.setControlMargin(BACKGND_MARGIN);

			return new ComponentInCompositeImpl(componentType, widget, componentBackground, fmr)
					.setTextAndReturnObject(componentLabel);
		} else {
			return null;
		}
	}

	public ObjectInCompositeContainer createLayoutFamilyObject(Point position, String cmpName, Canvas canvas,
			Figure contents) {
		// TODO Define graphical component
		System.err.println("Method undefined");
		return null;
	}

	public ContainerInComposite createContainerFamilyObject(GuiLabels.GUIBuilderContainer containerType, Point position,
			Canvas canvas) {
		switch (containerType) {
		case PANEL:

			break;
		default:
			throw new IllegalArgumentException("Switch case not defined!");
		}

		if (isInsideCanvas(position.x, position.y, DEFAULT_CONTAINER_DIM.width, DEFAULT_CONTAINER_DIM.height)) {
			RectangleFigure container = new RectangleFigure();
			container.setBounds(
					new Rectangle(position.x, position.y, DEFAULT_CONTAINER_DIM.width, DEFAULT_CONTAINER_DIM.height));
			container.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY));

			FigureMoverResizer fmr = new FigureMoverResizer(container, guiBuilderView, canvas, null, true,
					FigureMoverResizer.Handle.values());
			fmr.setControlMargin(BACKGND_MARGIN);

			return new ContainerInComposite(GUIBuilderContainer.PANEL, container, fmr);
		} else {
			return null;
		}
	}

	public boolean isInsideCanvas(Point pos) {
		return isInsideCanvas(pos.x, pos.y);
	}

	public boolean isInsideCanvas(int x, int y) {
		Dimension canvasDim = canvasBackgnd.getSize();

		return x < (canvasDim.width + DEFAULT_CANVAS_POS_OFFSET.x)
				&& y < (canvasDim.height + DEFAULT_CANVAS_POS_OFFSET.y + DEFAULT_CANVAS_TOPBAR_INIT_DIM.height)
				&& x >= DEFAULT_CANVAS_POS_OFFSET.x && y >= DEFAULT_CANVAS_TOPBAR_INIT_DIM.height;
	}

	public boolean isInsideCanvas(Point pos, Dimension dim) {
		return isInsideCanvas(pos.x, pos.y, dim.width, dim.height);
	}

	public boolean isInsideCanvas(int x, int y, int width, int height) {
		Dimension canvasDim = canvasBackgnd.getSize();

		return (x + width) < (canvasDim.width + DEFAULT_CANVAS_POS_OFFSET.x)
				&& (y + height) < (canvasDim.height + DEFAULT_CANVAS_POS_OFFSET.y
						+ DEFAULT_CANVAS_TOPBAR_INIT_DIM.height)
				&& x >= DEFAULT_CANVAS_POS_OFFSET.x && y >= DEFAULT_CANVAS_TOPBAR_INIT_DIM.height;
	}

	public Dimension getCanvasSize() {
		return canvasBackgnd.getSize();
	}
}
