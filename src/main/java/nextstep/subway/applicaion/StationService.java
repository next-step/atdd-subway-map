package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Station saveStation(Station requestStation) {
		if (isStationExists(requestStation)) {
			throw new UniqueKeyExistsException(requestStation.getName());
		}

		return stationRepository.save(requestStation);
	}

	public List<Station> findAllStations() {
		return stationRepository.findAll();
	}

	@Transactional
	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

	private boolean isStationExists(Station station) {
		return stationRepository.findByName(station.getName()).isPresent();
	}
}
