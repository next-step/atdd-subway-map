package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<StationResponse> stations;

    private LineResponse(Long id, String name, String color, int distance, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getDistance(),
                line.getStations().stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineResponse that = (LineResponse) o;
        return getDistance() == that.getDistance() && Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getColor(), that.getColor()) && Objects.equals(getStations(), that.getStations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getDistance(), getStations());
    }
}
