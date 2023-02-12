package subway.station.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.station.Station;

@Getter
@NoArgsConstructor
public class SectionResponse {
    private Long id;
    private Station upStation;
    private Station downStation;
    private Long distance;

    @Builder
    public SectionResponse(Long id, Station upStation, Station downStation, Long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }
}
