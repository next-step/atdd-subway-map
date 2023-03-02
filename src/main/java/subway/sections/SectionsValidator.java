package subway.sections;

import java.util.Objects;
import subway.section.Section;
import subway.station.Station;

class SectionsValidator {
  static void validateAddedSection(Sections sections, Section section) {
    if (isUpStationNotMatchesLastDownStation(sections, section)) {
      throw new IllegalArgumentException("하행역에만 새로운 구간 추가가 가능합니다.");
    }
    if (isAlreadyAddedSection(sections, section)) {
      throw new IllegalArgumentException("이미 등록된 역을 새로운 하행역으로 설정하실 수 없습니다.");
    };
  }

  static void validateRemoveSection(Sections sections, Station station) {
    if (sections.getSize() <= 1) {
      throw new IllegalStateException("노선의 구간은 한 개 이상 남아야 합니다.");
    }

    if (isStationNotLastDownStation(sections, station)) {
      throw new IllegalArgumentException("노선의 중간 구간들은 지울 수 없습니다.");
    }
  }

  private static boolean isUpStationNotMatchesLastDownStation(Sections sections,Section section) {
    return sections.getSize() != 0 && !sections.getDownStation().equals(section.getUpStation());
  }

  private static boolean isAlreadyAddedSection(Sections sections, Section section) {
    return sections.isStationContained(section.getDownStation()) && isStationNotLastDownStation(sections, section.getDownStation());
  }

  private static boolean isStationNotLastDownStation(Sections sections, Station station) {
    return !sections.getDownStation().equals(station);
  }
}
