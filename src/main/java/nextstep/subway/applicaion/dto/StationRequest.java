package nextstep.subway.applicaion.dto;

public class StationRequest {
    private String name;

    public StationRequest() {
    }

    private StationRequest(String name) {
        this.name = name;
    }

    public static StationRequest from(String name) {
        return new StationRequest(name);
    }

    public String getName() {
        return name;
    }
}
