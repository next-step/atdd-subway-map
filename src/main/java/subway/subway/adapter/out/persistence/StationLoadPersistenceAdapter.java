package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.StationJpa;
import subway.subway.adapter.out.persistence.mapper.StationJpaMapper;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.out.StationLoadPort;
import subway.subway.domain.Station;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class StationLoadPersistenceAdapter implements StationLoadPort {

    private final StationRepository stationRepository;
    private final StationJpaMapper stationJpaMapper;

    public StationLoadPersistenceAdapter(StationRepository stationRepository, StationJpaMapper stationJpaMapper) {
        this.stationRepository = stationRepository;
        this.stationJpaMapper = stationJpaMapper;
    }


    @Override
    public Station findOne(Station.Id stationId) {
        StationJpa stationJpa = stationRepository.findById(stationId.getValue()).orElseThrow(() -> new NoSuchElementException("해당하는 지하철 역이 없습니다."));
        return stationJpaMapper.toStation(stationJpa);
    }
}
