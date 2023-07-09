package subway.line.section;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.SubwayLine;
import subway.line.SubwayLineService;
import subway.station.Station;
import subway.station.StationService;

@Service
@RequiredArgsConstructor
public class LineSectionService {

  private final LineSectionRepository sectionRepository;
  private final StationService stationService;
  private final SubwayLineService subwayLineService;

  @Transactional(readOnly = true)
  public List<LineSectionResponse> getAllSectionsOfLine(Long lineId) {
    return sectionRepository.findByLineId(lineId).stream()
        .map(LineSectionResponse::fromEntity)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional
  public LineSection createSection(Long lineId, LineSectionRequest request) {

    SubwayLine line = subwayLineService.getLineOrThrowIfNotExist(lineId);

    Station upStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());
    Station downStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());

    LineSection newSection = request.toEntity(line, upStation, downStation);

    return sectionRepository.save(newSection);
  }
}
