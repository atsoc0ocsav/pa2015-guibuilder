package pt.iscte.pidesco.guibuilder.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.internal.ObjectInComposite;

public class GuiBuilderObjFactory {
	/*
	 * GUIBuilder specific parameters
	 */
	// Dimensions
	private static final Point DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET = new Point(5, 5);
	private static final Dimension DEFAULT_FAKEWINDOW_INIT_DIM = new Dimension(400, 400);
	private static int TOPBAR_HEIGH = 100;
	private static final Dimension LABELS_MARGIN = new Dimension(5, 5);

	// Default text
	private static final String DEFAULT_BTN_TXT = "New Button";
	private static final String DEFAULT_LABEL_TXT = "New Label";
	private static final String DEFAULT_TXTFIELD_TXT = "New Textfield";
	private static final String DEFAULT_RADIOBTN_TXT = "New choice";
	private static final String DEFAULT_CHCKBOX_TXT = "New checkbox";

	// Files
	private static final String BUILDER_CANVAS_BACKGND_FILENAME = "fake_window_complete_canvas.png";
	private static final String CANVAS_TOPBAR_FILENAME = "fake_window_topbar.png";

	// Other
	private static final Point WINDOWS_MOUSE_OFFSET = new Point(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x,
			-(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y + 113));

	public static Figure createGuiBuilderCanvas(Canvas canvas, Map<String, Image> imageMap) {
		if (imageMap.get(BUILDER_CANVAS_BACKGND_FILENAME) != null) {
			Image img = imageMap.get(BUILDER_CANVAS_BACKGND_FILENAME);
			ImageFigure image = new ImageFigure();
			image.setImage(resizeImage(img, DEFAULT_FAKEWINDOW_INIT_DIM.width, DEFAULT_FAKEWINDOW_INIT_DIM.height));
			image.setBounds(new Rectangle(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x, DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y,
					DEFAULT_FAKEWINDOW_INIT_DIM.width, DEFAULT_FAKEWINDOW_INIT_DIM.height));

			// new ImageResizer(image, null, "", false, img,
			// FigureMoverResizer.Handle.BOT_RIGHT);
			return image;
		} else {
			RectangleFigure fig = new RectangleFigure();
			fig.setBackgroundColor(canvas.getDisplay().getSystemColor(SWT.COLOR_GRAY));
			fig.setBounds(new Rectangle(DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x, DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y,
					DEFAULT_FAKEWINDOW_INIT_DIM.width, DEFAULT_FAKEWINDOW_INIT_DIM.height));
			new FigureMoverResizer(fig, null, "", false, FigureMoverResizer.Handle.BOT_RIGHT);
			return fig;
		}
	}

	public static ObjectInComposite createComponentFamilyObject(Point position, String cmpName, Canvas canvas,
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
				backgroundButton.setBounds(new Rectangle(position.x, position.y, buttonSize.x, buttonSize.y));
				backgroundButton.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundButton);

				Button button = new Button(canvas, SWT.BORDER);
				button.setText(DEFAULT_BTN_TXT);
				button.setLocation(position.x, position.y);
				button.setSize(buttonSize);
				button.setEnabled(false);

