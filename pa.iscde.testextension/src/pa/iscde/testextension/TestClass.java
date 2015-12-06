package pa.iscde.testextension;

import pa.iscde.test.ExtensionTestInterface;
import pt.iscte.pidesco.guibuilder.extensions.WidgetInterface;

public class TestClass implements ExtensionTestInterface,WidgetInterface{
	public TestClass() {

	}

	@Override
	public String getHelloWorld() {
		return "Hello World from TestClass";
	}

	@Override
	public String[] getWidgetNames() {
		// TODO Auto-generated method stub
		return new String[]{"widget1","widget2","widget3"};
	}
}
