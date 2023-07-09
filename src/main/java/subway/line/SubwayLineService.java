package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SubwayLineService {

  private final SubwayLineRepository lineRepository;

  @Transactional(readOnly = true)
  public List<SubwayLineResponse> getAllSubwayLine() {
    return lineRepository.findAll().stream()
        .map(SubwayLineResponse::new)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional(readOnly = true)
  public SubwayLineResponse getSubwayLine(Long lineId) {
    SubwayLine subwayLine = lineRepository.findById(lineId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new SubwayLineResponse(subwayLine);
  }

  @Transactional
  public SubwayLine createLine(LineRequest request) {
    SubwayLine subwayLine = request.toEntity();
    return lineRepository.save(subwayLine);
  }

  @Transactional
  public SubwayLineResponse editSubwayLine(Long id, SubwayLineEditRequest request) {
    SubwayLine subwayLine = lineRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    subwayLine.editLine(request);

    return new SubwayLineResponse(subwayLine);
  }

  @Transactional
  public void deleteSubwayLine(Long id) {
    SubwayLine subwayLine = lineRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    lineRepository.delete(subwayLine);
  }

  @Transactional(readOnly = true)
  public SubwayLine getLineOrThrowIfNotExist(Long lineId) {
    return lineRepository.findById(lineId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
