package subway;

public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }

    protected StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }
}
