package pa.iscde.testextension;

import pa.iscde.test.Teste;

public class TestClass implements Teste {
	public TestClass() {

	}

	@Override
	public String getHelloWorld() {
		return "Hello World from TestClass";
	}
}
