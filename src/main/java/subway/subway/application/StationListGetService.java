package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.in.StationListGetQuery;
import subway.subway.application.query.StationResponse;
import subway.rds_module.entity.StationJpa;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
class StationListGetService implements StationListGetQuery {
    private final StationRepository stationRepository;

    public StationListGetService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }
    @Override
    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    private StationResponse createStationResponse(StationJpa station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
