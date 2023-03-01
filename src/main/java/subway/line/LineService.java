package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.SectionCreateRequest;
import subway.section.SectionService;
import subway.station.Station;
import subway.station.StationService;

@Service
public class LineService {

  private final LineRepository lineRepository;
  private final StationService stationService;
  private final SectionService sectionService;

  public LineService(LineRepository repository, StationService stationService, SectionService sectionService) {
    this.lineRepository = repository;
    this.stationService = stationService;
    this.sectionService = sectionService;
  }

  @Transactional
  public LineResponse saveLine(LineCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId());
    Station downStation = stationService.findById(request.getDownStationId());

    Line created = lineRepository.save(
        new Line(request.getName(), request.getColor())
    );

    sectionService.createSection(
        created.getId(),
        new SectionCreateRequest(upStation.getId(), downStation.getId(), request.getDistance())
    );

    return LineResponse.from(created);
  }

  public void save(Line line) {
    lineRepository.save(line);
  }

  public Optional<LineResponse> showLine(Long id) {
    return lineRepository.findById(id).map(LineResponse::from);
  }

  public Line findById(Long id) {
    return lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
  }

  public List<LineResponse> showLines() {
    return lineRepository.findAll().stream()
        .map(LineResponse::from)
        .collect(Collectors.toList());
  }

  public void deleteLineById(Long id) {
    lineRepository.deleteById(id);
  }

  public Optional<LineResponse> updateLine(Long id, String name, String color) {
    Optional<Line> optionalLine = lineRepository.findById(id);

    return optionalLine.map(line -> line.updateLine(name, color))
        .map(line -> LineResponse.from(lineRepository.save(line)));
  }
}
