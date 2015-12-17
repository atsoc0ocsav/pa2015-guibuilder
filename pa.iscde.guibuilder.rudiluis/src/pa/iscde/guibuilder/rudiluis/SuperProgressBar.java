package pa.iscde.guibuilder.rudiluis;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ProgressBar;

import pa.iscde.guibuilder.codeGenerator.CodeGenerator.CodeTarget;
import pa.iscde.guibuilder.extensions.WidgetInComposite;

public class SuperProgressBar extends WidgetInComposite {
	
	private String widgetName = "Super Progress";

	public SuperProgressBar() {
		super(new ContextMenuItem[] { ContextMenuItem.SET_COLOR, ContextMenuItem.PLUGIN });
	}

	@Override
	public String getWidgetName() {
		return widgetName;
	}

	@Override
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	@Override
	public void createWidget(Canvas canvas, Point location, Point size) {
		this.location = location;
		this.size = size;
		
		control = new ProgressBar(canvas, SWT.HORIZONTAL);
		((ProgressBar) control).setMinimum(0);
		((ProgressBar) control).setMaximum(100);
		((ProgressBar) control).setSelection(50);
		((ProgressBar) control).setBounds(10, 10, 200, 20);
		((ProgressBar) control).setSize(size);
		((ProgressBar) control).setLocation(location);
		((ProgressBar) control).setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		((ProgressBar) control).setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));

	    this.backgroundColor = control.getBackground();
		this.foregroundColor = control.getForeground();
		this.enabled = control.isEnabled();
	}

	@Override
	public ArrayList<String> generateWidgetCode(CodeTarget target, String containerName, int count) {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

}
