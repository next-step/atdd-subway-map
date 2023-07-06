package subway;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class StationResponse {
    private Long id;
    private String name;

    public static StationResponse toResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
