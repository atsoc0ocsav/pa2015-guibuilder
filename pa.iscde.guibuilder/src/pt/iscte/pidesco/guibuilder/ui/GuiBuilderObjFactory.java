package pt.iscte.pidesco.guibuilder.ui;

import java.util.Map;

import javax.imageio.ImageReader;

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
	private final Point DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET = new Point(5, 5);
	private final Dimension DEFAULT_FAKEWINDOW_INIT_DIM = new Dimension(400, 400);
	private int TOPBAR_HEIGH = 100;
	private final Dimension LABELS_MARGIN = new Dimension(5, 5);
	private final Dimension BACKGND_MARGIN = new Dimension(6, 6);

	// Default text
	private final String DEFAULT_BTN_TXT = "New Button";
	private final String DEFAULT_LABEL_TXT = "New Label";
	private final String DEFAULT_TXTFIELD_TXT = "New Textfield";
	private final String DEFAULT_RADIOBTN_TXT = "New choice";
	private final String DEFAULT_CHCKBOX_TXT = "New checkbox";

	// Files
	private final String BUILDER_CANVAS_BACKGND_FILENAME = "fake_window_complete_canvas.png";
	private final String CANVAS_TOPBAR_FILENAME = "fake_window_topbar.png";

	// Other
	private final Point WINDOWS_MOUSE_OFFSET = new Point(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x,
			-(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y + 113));
	private GuiBuilderView guiBuilderView;
	private ImageFigure canvasBackground;

	public GuiBuilderObjFactory(GuiBuilderView guiBuilderView) {
		this.guiBuilderView = guiBuilderView;
	}

	public Figure createGuiBuilderCanvas(Canvas canvas, Map<String, Image> imageMap) {
		if (imageMap.get(BUILDER_CANVAS_BACKGND_FILENAME) != null) {

			Image img = imageMap.get(BUILDER_CANVAS_BACKGND_FILENAME);
			canvasBackground = new ImageFigure();
			canvasBackground
					.setImage(resizeImage(img, DEFAULT_FAKEWINDOW_INIT_DIM.width, DEFAULT_FAKEWINDOW_INIT_DIM.height));
			canvasBackground.setBounds(
					new Rectangle(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x, DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y,
							DEFAULT_FAKEWINDOW_INIT_DIM.width + DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x,
							DEFAULT_FAKEWINDOW_INIT_DIM.height + DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y));

			new ImageResizer(canvasBackground, img, canvas, false, ImageResizer.Handle.BOT_RIGHT);

			return canvasBackground;
		} else {
			RectangleFigure fig = new RectangleFigure();
			fig.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			fig.setBounds(new Rectangle(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x, DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y,
					DEFAULT_FAKEWINDOW_INIT_DIM.width, DEFAULT_FAKEWINDOW_INIT_DIM.height));
			new FigureMoverResizer(fig, guiBuilderView, null, "", false, FigureMoverResizer.Handle.BOT_RIGHT);
			return fig;
		}
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

		if (insideCanvas(position)) {
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

	public boolean insideCanvas(Point position) {
		return position.x < (DEFAULT_FAKEWINDOW_INIT_DIM.width + DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x)
				&& position.y < (DEFAULT_FAKEWINDOW_INIT_DIM.height + DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y)
				&& position.x > DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x
				&& position.y > DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y;
	}

	/*
	 * Thank to Chris Aniszczyk for providing this lines of code ;)
	 * http://aniszczyk.org/2007/08/09/resizing-images-using-swt/
	 */
	private Image resizeImage(Image image, int width, int height) {
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
