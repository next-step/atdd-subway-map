package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineJpaMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.SubwayLineDetailQueryPort;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class SubwayLineDetailQueryPersistenceAdapter implements SubwayLineDetailQueryPort {

    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineJpaMapper subwayLineJpaMapper;

    public SubwayLineDetailQueryPersistenceAdapter(SubwayLineRepository subwayLineRepository, SubwayLineJpaMapper subwayLineJpaMapper) {
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineJpaMapper = subwayLineJpaMapper;
    }

    @Override
    public SubwayLineResponse findOne(SubwayLine.Id id) {
        SubwayLineJpa subwayLineJpa = subwayLineRepository.findById(id.getValue())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 지하철 노선입니다."));
        return subwayLineJpaMapper.mapFrom(subwayLineJpa);
    }
}
