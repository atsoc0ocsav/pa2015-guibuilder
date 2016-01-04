package pa.iscde.guibuilder.atsoc0ocsav.packWindow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.ContextMenuElement;
import pa.iscde.guibuilder.extensions.ContextMenuElementCodeGenerator;
import pa.iscde.guibuilder.model.ObjectInCompositeContainer;
import pa.iscde.guibuilder.ui.GuiBuilderView;

public class PackWindowContextMenuItem implements ContextMenuElement, ContextMenuElementCodeGenerator {
	private final OBJECT_FAMILY[] accepts = { OBJECT_FAMILY.CANVAS };
	private final String COMMAND_MESSAGE = "Window is now %spacked!";
	private final String MENU_ITEM_TEXT = "%sack window";

	private boolean pack = false;

	@Override
	public boolean acceptsType(OBJECT_FAMILY o) {
		for (OBJECT_FAMILY object : accepts) {
			if (object.equals(o)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void generateMenuItem(Menu menu, final ObjectInCompositeContainer obj, final GuiBuilderView guiBuilderView) {
		MenuItem deleteItem = new MenuItem(menu, SWT.NONE);
		deleteItem.setText(String.format(MENU_ITEM_TEXT, pack ? "Unp" : "P"));
		deleteItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pack = !pack;
				guiBuilderView.setMessage(COMMAND_MESSAGE, pack ? "" : "un");
			}
		});
	}

	@Override
	public List<String> generateCodeForObject(CodeTarget target, ObjectInCompositeContainer object,
			String containerName, String objectName) {
		return null;
	}

	@Override
	public List<String> generateCommonCodeBegin(CodeTarget target, String containerName) {
		return null;
	}

	@Override
	public List<String> generateCommonCodeEnd(CodeTarget target, String containerName) {
		List<String> code = new ArrayList<String>();
		if (pack) {
			switch (target) {
			case SWING:
				code.add(containerName + ".pack();");
				break;
			case SWT:
				code.add(containerName + ".pack();");
				break;
			default:
				throw new IllegalArgumentException("Switch case not defined!");
			}
		}

		return code;
	}

}
