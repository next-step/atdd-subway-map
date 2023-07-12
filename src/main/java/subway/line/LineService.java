package subway.line;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.section.LineSection;
import subway.line.section.LineSectionRequest;
import subway.line.section.LineSectionService;
import subway.station.Station;
import subway.station.StationService;

@Service
@RequiredArgsConstructor
public class LineService {

  private final SubwayLineService subwayLineService;
  private final LineSectionService lineSectionService;
  private final StationService stationService;

  @Transactional
  public LineResponse createLine (SubwayLineRequest request) {
    Station upStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());
    Station downStation = stationService.getStationOrThrowIfNotExist(request.getDownStationId());

    SubwayLine line = subwayLineService.createLine(request, upStation);

    LineSection section = lineSectionService.createSection(line, upStation, downStation, new LineSectionRequest(request));
    List<Station> stations = List.of(section.getUpStation(), section.getDownStation());

    return new LineResponse(line, stations);
  }

  @Transactional
  public void deleteLine(Long lineId) {
    lineSectionService.deleteAllSectionInLine(lineId);
    subwayLineService.deleteSubwayLine(lineId);
  }

  @Transactional
  public LineResponse getSubwayLine(Long lineId) {
    SubwayLine subwayLine = subwayLineService.getSubwayLine(lineId);
    List<LineSection> sections = lineSectionService.getAllSectionsOfLineWithStationInOrder(lineId);
    List<Station> stations = extractStationsFromSections(sections);

    return new LineResponse(subwayLine, stations);
  }

  private List<Station> extractStationsFromSections(List<LineSection> sections) {
    List<Station> stations = new ArrayList<>(sections.size() * 2);

    stations.add(sections.get(0).getUpStation());
    for (LineSection section : sections) {
      stations.add(section.getDownStation());
    }

    return stations;
  }

  @Transactional
  public LineSection appendSection(Long lineId, LineSectionRequest request) {
    Station upStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());
    Station downStation = stationService.getStationOrThrowIfNotExist(request.getDownStationId());

    SubwayLine line = subwayLineService.getLineOrThrowIfNotExist(lineId);

    return lineSectionService.appendSection(line, upStation, downStation, request);
  }

  @Transactional
  public void deleteStationInSection(Long lineId, Long stationId) {
    SubwayLine line = subwayLineService.getLineOrThrowIfNotExist(lineId);
    Station station = stationService.getStationOrThrowIfNotExist(stationId);
    lineSectionService.deleteSection(line, station);
  }
}
