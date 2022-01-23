package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationQueryService {
	private final StationRepository stationRepository;

	public StationQueryService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public List<StationResponse> findAllStations() {
		return StationResponse.fromList(stationRepository.findAll());
	}

}
