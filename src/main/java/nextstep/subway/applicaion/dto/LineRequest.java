package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class LineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	public Line toLine() {
		return new Line(this.name, this.color, this.upStationId, this.downStationId, this.distance);
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

}
