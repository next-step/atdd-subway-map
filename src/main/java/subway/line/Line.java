package subway.line;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import subway.section.Section;
import subway.sections.Sections;
import subway.station.Station;

@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String color;

  @Embedded
  private Sections sections;

  public Line() {}

  public Line(String name, String color) {
    this.name = name;
    this.color = color;
    this.sections = new Sections();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  // 상행 종점역
  public Station getUpStation() {
    return sections.getUpStation();
  }

  // 하행 종점역
  public Station getDownStation() {
    return sections.getDownStation();
  }

  public String getColor() {
    return color;
  }

  public List<Station> getStations() {
    System.out.println("get Stations!");
    return sections.getStations();
  }

  public Sections getSections() {
    return sections;
  }

  public Line updateLine(String name, String color) {
    this.name = name;
    this.color = color;
    return this;
  }

  public Line addSection(Section add) {
    sections.addSection(add);
    return this;
  }

  public void removeSection(Station downStation) {
    sections.removeSection(downStation);
  }
}
