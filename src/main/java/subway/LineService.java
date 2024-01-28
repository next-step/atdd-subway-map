package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

  private final LineRepository lineRepository;
  private final StationRepository stationRepository;

  public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
    this.lineRepository = lineRepository;
    this.stationRepository = stationRepository;
  }

  // TODO request validation
  // TODO entity validation
  // TODO station not found 예외 처리
  @Transactional
  public LineResponse saveLine(final LineCreateRequest request) {
    final var upStation = stationRepository.findById(request.getUpStationId())
        .orElseThrow(() -> new RuntimeException("상행역 정보를 찾을 수 없습니다."));
    final var downStation = stationRepository.findById(request.getDownStationId())
        .orElseThrow(() -> new RuntimeException("하행역 정보를 찾을 수 없습니다."));

    final var line = lineRepository.save(request.to());

    return LineResponse.from(line, StationResponse.from(upStation), StationResponse.from(downStation));
  }

}
