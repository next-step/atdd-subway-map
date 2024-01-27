package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveStation(LineRequest lineRequest) {
        Line line = lineRepository.save(createLine(lineRequest));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 지하철라인 정보를 찾지 못했습니다."));
        return createLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 지하철라인 정보를 찾지 못했습니다."));
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    private Line createLine(LineRequest lineRequest) {
        return new Line(lineRequest.getName(),
                lineRequest.getColor(),
                getStation(lineRequest.getUpStationId()),
                getStation(lineRequest.getDownStationId()),
                lineRequest.getDistance());
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("해당 지하철역 정보를 찾지 못했습니다."));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                getStations(line));
    }

    private List<StationResponse> getStations(Line line) {
        return List.of(getStationResponse(line.getStationLink().getUpStation()), getStationResponse(line.getStationLink().getDownStation()));
    }

    private StationResponse getStationResponse(Station station) {
        return new StationResponse(station.getId(),
                station.getName());
    }

}
