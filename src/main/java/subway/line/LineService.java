package subway.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.Station;
import subway.StationRepository;

@Service
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public LineResponse saveLine(LineCreateRequest lineRequest) {
		Station upStation = stationRepository.findById(lineRequest.getUpStationsId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));
		Station downStation = stationRepository.findById(lineRequest.getDownStationsId()).orElseThrow(() -> new NullPointerException("Station doesn't exist"));

		Line saveLine = lineRepository.save(new Line(
			lineRequest.getName(), lineRequest.getColor(),
			upStation, downStation, lineRequest.getDistance()));

		return createLineResponse(saveLine);
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
