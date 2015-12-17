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

	public SuperProgressBar(ContextMenuItem[] contextMenuItems) {
		super(new ContextMenuItem[] { ContextMenuItem.SET_COLOR, ContextMenuItem.PLUGIN });
	}

	@Override
	public String getWidgetName() {
		// TODO Auto-generated method stub
		return widgetName;
	}

	@Override
	public void setWidgetName(String widgetName) {
		// TODO Auto-generated method stub
		this.widgetName = widgetName;
	}

	@Override
	public void createWidget(Canvas canvas, Point location, Point size) {
		// TODO Auto-generated method stub
		this.location = location;
		this.size = size;
		
		 ProgressBar pb = new ProgressBar(canvas, SWT.HORIZONTAL);
	    pb.setMinimum(0);
	    pb.setMaximum(100);
	    pb.setSelection(50);
	    pb.setBounds(10, 10, 200, 20);
	    pb.setSize(size);
	    pb.setLocation(location);
	    pb.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	    pb.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));

	    this.control = pb;
	    this.backgroundColor = pb.getBackground();
		this.foregroundColor = pb.getForeground();
		this.enabled = pb.isEnabled();
	}

	@Override
	public ArrayList<String> generateWidgetCode(CodeTarget target, String containerName, int count) {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

}
