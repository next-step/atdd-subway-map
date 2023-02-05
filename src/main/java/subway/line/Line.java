package subway.line;

import static javax.persistence.FetchType.LAZY;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import subway.section.Section;
import subway.station.Station;

@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String color;

  @ManyToOne
  private Station upStation; // 상행역

  @ManyToOne
  private Station downStation; // 하행역

  @OneToMany(fetch = LAZY)
  private List<Section> sections;

  private Long distance;

  public Line() {}

  public Line(String name, String color, Station upStation, Station downStation,
      Long distance) {
    this.name = name;
    this.color = color;
    this.upStation = upStation;
    this.downStation = downStation;
    this.sections = new LinkedList<>();
    this.distance = distance;
  }

  public Line(Long id, String name, String color, Station upStation, Station downStation, Long distance) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.upStation = upStation;
    this.downStation = downStation;
    this.sections = new LinkedList<>();
    this.distance = distance;
  }

  public Line(Long id, String name, String color, Station upStation, Station downStation,
      List<Section> sections, Long distance) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.upStation = upStation;
    this.downStation = downStation;
    this.sections = sections;
    this.distance = distance;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Station getUpStation() {
    return upStation;
  }

  public Station getDownStation() {
    return downStation;
  }

  public String getColor() {
    return color;
  }

  public Long getDistance() {
    return distance;
  }

  public List<Section> getSections() {
    return sections;
  }

  public Line updateLine(String name, String color) {
    this.name = name;
    this.color = color;
    return this;
  }

  public Line addSection(Section add) {
    if (sections.size() != 0 && !Objects.equals(downStation.getId(), add.getUpStation().getId())) {
      throw new InvalidUpstationAppendInSection();
    }
    if (sections.stream().anyMatch(s -> Objects.equals(add.getDownStation().getId(), s.getUpStation().getId()))) {
      throw new DuplicatedStationAddToSectionFailException();
    };

    sections.add(add);
    downStation = add.getDownStation();
    return this;
  }

  public void removeSection(Section remove) {
    if (sections.size() <= 1) {
      throw new ZeroSectionException();
    }
    if (!sections.get(sections.size() - 1).getId().equals(remove.getId())) {
      throw new MiddleSectionRemoveFailException();
    }

    sections.remove(sections.size() - 1);
    this.downStation = sections.get(sections.size() - 1).getDownStation();
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  static class DuplicatedStationAddToSectionFailException extends RuntimeException {
    public DuplicatedStationAddToSectionFailException() {
      super("이미 등록된 역을 새로운 하행역으로 설정하실 수 없습니다.");
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  static class InvalidUpstationAppendInSection extends RuntimeException {
    public InvalidUpstationAppendInSection() {
      super("하행역에만 새로운 구간 추가가 가능합니다.");
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  static class ZeroSectionException extends RuntimeException {
    public ZeroSectionException() {
      super("노선의 구간은 한 개 이상 남아야 합니다.");
    }
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  static class MiddleSectionRemoveFailException extends RuntimeException {
    public MiddleSectionRemoveFailException() {
      super("노선의 중간 구간들은 지울 수 없습니다.");
    }
  }
}
