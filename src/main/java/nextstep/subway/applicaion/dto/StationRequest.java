package nextstep.subway.applicaion.dto;

public class StationRequest {
    public static StationRequest from(String name) {
        return new StationRequest(name);
    }
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
