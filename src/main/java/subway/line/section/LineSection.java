package subway.line.section;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import subway.line.SubwayLine;
import subway.station.Station;

@Entity
@Table(name = "line_section")
public class LineSection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sectionId;

  @Column(name = "line_id", nullable = false, insertable = false, updatable = false)
  private Long lineId;

  @Column(name = "up_station_id", nullable = false, insertable = false, updatable = false)
  private Long upStationId;

  @Column(name = "down_station_id", nullable = false, insertable = false, updatable = false)
  private Long downStationId;

  public LineSection() {
  }

  public LineSection(SubwayLine subwayLine, Station upStation, Station downStation) {
    this.subwayLine = subwayLine;
    this.upStation = upStation;
    this.downStation = downStation;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private SubwayLine subwayLine;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private Station upStation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private Station downStation;
}
