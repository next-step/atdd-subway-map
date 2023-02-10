package subway.dto.request;

import lombok.Getter;

@Getter
public class SectionDeleteRequest {

    public SectionDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    private Long stationId;
}
