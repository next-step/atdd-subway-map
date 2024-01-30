package subway.dto;

public class SectionRequest {
	private Long lineId;

	private Long downStationId;

	private Long upStationId;

	private int distance;

	public SectionRequest() {
	}

	public SectionRequest(Long lineId, Long downStationId, Long upStationId, int distance) {
		this.lineId = lineId;
		this.downStationId = downStationId;
		this.upStationId = upStationId;
		this.distance = distance;
	}

	public Long getLineId() {
		return lineId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public int getDistance() {
		return distance;
	}
}
