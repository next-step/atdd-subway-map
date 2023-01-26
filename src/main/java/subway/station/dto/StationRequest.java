package subway.station.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationRequest {
    private String name;

    public StationRequest from(String name) {
        return new StationRequest(name);
    }

}
