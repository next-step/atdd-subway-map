package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.SubwaySectionAddPort;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
public class SubwaySectionAddPersistenceAdapter implements SubwaySectionAddPort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;

    public SubwaySectionAddPersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
    }

    @Override
    public void addSubwaySection(SubwayLine subwayLine) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaRepository.findOneWithSectionsById(subwayLine.getId().getValue()).orElseThrow(() -> new NoSuchElementException("해당하는 지하철 노선이 없습니다."));
        subwayLineJpa.updateSections(subwayLine);
        subwayLineJpa.addSections(subwayLine);
        subwayLineJpaRepository.save(subwayLineJpa);
    }
}
