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
	public String getWidgetName() {
		// TODO Auto-generated method stub
		return "teste concluido";
	}
}
