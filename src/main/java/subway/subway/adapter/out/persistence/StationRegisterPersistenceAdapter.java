package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.StationJpa;
import subway.subway.adapter.out.persistence.mapper.StationJpaMapper;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.out.StationRegisterPort;
import subway.subway.application.query.StationResponse;
import subway.subway.domain.Station;

@Component
public class StationRegisterPersistenceAdapter implements StationRegisterPort {

    private final StationRepository stationRepository;
    private final StationJpaMapper stationJpaMapper;

    public StationRegisterPersistenceAdapter(StationRepository stationRepository, StationJpaMapper stationJpaMapper) {
        this.stationRepository = stationRepository;
        this.stationJpaMapper = stationJpaMapper;
    }

    @Override
    public StationResponse save(Station station) {
        StationJpa stationJpa = stationJpaMapper.mapFrom(station);
        stationRepository.save(stationJpa);

        return stationJpaMapper.toStationResponse(stationJpa);
    }
}
