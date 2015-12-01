package pt.iscte.pidesco.guibuilder.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.guibuilder.internal.ComponentInComposite;
import pt.iscte.pidesco.guibuilder.internal.GeneratorCode;

public class GuiBuilderView implements PidescoView {
	/*
	 * Parameterization (measures in pixels)
	 */
	private final Dimension BOTTOM_COMPOSITE_BUTTONS_DIM = new Dimension(150, 90);
	private final int BOTTOM_COMPOSITE_MINIMUM_HEIGHT = BOTTOM_COMPOSITE_BUTTONS_DIM.height + 78;
	private final String COMPONENTS_TAB_ICON_FILENAME = "icon_tab_components.png";
	private final String LAYOUTS_TAB_ICON_FILENAME = "icon_tab_layouts.png";
	private final String CONTAINERS_TAB_ICON_FILENAME = "icon_tab_containers.png";

	/*
	 * Variables
	 */
	private Composite topComposite;
	private Composite bottomComposite;
	private ArrayList<ComponentInComposite> components = new ArrayList<ComponentInComposite>();

	/*
	 * Constructors and main methods
	 */
	public GuiBuilderView() {

	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		createBaseFrame(viewArea, imageMap);
		populateTopComposite(topComposite, imageMap);
		populateBottomComposite(bottomComposite, imageMap);
	}

	// TODO Change to include status bar
	private void createBaseFrame(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new FillLayout());

		// Create a LiveSashForm with VERTICAL and with a minimum height
		LiveSashForm sashForm = new LiveSashForm(viewArea, SWT.VERTICAL);

		topComposite = new Composite(sashForm, SWT.BORDER);
		topComposite.setLayout(new FillLayout());

		bottomComposite = new Composite(sashForm, SWT.BORDER);
		bottomComposite.setLayout(new FillLayout());

