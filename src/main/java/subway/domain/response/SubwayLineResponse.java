package subway.domain.response;

import org.springframework.beans.BeanUtils;
import subway.domain.entity.Station;
import subway.domain.entity.SubwayLine;

import java.util.List;

public class SubwayLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public SubwayLineResponse() {
    }

    public SubwayLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static SubwayLineResponse createResponseByEntity(SubwayLine subwayLine) {
        SubwayLineResponse response = new SubwayLineResponse();
        BeanUtils.copyProperties(subwayLine, response);

        Station upStation = subwayLine.getUpStation();
        Station downStation = subwayLine.getDownStation();
        response.setStations(List.of(StationResponse.createStationResponseByEntity(upStation), StationResponse.createStationResponseByEntity(downStation)));

        return response;
    }


}
