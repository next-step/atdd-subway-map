package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.ui.exception.UniqueKeyExistsException;

@Service
@Transactional(readOnly = true)
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

	public List<StationResponse> findAllStations() {
		return StationResponse.fromList(stationRepository.findAll());
	}

	@Transactional
	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

	private boolean isStationExists(StationRequest station) {
		return stationRepository.findByName(station.getName()).isPresent();
	}
}
