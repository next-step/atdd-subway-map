package subway.line;

import static java.util.stream.Collectors.*;

import java.util.Comparator;
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

	private Line findLineById(Long id) {
		return lineRepository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
	}

	private LineStationMap saveLineStationMap(Line line, Long stationId, Long upperId) {
		Station station = stationRepository.findById(stationId)
			.orElseThrow(EntityNotFoundException::new);
		LineStationMap lineStationMap = new LineStationMap(line, station, upperId);
		return lineStationMapRepository.save(lineStationMap);
	}

	private List<Station> extractStations(List<LineStationMap> lineStationMaps) {
		return lineStationMaps.stream()
			.sorted(lineStationMapComparator())
			.map(LineStationMap::getStation)
			.collect(toList());
	}

	private static Comparator<LineStationMap> lineStationMapComparator() {
		return (lineStationMap, nextLineStationMap) -> {
			final int sort = 1;
			if (lineStationMap.getId().equals(nextLineStationMap.getUpperStationId())) {
				return -sort;
			}
			return sort;
		};
	}

	private List<LineStationMap> findLineStationMaps(Long id) {
		return lineStationMapRepository.findAllByLineId(id);
	}

	private void checkFinalStation(Long upStationId, Long finalStationId) {
		if (!upStationId.equals(finalStationId)) {
			throw new IllegalArgumentException("해당 노선의 마지막 정류장이 아닙니다.");
		}
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

	public LineResponse line(Long id) {
		List<LineStationMap> lineStationMap = findLineStationMaps(id);

		Line line = lineStationMap.stream()
			.map(LineStationMap::getLine)
			.findFirst()
			.orElseThrow(EntityNotFoundException::new);

		List<Station> stations = extractStations(lineStationMap);

		return LineResponse.of(line, stations);
	}

	@Transactional
	public void update(Long id, LineUpdateRequest request) {
		Line line = findLineById(id);
		line.changeName(request.getName());
		line.changeColor(request.getColor());
	}

	@Transactional
	public void delete(Long id) {
		Line line = findLineById(id);
		List<LineStationMap> lineStationMaps = lineStationMapRepository.findAllByLineId(line.getId());
		List<Station> stations = extractStations(lineStationMaps);

		lineStationMapRepository.deleteAll(lineStationMaps);
		lineRepository.delete(line);
		stationRepository.deleteAll(stations);
	}

	@Transactional
	public LineResponse addSection(Long lineId, SectionRequest request) {
		checkExistsStationId(request);

		Line line = findLineById(lineId);
		checkExistingStation(line, request.getDownStationId());

		Long finalStationId = line.getFinalStation().getId();
		checkFinalStation(request.getUpStationId(), finalStationId);

		Line addedSectionToLine = addSectionToLine(line, request.getDownStationId(), finalStationId);
		List<Station> stations = extractStations(addedSectionToLine.getLineStationMaps());

		return LineResponse.of(addedSectionToLine, stations);
	}

	private Line addSectionToLine(Line line, Long downStationId, Long upperStationId) {
		LineStationMap lineStationMap = saveLineStationMap(line, downStationId, upperStationId);
		return line.addLineStationMap(lineStationMap);
	}

	private void checkExistsStationId(SectionRequest request) {
		checkExistsStationId(request.getUpStationId());
		checkExistsStationId(request.getDownStationId());
	}

	private void checkExistsStationId(Long stationId) {
		if(!stationRepository.existsById(stationId)) {
			throw new IllegalArgumentException("존재하지 않는 정류장입니다.");
		}
	}

	private void checkExistingStation(Line line, Long downStationId) {
		List<Long> stationIds = extractStations(line.getLineStationMaps())
			.stream()
			.map(Station::getId)
			.collect(toList());

		if (stationIds.contains(downStationId)) {
			throw new IllegalArgumentException("추가 할려는 정류장은 이미 해당 노선에 존재하는 정류장입니다.");
		}
	}

	@Transactional
	public void deleteSection(Long id, Long stationId) {
		List<LineStationMap> lineStationMaps = findLineStationMaps(id);
		checkOnlyTwoStation(lineStationMaps);

		LineStationMap deleteLineStationMap = lineStationMaps.stream()
			.filter(lineStationMap -> lineStationMap.getStation().getId().equals(stationId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("해당 노선에 포함되지 않는 정류장입니다."));

		checkFinalStation(lineStationMaps, stationId);

		lineStationMapRepository.delete(deleteLineStationMap);
	}

	private void checkOnlyTwoStation(List<LineStationMap> lineStationMaps) {
		if (lineStationMaps.size() <= 2) {
			throw new IllegalArgumentException("해당 노선은 두개의 정류장만 존재 하므로, 삭제할 수 없습니다.");
		}
	}

	private void checkFinalStation(List<LineStationMap> lineStationMaps, Long stationId) {
		Line line = lineStationMaps.stream()
			.map(LineStationMap::getLine)
			.findFirst()
			.orElseThrow();

		checkFinalStation(stationId, line.getFinalStation().getId());
	}
}
