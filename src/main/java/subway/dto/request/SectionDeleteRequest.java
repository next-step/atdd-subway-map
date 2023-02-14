package subway.dto.request;

import lombok.Getter;

@Getter
public class SectionDeleteRequest {

    private final Long stationId;

    public SectionDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }
}
