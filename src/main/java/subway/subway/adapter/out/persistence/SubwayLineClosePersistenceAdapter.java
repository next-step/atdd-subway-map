package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.subway.adapter.out.persistence.repository.SubwayLineJpaRepository;
import subway.subway.application.out.SubwayLineClosePort;
import subway.subway.domain.SubwayLine;

@Component
public class SubwayLineClosePersistenceAdapter implements SubwayLineClosePort {

    private final SubwayLineJpaRepository subwayLineJpaRepository;

    public SubwayLineClosePersistenceAdapter(SubwayLineJpaRepository subwayLineJpaRepository) {
        this.subwayLineJpaRepository = subwayLineJpaRepository;
    }

    @Override
    public void closeSubwayLine(SubwayLine.Id id) {
        subwayLineJpaRepository.deleteById(id.getValue());
    }
}
