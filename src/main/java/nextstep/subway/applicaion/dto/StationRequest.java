package nextstep.subway.applicaion.dto;

public class StationRequest {
    private String name;

    public StationRequest() {
    }

    public StationRequest(StationRequest stationRequest) {
        this(stationRequest.getName());
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
