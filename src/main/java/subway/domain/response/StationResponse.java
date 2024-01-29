package subway.domain.response;

import org.springframework.beans.BeanUtils;
import subway.domain.entity.Station;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse() {
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static StationResponse createStationResponseByEntity(Station station) {
        StationResponse response = new StationResponse();
        BeanUtils.copyProperties(station, response);

        return response;
    }
}
