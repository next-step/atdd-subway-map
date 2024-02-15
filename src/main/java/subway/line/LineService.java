package subway.line;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.BusinessException;
import subway.station.StationResponse;
import subway.station.StationService;

@Service
@Transactional(readOnly = true)
public class LineService {

  private final LineRepository lineRepository;
  private final SectionRepository sectionRepository;
  private final StationService stationService;

  public LineService(
      final LineRepository lineRepository,
      final SectionRepository sectionRepository,
      final StationService stationService
  ) {
    this.lineRepository = lineRepository;
    this.sectionRepository = sectionRepository;
    this.stationService = stationService;
  }

  @Transactional
  public LineResponse saveLine(final LineCreateRequest request) {
    final var upStation = stationService.findById(request.getUpStationId())
        .orElseThrow(() -> new RuntimeException("상행역 정보를 찾을 수 없습니다."));
    final var downStation = stationService.findById(request.getDownStationId())
        .orElseThrow(() -> new RuntimeException("하행역 정보를 찾을 수 없습니다."));

    final var line = lineRepository.save(request.to());
    this.saveSection(line.getId(), upStation.getId(), downStation.getId(), line.getDistance());

    return LineResponse.from(line, upStation, downStation);
  }

  public List<LineResponse> findAllLines() {
    // TODO fetch join
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

  public LineResponse findById(Long id) {
    // TODO fetch join
    final var line = lineRepository.findById(id)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));

    final var sections = line.getSections();
    final var stationIds = Stream.concat(
        sections.stream()
            .map(Section::getUpStationId),
        sections.stream()
            .map(Section::getDownStationId)
    ).collect(Collectors.toSet());

    final var stations = stationService.findByIds(stationIds);

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
  public SectionResponse saveSection(
      final Long lineId,
      final Long upStationId,
      final Long downStationId,
      final int distance
  ) {
    final var line = lineRepository.findById(lineId)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));
    final var section = new Section(null, upStationId, downStationId, distance, line);

    line.addSection(section);

    return SectionResponse.from(sectionRepository.save(section));
  }

  @Transactional
  public void deleteSection(
      final Long lineId,
      final Long stationId
  ) {
    final var line = lineRepository.findById(lineId)
        .orElseThrow(() -> new BusinessException("노선 정보를 찾을 수 없습니다."));

    final var section = line.getSections().stream()
        .filter(it -> it.getDownStationId().equals(stationId))
        .findAny()
        .orElseThrow(() -> new BusinessException("구간 정보를 찾을 수 없습니다."));

    line.removeSection(section);
  }

}
