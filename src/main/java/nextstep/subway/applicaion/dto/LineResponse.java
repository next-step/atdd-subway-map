package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Station;

@Builder
@Getter
public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private Station upStation;
	private Station downStation;
}
