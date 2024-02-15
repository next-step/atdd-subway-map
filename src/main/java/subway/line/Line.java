package subway.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 20, nullable = false)
  private String name;

  @Column(length = 20, nullable = false)
  private String color;

  @Column
  private Long upStationId;

  @Column
  private Long downStationId;

  @Column(nullable = false)
  private int distance;

  @OneToMany(mappedBy = "line", fetch = FetchType.LAZY)
  private final List<Section> sections = new ArrayList<>();

  public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  protected Line() {
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public int getDistance() {
    return distance;
  }

  public List<Section> getSections() {
    return sections;
  }

  public void updateLine(final String name, final String color) {
    this.name = name;
    this.color = color;
  }

  public void addSection(final Section section) {
    // 새 라인의 경우 section 검증 제외
    if (!isNewLine()) {
      LineValidator.checkSectionForAddition(this, section);
    }

    this.downStationId = section.getDownStationId();
    this.distance += section.getDistance();
    this.sections.add(section);
    section.registerLine(this);
  }

  public void removeSection(final Section section) {
    LineValidator.checkSectionForRemove(this, section);

    this.downStationId = section.getUpStationId();
    this.distance -= section.getDistance();
    this.sections.remove(section);
  }

  private boolean isNewLine() {
    return this.getSections().isEmpty();
  }
}
