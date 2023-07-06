package subway;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SubwayLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static SubwayLineResponse toResponse(final SubwayLine line) {
        return new SubwayLineResponse(line.getId(), line.getName(), line.getColor(),
                List.of(StationResponse.toResponse(line.getUpStation()), StationResponse.toResponse(line.getDownStation()))
        );
    }
}