		// Define the relation between both composites
		sashForm.setWeights(new int[] { 6, 2 });
		sashForm.dragMinimum = BOTTOM_COMPOSITE_MINIMUM_HEIGHT;
	}

	/*
	 * Top composite methods
	 */
	private void populateTopComposite(Composite composite, Map<String, Image> imageMap) {
		final Canvas canvas = new Canvas(composite, SWT.NONE);
		LightweightSystem lws = new LightweightSystem(canvas);
		final Figure contents = new Figure();

		XYLayout contentsLayout = new XYLayout();
		contents.setLayoutManager(contentsLayout);
		lws.setContents(contents);

		contents.add(GuiBuilderObjFactory.createGuiBuilderCanvas(canvas, imageMap));

		// Create the drop target on the composite
		DropTarget dt = new DropTarget(composite, DND.DROP_MOVE);
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

					switch (of) {
					case COMPONENTS:
						components.add(GuiBuilderObjFactory.createComponentFamilyObject(objectName, canvas, contents));
						break;
					case LAYOUTS:
						components.add(GuiBuilderObjFactory.createLayoutFamilyObject(objectName, canvas, contents));
						break;
					case CONTAINERS:
						components.add(GuiBuilderObjFactory.createContainerFamilyObject(objectName, canvas, contents));
						break;
					default:
						throw new IllegalAccessError("Switch case not defined!");
					}
				}
			}
		});
		mouseEventTopComposite(canvas);
	}

	// TODO Refactoring
	private void mouseEventTopComposite(final Canvas canvas) {
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent event) {
				if (event.button == 3) { // Right click
					for (ComponentInComposite componentInComposite : components) {
						boolean found = false;

						for (GuiLabels.GUIBuilderComponent c : GuiLabels.GUIBuilderComponent.values()) {
							if (componentInComposite.getId().contains(c.str())) {
								found = true;

								switch (c) {
								case BTN:
									RoundedRectangle jButton = ((RoundedRectangle) componentInComposite.getObject());

									if (event.x > jButton.getLocation().x
											&& event.x < jButton.getLocation().x + jButton.getBounds().width
											&& event.y > jButton.getLocation().y
											&& event.y < jButton.getLocation().y + jButton.getBounds().height) {
										System.out.println("Right click !");

										openDialogMenu(canvas, componentInComposite.getFmr(), event.x, event.y);
									}
									break;
								case LABEL:

									break;
								case TEXTFIELD:

									break;

								// case RADIO_BTN:
								//
								// break;

								case CHK_BOX:

									break;

								}
							}
						}

						if (!found) {
							for (GuiLabels.GUIBuilderLayout l : GuiLabels.GUIBuilderLayout.values()) {
								if (componentInComposite.getId().contains(l.str())) {
									found = true;

									// TODO Define method
									System.err.println("Undefined");
								}
							}
						}

						if (!found) {
							for (GuiLabels.GUIBuilderContainer c : GuiLabels.GUIBuilderContainer.values()) {
								if (componentInComposite.getId().contains(c.str())) {
									found = true;

									// TODO Define method
									System.err.println("Undefined");
								}
							}
						}
					}
				}

			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				System.out.println("Double click->Created method action \n");

			}
		});

	}

	private void openDialogMenu(Canvas canvas, FigureMoverResizer fmr, int x, int y) {
		Menu popupMenu = new Menu(canvas);
		MenuItem renameItem = new MenuItem(popupMenu, SWT.NONE);
		renameItem.setText(GuiLabels.DialogMenuLabel.RENAME.str());
		addDialogMenuListener(renameItem, canvas, fmr, x, y);

		// Menu COLOR
		MenuItem colorItem = new MenuItem(popupMenu, SWT.CASCADE);
		colorItem.setText(GuiLabels.DialogMenuLabel.CHOOSE_COLOR.str());
		Menu chooseColorItemMenu = new Menu(colorItem);
		colorItem.setMenu(chooseColorItemMenu);

		for (GuiLabels.Color c : GuiLabels.Color.values()) {
			MenuItem item = new MenuItem(chooseColorItemMenu, SWT.NONE);
			item.setText(c.name());
			addDialogMenuListener(item, canvas, fmr, x, y);
		}

		MenuItem goToCodeItem = new MenuItem(popupMenu, SWT.NONE);
		goToCodeItem.setText(GuiLabels.DialogMenuLabel.GO_TO_CODE.str());
		goToCodeItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// create dialog to select target
				new GeneratorCode(GeneratorCode.selectTarget.SWING, "");
			}
		});

		popupMenu.setVisible(true);

	}

	private void addDialogMenuListener(final MenuItem item, final Canvas canvas, final FigureMoverResizer fmr,
			final int x, final int y) {
		item.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String itemText = item.getText();

				if (itemText.equals(GuiLabels.DialogMenuLabel.RENAME.str())) {
					String inputText = new InputDialog(x, y, topComposite.getShell(), SWT.BAR).open();
					fmr.setText(inputText);
				} else if (itemText.equals(GuiLabels.DialogMenuLabel.CHOOSE_COLOR.str())) {
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
			}
		});
	}

	/*
	 * Bottom composite methods
	 */
	private void populateBottomComposite(Composite composite, Map<String, Image> imageMap) {
		TabFolder tabFolder = new TabFolder(composite, SWT.TOP);

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
					addDragListener(button, tabLabel.ordinal());
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
					addDragListener(button, tabLabel.ordinal());
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
					addDragListener(button, tabLabel.ordinal());
				}
				compositeButtons.setSize(
						BOTTOM_COMPOSITE_BUTTONS_DIM.width * GuiLabels.GUIBuilderContainer.values().length,
						BOTTOM_COMPOSITE_BUTTONS_DIM.height);
				break;

			default:
				throw new IllegalAccessError("Switch case not defined!");
			}
		}
	}

	private void addDragListener(final Button button, final int objectTypeOrdinal) {
		DragSource ds = new DragSource(button, DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });

		ds.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				event.data = objectTypeOrdinal + "\t" + button.getText();
			}
		});
	}
}
