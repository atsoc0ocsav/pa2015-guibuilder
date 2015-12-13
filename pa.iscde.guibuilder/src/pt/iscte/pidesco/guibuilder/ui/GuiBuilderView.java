package pt.iscte.pidesco.guibuilder.ui;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.MessageDialog;
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
import pt.iscte.pidesco.guibuilder.internal.codeGenerator.CodeGenerator;
import pt.iscte.pidesco.guibuilder.internal.graphic.CanvasResizer;
import pt.iscte.pidesco.guibuilder.internal.graphic.FigureMoverResizer;
import pt.iscte.pidesco.guibuilder.internal.graphic.GuiBuilderObjFactory;
import pt.iscte.pidesco.guibuilder.internal.graphic.ObjectMoverResizer;
import pt.iscte.pidesco.guibuilder.internal.model.ObjectInComposite;
import pt.iscte.pidesco.guibuilder.internal.model.ObjectInCompositeContainer;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.CanvasInComposite;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ComponentInCompositeImpl;
import pt.iscte.pidesco.guibuilder.internal.model.compositeContents.ContainerInComposite;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderComponent;
import pt.iscte.pidesco.guibuilder.ui.GuiLabels.GUIBuilderObjectFamily;

public class GuiBuilderView implements PidescoView, ExtensionTestInterface {
	private boolean DEBUG = true;

	/*
	 * Parameterization (measures in pixels)
	 */
	// Dimensions and images
	private final Dimension BOTTOM_COMPOSITE_BUTTONS_DIM = new Dimension(150, 90);
	private final int BOTTOM_COMPOSITE_MINIMUM_HEIGHT = BOTTOM_COMPOSITE_BUTTONS_DIM.height + 78;
	private final String COMPONENTS_TAB_ICON_FILENAME = "icon_tab_components.png";
	private final String LAYOUTS_TAB_ICON_FILENAME = "icon_tab_layouts.png";
	private final String CONTAINERS_TAB_ICON_FILENAME = "icon_tab_containers.png";

	// Messages
	private final String INITIAL_MSG = "Welcome to GUIBuilder!";
	private final String ADDED_OBJECT_MSG = "Added %s to canvas";
	public final String OUT_OF_BOUNDS_OBJECT_MSG = "Object %s dropped out of canvas";
	public final String CHANGED_TITLE_MSG = "Changed window title to \"%s\"";
	private final String SET_LAYOUT = "Set %s on canvas";
	public final String OVER_OBJECT_MSG = "Tried to drop %s over other object";
	public final String TOO_BIG_OBJECT = "Object %s is too big for canvas";
	public final String GENERATED_CODE_FOR_TARGET = "Generated source code for %s target";

	/*
	 * Variables
	 */
	private Composite viewArea;
	private Map<String, Image> imageMap;
	private Composite topComposite;
	private Composite bottomComposite;
	private ObjectInCompositeContainer rootComponent;
	private Text messageArea;
	private GuiBuilderObjFactory objectFactory;
	private Canvas topCanvas;

	/*
	 * Extension points stuff
	 */
	private ExtensionPointsData extensionPointsData;

