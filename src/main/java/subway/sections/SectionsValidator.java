package subway.sections;

import java.util.Objects;
import subway.line.LineException.DuplicatedStationAddToSectionFailException;
import subway.line.LineException.InvalidUpstationAppendInSection;
import subway.line.LineException.MiddleSectionRemoveFailException;
import subway.line.LineException.ZeroSectionException;
import subway.section.Section;
import subway.station.Station;

class SectionsValidator {
  static void validateAddedSection(Sections sections, Section section) {
    if (isUpStationNotMatchesLastDownStation(sections, section)) {
      throw new InvalidUpstationAppendInSection();
    }
    if (isAlreadyAddedSection(sections, section)) {
      throw new DuplicatedStationAddToSectionFailException();
    };
  }

  static void validateRemoveSection(Sections sections, Station station) {
    if (sections.getSize() <= 1) {
      throw new ZeroSectionException();
    }

    if (isStationNotMatchedDownStation(sections, station)) {
      throw new MiddleSectionRemoveFailException();
    }
  }

  private static boolean isUpStationNotMatchesLastDownStation(Sections sections,Section section) {
    return sections.getSize() != 0 && !Objects.equals(sections.getDownStation().getId(), section.getUpStation().getId());
  }

  private static boolean isAlreadyAddedSection(Sections sections, Section section) {
    return sections.isStationContained(section.getDownStation()) && !isLastDownStation(sections, section.getDownStation());
  }

  private static boolean isStationNotMatchedDownStation(Sections sections, Station station) {
    return !Objects.equals(sections.getDownStation().getId(), station.getId());
  }

  private static boolean isLastDownStation(Sections sections, Station station) {
    return Objects.equals(sections.getDownStation().getId(), station.getId());
  }
}
