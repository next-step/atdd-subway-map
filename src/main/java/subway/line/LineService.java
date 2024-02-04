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
	private final SectionRepository sectionRepository;

	public LineService(
		StationRepository stationRepository,
		LineRepository lineRepository,
		SectionRepository sectionRepository
	) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
	}

	private Station findStationById(Long stationId) {
		return stationRepository.findById(stationId)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 정류장입니다."));
	}

	public List<LineResponse> lines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(LineResponse::of)
			.collect(toList());
	}

	@Transactional
	public LineResponse save(LineCreateRequest request) {
		Line line = request.toEntity();
		Line savedLine = lineRepository.save(line);

		Station upStation = findStationById(request.getUpStationId());
		Station downStation = findStationById(request.getDownStationId());

		savedLine.addSection(upStation, downStation, request.getDistance());

		return LineResponse.of(savedLine);
	}

	public LineResponse line(Long lineId) {
		Line line = findLineById(lineId);
		return LineResponse.of(line);
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
		List<Section> sections = sectionRepository.findAllByLineId(line.getId());
		List<Station> stations = line.getSortedStations();

		sectionRepository.deleteAll(sections);
		lineRepository.delete(line);
		stationRepository.deleteAll(stations);
	}

	@Transactional
	public LineResponse addSection(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);

		Station upStation = findStationById(request.getUpStationId());
		Station downStation = findStationById(request.getDownStationId());
		line.addSection(upStation, downStation, request.getDistance());

		return LineResponse.of(line);
	}

	@Transactional
	public void deleteSection(Long lineId, Long deleteTargetStationId) {
		Line line = findLineById(lineId);
		Station station = findStationById(deleteTargetStationId);
		line.removeStation(station);
	}
}
