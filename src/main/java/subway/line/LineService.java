package subway.line;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationResponse;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

  private final LineRepository lineRepository;
  private final StationService stationService;

  public LineService(final LineRepository lineRepository, final StationService stationService) {
    this.lineRepository = lineRepository;
    this.stationService = stationService;
  }

  // TODO request validation
  // TODO entity validation
  // TODO station not found 예외 처리
  @Transactional
  public LineResponse saveLine(final LineCreateRequest request) {
    final var upStation = stationService.findById(request.getUpStationId())
        .orElseThrow(() -> new RuntimeException("상행역 정보를 찾을 수 없습니다."));
    final var downStation = stationService.findById(request.getDownStationId())
        .orElseThrow(() -> new RuntimeException("하행역 정보를 찾을 수 없습니다."));

    final var line = lineRepository.save(request.to());

    return LineResponse.from(line, upStation, downStation);
  }

  // TODO pagination
  // TODO entity mapping 이후 쿼리 수정
  public List<LineResponse> findAllLines() {
    final var lines = lineRepository.findAll();
    final Map<Long /* station ID */, StationResponse> stationById = stationService.findAllStations().stream()
        .collect(Collectors.toMap(StationResponse::getId, Function.identity()));

    return lines.stream()
        .map(line -> LineResponse.from(
            line,
            stationById.get(line.getUpStationId()),
            stationById.get(line.getDownStationId())
        )).collect(Collectors.toList());
  }

  // TODO entity mapping 이후 쿼리 수정
  public LineResponse findById(Long id) {
    final var line = lineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("노선 정보를 찾을 수 없습니다."));
    final var upStation = stationService.findById(line.getUpStationId())
        .orElseThrow(() -> new RuntimeException("상행역 정보를 찾을 수 없습니다."));
    final var downStation = stationService.findById(line.getDownStationId())
        .orElseThrow(() -> new RuntimeException("하행역 정보를 찾을 수 없습니다."));

    return LineResponse.from(
        line,
        upStation,
        downStation
    );
  }

  @Transactional
  public void updateLine(final Long id, final LineUpdateRequest request) {
    final var line = lineRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("노선 정보를 찾을 수 없습니다."));

    line.updateLine(request.getName(), request.getColor());
  }

  @Transactional
  public void deleteLineById(final Long id) {
    lineRepository.deleteById(id);
  }

}
