package nextstep.subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.NotRegisteredException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.LineStation;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.LineStationCreateRequest;

@Service
@Transactional
public class LineStationService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineStationService(LineRepository lineRepository,
		StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	private Line findLineById(Long lineId) {
		return lineRepository.findById(lineId).orElseThrow(() -> new NotRegisteredException("no line found."));
	}

	private Station findStationById(Long stationId) {
		return stationRepository.findById(stationId).orElseThrow(() -> new NotRegisteredException("no station found."));
	}

	private LineStation findLineStationByLineIdAndStationId(Long lineId, Long lineStationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new NotRegisteredException("no line found."));
		return line.findLineStationByLineStationId(lineStationId);
	}

	public void registerLineStation(LineStationCreateRequest dto) {
		Line line = findLineById(dto.getLineId());
		Station station = findStationById(dto.getStationId());
		LineStation lineStation = LineStation.builder()
			.station(station)
			.preStationId(dto.getPreStationId())
			.distance(dto.getDistance())
			.duration(dto.getDuration())
			.build();
		line.addLineStation(lineStation);
	}

	public void unregisterLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		LineStation lineStation = findLineStationByLineIdAndStationId(lineId, stationId);
		line.unregisterLineStation(lineStation);
	}
}
