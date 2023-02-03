package subway.station.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

    @Builder
    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
