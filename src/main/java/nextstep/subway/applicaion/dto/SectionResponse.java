package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SectionResponse {
	private StationResponse upStation;
	private StationResponse downStation;
	private Long distance;

	public SectionResponse(StationResponse upStation, StationResponse downStation, Long distance) {
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}
}
