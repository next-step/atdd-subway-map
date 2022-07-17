package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.common.exception.SectionException;

@Getter
public class Sections {

  private List<Section> sections = new ArrayList<>();

  public Sections(List<Section> sections) {
    this.sections = sections;
  }

  public void addSection(Section section) {
    this.sections.add(section);
  }

  public long getLastStationId() {
    return getLastSection().getDownStation().getId();
  }

  public Section getLastSection() {
    return sections.get(sections.size() - 1);
  }

  public void validateAddSection(long upStationId, long downStationId) {
    if (getLastStationId() != upStationId) {
      throw new SectionException(ErrorMessage.INVALID_STATION);
    }

    boolean duplicateCheck = sections.stream()
        .anyMatch(section -> section.getUpStationId() == downStationId);

    if (duplicateCheck) {
      throw new SectionException(ErrorMessage.LINE_CONTAINS_STATION);
    }
  }
}
