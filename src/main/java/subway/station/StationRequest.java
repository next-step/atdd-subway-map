package subway.station;

import subway.common.Request;

public class StationRequest implements Request {
    private String name;

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
