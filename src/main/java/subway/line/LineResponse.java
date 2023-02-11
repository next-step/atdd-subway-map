package subway.line;

import lombok.*;
import subway.*;
import subway.line.section.*;

import java.util.*;
import java.util.stream.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse from(final Line line) {

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections()
                        .getValues()
                        .stream()
                        .flatMap(LineResponse::getStationResponse)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    private static Stream<StationResponse> getStationResponse(final Section section) {
        return Stream.of(
                new StationResponse(
                        section.getUpStation().getId(),
                        section.getUpStation().getName()
                ),
                new StationResponse(
                        section.getDownStation().getId(),
                        section.getDownStation().getName()
                )
        );
    }
}
