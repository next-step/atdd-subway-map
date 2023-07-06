package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.subway.adapter.out.persistence.repository.SubwayLineRepository;
import subway.subway.application.out.SubwayLineClosePort;
import subway.subway.domain.SubwayLine;

@Component
public class SubwayLineClosePersistenceAdapter implements SubwayLineClosePort {

    private final SubwayLineRepository subwayLineRepository;

    public SubwayLineClosePersistenceAdapter(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    @Override
    public void closeSubwayLine(SubwayLine.Id id) {
        subwayLineRepository.deleteById(id.getValue());
    }
}
