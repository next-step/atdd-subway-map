package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.StationLine;

public class StationLineRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	public StationLine toStationLine() {
		return new StationLine(this.name, this.color, this.upStationId, this.downStationId, this.distance);
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
