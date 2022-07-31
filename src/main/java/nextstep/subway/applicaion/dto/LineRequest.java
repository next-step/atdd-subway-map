package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Line;

@Getter
public class LineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public Line toLineEntity() {
		return new Line(name, color, upStationId, downStationId, distance);
	}
}
