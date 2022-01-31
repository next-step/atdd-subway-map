package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;

public class LineReadResponse extends BaseLineResponse {

    private final List<StationResponse> stations;

    public LineReadResponse(
            Long id,
            String name,
            String color,
            List<StationResponse> stations,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        super(id, name, color, createdDate, modifiedDate);
        this.stations = stations;
    }

    public static LineReadResponse of(Line line) {
        return new LineReadResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections().stream()
                        .map(
                                section -> {
                                    return StationResponse.of(section.getDownStation());
                                })
                        .collect(Collectors.toList()),
                line.getCreatedDate(),
                line.getModifiedDate());
    }
}
