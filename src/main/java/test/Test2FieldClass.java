package test;

import injector.AutoInjectable;

public class Test2FieldClass implements ITestFieldClass {

    @AutoInjectable
    public Test1FieldClass test1;

    @Override
    public void doSome() {
        System.out.println("C");
    }

}

