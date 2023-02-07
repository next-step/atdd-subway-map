package subway.station.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationFindAllResponse {
    private Long id;
    private String name;

    @Builder
    public StationFindAllResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
