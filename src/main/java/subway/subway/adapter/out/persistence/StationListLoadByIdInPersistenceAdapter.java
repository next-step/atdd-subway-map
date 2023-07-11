package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.StationJpa;
import subway.subway.adapter.out.persistence.mapper.StationJpaMapper;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.out.StationListLoadByIdInPort;
import subway.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationListLoadByIdInPersistenceAdapter implements StationListLoadByIdInPort {

    private final StationRepository stationRepository;

    private final StationJpaMapper stationJpaMapper;


    public StationListLoadByIdInPersistenceAdapter(StationRepository stationRepository, StationJpaMapper stationJpaMapper) {
        this.stationRepository = stationRepository;
        this.stationJpaMapper = stationJpaMapper;
    }

    @Override
    public List<Station> findAllIn(List<Station.Id> stationIds) {
        List<Long> ids = stationIds.stream().map(Station.Id::getValue).collect(Collectors.toList());
        List<StationJpa> stationJpas = stationRepository.findAllByIdIn(ids);

        if (stationJpas.size() != stationIds.size()) {
            throw new IllegalStateException("역이 존재하지 않습니다.");
        }
        return stationJpas.stream().map(stationJpaMapper::toStation).collect(Collectors.toList());

    }
}
