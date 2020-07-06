package nextstep.subway.station.dto;

import lombok.Getter;

@Getter
public class LineStationCreateRequest {

	private final Long stationId;
	private final Long preStationId;
	private Long lineId;
	private final int distance;
	private final int duration;

	public LineStationCreateRequest(Long stationId, Long preStationId, int distance, int duration) {
		this.stationId = stationId;
		this.preStationId = preStationId;
		this.distance = distance;
		this.duration = duration;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
}
