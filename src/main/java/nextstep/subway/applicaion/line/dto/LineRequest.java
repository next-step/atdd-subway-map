package nextstep.subway.applicaion.line.dto;

import nextstep.subway.applicaion.line.domain.Line;

public class LineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Integer getDistance() {
		return distance;
	}

	public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public Line toLine() {
		return new Line(name, color);
	}
}