				FigureMoverResizer fmrButton = new FigureMoverResizer(backgroundButton, button, canvas, DEFAULT_BTN_TXT,
						true, FigureMoverResizer.Handle.values());

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), button, fmrButton);

			case LABEL:
				FontMetrics fmLabel = new GC(canvas).getFontMetrics();
				Point labelSize = new Point(
						(fmLabel.getAverageCharWidth() * DEFAULT_LABEL_TXT.length()) + LABELS_MARGIN.width,
						fmLabel.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundLabel = new RectangleFigure();
				backgroundLabel.setBounds(new Rectangle(position.x, position.y, labelSize.x, labelSize.y));
				backgroundLabel.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundLabel);

				Label label = new Label(canvas, SWT.BORDER);
				label.setText(DEFAULT_LABEL_TXT);
				label.setLocation(position.x, position.y);
				label.setSize(labelSize);
				label.setEnabled(false);

				FigureMoverResizer fmrLabel = new FigureMoverResizer(backgroundLabel, label, canvas, DEFAULT_LABEL_TXT,
						true, FigureMoverResizer.Handle.values());

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), label, fmrLabel);

			case TEXTFIELD:
				FontMetrics fmTxtField = new GC(canvas).getFontMetrics();
				Point txtFieldSize = new Point(
						(fmTxtField.getAverageCharWidth() * DEFAULT_TXTFIELD_TXT.length()) + LABELS_MARGIN.width,
						fmTxtField.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundTxtField = new RectangleFigure();
				backgroundTxtField.setBounds(new Rectangle(position.x, position.y, txtFieldSize.x, txtFieldSize.y));
				backgroundTxtField.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundTxtField);

				Text txtField = new Text(canvas, SWT.BORDER);
				txtField.setText(DEFAULT_TXTFIELD_TXT);
				txtField.setLocation(position.x, position.y);
				txtField.setSize(txtFieldSize);
				txtField.setEditable(false);
				txtField.setEnabled(false);

				FigureMoverResizer fmrTxtField = new FigureMoverResizer(backgroundTxtField, txtField, canvas,
						DEFAULT_TXTFIELD_TXT, true, FigureMoverResizer.Handle.values());

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), txtField, fmrTxtField);

			case RADIO_BTN:
				FontMetrics fmRadioBtn = new GC(canvas).getFontMetrics();
				Point radioBtnSize = new Point(
						(fmRadioBtn.getAverageCharWidth() * DEFAULT_RADIOBTN_TXT.length()) + LABELS_MARGIN.width + 10,
						fmRadioBtn.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundRadioBtn = new RectangleFigure();
				backgroundRadioBtn.setBounds(new Rectangle(position.x, position.y, radioBtnSize.x, radioBtnSize.y));
				backgroundRadioBtn.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundRadioBtn);

				Button radioBtn = new Button(canvas, SWT.RADIO);
				radioBtn.setText(DEFAULT_RADIOBTN_TXT);
				radioBtn.setLocation(position.x, position.y);
				radioBtn.setSelection(true);
				radioBtn.setSize(radioBtnSize);
				radioBtn.setEnabled(false);

				FigureMoverResizer fmrRadioBtn = new FigureMoverResizer(backgroundRadioBtn, radioBtn, canvas,
						DEFAULT_RADIOBTN_TXT, true, FigureMoverResizer.Handle.values());

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), radioBtn, fmrRadioBtn);

			case CHK_BOX:
				FontMetrics fmChckBox = new GC(canvas).getFontMetrics();
				Point chckBoxSize = new Point(
						(fmChckBox.getAverageCharWidth() * DEFAULT_CHCKBOX_TXT.length()) + LABELS_MARGIN.width + 18,
						fmChckBox.getHeight() + LABELS_MARGIN.height);

				RectangleFigure backgroundChckBox = new RectangleFigure();
				backgroundChckBox.setBounds(new Rectangle(position.x, position.y, chckBoxSize.x, chckBoxSize.y));
				backgroundChckBox.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
				contents.add(backgroundChckBox);

				Button chckBox = new Button(canvas, SWT.CHECK);
				chckBox.setText(DEFAULT_CHCKBOX_TXT);
				chckBox.setLocation(position.x, position.y);
				chckBox.setSize(chckBoxSize);
				chckBox.setEnabled(false);

				FigureMoverResizer fmrChckBox = new FigureMoverResizer(backgroundChckBox, chckBox, canvas,
						DEFAULT_CHCKBOX_TXT, true, FigureMoverResizer.Handle.values());

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), chckBox, fmrChckBox);

			default:
				throw new IllegalAccessError("Switch case not defined!");
			}
		} else {
			return null;
		}
	}

	public static ObjectInComposite createLayoutFamilyObject(Point position, String cmpName, Canvas canvas,
			Figure contents) {
		// TODO Define method
		System.err.println("Method undefined");
		return null;
	}

	public static ObjectInComposite createContainerFamilyObject(Point position, String cmpName, Canvas canvas,
			Figure contents) {
		// TODO Define method
		System.err.println("Method undefined");
		return null;
	}

	private static boolean insideCanvas(Point position) {
		return position.x < (DEFAULT_FAKEWINDOW_INIT_DIM.width + DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x)
				&& position.y < (DEFAULT_FAKEWINDOW_INIT_DIM.height + DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y)
				&& position.x > DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.x
				&& position.y > DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET.y;
	}

	/*
	 * Thank to Chris Aniszczyk for providing this lines of code ;)
	 * http://aniszczyk.org/2007/08/09/resizing-images-using-swt/
	 */
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
