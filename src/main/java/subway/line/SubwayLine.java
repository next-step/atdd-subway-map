package subway.line;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import subway.line.section.LineSection;
import subway.line.section.SubwayLineSection;
import subway.station.Station;

@Entity
@Table(name = "subway_line")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubwayLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lineId;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false)
  private String name;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "start_station_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  private Station startStation;

  @Embedded
  private SubwayLineSection sections;

  public void editLine(SubwayLineEditRequest request) {
    if (StringUtils.hasText(request.getName())) {
      this.name = request.getName();
    }

    if (StringUtils.hasText(request.getColor())) {
      this.color = request.getColor();
    }
  }

  public List<Station> getStationsInOrder() {
    return sections.getStationsInOrder();
  }

  public LineSection addSections(LineSection newSection) {
    return sections.addSection(newSection);
  }

  public void deleteSectionsInLastStation(Station station) {
    sections.deleteStation(station);
  }
}

