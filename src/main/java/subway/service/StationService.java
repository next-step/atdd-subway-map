package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.Station;
import subway.repository.StationRepository;
import subway.service.exception.NotFoundStationException;

@Service
@Transactional(readOnly = true)
public class StationService {
	private StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional
	public StationResponse saveStation(StationRequest stationRequest) {
		Station station = stationRepository.save(new Station(stationRequest.getName()));
		return createStationResponse(station);
	}

	public List<StationResponse> findAllStations() {
		return stationRepository.findAll().stream()
				.map(this::createStationResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

	public StationResponse findStationById(Long id) {
		return stationRepository
				.findById(id)
				.map(this::createStationResponse)
				.orElseThrow(() -> new NotFoundStationException(id));
	}

	private StationResponse createStationResponse(Station station) {
		return new StationResponse(station.getId(), station.getName());
	}
}
