package nextstep.subway.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor
@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public static LineResponse of(Line line, List<Station> stations) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stations)
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .build();
    }

    public static LineResponse of(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.stations())
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .build();
    }

    public static List<LineResponse> lineResponses(List<Line> lines) {
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }
}
