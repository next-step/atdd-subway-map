package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.repository.LineRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.StationRepository;
import subway.entity.Line;
import subway.entity.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());

		return createLineResponse(lineRepository.save(line));
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
				.map(this::createLineResponse).collect(Collectors.toList());
	}
	private LineResponse createLineResponse(Line line) {
		Station upStation = stationRepository.findById(line.getUpStationId())
				.orElseThrow(IllegalAccessError::new);
		Station downStation = stationRepository.findById(line.getDownStationId())
				.orElseThrow(IllegalAccessError::new);

		return new LineResponse(line, List.of(upStation, downStation));
	}
}
