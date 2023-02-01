package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationService;

@Service
public class LineService {

  private final LineRepository lineRepository;
  private final StationService stationService;

  public LineService(LineRepository repository, StationService stationService) {
    this.lineRepository = repository;
    this.stationService = stationService;
  }

  @Transactional
  public LineResponse saveLine(LineCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId()).toEntity();
    Station downStation = stationService.findById(request.getDownStationId()).toEntity();
    Line created = lineRepository.save(
        new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance())
    );

    return LineResponse.of(created);
  }

  public Optional<LineResponse> showLine(Long id) {
    return lineRepository.findById(id).map(LineResponse::of);
  }

  public List<LineResponse> showLines() {
    return lineRepository.findAll().stream()
        .map(LineResponse::of)
        .collect(Collectors.toList());
  }

  public void deleteLineById(Long id) {
    lineRepository.deleteById(id);
  }

  public Optional<LineResponse> updateLine(Long id, String name, String color) {
    Optional<Line> optionalLine = lineRepository.findById(id);

    return optionalLine.map(line -> line.updateLine(name, color))
        .map(line -> LineResponse.of(lineRepository.save(line)));
  }
}
