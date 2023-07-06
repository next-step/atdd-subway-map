package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.SubwayLineLoadPort;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
public class SubwayLineLoadPersistenceAdapter implements SubwayLineLoadPort {

    private final SubwayLineRepository subwayLineRepository;
    private final SubwayLineMapper subwayLineMapper;

    public SubwayLineLoadPersistenceAdapter(SubwayLineRepository subwayLineRepository, SubwayLineMapper subwayLineMapper) {
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineMapper = subwayLineMapper;
    }

    @Override
    public SubwayLine findOne(SubwayLine.Id id) {
        SubwayLineJpa subwayLineJpa = subwayLineRepository.findById(id.getValue()).orElseThrow(() -> new NoSuchElementException("해당하는 지하철 노선이 없습니다."));
        return subwayLineMapper.mapFrom(subwayLineJpa);
    }
}
