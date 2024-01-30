package subway.line;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.station.Station;
import subway.station.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {
	private final StationRepository stationRepository;
	private final LineRepository lineRepository;
	private final LineStationMapRepository lineStationMapRepository;

	public LineService(
		StationRepository stationRepository,
		LineRepository lineRepository,
		LineStationMapRepository lineStationMapRepository
	) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
		this.lineStationMapRepository = lineStationMapRepository;
	}

	public List<LineResponse> lines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(line ->
				LineResponse.of(
					line,
					line.getLineStationMaps()
						.stream()
						.map(LineStationMap::getStation)
						.collect(toList())
				)
			).collect(toList());
	}

	@Transactional
	public LineResponse save(LineCreateRequest request) {
		Line line = request.toEntity();
		Line savedLine = lineRepository.save(line);
		List<Station> stations = createLineStationMap(savedLine, request.getUpStationId(), request.getDownStationId());
		return LineResponse.of(savedLine, stations);
	}

	private List<Station> createLineStationMap(Line line, Long upStationId, Long downStationId) {
		LineStationMap upLane = saveLineStationMap(line, upStationId, 0L);
		LineStationMap downLane = saveLineStationMap(line, downStationId, upLane.getStation().getId());
		return List.of(upLane.getStation(), downLane.getStation());
	}

	private LineStationMap saveLineStationMap(Line line, Long stationId, Long upperId) {
		Station station = stationRepository.findById(stationId)
			.orElseThrow(EntityNotFoundException::new);
		LineStationMap lineStationMap = new LineStationMap(line, station, upperId);
		return lineStationMapRepository.save(lineStationMap);
	}

	public LineResponse line(Long id) {
		Line line = findLineById(id);
		List<LineStationMap> lineStationMap = lineStationMapRepository.findAllByLineId(line.getId());
		List<Station> stations = lineStationMap.stream().map(LineStationMap::getStation).collect(toList());
		return LineResponse.of(line, stations);
	}

	@Transactional
	public void update(Long id, LineUpdateRequest request) {
		Line line = findLineById(id);
		line.changeName(request.getName());
		line.changeColor(request.getColor());
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
	}

	@Transactional
	public void delete(Long id) {
		Line line = findLineById(id);
		List<LineStationMap> lineStationMaps = lineStationMapRepository.findAllByLineId(line.getId());
		List<Station> stations = lineStationMaps.stream().map(LineStationMap::getStation).collect(toList());

		lineStationMapRepository.deleteAll(lineStationMaps);
		lineRepository.delete(line);
		stationRepository.deleteAll(stations);
	}
}
