package nextstep.subway.applicaion;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.applicaion.dto.LineResponse.createResponse;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineQueryService {

  private final LineRepository lineRepository;

  public List<LineResponse> getAllLine() {
    return lineRepository.findAll().stream()
        .map(LineResponse::createResponse)
        .collect(toList());
  }

  public LineResponse getLine(Long id) {
    return createResponse(findLine(id));
  }

  private Line findLine(Long id) {
    return lineRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.LINE_NOT_FOUND));
  }
}
