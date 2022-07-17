package nextstep.subway.section.application.dto;

public class AppendSectionRequest {

	private Long upStationId;
	private Long downStationId;
	private int distance;

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public int getDistance() {
		return distance;
	}
}
