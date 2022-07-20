package nextstep.subway.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Embedded
  private Color color;

  @Embedded
  private final Sections sections = new Sections();

  @Builder
  public Line(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  public void changeName(String name) {
    this.name = name;
  }

  public void changeColor(Color color) {
    this.color = color;
  }

  public void addSections(Section section) {
    this.sections.addSection(section);
  }

  public void deleteLastSection() {
    this.sections.getSections().remove(this.sections.getSections().size()-1);
  }
}
