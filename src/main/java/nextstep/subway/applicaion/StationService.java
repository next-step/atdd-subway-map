package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.ui.exception.UniqueKeyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService {
	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional
	public StationResponse saveStation(StationRequest requestStation) {
		if (isStationExists(requestStation)) {
			throw new UniqueKeyExistsException(requestStation.getName());
		}

		final Station station = stationRepository.save(requestStation.toEntity());
		return StationResponse.from(station);
	}

	@Transactional
	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

	private boolean isStationExists(StationRequest station) {
		return stationRepository.findByName(station.getName()).isPresent();
	}
}
