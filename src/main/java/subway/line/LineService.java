package subway.line;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.BusinessException;
import subway.station.StationResponse;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

  private final LineRepository lineRepository;
  private final StationService stationService;

  public LineService(
      final LineRepository lineRepository,
      final StationService stationService
  ) {
    this.lineRepository = lineRepository;
    this.stationService = stationService;
  }

  @Transactional
  public LineResponse saveLine(final LineCreateRequest request) {
    final var upStation = stationService.findById(request.getUpStationId())
        .orElseThrow(() -> new RuntimeException("상행역 정보를 찾을 수 없습니다."));
    final var downStation = stationService.findById(request.getDownStationId())
        .orElseThrow(() -> new RuntimeException("하행역 정보를 찾을 수 없습니다."));

    final var line = lineRepository.save(request.to());
    this.saveSection(line.getId(), upStation.getId(), downStation.getId(), request.getDistance());

    return LineResponse.from(line, upStation, downStation);
  }

  public List<LineResponse> findAllLines() {
    final var lines = lineRepository.findAllWithSectionsUsingFetchJoin();

    return lines.stream()
        .map(line -> LineResponse.from(line, this.getStations(line)))
        .collect(Collectors.toList());
  }

  public LineResponse findById(Long id) {
    final var line = lineRepository.findByIdWithSectionsUsingFetchJoin(id)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));
    final var stations = getStations(line);

    return LineResponse.from(line, stations);
  }

  @Transactional
  public void updateLine(final Long id, final LineUpdateRequest request) {
    final var line = lineRepository.findById(id)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));

    line.updateLine(request.getName(), request.getColor());
  }

  @Transactional
  public void deleteLine(final Long id) {
    lineRepository.deleteById(id);
  }

  @Transactional
  public void saveSection(
      final Long lineId,
      final Long upStationId,
      final Long downStationId,
      final int distance
  ) {
    final var line = lineRepository.findById(lineId)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));
    final var section = new Section(null, upStationId, downStationId, distance, line);

    line.addSection(section);
  }

  @Transactional
  public void deleteSection(
      final Long lineId,
      final Long stationId
  ) {
    final var line = lineRepository.findByIdWithSectionsUsingFetchJoin(lineId)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));

    final var section = line.getSections().stream()
        .filter(it -> it.getDownStationId().equals(stationId))
        .findAny()
        .orElseThrow(() -> new BusinessException("구간 정보를 찾을 수 없습니다."));

    line.removeSection(section);
  }

  private List<StationResponse> getStations(Line line) {
    if (line.getSections().isEmpty()) {
      return Collections.emptyList();
    }

    List<Long> stationIds = line.getSections().stream()
        .map(Section::getDownStationId)
        .collect(Collectors.toList());
    stationIds.add(0, line.getSections().get(0).getUpStationId());

    return stationService.findByIds(stationIds);
  }
}
