package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import subway.station.Station;

@Service
@RequiredArgsConstructor
public class SubwayLineService {

  private final SubwayLineRepository lineRepository;

  @Transactional(readOnly = true)
  public List<LineResponse> getAllSubwayLine() {
    return lineRepository.findAll().stream()
        .map(LineResponse::new)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional(readOnly = true)
  public SubwayLine getSubwayLine(Long lineId) {
    SubwayLine subwayLine = lineRepository.findById(lineId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return subwayLine;
  }

  @Transactional
  public SubwayLine createLine(SubwayLineRequest request, Station startStation) {
    SubwayLine subwayLine = request.toEntity(startStation);
    return lineRepository.save(subwayLine);
  }

  @Transactional
  public LineResponse editSubwayLine(Long id, SubwayLineEditRequest request) {
    SubwayLine subwayLine = lineRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    subwayLine.editLine(request);

    return new LineResponse(subwayLine);
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
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 노선이 없습니다."));
  }
}
