package nextstep.subway.applicaion;

import java.util.ArrayList;
import java.util.List;

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
		List<StationLineResponse> stationLineResponseList = new ArrayList<>();
		List<StationLine> stationLines = stationLineRepository.findAll();
		stationLines.forEach(stationLine ->
			stationLineResponseList.add(createStationLineResponse(stationLine)));
		return stationLineResponseList;
	}

	public StationLineResponse findStationLine(Long stationId) {

		StationLine stationLine = stationLineRepository.findById(stationId)
			.orElseThrow(() -> new RuntimeException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE));

		return createStationLineResponse(stationLine);
	}

	@Transactional
	public void updateStationLine(Long stationId, StationLineRequest stationLineRequest) {
		StationLine stationLine = stationLineRepository.findById(stationId)
			.orElseThrow(() -> new RuntimeException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE));
		stationLine.updateStationLineInformation(stationLineRequest.getName(), stationLineRequest.getColor());
	}

	private StationLineResponse createStationLineResponse(StationLine stationLine) {

		return new StationLineResponse(stationLine.getId(),
			stationLine.getStationName(),
			stationLine.getColor(),
			new StationResponse(stationLine.getUpStationId(), STATION_NAME),
			new StationResponse(stationLine.getDownStationId(), NEW_STATION_NAME));
	}
}
