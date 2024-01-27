package subway;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private SubwayLineRepository subwayLineRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    @Transactional
    public SubwayLineResponse saveLine(SubwayLineRequest subwayLineRequest) {
        SubwayLine line = subwayLineRepository.save(new SubwayLine(subwayLineRequest.getName()));

        return createSubwayLineResponse(line);
    }

    public List<SubwayLineResponse> findAllLines() {
        return subwayLineRepository.findAll().stream().map(this::createSubwayLineResponse).collect(
            Collectors.toList());
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine line) {
        return new SubwayLineResponse(
            line.getId(),
            line.getName()
        );
    }
}
