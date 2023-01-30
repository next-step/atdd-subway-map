package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineService {

  private final LineRepository lineRepository;

  public LineService(LineRepository repository) {
    this.lineRepository = repository;
  }

  @Transactional
  public LineResponse saveLine(LineRequest request) {
    Line line = lineRepository.save(new Line(request.getName(), request.getInbound(), request.getOutbound()));
    return createServiceResponse(line);
  }

  public LineResponse showLine(Long id) {
    Optional<Line> optionalLine = lineRepository.findById(id);

    return optionalLine.map(this::createServiceResponse).orElse(null);
  }

  public List<LineResponse> showLines() {
    return lineRepository.findAll().stream()
        .map(this::createServiceResponse)
        .collect(Collectors.toList());
  }

  public void deleteLineById(Long id) {
    lineRepository.deleteById(id);
  }

  public LineResponse updateLine(Long id, LineRequest request) {
    Optional<Line> optionalLine = lineRepository.findById(id);

    if (optionalLine.isPresent()) {
      Line line = optionalLine.get();
      line.setName(request.getName());
      line.setInboundStation(request.getInbound());
      line.setOutboundStation(request.getOutbound());
      return createServiceResponse(lineRepository.save(line));
    }
    return null;
  }

  private LineResponse createServiceResponse(Line line) {
    return new LineResponse(line.getId(), line.getName());
  }
}
