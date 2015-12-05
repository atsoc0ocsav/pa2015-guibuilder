package pt.iscte.pidesco.guibuilder.ui;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.internal.FigureMoverResizer;
import pt.iscte.pidesco.guibuilder.internal.ImageResizer;
import pt.iscte.pidesco.guibuilder.internal.ObjectInComposite;

public class GuiBuilderObjFactory {
	// Dimensions
	private static final Point DEFAULT_CANVAS_POS_OFFSET = new Point(5, 5);
	private static final Dimension DEFAULT_CANVAS_INIT_DIM = new Dimension(400, 400);
	private static final Dimension DEFAULT_CANVAS_TOPBAR_INIT_DIM = new Dimension(DEFAULT_CANVAS_INIT_DIM.width, 35);
	private static final Dimension LABELS_MARGIN = new Dimension(5, 5);
	private static final Dimension BACKGND_MARGIN = new Dimension(6, 6);

	// Default text
	private static final String DEFAULT_BTN_TXT = "New Button";
	private static final String DEFAULT_LABEL_TXT = "New Label";
	private static final String DEFAULT_TXTFIELD_TXT = "New Textfield";
	private static final String DEFAULT_RADIOBTN_TXT = "New choice";
	private static final String DEFAULT_CHCKBOX_TXT = "New checkbox";

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

	public Figure createGuiBuilderCanvas(Canvas canvas, Map<String, Image> imageMap) {
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

			new ImageResizer(guiBuilderView, canvasBackgnd, canvasTopbar, imgCanvas.getImageData(),
					imgCanvasTopbar.getImageData(), DEFAULT_CANVAS_TOPBAR_INIT_DIM.height, backgroundCanvas, canvas,
					false, ImageResizer.Handle.BOT_RIGHT);

			imgCanvas.dispose();
			imgCanvasTopbar.dispose();

			return backgroundCanvas;
		} else {
			RectangleFigure fig = new RectangleFigure();
			fig.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			fig.setBounds(new Rectangle(DEFAULT_CANVAS_POS_OFFSET.x, DEFAULT_CANVAS_POS_OFFSET.y,
					DEFAULT_CANVAS_INIT_DIM.width, DEFAULT_CANVAS_INIT_DIM.height));
			new FigureMoverResizer(fig, guiBuilderView, null, "", false, FigureMoverResizer.Handle.BOT_RIGHT);
			return fig;
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

	public ObjectInComposite createComponentFamilyObject(Point position, String cmpName, Canvas canvas,
			Figure contents) {
		GuiLabels.GUIBuilderComponent component = null;
		for (GuiLabels.GUIBuilderComponent c : GuiLabels.GUIBuilderComponent.values()) {
			if (c.str().equals(cmpName)) {
				component = c;
				break;
			}
		}

		if (isInsideCanvas(position)) {
			switch (component) {
			case BTN:
				FontMetrics fmButton = new GC(canvas).getFontMetrics();
				Point buttonSize = new Point(
						(fmButton.getAverageCharWidth() * DEFAULT_BTN_TXT.length()) + LABELS_MARGIN.width,
						fmButton.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundButton = new RectangleFigure();
				backgroundButton.setBounds(new Rectangle(position.x, position.y, buttonSize.x + BACKGND_MARGIN.width,
						buttonSize.y + BACKGND_MARGIN.height));
				backgroundButton.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundButton);

				Button button = new Button(canvas, SWT.BORDER);
				button.setText(DEFAULT_BTN_TXT);
				button.setLocation(position.x + BACKGND_MARGIN.width / 2, position.y + BACKGND_MARGIN.height / 2);
				button.setSize(buttonSize);
				button.setEnabled(false);

				FigureMoverResizer fmrButton = new FigureMoverResizer(backgroundButton, guiBuilderView, button, canvas,
						true, FigureMoverResizer.Handle.values());
				fmrButton.setControlMargin(BACKGND_MARGIN);

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), backgroundButton, fmrButton);

			case LABEL:
				FontMetrics fmLabel = new GC(canvas).getFontMetrics();
				Point labelSize = new Point(
						(fmLabel.getAverageCharWidth() * DEFAULT_LABEL_TXT.length()) + LABELS_MARGIN.width,
						fmLabel.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundLabel = new RectangleFigure();
				backgroundLabel.setBounds(new Rectangle(position.x, position.y, labelSize.x + BACKGND_MARGIN.width,
						labelSize.y + BACKGND_MARGIN.height));
				backgroundLabel.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundLabel);

				Label label = new Label(canvas, SWT.BORDER);
				label.setText(DEFAULT_LABEL_TXT);
				label.setLocation(position.x + BACKGND_MARGIN.width / 2, position.y + BACKGND_MARGIN.height / 2);
				label.setSize(labelSize);
				label.setEnabled(false);

				FigureMoverResizer fmrLabel = new FigureMoverResizer(backgroundLabel, guiBuilderView, label, canvas,
						true, FigureMoverResizer.Handle.values());
				fmrLabel.setControlMargin(BACKGND_MARGIN);

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), backgroundLabel, fmrLabel);

