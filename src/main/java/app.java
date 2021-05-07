import injector.Injector;

public class app {

    public static void main(String[] args) {
        var injector = new Injector("inject.properties");
        var testClass = new TestClass();
        injector.inject(testClass).testField.doSome();
    }

}
