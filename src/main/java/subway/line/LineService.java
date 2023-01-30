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
  public LineResponse saveLine(Line line) {
    Line created = lineRepository.save(line);
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

  public Optional<LineResponse> updateLine(Long id, LineRequest request) {
    Optional<Line> optionalLine = lineRepository.findById(id);

    return optionalLine.map(line ->
            line.updateLine(request.getName(), request.getInbound(), request.getOutbound())
        ).map(lineRepository::save)
        .map(this::createServiceResponse);
  }

  private LineResponse createServiceResponse(Line line) {
    return new LineResponse(line.getId(), line.getName(), line.getInboundStation(), line.getOutboundStation());
  }
}
