package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;

@Service
public class StationLineService {
	private static final String STATION_NAME = "지하철역";
	private static final String NEW_STATION_NAME = "새로운지하철역";
	private static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE = "Entity Not Found ";
	private StationLineRepository stationLineRepository;

	public StationLineService(StationLineRepository stationLineRepository) {
		this.stationLineRepository = stationLineRepository;
	}

	@Transactional
	public StationLineResponse createStationLines(StationLineRequest stationLineRequest) {
		StationLine stationLine = stationLineRepository.save(stationLineRequest.toStationLine());

		return createStationLineResponse(stationLine);
	}

	public List<StationLineResponse> findAllStationLines() {
		return stationLineRepository.findAll()
			.stream()
			.map(this::createStationLineResponse)
			.collect(Collectors.toList());
	}

	public StationLineResponse findStationLine(Long stationId) {
		return createStationLineResponse(stationLineRepository.findById(stationId)
			.orElseThrow(() -> new RuntimeException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE)));
	}

	@Transactional
	public void updateStationLine(Long stationId, StationLineRequest stationLineRequest) {
		StationLine stationLine = stationLineRepository.findById(stationId)
			.orElseThrow(() -> new RuntimeException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE));
		stationLine.updateStationLineInformation(stationLineRequest.getName(), stationLineRequest.getColor());
	}

	@Transactional
	public void deleteStationLine(Long stationId) {

		stationLineRepository.delete(stationLineRepository.findById(stationId)
			.orElseThrow(() -> new RuntimeException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE)));
	}

	private StationLineResponse createStationLineResponse(StationLine stationLine) {

		return new StationLineResponse(stationLine.getId(),
			stationLine.getStationName(),
			stationLine.getColor(),
			new StationResponse(stationLine.getUpStationId(), STATION_NAME),
			new StationResponse(stationLine.getDownStationId(), NEW_STATION_NAME));
	}
}
