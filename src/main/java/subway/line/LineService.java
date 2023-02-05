package subway.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.section.Section;
import subway.section.SectionCreateRequest;
import subway.section.SectionService;

@Service
public class LineService {
	private SectionService sectionService;

	private LineRepository lineRepository;

	public LineService(SectionService sectionService, LineRepository lineRepository) {
		this.sectionService = sectionService;
		this.lineRepository = lineRepository;
	}

	@Transactional
	public LineResponse saveLine(LineCreateRequest lineRequest) {
		Line saveLine = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));

		Section newSection = sectionService.saveSection(new SectionCreateRequest(lineRequest.getDownStationsId(), lineRequest.getUpStationsId(), lineRequest.getDistance()));
		saveLine.addSection(newSection);

		LineResponse lineResponse = createLineResponse(saveLine);
		return lineResponse;
	}

	@Transactional(readOnly = true)
	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public LineResponse findLineById(Long id) {
		return createLineResponse(lineRepository.findById(id).orElseThrow(() -> new NullPointerException("Line doesn't exist")));
	}

	@Transactional
	public void updateLineById(Long id, LineUpdateRequest lineUpdateRequest) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new NullPointerException("Line doesn't exist"));
		line.updateLine(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	private LineResponse createLineResponse(Line line) {
		return new LineResponse(line);
	}
}
