package subway;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class SubwayLineService {

    private final SubwayLineRepository subwayLineRepository;
    private final StationRepository stationRepository;

    public SubwayLineResponse saveLine(SubwayLineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(
            ResourceNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(
            ResourceNotFoundException::new);
        SubwayLine subwayLine = subwayLineRepository.save(
            new SubwayLine(request.getName(), request.getColor(), upStation, downStation,
                request.getDistance()));

        return SubwayLineResponse.createSubwayLineResponse(subwayLine);
    }

    @Transactional(readOnly = true)
    public List<SubwayLineResponse> getSubwayLineList() {
        List<SubwayLine> lines = subwayLineRepository.findAll();
        return lines.stream().map(SubwayLineResponse::createSubwayLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubwayLineResponse getSubwayLine(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
        return SubwayLineResponse.createSubwayLineResponse(subwayLine);
    }

}
