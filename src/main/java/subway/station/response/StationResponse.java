package subway.station.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StationResponse {
    private Long id;
    private String name;

    @Builder
    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
