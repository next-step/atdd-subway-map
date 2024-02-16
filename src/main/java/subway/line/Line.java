package subway.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

  @Column(length = 20)
  private String color;

  @OneToMany(mappedBy = "line", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<Section> sections = new ArrayList<>();

  public Line(String name, String color) {
    this.name = name;
    this.color = color;
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

    this.sections.add(section);
    section.registerLine(this);
  }

  public void removeSection(final Section section) {
    LineValidator.checkSectionForRemove(this, section);
    this.sections.remove(section);
  }

  private boolean isNewLine() {
    return this.getSections().isEmpty();
  }
}
