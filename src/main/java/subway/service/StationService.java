package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import subway.controller.dto.StationRequest;
import subway.controller.dto.StationResponse;
import subway.domain.Station;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    private final SectionRepository sectionRepository;

    public StationService(StationRepository stationRepository,
        SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
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
        sectionRepository.deleteAllByStartStationId(id);
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
