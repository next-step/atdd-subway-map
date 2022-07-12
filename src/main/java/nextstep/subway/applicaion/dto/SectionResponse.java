package nextstep.subway.applicaion.dto;

public class SectionResponse {
	private long selectionId;
	private long upStationId;
	private long downStationId;
	private long distance;

	public SectionResponse(long selectionId, long upStationId, long downStationId, long distance) {
		this.selectionId = selectionId;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public long getSelectionId() {
		return selectionId;
	}

	public long getUpStationId() {
		return upStationId;
	}

	public long getDownStationId() {
		return downStationId;
	}

	public long getDistance() {
		return distance;
	}
}
