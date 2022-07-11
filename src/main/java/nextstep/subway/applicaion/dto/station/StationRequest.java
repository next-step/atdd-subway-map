package nextstep.subway.applicaion.dto.station;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }
}
