package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionCreateRequest;
import subway.section.SectionRepository;
import subway.section.SectionResponse;
import subway.station.Station;
import subway.station.StationService;

@Service
public class LineService {

  private final LineRepository lineRepository;
  private final StationService stationService;
  private final SectionRepository sectionRepository;

  public LineService(LineRepository repository, StationService stationService, SectionRepository sectionRepository) {
    this.lineRepository = repository;
    this.stationService = stationService;
    this.sectionRepository = sectionRepository;
  }

  @Transactional
  public LineResponse saveLine(LineCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId());
    Station downStation = stationService.findById(request.getDownStationId());

    Line created = lineRepository.save(new Line(request.getName(), request.getColor()));
    createSection(created.getId(), new SectionCreateRequest(upStation.getId(), downStation.getId(), request.getDistance()));

    return LineResponse.from(created);
  }

  public Optional<LineResponse> showLine(Long id) {
    return lineRepository.findById(id).map(LineResponse::from);
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

  @Transactional
  public SectionResponse createSection(Long lineId, SectionCreateRequest request) {
    Station upStation = stationService.findById(request.getUpStationId());
    Station downStation = stationService.findById(request.getDownStationId());
    Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);

    Section created = sectionRepository.save(new Section(upStation, downStation, request.getDistance()));

    lineRepository.save(line.addSection(created));

    return SectionResponse.from(created);
  }

  public void removeSection(Long lineId, Long downStationId) {
    Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
    Station station = stationService.findById(downStationId);
    line.removeSection(station);
  }
}
