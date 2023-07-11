package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineJpaMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.SubwayLineUpdatePort;
import subway.subway.domain.SubwayLine;

@Component
class SubwayLineUpdatePersistenceAdapter implements SubwayLineUpdatePort {

    private final SubwayLineRepository subwayLineJpaRepository;
    private final SubwayLineJpaMapper subwayLineJpaMapper;

    public SubwayLineUpdatePersistenceAdapter(SubwayLineRepository subwayLineJpaRepository, SubwayLineJpaMapper subwayLineJpaMapper) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
        this.subwayLineJpaMapper = subwayLineJpaMapper;
    }
    @Override
    public void update(SubwayLine subwayLine) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaMapper.toSubwayLineJpa(subwayLine);
        subwayLineJpaRepository.save(subwayLineJpa);
    }
}
