package subway.line;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.station.Station;

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
  private List<Station> stations;

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

  public List<Station> getStations() {
    return stations;
  }

  public void updateLine(final String name, final String color) {
    this.name = name;
    this.color = color;
  }

  public void addSection(Section section) {
    this.downStationId = section.getDownStationId();
    this.distance += section.getDistance();
  }

}
