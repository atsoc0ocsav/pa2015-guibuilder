package pt.iscte.pidesco.guibuilder.ui;

import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import pt.iscte.pidesco.guibuilder.internal.ObjectInComposite;

public class GuiBuilderObjFactory {
	/*
	 * GUIBuilder specific parameters
	 */
	// Dimensions Canvas
	private static final Point DEFAULT_CANVAS_LEFTTOPCORNER_OFFSET = new Point(5, 5);
	private static final Dimension DEFAULT_FAKEWINDOW_INIT_DIM = new Dimension(400, 400);
	private static int TOPBAR_HEIGH = 100;

	// Dimensions Elements
	private static final Dimension DEFAULT_BUTTON_DIM = new Dimension(100, 90);
	private static final Dimension DEFAULT_LABEL_BACKGND_DIM = new Dimension(70, 30);
	private static final Dimension DEFAULT_LABEL_DIM = new Dimension(55, 15);
	private static final Dimension DEFAULT_TXTFIELD_BACKGND_DIM = new Dimension(150, 40);
	private static final Dimension DEFAULT_TXTFIELD_DIM = new Dimension(100, 20);
	private static final Dimension DEFAULT_RADIOBTN_BACKGND_DIM = new Dimension(150, 40);
	private static final Dimension DEFAULT_RADIOBTN_DIM = new Dimension(100, 20);
	private static final Dimension DEFAULT_CHCKBOX_BACKGND_DIM = new Dimension(150, 40);
	private static final Dimension DEFAULT_CHCKBOX_DIM = new Dimension(100, 20);

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

			new ImageResizer(image, null, "", false, img, FigureMoverResizer.Handle.BOT_RIGHT);
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

	public static ObjectInComposite createComponentFamilyObject(String cmpName, Canvas canvas, Figure contents) {
		Point absCursorLocation = Display.getCurrent().getCursorLocation();
		Point mousePosition = Display.getCurrent().getFocusControl().toControl(absCursorLocation);

		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
			mousePosition.x = mousePosition.x + WINDOWS_MOUSE_OFFSET.x;
			mousePosition.y = absCursorLocation.y + WINDOWS_MOUSE_OFFSET.y;
		}

		GuiLabels.GUIBuilderComponent component = null;
		for (GuiLabels.GUIBuilderComponent c : GuiLabels.GUIBuilderComponent.values()) {
			if (c.str().equals(cmpName)) {
				component = c;
				break;
			}
		}

