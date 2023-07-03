package subway.line;

import subway.Station;

import javax.persistence.*;

@Entity
public class LineStation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "line_id")
  private Line line;

  @ManyToOne
  @JoinColumn(name = "station_id")
  private Station station;

  public Long getId() {
    return id;
  }

  public Line getLine() {
    return line;
  }

  public Station getStation() {
    return station;
  }

  protected LineStation() {
  }

  public LineStation(Line line, Station station) {
    this.line = line;
    this.station = station;
  }

}
