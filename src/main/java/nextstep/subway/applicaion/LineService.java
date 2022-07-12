package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
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
	public StationLineResponse createStationLines(StationLineRequest stationLineRequest) {
		Line line = lineRepository.save(stationLineRequest.toLine());
		return createStationLineResponse(line);
	}

	public List<StationLineResponse> findAllStationLines() {
		return lineRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
			.stream()
			.map(this::createStationLineResponse)
			.collect(Collectors.toList());
	}

	public StationLineResponse findStationLine(Long stationId) {

		return createStationLineResponse(lineRepository.findById(stationId)
			.orElseThrow(IllegalArgumentException::new));
	}

	@Transactional
	public void updateStationLine(Long stationId, StationLineRequest stationLineRequest) {
		Line line = lineRepository.findById(stationId)
			.orElseThrow(() -> new IllegalArgumentException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE));
		line.updateStationLineInformation(stationLineRequest.getName(), stationLineRequest.getColor());
	}

	@Transactional
	public void deleteStationLine(Long stationId) {
		lineRepository.delete(lineRepository.findById(stationId)
			.orElseThrow(() -> new IllegalArgumentException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE)));
	}

	private StationLineResponse createStationLineResponse(Line line) {
		return new StationLineResponse(line.getId(),
			line.getName(),
			line.getColor(),
			new StationResponse(line.getUpStationId(), STATION_NAME),
			new StationResponse(line.getDownStationId(), NEW_STATION_NAME));
	}

}
