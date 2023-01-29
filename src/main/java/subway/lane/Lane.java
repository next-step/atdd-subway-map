package subway.lane;

import javax.persistence.*;
import subway.station.Station;

@Entity
public class Lane {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name; // 노선 번호

  @ManyToOne(cascade = CascadeType.ALL)
  private Station inboundStation; // 상행역

  @ManyToOne(cascade = CascadeType.ALL)
  private Station outboundStation; // 하행역

  public Lane() {}

  public Lane(String name, Station inboundStation, Station outboundStation) {
    this.name = name;
    this.inboundStation = inboundStation;
    this.outboundStation = outboundStation;
  }

  public Long getId() {
    return id;
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setInboundStation(Station inboundStation) {
    this.inboundStation = inboundStation;
  }

  public void setOutboundStation(Station outboundStation) {
    this.outboundStation = outboundStation;
  }
}
