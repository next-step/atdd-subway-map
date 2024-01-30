package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
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
		Line line = findLindById(id);

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
		Line line = findLindById(id);

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

	@Transactional
	public void deleteSection(Long id, Long stationId) {
		Line line = findLindById(id);

		Section section = sectionRepository.findByLineAndDownStationId(line, stationId);

		if(!stationId.equals(line.getEndStationId())) {
			throw new IllegalArgumentException("노선의 하행 종점역만 제거할 수 있습니다.");
		}

		if(line.getStartStationId().equals(section.getUpStationId())) {
			throw new IllegalArgumentException("상행 종점역과 하행 종점역만 있는 노선입니다.");
		}

		sectionRepository.delete(section);

		line.removeSection(section.getUpStationId(), section.getDistance());
		lineRepository.save(line);
	}

	public List<SectionResponse> findSectionsByLine(Long id) {
		Line line = findLindById(id);

		return sectionRepository.findByLine(line).stream()
				.map(this::createSectionResponse).collect(Collectors.toList());
	}

	private Line findLindById(Long id) {
		return lineRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
	}

	private LineResponse createLineResponse(Line line) {
		return new LineResponse(line,
				List.of(stationService.findStationById(line.getStartStationId()), stationService.findStationById(line.getEndStationId())));
	}

	private SectionResponse createSectionResponse(Section section) {
		return new SectionResponse(section);
	}
}
