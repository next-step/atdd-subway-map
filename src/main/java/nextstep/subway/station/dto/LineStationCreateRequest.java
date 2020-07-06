package nextstep.subway.station.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LineStationCreateRequest {

	private final Long stationId;
	private final Long nextStationId;
	private Long lineId;
	private final int distance;
	private final int duration;

	public LineStationCreateRequest(Long stationId, Long nextStationId, int distance, int duration) {
		this.stationId = stationId;
		this.nextStationId = nextStationId;
		this.distance = distance;
		this.duration = duration;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
}