	/*
	 * Constructors and main methods
	 */
	@Override
	public void createContents(final Composite viewArea, final Map<String, Image> imageMap) {
		this.viewArea = viewArea;
		this.imageMap = imageMap;

		objectFactory = new GuiBuilderObjFactory(this);

		createBaseFrame();
		populateTopComposite();

		extensionPointsData = new ExtensionPointsData(this);

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

		CanvasInComposite canvas = objectFactory.createGuiBuilderCanvas(topCanvas, imageMap);
		rootComponent = new ObjectInCompositeContainer("canvas", canvas, null);
		rootComponent.setRoot(true);
		rootComponent.setCanBeParent(true);

		contents.add(canvas.getFigure());

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
					Point position = event.display.map(null, topComposite, new Point(event.x, event.y));
					String objectName = data[1];

					if (!isOverObject(position, false)) {
						GuiLabels.GUIBuilderObjectFamily of = GuiLabels.GUIBuilderObjectFamily.values()[index];

						switch (of) {
						case COMPONENTS:
							ObjectInCompositeContainer newObjectInCompositeComponentContainer = null;
							GuiLabels.GUIBuilderComponent componentType = null;
							for (GuiLabels.GUIBuilderComponent c : GuiLabels.GUIBuilderComponent.values()) {
								if (c.str().equals(objectName)) {
									componentType = c;
									break;
								}
							}

							ComponentInCompositeImpl newComponent;
							if (objectName.contains(GUIBuilderComponent.WIDGET.str())) {
								newComponent = objectFactory.createComponentFamilyObject(componentType, position,
										topCanvas, extensionPointsData.getWidgetName(),
										extensionPointsData.getWidget());
							} else {
								newComponent = objectFactory.createComponentFamilyObject(componentType, position,
										topCanvas);
							}

							if (newComponent != null) {
								ObjectInCompositeContainer container = getContainerInPosition(position.x, position.y);

								contents.add(newComponent.getFigure());
								newObjectInCompositeComponentContainer = new ObjectInCompositeContainer(
										objectName + "\t" + System.currentTimeMillis(), newComponent, container);
								newObjectInCompositeComponentContainer.setCanBeParent(false);
								container.addChild(newObjectInCompositeComponentContainer);

								if (componentType == GUIBuilderComponent.WIDGET) {
									setMessage(ADDED_OBJECT_MSG,
											objectName.replaceAll(GUIBuilderComponent.WIDGET.str(), ""));
								} else {
									setMessage(ADDED_OBJECT_MSG, objectName);
								}
							} else {
								if (componentType == GUIBuilderComponent.WIDGET) {
									setMessage(OUT_OF_BOUNDS_OBJECT_MSG,
											Display.getCurrent().getSystemColor(SWT.COLOR_RED),
											objectName.replaceAll(GUIBuilderComponent.WIDGET.str(), ""));
								} else {
									setMessage(OUT_OF_BOUNDS_OBJECT_MSG,
											Display.getCurrent().getSystemColor(SWT.COLOR_RED), objectName);
								}
							}
							break;
						case LAYOUTS:
							// newObject =
							// objectFactory.createLayoutFamilyObject(position,
							// objectName, topCanvas, contents);

							for (GuiLabels.GUIBuilderLayout l : GuiLabels.GUIBuilderLayout.values()) {
								if (l.str().equals(objectName)) {
									((CanvasInComposite) rootComponent.getObjectInComposite()).setActiveLayout(l);
									setMessage(SET_LAYOUT, objectName);
								}
							}
							break;
						case CONTAINERS:
							ObjectInCompositeContainer newObjectInCompositeContainerContainer = null;
							GuiLabels.GUIBuilderContainer containerType = null;
							for (GuiLabels.GUIBuilderContainer c : GuiLabels.GUIBuilderContainer.values()) {
								if (c.str().equals(objectName)) {
									containerType = c;
									break;
								}
							}

							ContainerInComposite newContainer = objectFactory.createContainerFamilyObject(containerType,
									position, topCanvas);
							if (newContainer != null) {
								ObjectInCompositeContainer container = getContainerInPosition(position.x, position.y);

								contents.add(newContainer.getFigure());
								newObjectInCompositeContainerContainer = new ObjectInCompositeContainer(
										objectName + "\t" + System.currentTimeMillis(), newContainer, container);
								newObjectInCompositeContainerContainer.setCanBeParent(true);
								container.addChild(newObjectInCompositeContainerContainer);

								setMessage(ADDED_OBJECT_MSG, objectName);
							} else {
								setMessage(OUT_OF_BOUNDS_OBJECT_MSG, Display.getCurrent().getSystemColor(SWT.COLOR_RED),
										objectName);
							}
							break;
						default:
							throw new IllegalArgumentException("Switch case not defined!");
						}
					} else {
						setMessage(OVER_OBJECT_MSG, Display.getCurrent().getSystemColor(SWT.COLOR_RED), objectName);
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
			if (tabLabel == GUIBuilderObjectFamily.CANVAS)
				continue;

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
					if (c != GuiLabels.GUIBuilderComponent.WIDGET) {
						if (c == GUIBuilderComponent.RADIO_BTN)
							continue;

						Button button = new Button(compositeButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
						button.setAlignment(SWT.CENTER);
						button.setText(c.str());
						addDragListener(button, tabLabel.ordinal(), false);
					} else {
						// add widget
						addNewWidget(compositeButtons, tabLabel);
					}
				}

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

			case CONTAINERS:
				tabItem.setImage(imageMap.get(CONTAINERS_TAB_ICON_FILENAME));
				for (GuiLabels.GUIBuilderContainer c : GuiLabels.GUIBuilderContainer.values()) {
					Button button = new Button(compositeButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
					button.setAlignment(SWT.CENTER);
					button.setText(c.str());
					addDragListener(button, tabLabel.ordinal(), false);
				}
				compositeButtons.setSize(
						BOTTOM_COMPOSITE_BUTTONS_DIM.width * GuiLabels.GUIBuilderContainer.values().length,
						BOTTOM_COMPOSITE_BUTTONS_DIM.height);
				break;
			case CANVAS:
				break;
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
					event.data = objectTypeOrdinal + "\t" + GUIBuilderComponent.WIDGET.str() + button.getText();
				}

			}
		});
	}

	private void addNewWidget(Composite compositeButtons, GUIBuilderObjectFamily tabLabel) {
		Button button = new Button(compositeButtons, SWT.CENTER | SWT.WRAP | SWT.PUSH);
		button.setAlignment(SWT.CENTER);
		button.setText(extensionPointsData.getWidgetName());
		addDragListener(button, tabLabel.ordinal(), true);
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

						((FigureMoverResizer) fmr).renameControl(inputText);

						((ComponentInCompositeImpl) fmr.getObjectInCompositeContainer().getObjectInComposite())
								.setText(inputText);
					}
				}
			});

			// Item Change Background color and sub-menu
			MenuItem backgroundColorItem = new MenuItem(popupMenu, SWT.CASCADE);
			backgroundColorItem.setText(GuiLabels.DialogMenuLabel.CHOOSE_BACKGROUND_COLOR.str());
			Menu chooseBackgroundColorItemMenu = new Menu(backgroundColorItem);
			backgroundColorItem.setMenu(chooseBackgroundColorItemMenu);

			for (GuiLabels.Color c : GuiLabels.Color.values()) {
				MenuItem item = new MenuItem(chooseBackgroundColorItemMenu, SWT.NONE);
				item.setText(c.name());
				addColorDialogMenuListener(item, topCanvas, ((FigureMoverResizer) fmr), true);
			}

			// Item Change Foreground color and sub-menu
			MenuItem foregroundColorItem = new MenuItem(popupMenu, SWT.CASCADE);
			foregroundColorItem.setText(GuiLabels.DialogMenuLabel.CHOOSE_FOREGROUND_COLOR.str());
			Menu chooseForegroundColorItemMenu = new Menu(foregroundColorItem);
			foregroundColorItem.setMenu(chooseForegroundColorItemMenu);

			for (GuiLabels.Color c : GuiLabels.Color.values()) {
				MenuItem item = new MenuItem(chooseForegroundColorItemMenu, SWT.NONE);
				item.setText(c.name());
				addColorDialogMenuListener(item, topCanvas, ((FigureMoverResizer) fmr), false);
			}

			// Delete Item
			MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
			deleteItem.setText(GuiLabels.DialogMenuLabel.DELETE_OBJECT.str());
			deleteItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ObjectInCompositeContainer object = fmr.getObjectInCompositeContainer();

					if (object.hasChilds()) {
						if (!MessageDialog.openConfirm(topCanvas.getShell(),
								GuiLabels.DialogMenuLabel.DELETE_OBJECT.str(),
								GuiLabels.DialogMenuLabel.DELETE_OBJECT_CONFIRM_MSG.str())) {
							return;
						}
					}

					ObjectInCompositeContainer objectParent = object.getParent();

					if (object.getObjectInComposite().getObjectFamily() == GUIBuilderObjectFamily.COMPONENTS) {
						((ComponentInCompositeImpl) object.getObjectInComposite()).getControl().dispose();
					}
					object.getObjectInComposite().getFigure().setVisible(false);

					objectParent.removeChild(object);

					topCanvas.update();
					topCanvas.redraw();
					topCanvas.layout();
				}
			});

			popupMenu.setVisible(true);
		} else if (fmr instanceof CanvasResizer) {
			if (!isOverObject(new Point(x, y), false)) {
				// Item Window Name
				MenuItem setWindowName = new MenuItem(popupMenu, SWT.NONE);
				setWindowName.setText(GuiLabels.DialogMenuLabel.SET_WINDOW_TITLE.str());
				setWindowName.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						Point position = topCanvas.getDisplay().map(topCanvas, null, new Point(x, y));
						String inputText = new InputDialog(position.x, position.y, topComposite.getShell(), SWT.BAR)
								.open();

						if (inputText != null) {
							((CanvasResizer) fmr).setText(inputText);
							((CanvasInComposite) fmr.getObjectInCompositeContainer().getObjectInComposite())
									.setLabel(inputText);
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

						ArrayList<String> targets = new ArrayList<String>();
						for (CodeGenerator.CodeTarget t : CodeGenerator.CodeTarget.values()) {
							targets.add(t.getTarget());
						}

						if (DEBUG) {
							targets.add("All targets");
						}

						dialog.setInput(targets.toArray());
						dialog.setTitle("Select target");

						if (dialog.open() == Window.OK) {
							Object[] result = dialog.getResult();

							if (!DEBUG || (DEBUG && !result[0].equals("All targets"))) {
								CodeGenerator.CodeTarget target = null;
								for (CodeGenerator.CodeTarget t : CodeGenerator.CodeTarget.values()) {
									if (t.getTarget().equals(result[0])) {
										target = t;
									}
								}

								new CodeGenerator(target, rootComponent, GuiBuilderView.this).generateCode();
							} else {
								for (CodeGenerator.CodeTarget t : CodeGenerator.CodeTarget.values()) {
									new CodeGenerator(t, rootComponent, GuiBuilderView.this).generateCode();
								}
							}
						}

					}
				});

				popupMenu.setVisible(true);

			}
		}

	}

	private void addColorDialogMenuListener(final MenuItem item, final Canvas canvas, final FigureMoverResizer fmr,
			final boolean background) {
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
							Color color = new Color(canvas.getDisplay(), dlg.open());

							if (background) { // Set background color
								((ComponentInCompositeImpl) fmr.getObjectInCompositeContainer().getObjectInComposite())
										.setBackgroundColor(color);
								fmr.setBackgroundColor(color);
							} else {
								((ComponentInCompositeImpl) fmr.getObjectInCompositeContainer().getObjectInComposite())
										.setForegroundColor(color);
								fmr.setForegroundColor(color);
							}
						} else {
							Color color = canvas.getDisplay().getSystemColor(c.swt_value());
							if (background) { // Set background color
								((ComponentInCompositeImpl) fmr.getObjectInCompositeContainer().getObjectInComposite())
										.setBackgroundColor(color);
								fmr.setBackgroundColor(color);
							} else {
								((ComponentInCompositeImpl) fmr.getObjectInCompositeContainer().getObjectInComposite())
										.setForegroundColor(color);
								fmr.setForegroundColor(color);
							}
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

	private boolean isOverObject(Point location, boolean considerContainers) {
		for (ObjectInCompositeContainer o : rootComponent.getAllSubChilds()) {
			Figure fig = o.getObjectInComposite().getFigure();

			if (!considerContainers && o.getObjectInComposite().getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS)
				continue;

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

	public Dimension getCanvasSize() {
		return objectFactory.getCanvasSize();
	}

	public boolean isInsideCanvas(int x, int y, int width, int height) {
		return objectFactory.isInsideCanvas(x, y, width, height);
	}

	public ObjectInCompositeContainer getContainerInPosition(int x, int y) {
		ObjectInCompositeContainer container = null;

		for (ObjectInCompositeContainer c : rootComponent.getAllChildsByType(GUIBuilderObjectFamily.CONTAINERS)) {
			Point location = c.getObjectInComposite().getLocation();
			Point size = c.getObjectInComposite().getSize();

			if (location.x <= x && location.y <= y && (location.x + size.x) > x && (location.y + size.y) > y) {
				container = c;
				break;
			}
		}

		if (container == null) {
			container = rootComponent;
		}

		System.out.println("Container: " + container.getId());
		return container;
	}

	public boolean isOverObject(int x, int y, int width, int height, ObjectInComposite object,
			boolean considerContainers) {
		for (ObjectInCompositeContainer o : rootComponent.getAllSubChilds()) {
			if (object == o.getObjectInComposite() || (!considerContainers
					&& o.getObjectInComposite().getObjectFamily() == GUIBuilderObjectFamily.CONTAINERS))
				continue;

			Point figPos = new Point(o.getObjectInComposite().getFigure().getLocation().x,
					o.getObjectInComposite().getFigure().getLocation().y);
			Dimension figDim = o.getObjectInComposite().getFigure().getSize();

			// Not the most efficient way to do it, but is more organized and
			// understandable like this, so lets go with the if conditions
			// Is figure inside component?
			if (x >= figPos.x && x < (figPos.x + figDim.width)) {
				if (y >= figPos.y && y < (figPos.y + figDim.height)) { // TOP_LEFT
					// System.out.println("Figure TOP_LEFT inside");
					return true;
				}

				if ((y + height) >= figPos.y && (y + height) < (figPos.y + figDim.height)) { // BOTTOM_LEFT
					// System.out.println("Figure BOTTOM_LEFT inside");
					return true;
				}
			}

			if ((x + width) >= figPos.x && (x + width) < (figPos.x + figDim.width)) {
				if (y >= figPos.y && y < (figPos.y + figDim.height)) { // TOP_RIGHT
					// System.out.println("Figure TOP_RIGHT inside");
					return true;
				}

				if ((y + height) >= figPos.y && (y + height) < (figPos.y + figDim.height)) { // BOTTOM_RIGHT
					// System.out.println("Figure BOTTOM_RIGHT inside");
					return true;
				}
			}

			// Is component inside figure?
			if (figPos.x >= x && figPos.x < (x + width)) {
				if (y >= figPos.y && y < (figPos.y + figDim.height)) { // TOP_LEFT
					// System.out.println("Figure TOP_LEFT inside");
					return true;
				}

				if ((y + height) >= figPos.y && (y + height) < (figPos.y + figDim.height)) { // BOTTOM_LEFT
					// System.out.println("Figure BOTTOM_LEFT inside");
					return true;
				}
			}

			if ((figPos.x + figDim.width) >= x && (figPos.x + figDim.width) < (x + width)) {
				if (figPos.y >= y && figPos.y < (y + height)) { // TOP_RIGHT
					// System.out.println("Figure TOP_RIGHT inside");
					return true;
				}

				if ((figPos.y + figDim.height) >= y && (figPos.y + figDim.height) < (y + height)) { // BOTTOM_RIGHT
					// System.out.println("Figure BOTTOM_RIGHT inside");
					return true;
				}
			}
		}

		if (considerContainers && isInsideCanvas(x, y, width, height)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getHelloWorld() {
		return "Hello World from GuiBuilderView";
	}

	public Canvas getTopCanvas() {
		return topCanvas;
	}
}
