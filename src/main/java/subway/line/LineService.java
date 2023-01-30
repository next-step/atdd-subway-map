package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

@Service
public class LineService {

  private final LineRepository lineRepository;
  private final StationRepository stationRepository;

  public LineService(LineRepository repository, StationRepository stationRepository) {
    this.lineRepository = repository;
    this.stationRepository = stationRepository;
  }

  @Transactional
  public LineResponse saveLine(String name, String inboundName, String outboundName) {
    Station inbound = stationRepository.findByName(inboundName);
    Station outbound = stationRepository.findByName(outboundName);
    Line created = lineRepository.save(new Line(name, inbound, outbound));
    return createServiceResponse(created);
  }

  public Optional<LineResponse> showLine(Long id) {
    Optional<Line> optionalLine = lineRepository.findById(id);

    return optionalLine.map(this::createServiceResponse);
  }

  public List<LineResponse> showLines() {
    return lineRepository.findAll().stream()
        .map(this::createServiceResponse)
        .collect(Collectors.toList());
  }

  public void deleteLineById(Long id) {
    lineRepository.deleteById(id);
  }

  public Optional<LineResponse> updateLine(Long id, String name, String inboundName, String outboundName) {
    Station inbound = stationRepository.findByName(inboundName);
    Station outbound = stationRepository.findByName(outboundName);
    Optional<Line> optionalLine = lineRepository.findById(id);

    return optionalLine.map(line -> line.updateLine(name, inbound, outbound))
        .map(lineRepository::save)
        .map(this::createServiceResponse);
  }

  private LineResponse createServiceResponse(Line line) {
    return new LineResponse(line.getId(), line.getName(), line.getInboundStation(), line.getOutboundStation());
  }
}
