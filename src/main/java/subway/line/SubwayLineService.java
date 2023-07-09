package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SubwayLineService {

  private final SubwayLineRepository repository;

  public SubwayLineService(SubwayLineRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public List<SubwayLineResponse> getAllSubwayLine() {
    return repository.findAll().stream()
        .map(SubwayLineResponse::new)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional(readOnly = true)
  public SubwayLineResponse getSubwayLine(Long id) {
    SubwayLine subwayLine = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new SubwayLineResponse(subwayLine);
  }

  @Transactional
  public SubwayLineResponse createLine(SubwayLineRequest request) {
    SubwayLine subwayLine = request.toEntity();
    repository.save(subwayLine);

    return new SubwayLineResponse(subwayLine);
  }

  @Transactional
  public SubwayLineResponse editSubwayLine(Long id, SubwayLineEditRequest request) {
    SubwayLine subwayLine = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    subwayLine.editLine(request);

    return new SubwayLineResponse(subwayLine);
  }

  @Transactional
  public void deleteSubwayLine(Long id) {
    SubwayLine subwayLine = repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    repository.delete(subwayLine);
  }

  public SubwayLine getLineOrThrowIfNotExist(Long lineId) {
    return repository.findById(lineId)
        .orElseThrow();
  }
}
