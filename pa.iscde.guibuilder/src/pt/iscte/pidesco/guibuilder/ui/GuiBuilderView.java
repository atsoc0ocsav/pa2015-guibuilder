package pt.iscte.pidesco.guibuilder.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ListDialog;

import pa.iscde.test.ExtensionTestInterface;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.guibuilder.extensions.ExtensionPointsData;
import pt.iscte.pidesco.guibuilder.internal.FigureMoverResizer;
import pt.iscte.pidesco.guibuilder.internal.GeneratorCode;
import pt.iscte.pidesco.guibuilder.internal.ImageResizer;
import pt.iscte.pidesco.guibuilder.internal.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.internal.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderLayout;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class GuiBuilderView implements PidescoView, ExtensionTestInterface {
	/*
	 * Parameterization (measures in pixels)
	 */
	// Dimensions and images
	private final Dimension BOTTOM_COMPOSITE_BUTTONS_DIM = new Dimension(150, 90);
	private final int BOTTOM_COMPOSITE_MINIMUM_HEIGHT = BOTTOM_COMPOSITE_BUTTONS_DIM.height + 78;
	private final String COMPONENTS_TAB_ICON_FILENAME = "icon_tab_components.png";
	private final String LAYOUTS_TAB_ICON_FILENAME = "icon_tab_layouts.png";
	@SuppressWarnings("unused")
	private final String CONTAINERS_TAB_ICON_FILENAME = "icon_tab_containers.png";

	// Messages
	private final String INITIAL_MSG = "Welcome to GUIBuilder!";
	private static final String ADDED_OBJECT_MSG = "Added %s to canvas";
	public static final String OUT_OF_BOUNDS_OBJECT_MSG = "Object %s dropped out of canvas";
	public static final String CHANGED_TITLE_MSG = "Changed window title to \"%s\"";
	private static final String SET_LAYOUT = "Set %s on canvas";

	/*
	 * Variables
	 */
	private Composite viewArea;
	private Map<String, Image> imageMap;
	private Composite topComposite;
	private Composite bottomComposite;
	private ArrayList<ObjectInComposite> components = new ArrayList<ObjectInComposite>();
	private Text messageArea;
	private GuiBuilderObjFactory objectFactory;
	public static Canvas topCanvas;
	private GuiLabels.GUIBuilderLayout activeLayout = GUIBuilderLayout.ABSOLUTE;

	/*
	 * Constructors and main methods
	 */
	public GuiBuilderView() {

	}

	@Override
	public void createContents(final Composite viewArea, final Map<String, Image> imageMap) {
		this.viewArea = viewArea;
		this.imageMap = imageMap;

		objectFactory = new GuiBuilderObjFactory(this);

		createBaseFrame();
		populateTopComposite();
		populateBottomComposite();
	}

	private void createBaseFrame() {
		viewArea.setLayout(new GridLayout(1, false));

		// Create a LiveSashForm with VERTICAL and with a minimum height
		LiveSashForm sashForm = new LiveSashForm(viewArea, SWT.VERTICAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		sashForm.setLayoutData(gridData);

		// Top Composite (the GUIBuilder canvas)
		topComposite = new Composite(sashForm, SWT.BORDER);
		topComposite.setLayout(new FillLayout());

		// Bottom Composite (where the tabs with the elements are)
		bottomComposite = new Composite(sashForm, SWT.BORDER);
		bottomComposite.setLayout(new FillLayout());

		// Define the relation between both top and bottom composites
		sashForm.setWeights(new int[] { 7, 2 });
		sashForm.dragMinimum = BOTTOM_COMPOSITE_MINIMUM_HEIGHT;

		// Create the message fields
		messageArea = new Text(viewArea, SWT.READ_ONLY | SWT.BORDER);
		messageArea.setText(INITIAL_MSG);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		messageArea.setLayoutData(gridData);
	}

	/*
	 * Top composite methods
	 */
	private void populateTopComposite() {
		topCanvas = new Canvas(topComposite, SWT.NONE);
		LightweightSystem lws = new LightweightSystem(topCanvas);
		final Figure contents = new Figure();

		XYLayout contentsLayout = new XYLayout();
		contents.setLayoutManager(contentsLayout);
		lws.setContents(contents);

		contents.add(objectFactory.createGuiBuilderCanvas(topCanvas, imageMap));

		// Create the drop target on the composite
		DropTarget dt = new DropTarget(topComposite, DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				String[] data = event.data.toString().split("\t");

				if (data.length != 2) {
					throw new IllegalArgumentException("Invalid reference for draggable object");
				} else {
					int index = Integer.parseInt(data[0]);
					GuiLabels.GUIBuilderObjectFamily of = GuiLabels.GUIBuilderObjectFamily.values()[index];
					String objectName = data[1];
					ObjectInComposite newObject = null;
					Point position = event.display.map(null, topComposite, new Point(event.x, event.y));

					switch (of) {
					case COMPONENTS:
						newObject = objectFactory.createComponentFamilyObject(position, objectName, topCanvas,
								contents);

						if (objectName.contains("widget")) {

							ExtensionPointsData extensionPointsData = new ExtensionPointsData();
							for (int i = 0; i < extensionPointsData.getWidgets().length; i++) {
								if (objectName.contains(extensionPointsData.getWidgetNames()[i])) {
									newObject = objectFactory.createComponentWidgetObject(
											extensionPointsData.getWidgets()[i],
											extensionPointsData.getWidgetNames()[i], position, objectName, topCanvas,
											contents);
								}
							}

						}
						System.err.println("I need refactoring!!!!!!!!!!");

						if (newObject != null) {
							components.add(newObject);
							if (objectName.contains("widget")) {
								setMessage(ADDED_OBJECT_MSG, objectName.replaceAll("widget", ""));
							} else {
								setMessage(ADDED_OBJECT_MSG, objectName);
							}
						} else {
							if (objectName.contains("widget")) {
								setMessage(OUT_OF_BOUNDS_OBJECT_MSG, Display.getCurrent().getSystemColor(SWT.COLOR_RED),
										objectName.replaceAll("widget", ""));
							} else {
								setMessage(OUT_OF_BOUNDS_OBJECT_MSG, Display.getCurrent().getSystemColor(SWT.COLOR_RED),
										objectName);
							}
						}
						break;
					case LAYOUTS:
						// newObject =
						// objectFactory.createLayoutFamilyObject(position,
						// objectName, topCanvas, contents);

						for (GuiLabels.GUIBuilderLayout l : GuiLabels.GUIBuilderLayout.values()) {
							if (l.str().equals(objectName)) {
								activeLayout = l;
								setMessage(SET_LAYOUT, objectName);
							}
						}
						break;
					// case CONTAINERS:
					// newObject =
					// objectFactory.createContainerFamilyObject(position,
					// objectName, topCanvas,
					// contents);
					// break;

					default:
						throw new IllegalAccessError("Switch case not defined!");
					}

				}
			}
		});

	}

	/*
	 * Bottom composite methods
	 */
	private void populateBottomComposite() {
		TabFolder tabFolder = new TabFolder(bottomComposite, SWT.TOP);

		for (GuiLabels.GUIBuilderObjectFamily tabLabel : GuiLabels.GUIBuilderObjectFamily.values()) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);

			ScrolledComposite sci = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL);
			Composite compositeButtons = new Composite(sci, SWT.NONE);
			compositeButtons.setLayout(new FillLayout());

			sci.setContent(compositeButtons);
			tabItem.setControl(sci);
			tabItem.setText(tabLabel.str());

			switch (tabLabel) {
			case COMPONENTS:
				tabItem.setImage(imageMap.get(COMPONENTS_TAB_ICON_FILENAME));
				for (GuiLabels.GUIBuilderComponent c : GuiLabels.GUIBuilderComponent.values()) {
					Button button = new Button(compositeButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
					button.setAlignment(SWT.CENTER);
					button.setText(c.str());
					addDragListener(button, tabLabel.ordinal(), false);
				}
				// add widget
				addNewWidget(compositeButtons, tabLabel);

				compositeButtons.setSize(
						BOTTOM_COMPOSITE_BUTTONS_DIM.width * GuiLabels.GUIBuilderComponent.values().length,
						BOTTOM_COMPOSITE_BUTTONS_DIM.height);
				break;

			case LAYOUTS:
				tabItem.setImage(imageMap.get(LAYOUTS_TAB_ICON_FILENAME));
				for (GuiLabels.GUIBuilderLayout c : GuiLabels.GUIBuilderLayout.values()) {
					Button button = new Button(compositeButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
					button.setAlignment(SWT.CENTER);
					button.setText(c.str());
					addDragListener(button, tabLabel.ordinal(), false);
				}
				compositeButtons.setSize(
						BOTTOM_COMPOSITE_BUTTONS_DIM.width * GuiLabels.GUIBuilderLayout.values().length,
						BOTTOM_COMPOSITE_BUTTONS_DIM.height);
				break;

			// case CONTAINERS:
			// tabItem.setImage(imageMap.get(CONTAINERS_TAB_ICON_FILENAME));
			// for (GuiLabels.GUIBuilderContainer c :
			// GuiLabels.GUIBuilderContainer.values()) {
			// Button button = new Button(compositeButtons, SWT.CENTER |
			// SWT.WRAP | SWT.PUSH);
			// button.setAlignment(SWT.CENTER);
			// button.setText(c.str());
			// addDragListener(button, tabLabel.ordinal());
			// }
			// compositeButtons.setSize(
			// BOTTOM_COMPOSITE_BUTTONS_DIM.width *
			// GuiLabels.GUIBuilderContainer.values().length,
			// BOTTOM_COMPOSITE_BUTTONS_DIM.height);
			// break;

			default:
				throw new IllegalAccessError("Switch case not defined!");
			}
		}
	}

	private void addDragListener(final Button button, final int objectTypeOrdinal, final boolean isWidget) {
		DragSource ds = new DragSource(button, DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });

		ds.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				if (!isWidget) {
					event.data = objectTypeOrdinal + "\t" + button.getText();
				} else {
					event.data = objectTypeOrdinal + "\t" + "widget" + button.getText();
				}

			}
		});
	}

	private void addNewWidget(Composite compositeButtons, GUIBuilderObjectFamily tabLabel) {
		ExtensionPointsData extensionPointsData = new ExtensionPointsData();

		for (int i = 0; i < extensionPointsData.getWidgetNames().length; i++) {
			Button button = new Button(compositeButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
			button.setAlignment(SWT.CENTER);
			button.setText(extensionPointsData.getWidgetNames()[i]);
			addDragListener(button, tabLabel.ordinal(), true);
		}

	}

	/*
	 * Menus and listeners
	 */
	public void openDialogMenu(final ObjectMoverResizer fmr, final int x, final int y) {
		Menu popupMenu = new Menu(topCanvas);

		if (fmr instanceof FigureMoverResizer) {
			// Item Rename
			MenuItem renameItem = new MenuItem(popupMenu, SWT.NONE);
			renameItem.setText(GuiLabels.DialogMenuLabel.RENAME.str());
			renameItem.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Point position = topCanvas.getDisplay().map(topCanvas, null, new Point(x, y));
					String inputText = new InputDialog(position.x, position.y, topComposite.getShell(), SWT.BAR).open();

					if (inputText != null) {
						((FigureMoverResizer) fmr).setText(inputText);
					}
				}
			});

			// Item Change Background color and sub-menu
			MenuItem colorItem = new MenuItem(popupMenu, SWT.CASCADE);
			colorItem.setText(GuiLabels.DialogMenuLabel.CHOOSE_COLOR.str());
			Menu chooseColorItemMenu = new Menu(colorItem);
			colorItem.setMenu(chooseColorItemMenu);

			for (GuiLabels.Color c : GuiLabels.Color.values()) {
				MenuItem item = new MenuItem(chooseColorItemMenu, SWT.NONE);
				item.setText(c.name());
				addColorDialogMenuListener(item, topCanvas, ((FigureMoverResizer) fmr));
			}

			// Delete Item
			MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
			deleteItem.setText(GuiLabels.DialogMenuLabel.DELETE_OBJECT.str());
			deleteItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ObjectInComposite object = null;
					for (ObjectInComposite o : components) {
						if (o.getFigure().equals(((FigureMoverResizer) fmr).getFigure())) {
							object = o;
						}
					}

					if (object != null) {
						try {
							object.getFmr().getControl().dispose();
						} catch (NullPointerException exception) {
						}

						((FigureMoverResizer) fmr).getFigure().setVisible(false);
						components.remove(object);

						topCanvas.update();
						topCanvas.redraw();
						topCanvas.layout();
					}
				}
			});

			popupMenu.setVisible(true);
		} else if (fmr instanceof ImageResizer) {
			if (!isInsideObjectOnCanvas(new Point(x, y))) {
				// Item Window Name
				MenuItem setWindowName = new MenuItem(popupMenu, SWT.NONE);
				setWindowName.setText(GuiLabels.DialogMenuLabel.SET_WINDW_TITLE.str());
				setWindowName.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						Point position = topCanvas.getDisplay().map(topCanvas, null, new Point(x, y));
						String inputText = new InputDialog(position.x, position.y, topComposite.getShell(), SWT.BAR)
								.open();

						if (inputText != null) {
							((ImageResizer) fmr).setText(inputText);
							setMessage(CHANGED_TITLE_MSG, inputText);
						}
					}

				});

				// Generate code
				MenuItem generateCode = new MenuItem(popupMenu, SWT.NONE);
				generateCode.setText(GuiLabels.DialogMenuLabel.GENERATE_CODE.str());
				generateCode.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						ListDialog dialog = new ListDialog(topCanvas.getShell());
						dialog.setAddCancelButton(true);
						dialog.setContentProvider(new ArrayContentProvider());
						dialog.setLabelProvider(new LabelProvider());
						dialog.setInput(new String[] { "SWING", "SWT" });
						dialog.setTitle("Select target");
						dialog.open();

						Object[] result = dialog.getResult();

						if (dialog.open() == Window.OK) {
							if (result[0].toString().equals("SWING")) {
								System.out.println("Generating swing...");
								new GeneratorCode(GeneratorCode.selectTarget.SWING,
										((ImageResizer) fmr).getTitleFrame(), components);

							}
							if (result[0].toString().equals("SWT")) {
								System.out.println("Generating swt...");
								new GeneratorCode(GeneratorCode.selectTarget.SWT, ((ImageResizer) fmr).getTitleFrame(),
										components);
							}

						}
					}
				});

				popupMenu.setVisible(true);
			}
		}
	}

	private void addColorDialogMenuListener(final MenuItem item, final Canvas canvas, final FigureMoverResizer fmr) {
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String itemText = item.getText();

				for (GuiLabels.Color c : GuiLabels.Color.values()) {
					if (c.name().equals(itemText)) {
						if (itemText.equals(GuiLabels.Color.Other.name())) {
							ColorDialog dlg = new ColorDialog(canvas.getShell());
							dlg.setRGB(fmr.getFigure().getBackgroundColor().getRGB());

							// Change the title bar text
							dlg.setText("Choose a Color");
							fmr.getFigure().setBackgroundColor(new Color(canvas.getDisplay(), dlg.open()));
						} else {
							fmr.getFigure().setBackgroundColor(canvas.getDisplay().getSystemColor(c.swt_value()));
						}
						break;
					}
				}
			}
		});
	}

	/*
	 * Getters and Setters
	 */
	public void setMessage(String message, Object... args) {
		setMessage(message, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), args);
	}

	public void setMessage(String message, Color color, Object... args) {
		messageArea.setForeground(color);
		messageArea.setText(String.format(message, args));
	}

	private boolean isInsideObjectOnCanvas(Point location) {
		for (ObjectInComposite o : components) {
			Figure fig = o.getFigure();

			if (location.x >= fig.getLocation().x && location.y >= fig.getLocation().y
					&& location.x < (fig.getLocation().x + fig.getSize().width)
					&& location.y < (fig.getLocation().y + fig.getSize().height)) {
				return true;
			}
		}
		return false;
	}

	public Composite getTopComposite() {
		return topComposite;
	}

	public void addComponentTopComposite(ObjectInComposite newObject) {
		components.add(newObject);
	}

	public boolean isInsideCanvas(int x, int y) {
		return objectFactory.isInsideCanvas(x, y);
	}

	public GuiLabels.GUIBuilderLayout getActiveLayout() {
		return activeLayout;
	}

	@Override
	public String getHelloWorld() {
		return "Hello World from GuiBuilderView";
	}
}
