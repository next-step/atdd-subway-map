package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Long lineId;

  @Column
  private Long upStationId;

  @Column
  private Long downStationId;

  @Column(nullable = false)
  private int distance;

  public Section(Long id, Long upStationId, Long downStationId, int distance) {
    this.id = id;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public Section(Long upStationId, Long downStationId, int distance) {
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  protected Section() {
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public int getDistance() {
    return distance;
  }

  public void registerLine(final Long lineId) {
    this.lineId = lineId;
  }

}
