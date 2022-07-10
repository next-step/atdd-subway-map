package nextstep.subway.applicaion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;

@Service
public class StationLineService {
	private static final String STATION_NAME = "지하철역";
	private static final String NEW_STATION_NAME = "새로운지하철역";
	private StationRepository stationRepository;
	private StationLineRepository stationLineRepository;

	public StationLineService(StationRepository stationRepository, StationLineRepository stationLineRepository) {
		this.stationRepository = stationRepository;
		this.stationLineRepository = stationLineRepository;
	}

	@Transactional
	public StationLineResponse createStationLines(StationLineRequest stationLineRequest) {
		StationLine stationLine = stationLineRepository.save(stationLineRequest.toStationLine());

		return createStationLineResponse(stationLine);
	}

	private StationLineResponse createStationLineResponse(StationLine stationLine) {

		return new StationLineResponse(stationLine.getId(),
			stationLine.getStationName(),
			stationLine.getColor(),
			new StationResponse(stationLine.getUpStationId(), STATION_NAME),
			new StationResponse(stationLine.getDownStationId(), NEW_STATION_NAME));
	}
}