		switch (component) {
		case BTN:
			RoundedRectangle button = new RoundedRectangle();
			button.setCornerDimensions(new Dimension(20, 20));

			boolean insideCanvas = true;
			if (mousePosition.x < DEFAULT_FAKEWINDOW_INIT_DIM.width && mousePosition.x > (DEFAULT_BUTTON_DIM.width / 2)
					&& mousePosition.y < DEFAULT_FAKEWINDOW_INIT_DIM.height
					&& mousePosition.y > (DEFAULT_BUTTON_DIM.height / 2)) {
				button.setBounds(new Rectangle(mousePosition.x - (DEFAULT_BUTTON_DIM.width / 2),
						mousePosition.y - (DEFAULT_BUTTON_DIM.height / 2), DEFAULT_BUTTON_DIM.width,
						DEFAULT_BUTTON_DIM.height));
				// System.out.println("Option 1");
			} else if (mousePosition.x < (DEFAULT_BUTTON_DIM.width / 2)
					&& mousePosition.y < (DEFAULT_BUTTON_DIM.height / 2)) {
				button.setBounds(new Rectangle(0, 0, DEFAULT_BUTTON_DIM.width, DEFAULT_BUTTON_DIM.height));
				// System.out.println("Option 2");
			} else if (mousePosition.x < (DEFAULT_BUTTON_DIM.width / 2)
					&& mousePosition.x < DEFAULT_FAKEWINDOW_INIT_DIM.width
					&& mousePosition.y < DEFAULT_FAKEWINDOW_INIT_DIM.height) {
				button.setBounds(new Rectangle(0, mousePosition.y - (DEFAULT_BUTTON_DIM.height / 2),
						DEFAULT_BUTTON_DIM.width, DEFAULT_BUTTON_DIM.height));
				// System.out.println("Option 3");

			} else if (mousePosition.y < (DEFAULT_BUTTON_DIM.height / 2)
					&& mousePosition.x < DEFAULT_FAKEWINDOW_INIT_DIM.width
					&& mousePosition.y < DEFAULT_FAKEWINDOW_INIT_DIM.height) {
				button.setBounds(new Rectangle(mousePosition.x - (DEFAULT_BUTTON_DIM.width / 2), 0,
						DEFAULT_BUTTON_DIM.width, DEFAULT_BUTTON_DIM.height));
				// System.out.println("Option 4");
			} else {
				insideCanvas = false;
			}

			if (insideCanvas) {
				contents.add(button);
				FigureMoverResizer fmrButton = new FigureMoverResizer(button, canvas, DEFAULT_BTN_TXT, true,
						FigureMoverResizer.Handle.values());

				return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), button, fmrButton);
			} else {
				return null;
			}

		case LABEL:
			Label label = new Label(canvas, SWT.BORDER);
			label.setText(DEFAULT_LABEL_TXT);
			label.setSize(DEFAULT_LABEL_DIM.width, DEFAULT_LABEL_DIM.height);
			label.setLocation(mousePosition.x, mousePosition.y);

			RoundedRectangle backgroundLabel = new RoundedRectangle();
			backgroundLabel.setCornerDimensions(new Dimension(10, 10));
			backgroundLabel.setBounds(new Rectangle(mousePosition.x, mousePosition.y, DEFAULT_LABEL_BACKGND_DIM.width,
					DEFAULT_LABEL_BACKGND_DIM.height));
			contents.add(backgroundLabel);

			// FigureMoverResizer fmrLabel = new
			// FigureMoverResizer(backgroundLabel, canvas, label,
			// DEFAULT_LABEL_DIM.width, DEFAULT_LABEL_DIM.height);

			return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), label, null);

		case TEXTFIELD:
			Text textField = new Text(canvas, SWT.BORDER);
			textField.setText(DEFAULT_TXTFIELD_TXT);
			textField.setSize(DEFAULT_TXTFIELD_DIM.width, DEFAULT_TXTFIELD_DIM.height);
			textField.setLocation(mousePosition.x, mousePosition.y);

			RoundedRectangle backgroundTextField = new RoundedRectangle();
			backgroundTextField.setCornerDimensions(new Dimension(10, 10));
			backgroundTextField.setBounds(new Rectangle(mousePosition.x, mousePosition.y,
					DEFAULT_TXTFIELD_BACKGND_DIM.width, DEFAULT_TXTFIELD_BACKGND_DIM.height));
			contents.add(backgroundTextField);

			// FigureMoverResizer fmrTextField = new
			// FigureMoverResizer(backgroundTextField, canvas, textField,
			// DEFAULT_TXTFIELD_DIM.width, DEFAULT_TXTFIELD_DIM.height);

			return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), textField, null);

		// case RADIO_BTN:
		// Button radioButton = new Button(canvas, SWT.RADIO);
		// radioButton.setText(DEFAULT_RADIOBTN_TXT);
		// radioButton.setSize(DEFAULT_RADIOBTN_DIM.width,DEFAULT_RADIOBTN_DIM.height);
		// radioButton.setLocation(mousePosition.x,
		// mousePosition.y);
		// radioButton.setSelection(true);
		//
		// RoundedRectangle backgroundRadioButton = new RoundedRectangle();
		// backgroundRadioButton.setCornerDimensions(new Dimension(10, 10));
		// backgroundRadioButton.setBounds(new
		// Rectangle(mousePosition.x, mousePosition.y,
		// DEFAULT_RADIOBTN_BACKGND_DIM.width,
		// DEFAULT_RADIOBTN_BACKGND_DIM.height));
		// contents.add(backgroundRadioButton);
		//
		// // FigureMoverResizer fmrRadioButton = new
		// // FigureMoverResizer(backgroundRadioButton, canvas, radioButton,
		// // DEFAULT_RADIOBTN_DIM.x, DEFAULT_RADIOBTN_DIM.y);
		//
		// return new ComponentInComposite(cmpName + "\t" +
		// System.currentTimeMillis(), radioButton, null);

		case CHK_BOX:
			Button checkBox = new Button(canvas, SWT.CHECK);
			checkBox.setText(DEFAULT_CHCKBOX_TXT);
			checkBox.setSize(DEFAULT_CHCKBOX_DIM.width, DEFAULT_CHCKBOX_DIM.height);
			checkBox.setLocation(mousePosition.x, mousePosition.y);

			RoundedRectangle backgroundCheckBox = new RoundedRectangle();
			backgroundCheckBox.setCornerDimensions(new Dimension(10, 10));
			backgroundCheckBox.setBounds(new org.eclipse.draw2d.geometry.Rectangle(mousePosition.x, mousePosition.y,
					DEFAULT_CHCKBOX_BACKGND_DIM.width, DEFAULT_CHCKBOX_BACKGND_DIM.height));
			contents.add(backgroundCheckBox);

			// FigureMoverResizer fmrCheckBox = new
			// FigureMoverResizer(backgroundCheckBox, canvas, checkBox,
			// DEFAULT_CHCKBOX_DIM.x, DEFAULT_CHCKBOX_DIM.y);

			return new ObjectInComposite(cmpName + "\t" + System.currentTimeMillis(), checkBox, null);

		default:
			throw new IllegalAccessError("Switch case not defined!");
		}
	}

	public static ObjectInComposite createLayoutFamilyObject(String cmpName, Canvas canvas, Figure contents) {
		// TODO Define method
		System.err.println("Method undefined");
		return null;
	}

	public static ObjectInComposite createContainerFamilyObject(String cmpName, Canvas canvas, Figure contents) {
		// TODO Define method
		System.err.println("Method undefined");
		return null;
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