			case TEXTFIELD:
				FontMetrics fmTxtField = new GC(canvas).getFontMetrics();
				Point txtFieldSize = new Point(
						(fmTxtField.getAverageCharWidth() * DEFAULT_TXTFIELD_TXT.length()) + LABELS_MARGIN.width,
						fmTxtField.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundTxtField = new RectangleFigure();
				backgroundTxtField.setBounds(new Rectangle(position.x, position.y,
						txtFieldSize.x + BACKGND_MARGIN.width, txtFieldSize.y + BACKGND_MARGIN.height));
				backgroundTxtField.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundTxtField);

				Text txtField = new Text(canvas, SWT.BORDER);
				txtField.setText(DEFAULT_TXTFIELD_TXT);
				txtField.setLocation(position.x + BACKGND_MARGIN.width / 2, position.y + BACKGND_MARGIN.height / 2);
				txtField.setSize(txtFieldSize);
				txtField.setEditable(false);
				txtField.setEnabled(false);

				FigureMoverResizer fmrTxtField = new FigureMoverResizer(backgroundTxtField, guiBuilderView, txtField,
						canvas, true, FigureMoverResizer.Handle.values());
				fmrTxtField.setControlMargin(BACKGND_MARGIN);

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), backgroundTxtField,
						fmrTxtField);

			case RADIO_BTN:
				FontMetrics fmRadioBtn = new GC(canvas).getFontMetrics();
				Point radioBtnSize = new Point(
						(fmRadioBtn.getAverageCharWidth() * DEFAULT_RADIOBTN_TXT.length()) + LABELS_MARGIN.width + 10,
						fmRadioBtn.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundRadioBtn = new RectangleFigure();
				backgroundRadioBtn.setBounds(new Rectangle(position.x, position.y,
						radioBtnSize.x + BACKGND_MARGIN.width, radioBtnSize.y + BACKGND_MARGIN.height));
				backgroundRadioBtn.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundRadioBtn);

				Button radioBtn = new Button(canvas, SWT.RADIO);
				radioBtn.setText(DEFAULT_RADIOBTN_TXT);
				radioBtn.setLocation(position.x + BACKGND_MARGIN.width / 2, position.y + BACKGND_MARGIN.height / 2);
				radioBtn.setSelection(true);
				radioBtn.setSize(radioBtnSize);
				radioBtn.setEnabled(false);

				FigureMoverResizer fmrRadioBtn = new FigureMoverResizer(backgroundRadioBtn, guiBuilderView, radioBtn,
						canvas, true, FigureMoverResizer.Handle.values());
				fmrRadioBtn.setControlMargin(BACKGND_MARGIN);

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), backgroundRadioBtn,
						fmrRadioBtn);

			case CHK_BOX:
				FontMetrics fmChckBox = new GC(canvas).getFontMetrics();
				Point chckBoxSize = new Point(
						(fmChckBox.getAverageCharWidth() * DEFAULT_CHCKBOX_TXT.length()) + LABELS_MARGIN.width + 18,
						fmChckBox.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundChckBox = new RectangleFigure();
				backgroundChckBox.setBounds(new Rectangle(position.x, position.y, chckBoxSize.x + BACKGND_MARGIN.width,
						chckBoxSize.y + BACKGND_MARGIN.height));
				backgroundChckBox.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundChckBox);

				Button chckBox = new Button(canvas, SWT.CHECK);
				chckBox.setText(DEFAULT_CHCKBOX_TXT);
				chckBox.setLocation(position.x + BACKGND_MARGIN.width / 2, position.y + BACKGND_MARGIN.height / 2);
				chckBox.setSize(chckBoxSize);
				chckBox.setEnabled(false);

				FigureMoverResizer fmrChckBox = new FigureMoverResizer(backgroundChckBox, guiBuilderView, chckBox,
						canvas, true, FigureMoverResizer.Handle.values());
				fmrChckBox.setControlMargin(BACKGND_MARGIN);

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), backgroundChckBox,
						fmrChckBox);

			default:
				throw new IllegalAccessError("Switch case not defined!");
			}
		} else {
			return null;
		}
	}

	public ObjectInComposite createLayoutFamilyObject(Point position, String cmpName, Canvas canvas, Figure contents) {
		// TODO Define method
		System.err.println("Method undefined");
		return null;
	}

	public ObjectInComposite createContainerFamilyObject(Point position, String cmpName, Canvas canvas,
			Figure contents) {
		// TODO Define method
		System.err.println("Method undefined");
		return null;
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
}
