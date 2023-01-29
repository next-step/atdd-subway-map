package subway.stationLine;

import javax.persistence.*;
import subway.station.Station;

@Entity
public class StationLane {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name; // 노선 번호

  @ManyToOne
  private Station inboundStation; // 상행역

  @ManyToOne
  private Station outboundStation; // 하행역

  public StationLane() {}

  public StationLane(String name, Station inboundStation, Station outboundStation) {
    this.name = name;
    this.inboundStation = inboundStation;
    this.outboundStation = outboundStation;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Station getInboundStation() {
    return inboundStation;
  }

  public Station getOutboundStation() {
    return outboundStation;
  }
}
