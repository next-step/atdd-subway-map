package subway.station.service.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StationRequest {
    private String name;

    public StationRequest(final String name) {
        this.name = name;
    }
}
