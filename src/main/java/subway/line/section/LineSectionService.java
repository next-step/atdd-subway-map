package subway.line.section;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
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
  public List<LineSectionResponse> getAllSectionsOfLineAsResponse(Long lineId) {
    return sectionRepository.findByLineId(lineId).stream()
        .map(LineSectionResponse::fromEntity)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional(readOnly = true)
  public List<LineSection> getAllSectionsOfLineWithStationInOrder(Long lineId) {
    return sectionRepository.findByLineIdWithStationOrderByRegDateTime(lineId);
  }

  @Transactional
  public LineSection createSection(Long lineId, LineSectionRequest request) {

    SubwayLine line = subwayLineService.getLineOrThrowIfNotExist(lineId);

    Station upStation = stationService.getStationOrThrowIfNotExist(request.getUpStationId());
    Station downStation = stationService.getStationOrThrowIfNotExist(request.getDownStationId());

    List<Station> stations = stationService.stationsOfSections(sectionRepository.findByLineIdWithStationOrderByRegDateTime(lineId));
    throwIfUpstationIsNotEndpoint(upStation, stations);
    throwIfDownStationAlreadyIncluded(downStation, stations);

    LineSection newSection = request.toEntity(line, upStation, downStation);

    return sectionRepository.save(newSection);
  }

  private void throwIfUpstationIsNotEndpoint(Station upStation, List<Station> stations) {
    if (CollectionUtils.isEmpty(stations)) {
      return;
    }

    Station lastStation = CollectionUtils.lastElement(stations);
    if (!lastStation.getStationId().equals(upStation.getStationId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상행역은 반드시 하행 종점역이여야합니다.");
    }
  }

  private void throwIfDownStationAlreadyIncluded(Station downStation, List<Station> stations) {
    long duplicateStationIdCnt = stations.stream()
        .map(Station::getStationId)
        .filter(downStation.getStationId()::equals)
        .count();

    if (duplicateStationIdCnt != 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "하행역이 기존 구간에 포함되어있는 역입니다.");
    }
  }

  @Transactional
  public void deleteAllSectionInLine(Long lineId) {
    List<LineSection> sections = sectionRepository.findByLineId(lineId);
    sectionRepository.deleteAllInBatch(sections);
  }
}
