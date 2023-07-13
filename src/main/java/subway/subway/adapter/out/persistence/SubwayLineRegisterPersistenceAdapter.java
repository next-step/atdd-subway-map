package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineJpaMapper;
import subway.subway.adapter.out.persistence.mapper.SubwayLineResponseMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.SubwayLineRegisterPort;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

@Component
class SubwayLineRegisterPersistenceAdapter implements SubwayLineRegisterPort {

    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineResponseMapper subwayLineResponseMapper;
    private final SubwayLineJpaMapper subwayLineJpaMapper;

    public SubwayLineRegisterPersistenceAdapter(SubwayLineRepository subwayLineRepository, SubwayLineResponseMapper subwayLineResponseMapper, SubwayLineJpaMapper subwayLineJpaMapper) {
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineResponseMapper = subwayLineResponseMapper;
        this.subwayLineJpaMapper = subwayLineJpaMapper;
    }

    @Override
    public SubwayLineResponse register(SubwayLine subwayLine) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaMapper.from(subwayLine);

        subwayLineRepository.save(subwayLineJpa);
        return subwayLineResponseMapper.from(subwayLineJpa);
    }
}
