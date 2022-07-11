package nextstep.subway.applicaion.dto;

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
