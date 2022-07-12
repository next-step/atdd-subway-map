package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;

@Service
public class LineService {
	private static final String STATION_NAME = "지하철역";
	private static final String NEW_STATION_NAME = "새로운지하철역";
	private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Entity Not Found ";
	private LineRepository lineRepository;

	public LineService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	@Transactional
	public LineResponse createLines(LineRequest LineRequest) {
		Line line = lineRepository.save(LineRequest.toLine());
		return createLineResponse(line);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
			.stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	public LineResponse findLine(Long stationId) {

		return createLineResponse(lineRepository.findById(stationId)
			.orElseThrow(IllegalArgumentException::new));
	}

	@Transactional
	public void updateLine(Long stationId, LineRequest LineRequest) {
		Line line = lineRepository.findById(stationId)
			.orElseThrow(() -> new IllegalArgumentException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE));
		line.updateStationLineInformation(LineRequest.getName(), LineRequest.getColor());
	}

	@Transactional
	public void deleteLine(Long stationId) {
		lineRepository.delete(lineRepository.findById(stationId)
			.orElseThrow(() -> new IllegalArgumentException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE)));
	}

	private LineResponse createLineResponse(Line line) {
		return new LineResponse(line.getId(),
			line.getName(),
			line.getColor(),
			new StationResponse(line.getUpStationId(), STATION_NAME),
			new StationResponse(line.getDownStationId(), NEW_STATION_NAME));
	}

}
