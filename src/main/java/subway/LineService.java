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
public class LineService {

    private final LineRepository subwayLineRepository;
    private final StationRepository stationRepository;

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(
            ResourceNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(
            ResourceNotFoundException::new);
        Line line = subwayLineRepository.save(
            new Line(request.getName(), request.getColor(), upStation, downStation,
                request.getDistance()));

        return LineResponse.createSubwayLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getSubwayLineList() {
        List<Line> lines = subwayLineRepository.findAll();
        return lines.stream().map(LineResponse::createSubwayLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getSubwayLine(Long id) {
        Line line = subwayLineRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
        return LineResponse.createSubwayLineResponse(line);
    }

    public void updateSubwayLine(Long id, LineRequest lineRequest) {
        Line line = subwayLineRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
        line.update(id, lineRequest);
    }

    public void deleteSubwayLine(Long id) {
        subwayLineRepository.deleteById(id);
    }
}
