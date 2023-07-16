package subway.subway.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.mapper.SubwayLineMapper;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.SubwayLineLoadPort;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
public class SubwayLineLoadPersistenceAdapter implements SubwayLineLoadPort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;
    private final SubwayLineMapper subwayLineMapper;

    @Autowired
    public SubwayLineLoadPersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository, SubwayLineMapper subwayLineMapper) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
        this.subwayLineMapper = subwayLineMapper;
    }

    @Override
    public SubwayLine findOne(SubwayLine.Id id) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaRepository.findOneWithSectionsById(id.getValue()).orElseThrow(() -> new NoSuchElementException("해당하는 지하철 노선이 없습니다."));
        return subwayLineMapper.toSubwayLine(subwayLineJpa);
    }
}
