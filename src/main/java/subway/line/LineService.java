package subway.line;

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

  private final StationService stationService;
  private final SubwayLineService subwayLineService;
  private final LineSectionService lineSectionService;

  @Transactional
  public SubwayLineResponse createLine (LineRequest request) {

    SubwayLine line = subwayLineService.createLine(request);
    LineSection section = lineSectionService.createSection(line.getLineId(), new LineSectionRequest(request));

    List<Station> stations = List.of(section.getUpStation(), section.getDownStation());

    return new SubwayLineResponse(line, stations);
  }
}
