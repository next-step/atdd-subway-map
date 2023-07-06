package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import subway.station.Station;

@Entity
public class LineStation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "line_id", nullable = false, insertable = false, updatable = false)
  private Long lineId;

  @Column(name = "station_id", nullable = false, insertable = false, updatable = false)
  private Long stationId;

  @ManyToOne
  @JoinColumn(name = "line_id")
  private SubwayLine subwayLine;

  @ManyToOne
  @JoinColumn(name = "station_id")
  private Station station;
}
