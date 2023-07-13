package subway.station.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(staticName = "from")
@NoArgsConstructor
public class StationRequest {
    private String name;
}
