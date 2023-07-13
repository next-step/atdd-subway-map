package subway.subway.adapter.out.persistence.query;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineResponseMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.query.SubwayLineDetailQueryPort;
import subway.subway.application.query.SubwayLineResponse;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
public class SubwayLineDetailQueryPersistenceAdapter implements SubwayLineDetailQueryPort {

    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineResponseMapper subwayLineResponseMapper;

    public SubwayLineDetailQueryPersistenceAdapter(SubwayLineRepository subwayLineRepository, SubwayLineResponseMapper subwayLineResponseMapper) {
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineResponseMapper = subwayLineResponseMapper;
    }

    @Override
    public SubwayLineResponse findOne(SubwayLine.Id id) {
        SubwayLineJpa subwayLineJpa = subwayLineRepository.findById(id.getValue())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 지하철 노선입니다."));
        return subwayLineResponseMapper.from(subwayLineJpa);
    }
}
