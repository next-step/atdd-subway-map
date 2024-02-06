package subway.section.presentation.response;

import subway.station.service.StationDto;

import java.util.List;

public class SectionResponse {

    private Long id;
    private Integer distance;
    private List<StationDto> stations;

    public SectionResponse(Long id, Integer distance, List<StationDto> stationDtos) {
        this.id = id;
        this.distance = distance;
        this.stations = stationDtos;
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<StationDto> getStations() {
        return stations;
    }

}
