package nextstep.subway.applicaion.dto;

public class SectionResponse {
	private long sectionId;
	private long upStationId;
	private long downStationId;
	private long distance;

	public SectionResponse(long sectionId, long upStationId, long downStationId, long distance) {
		this.sectionId = sectionId;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public long getSectionId() {
		return sectionId;
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
