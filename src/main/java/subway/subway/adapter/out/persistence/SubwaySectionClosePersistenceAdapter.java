package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.rds_module.entity.SubwayLineJpa;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.SubwaySectionClosePort;
import subway.subway.domain.SubwayLine;

import java.util.NoSuchElementException;

@Component
public class SubwaySectionClosePersistenceAdapter implements SubwaySectionClosePort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;

    public SubwaySectionClosePersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
    }


    @Override
    public void closeSection(SubwayLine subwayLine) {
        SubwayLineJpa subwayLineJpa = subwayLineJpaRepository.findOneWithSectionsById(subwayLine.getId().getValue()).orElseThrow(() -> new NoSuchElementException("해당하는 지하철 노선이 없습니다."));
        subwayLineJpa.deleteSections(subwayLine);
        subwayLineJpa.updateSections(subwayLine);
        subwayLineJpaRepository.save(subwayLineJpa);

    }
}
