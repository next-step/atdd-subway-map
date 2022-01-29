package nextstep.subway.application.service;

import nextstep.subway.application.dto.request.StationRequest;
import nextstep.subway.application.dto.response.StationResponse;
import nextstep.subway.application.exception.UniqueKeyExistsException;
import nextstep.subway.application.repository.StationRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService {
	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public StationResponse saveStation(StationRequest requestStation) {
		if (isStationExists(requestStation)) {
			throw new UniqueKeyExistsException(requestStation.getName());
		}

		final Station station = stationRepository.save(requestStation.toEntity());
		return StationResponse.from(station);
	}

	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

	private boolean isStationExists(StationRequest station) {
		return stationRepository.findByName(station.getName()).isPresent();
	}
}
