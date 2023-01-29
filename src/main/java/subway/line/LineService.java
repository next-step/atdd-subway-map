package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationService.findOneById(request.getUpStationId());
        Station downStation = stationService.findOneById(request.getDownStationId());
        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return LineResponse.of(line);
    }
    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findOneById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow();
    }

    @Transactional
    public LineResponse updateById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        return LineResponse.of(lineRepository.save(line.updateLine(lineRequest)));
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }
}
