package injector;

public class Inject {

    private String initialization;
    private String initiator;

    public Inject(String line) {
        int index = line.indexOf('=');
        if (index == -1)
            throw new RuntimeException("В строке " + line + " отсутвует =");
        initialization = line.substring(0, index);
        initiator = line.substring(index + 1);
    }

    public String getInitialization() {
        return initialization;
    }

    public String getInitiator() {
        return initiator;
    }
}
