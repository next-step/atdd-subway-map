package nextstep.subway.line.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.LineStationCreateRequest;

@Service
public class LineStationService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineStationService(LineRepository lineRepository,
		StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	private Line findLineById(Long lineId) {
		return lineRepository.findById(lineId).orElseThrow(() -> new IllegalArgumentException("no line found."));
	}

	private Station findStationById(Long stationId) {
		return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("no station found."));
	}

	public void registerLineStation(LineStationCreateRequest dto) {
		Line line = findLineById(dto.getLineId());
		Station station = findStationById(dto.getStationId());
		LineStation lineStation = LineStation.builder()
			.line(line)
			.station(station)
			.distance(dto.getDistance())
			.duration(dto.getDuration())
			.build();
		line.addLineStation(lineStation);
	}
}
