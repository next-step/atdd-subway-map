package subway.line;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import subway.station.Station;

@Entity
public class Line {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne(cascade = CascadeType.ALL)
  private Station inboundStation; // 상행역

  @ManyToOne(cascade = CascadeType.ALL)
  private Station outboundStation; // 하행역

  public Line() {}

  public Line(String name, Station inboundStation, Station outboundStation) {
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
