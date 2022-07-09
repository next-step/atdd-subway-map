package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private StationResponse upStation;
	private StationResponse downStation;
}
