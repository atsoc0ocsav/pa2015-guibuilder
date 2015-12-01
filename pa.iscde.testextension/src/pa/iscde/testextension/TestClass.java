package pa.iscde.testextension;

import pa.iscde.test.ExtensionTestInterface;

public class TestClass implements ExtensionTestInterface {
	public TestClass() {

	}

	@Override
	public String getHelloWorld() {
		return "Hello World from TestClass";
	}
}
