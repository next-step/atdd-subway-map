package subway.subway.adapter.out.persistence.query;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineResponseMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.query.SubwayLineDetailQueryPort;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
public class SubwayLineDetailQueryPersistenceAdapter implements SubwayLineDetailQueryPort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;
    private final SubwayLineResponseMapper subwayLineResponseMapper;

    public SubwayLineDetailQueryPersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository, SubwayLineResponseMapper subwayLineResponseMapper) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
        this.subwayLineResponseMapper = subwayLineResponseMapper;
    }

    @Override
    public SubwayLineResponse findOne(SubwayLine.Id id) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaRepository.findById(id.getValue())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 지하철 노선입니다."));
        return subwayLineResponseMapper.from(subwayLineJpa);
    }
}
