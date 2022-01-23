package nextstep.subway.applicaion.dto;

public class LineSectionRequest {

	private Long upStationId;
	private Long downStationId;
	private int distance;

	public LineSectionRequest(
					Long upStationId,
					Long downStationId,
					int distance
	) {
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

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
