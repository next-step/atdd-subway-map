package subway.section;

import subway.Station;
import subway.line.Line;

import javax.persistence.*;
import java.util.List;

@Entity
public class Section implements Comparable<Section>{
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "line_id")
  private Line line;

  @ManyToOne
  @JoinColumn(name = "up_station_id")
  private Station upStation;

  @ManyToOne
  @JoinColumn(name = "down_station_id")
  private Station downStation;

  private Long distance;

  @Column(nullable = false)
  private Long sequence;

  protected Section() {
  }

  public Section(final Line line, final Station upStation, final Station downStation, final Long distance) {
      this.line = line;
      this.upStation = upStation;
      this.downStation = downStation;
      this.distance = distance;
      this.sequence = line.getNextSequence();
  }

  @Override
  public int compareTo(Section o) {
        return Long.compare(this.sequence, o.sequence);
  }

  public Long getId() {
    return id;
  }

  public Line getLine() {
    return line;
  }

  public Station getUpStation() {
    return upStation;
  }

  public Station getDownStation() {
    return downStation;
  }

  public Long getDistance() {
    return distance;
  }

  public Long getSequence() {
      return sequence;
  }

  public List<Station> getStations() {
    return List.of(upStation, downStation);
  }
}
