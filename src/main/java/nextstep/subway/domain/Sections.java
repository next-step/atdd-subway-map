package nextstep.subway.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.common.exception.CustomException;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {

  private static final int MIN_SECTION_COUNT = 1;

  @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
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

  public Section getFirstSection() {
    return sections.get(0);
  }

  public Section getLastSection() {
    return sections.get(sections.size() - 1);
  }

  public List<Station> getAllStations() {
    if (isOneSections()) {
      return Arrays.asList(getFirstSection().getUpStation(), getFirstSection().getDownStation());
    }

    return Stream.concat(
        Stream.of(getFirstSection().getUpStation()),
        sections.stream().map(Section::getDownStation)
    )
    .collect(toList());
  }

  public void validateAddSection(long upStationId, long downStationId) {
    if (getLastStationId() != upStationId) {
      throw new CustomException(HttpStatus.CONFLICT, ErrorMessage.INVALID_STATION);
    }

    boolean duplicateCheck = sections.stream()
        .anyMatch(section -> section.getUpStationId() == downStationId);

    if (duplicateCheck) {
      throw new CustomException(HttpStatus.CONFLICT, ErrorMessage.LINE_CONTAINS_STATION);
    }
  }

  public void validateDeleteSection(long stationId) {
    if (getLastStationId() != stationId) {
      throw new CustomException(HttpStatus.CONFLICT, ErrorMessage.SECTION_NO_LAST_DELETE);
    }

    if (isOneSections()) {
      throw new CustomException(HttpStatus.CONFLICT, ErrorMessage.SECTION_ONE_NO_DELETE);
    }
  }

  private boolean isOneSections() {
    return this.sections.size() <= MIN_SECTION_COUNT;
  }
}
