import injector.Injector;
import test.Test2Class;
import test.TestClass;

public class app {

    public static void main(String[] args) {
        var injector = new Injector("inject.properties");
        var injector2 = new Injector("second_inject.properties");
        var testClass = new TestClass();
        var testClass2 = new TestClass();
        var test2Class = new Test2Class();
        injector.inject(testClass).testField.doSome();
        injector2.inject(testClass2).testField.doSome();
        injector2.inject(test2Class).test.doSome();
        test2Class.test.test1.doSome();
    }

}
