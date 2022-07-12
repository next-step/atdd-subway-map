package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Selection;

public class SelectionRequest {
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	public Selection toSelection(long lineId) {
		return new Selection(lineId, this.upStationId, this.downStationId, this.distance);
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
