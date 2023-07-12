package subway.line.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import subway.line.SubwayLine;
import subway.station.Station;

@Service
@RequiredArgsConstructor
public class LineSectionService {

  private final LineSectionRepository sectionRepository;

  @Transactional(readOnly = true)
  public List<LineSectionResponse> getAllSectionsOfLineAsResponse(Long lineId) {
    return sectionRepository.findByLineId(lineId).stream()
        .map(LineSectionResponse::fromEntity)
        .collect(Collectors.toUnmodifiableList());
  }

  @Transactional(readOnly = true)
  public List<LineSection> getAllSectionsOfLineWithStationInOrder(Long lineId) {
    return sectionRepository.findByLineIdWithStation(lineId);
  }

  @Transactional
  public LineSection createSection(SubwayLine line, Station upStation, Station downStation, LineSectionRequest request) {
    LineSection newSection = request.toEntity(line, upStation, downStation);
    return sectionRepository.save(newSection);
  }

  @Transactional
  public LineSection appendSection(SubwayLine line, Station upStation, Station downStation, LineSectionRequest request) {
    List<Station> stations = stationsInOrder(line, sectionRepository.findByLineIdWithStation(line.getLineId()));

    throwIfUpStationIsNotEndpoint(upStation, stations);
    throwIfDownStationAlreadyIncluded(downStation, stations);

    LineSection newSection = request.toEntity(line, upStation, downStation);

    return sectionRepository.save(newSection);
  }

  private void throwIfUpStationIsNotEndpoint(Station upStation, List<Station> stations) {
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

  /**
   * 지하철 구간의 역들을 순서대로 리턴
   */
  @Transactional
  public List<Station> stationsInOrder(SubwayLine line, List<LineSection> sections) {
    List<Station> stations = new ArrayList<>(sections.size() * 2);

    Long startStationId = line.getStartStationId();
    Map<Long, LineSection> upStationSectionMap = sections.stream()
        .collect(Collectors.toMap(LineSection::getUpStationId, Function.identity()));

    LineSection startSection = upStationSectionMap.get(startStationId);
    LineSection lastSection = startSection;

    // 첫 구간 상행 출발역 처리
    stations.add(startSection.getUpStation());
    LineSection currentSection = upStationSectionMap.get(startSection.getDownStationId());

    // 사이구간 역 추가
    while (currentSection != null) {
      stations.add(currentSection.getUpStation());
      LineSection nextSection = upStationSectionMap.get(currentSection.getDownStationId());

      if (nextSection != null) {
        currentSection = nextSection;
        continue;
      }

      lastSection = currentSection;
      break;
    }

    // 마지막구간 하행종점 역 추가
    stations.add(lastSection.getDownStation());

    return stations;
  }

  @Transactional
  public void deleteAllSectionInLine(Long lineId) {
    List<LineSection> sections = sectionRepository.findByLineId(lineId);
    sectionRepository.deleteAllInBatch(sections);
  }

  @Transactional
  public void deleteSection(SubwayLine line, Station station) {
    List<LineSection> sections = sectionRepository.findByLineIdWithStation(line.getLineId());
    if (sections.size() <= 1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 노선에 구간이 두개 이상이여야 삭제 할 수 있습니다.");
    }

    List<Station> stations = stationsInOrder(line, sections);
    if (CollectionUtils.isEmpty(stations)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 노선에 역이 없습니다.");
    }

    Station lastStation = CollectionUtils.lastElement(stations);
    if (station.isNotEqual(lastStation)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "하행 종점역만 삭제할 수 있습니다.");
    }

    LineSection lineSection = sections.stream()
        .filter(section -> section.getDownStation().isEqual(station))
        .findFirst()
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 역이 없습니다.")
        );

    sectionRepository.delete(lineSection);
  }
}
