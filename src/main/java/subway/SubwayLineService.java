package subway;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayLineService {
    private final SubwayLineRepository subwayLineRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    public SubwayLineResponse saveSubwayLine(SubwayLineRequest request) {
        var subwayLine = subwayLineRepository.save(request.toSubwayLine());
        return SubwayLineResponse.from(subwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(SubwayLineResponse::from)
                .collect(Collectors.toList());
    }
}
