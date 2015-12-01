package pa.iscde.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import pt.iscte.pidesco.extensibility.PidescoView;

public class ExtensionPointsView implements PidescoView {

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		final IExtensionRegistry extRegistry = Platform.getExtensionRegistry();

		viewArea.setLayout(new GridLayout(1, false));

		Composite comp = new Composite(viewArea, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		comp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		final Combo combo = new Combo(comp, SWT.BORDER);
		combo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));

		IExtensionPoint[] extensionPoints = extRegistry.getExtensionPoints();
		combo.setItems(filterExtensionPoints(extensionPoints));

		Button button = new Button(comp, SWT.PUSH);
		button.setText("Show extensions");

		final TableViewer table = new TableViewer(viewArea);
		table.getTable().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		table.setContentProvider(new ArrayContentProvider());
		table.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				IExtension ext = (IExtension) element;
				String label = ext.getLabel();
				if (label.isEmpty())
					label = "label undefined";
				String id = ext.getUniqueIdentifier();
				if (id == null)
					id = "id undefined";
				return label + " (" + id + ")";
			}
		});

		table.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				StringBuffer str = new StringBuffer();

				IStructuredSelection sel = (IStructuredSelection) table.getSelection();
				IExtension ext = (IExtension) sel.getFirstElement();

				IConfigurationElement[] elements = ext.getConfigurationElements();
				for (IConfigurationElement e : elements) {
					String[] attributeNames = e.getAttributeNames();
					for (String att : attributeNames) {
						str.append(att + ": " + e.getAttribute(att) + "\n");

						System.out.println("Att: " + att + "\t Content: " + e.getAttribute(att));
					}
					if (combo.getItem(combo.getSelectionIndex()).endsWith("textext")) {
						Teste o;
						try {
							o = (Teste) e.createExecutableExtension("class");

							System.out.println(o.getHelloWorld());
						} catch (CoreException e1) {
							System.err.println(e1.getMessage());
						}
					}
				}

				MessageDialog.openInformation(null, "Attributes", str.toString());
			}
		});

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IExtensionPoint extPoint = extRegistry.getExtensionPoint(combo.getText());
				IExtension[] extensions = extPoint.getExtensions();
				table.setInput(extensions);
			}
		});
	}

	private String[] filterExtensionPoints(IExtensionPoint[] extensionPoints) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < extensionPoints.length; i++) {
			String id = extensionPoints[i].getUniqueIdentifier();
			if (id.startsWith("pt.iscte") || id.startsWith("pa.iscde"))
				list.add(id);
		}

		return list.toArray(new String[list.size()]);
	}
}
