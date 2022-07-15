package nextstep.subway.applicaion.dto;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    @Min(value = 1, message = "1~30 사이의 값을 입력해주세요.")
    @Max(value = 30 , message = "distance는 30보다 클 수 없습니다.")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
