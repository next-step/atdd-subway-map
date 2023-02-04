package subway.web.response;

import subway.domain.LineLoadDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LineLoadDtoResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineLoadDtoResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineLoadDtoResponse from(LineLoadDto lineLoadDto) {
        List<StationResponse> stationResponses = lineLoadDto.getStations().stream().map(StationResponse::from).collect(Collectors.toList());
        return new LineLoadDtoResponse(lineLoadDto.getId(), lineLoadDto.getName(), lineLoadDto.getColor(), stationResponses);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineLoadDtoResponse that = (LineLoadDtoResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
