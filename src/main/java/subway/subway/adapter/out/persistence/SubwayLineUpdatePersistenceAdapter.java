package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.SubwayLineUpdatePort;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
class SubwayLineUpdatePersistenceAdapter implements SubwayLineUpdatePort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;

    public SubwayLineUpdatePersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
    }
    @Override
    public void update(SubwayLine subwayLine) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaRepository.findOneWithSectionsById(subwayLine.getId().getValue()).orElseThrow(() -> new NoSuchElementException("해당하는 지하철 노선이 없습니다."));
        subwayLineJpa.update(subwayLine.getName(), subwayLine.getColor());
        subwayLineJpaRepository.save(subwayLineJpa);
    }
}
