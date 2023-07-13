package subway.subway.adapter.out.persistence.query;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.StationJpa;
import subway.subway.adapter.out.persistence.mapper.StationJpaMapper;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.out.query.StationListQueryPort;
import subway.subway.application.query.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationListQueryPersistenceAdapter implements StationListQueryPort {

    private final StationRepository stationRepository;
    private final StationJpaMapper stationJpaMapper;

    public StationListQueryPersistenceAdapter(StationRepository stationRepository, StationJpaMapper stationJpaMapper) {
        this.stationRepository = stationRepository;
        this.stationJpaMapper = stationJpaMapper;
    }

    @Override
    public List<StationResponse> findAll() {
        List<StationJpa> stationJpas = stationRepository.findAll();
        return stationJpas.stream().map(stationJpaMapper::toStationResponse).collect(Collectors.toList());
    }
}
