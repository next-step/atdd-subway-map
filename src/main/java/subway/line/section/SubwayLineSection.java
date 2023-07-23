package subway.line.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import subway.station.Station;

@Getter
@Embeddable
public class SubwayLineSection {

  @Column(name = "start_station_id", nullable = false, insertable = false, updatable = false)
  private Long startStationId;

  @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
  private List<LineSection> sections = new ArrayList<>();

  /**
   * 지하철 구간의 역들을 순서대로 리턴
   */
  public List<Station> getStationsInOrder() {
    List<Station> stations = new ArrayList<>(sections.size() * 2);

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

  public LineSection addSection(LineSection newSection) {
    if (CollectionUtils.isEmpty(sections)) {
      sections.add(newSection);
      return newSection;
    }

    List<Station> stations = this.getStationsInOrder();

    throwIfUpStationIsNotEndpoint(newSection.getUpStation(), stations);
    throwIfDownStationAlreadyIncluded(newSection.getDownStation(), stations);
    sections.add(newSection);
    return newSection;
  }

  private void throwIfUpStationIsNotEndpoint(Station upStation, List<Station> stations) {
    if (CollectionUtils.isEmpty(stations)) {
      return;
    }

    Station lastStation = CollectionUtils.lastElement(stations);
    if (lastStation == null) {
      return;
    }

    if (!lastStation.equals(upStation)) {
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

  public void deleteStation(Station station) {
    if (sections.size() <= 1) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 노선에 구간이 두개 이상이여야 삭제 할 수 있습니다.");
    }

    List<Station> stations = getStationsInOrder();
    if (CollectionUtils.isEmpty(stations)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 노선에 역이 없습니다.");
    }

    Station lastStation = CollectionUtils.lastElement(stations);
    if (station.isNotEqual(lastStation)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "하행 종점역만 삭제할 수 있습니다.");
    }

    // 하행 종점역만 제거 할 수 있음
    int lastIdx = sections.size() - 1;
    LineSection lastSection = sections.get(lastIdx);
    if (lastSection.getDownStation().equals(station)) {
      sections.remove(lastIdx);
    }
  }
}
