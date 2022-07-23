package nextstep.subway.acceptance.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionRegistrationRequest {

    private long upStationId;

    private long downStationId;

    private int distance;

}
