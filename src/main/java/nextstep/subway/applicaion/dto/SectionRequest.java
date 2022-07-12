package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SectionRequest {
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	public Section toSelection(long lineId) {
		return new Section(lineId, this.upStationId, this.downStationId, this.distance);
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public Long getDistance() {
		return distance;
	}
}
