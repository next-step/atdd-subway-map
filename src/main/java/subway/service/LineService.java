package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.repository.LineRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.Line;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
	private final LineRepository lineRepository;
	private final StationService stationService;

	public LineService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getStartStationId(), lineRequest.getEndStationId(), lineRequest.getDistance());

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
		lineRepository.deleteById(id);
	}

	private LineResponse createLineResponse(Line line) {
		return new LineResponse(line,
				List.of(stationService.findStationById(line.getStartStationId()), stationService.findStationById(line.getEndStationId())));
	}
}
