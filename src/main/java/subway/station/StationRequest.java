package subway.station;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class StationRequest {
    private String name;

    public StationRequest(final String name) {
        this.name = name;
    }
}
