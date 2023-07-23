package subway.line;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.section.LineSection;
import subway.line.section.LineSectionRequest;
import subway.station.Station;
import subway.station.StationService;

@Service
@RequiredArgsConstructor
public class LineService {

  private final SubwayLineService subwayLineService;
  private final StationService stationService;

  @Transactional
  public LineResponse createLine (SubwayLineRequest request) {
    Station upStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());
    Station downStation = stationService.getStationOrThrowIfNotExist(request.getDownStationId());

    LineSectionRequest lineSectionRequest = new LineSectionRequest(request);

    SubwayLine line = subwayLineService.createLine(request, upStation);
    LineSection section = lineSectionRequest.toEntity(line, upStation, downStation);
    line.addSections(section);

    List<Station> stations = List.of(section.getUpStation(), section.getDownStation());

    return new LineResponse(line, stations);
  }

  @Transactional
  public void deleteLine(Long lineId) {
    SubwayLine subwayLine = subwayLineService.getLineOrThrowIfNotExist(lineId);
    subwayLineService.deleteSubwayLine(subwayLine);
  }

  @Transactional
  public LineResponse getSubwayLine(Long lineId) {
    SubwayLine subwayLine = subwayLineService.getSubwayLine(lineId);
    List<Station> stations = subwayLine.getStationsInOrder();

    return new LineResponse(subwayLine, stations);
  }

  @Transactional
  public LineSection appendSection(Long lineId, LineSectionRequest request) {
    Station upStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());
    Station downStation = stationService.getStationOrThrowIfNotExist(request.getDownStationId());

    SubwayLine line = subwayLineService.getLineOrThrowIfNotExist(lineId);
    LineSection newSection = request.toEntity(line, upStation, downStation);

    return line.addSections(newSection);
  }

  @Transactional
  public void deleteStationInSection(Long lineId, Long stationId) {
    SubwayLine line = subwayLineService.getLineOrThrowIfNotExist(lineId);
    Station station = stationService.getStationOrThrowIfNotExist(stationId);

    line.deleteSectionsInLastStation(station);
  }
}
