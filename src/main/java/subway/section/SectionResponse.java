package subway.section;

import subway.line.LineResponse;
import subway.station.StationResponse;

import java.util.List;

public class SectionResponse {
    private Long id;
    private int distance;
    private LineResponse line;
    private List<StationResponse> stations;

    private SectionResponse(Long id, int distance, LineResponse line, List<StationResponse> stations) {
        this.id = id;
        this.distance = distance;
        this.line = line;
        this.stations = stations;
    }

    public static SectionResponse toResponse(Section section, List<StationResponse> stations) {
        return new SectionResponse(
                section.getId(),
                section.getDistance(),
                LineResponse.toResponse(section.getLine(), null),
                stations);
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public LineResponse getLine() {
        return line;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
