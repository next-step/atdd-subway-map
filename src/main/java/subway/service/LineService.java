package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.SectionRequest;
import subway.entity.Section;
import subway.repository.LineRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.Line;
import subway.repository.SectionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
	private final LineRepository lineRepository;
	private final StationService stationService;
	private final SectionRepository sectionRepository;

	public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getStartStationId(), lineRequest.getEndStationId(), lineRequest.getDistance());
		Section section = new Section(line, lineRequest.getStartStationId(), line.getEndStationId(), lineRequest.getDistance());
		sectionRepository.save(section);

		return createLineResponse(lineRepository.save(line));
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
				.map(this::createLineResponse).collect(Collectors.toList());
	}

	public LineResponse findLineById(Long id) {
		return lineRepository.findById(id)
				.map(this::createLineResponse)
				.orElseThrow(EntityNotFoundException::new);
	}

	@Transactional
	public void updateLine(Long id, LineRequest lineRequest) {
		Line line = lineRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		line.setUpdateInfo(lineRequest.getName(), lineRequest.getColor());

		lineRepository.save(line);
	}

	@Transactional
	public void deleteLine(Long id) {
		sectionRepository.deleteByLine_Id(id);
		lineRepository.deleteById(id);
	}

	@Transactional
	public void createSection(Long id, SectionRequest sectionRequest) {
		Line line = lineRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		if(!line.getEndStationId().equals(sectionRequest.getUpStationId())) {
			throw new IllegalArgumentException("노선의 하행 종점역과 구간의 상행역은 같아야 합니다.");
		}

		sectionRepository.findByLine(line).forEach(x -> {
			if (sectionRequest.getDownStationId().equals(x.getUpStationId()) || sectionRequest.getDownStationId().equals(x.getDownStationId())) {
				throw new IllegalArgumentException("해당 노선에 " + sectionRequest.getDownStationId() + "역이 이미 존재합니다.");
			}
		});

		line.addSection(sectionRequest.getDownStationId(), sectionRequest.getDistance());
		lineRepository.save(line);

		Section section = new Section(line, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
		sectionRepository.save(section);
	}

	private LineResponse createLineResponse(Line line) {
		return new LineResponse(line,
				List.of(stationService.findStationById(line.getStartStationId()), stationService.findStationById(line.getEndStationId())));
	}
}
