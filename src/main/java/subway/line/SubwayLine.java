package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "subway_line")
@Builder
public class SubwayLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Long upStationId;

  @Column(nullable = false)
  private Long downStationId;

  @Column(nullable = false)
  private int distance;

  public SubwayLine() {}

  public SubwayLine(Long id, String color, String name, Long upStationId, Long downStationId, int distance) {
    this.id = id;
    this.color = color;
    this.name = name;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public Long getId() {
    return id;
  }

  public String getColor() {
    return color;
  }

  public String getName() {
    return name;
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

  public void editLine(SubwayLineEditRequest request) {
    if (StringUtils.hasText(request.getName())) {
      this.name = request.getName();
    }

    if (StringUtils.hasText(request.getColor())) {
      this.color = request.getColor();
    }

    if (request.getDistance() != null) {
      this.distance = request.getDistance();
    }

    if (request.getDownStationId() != null) {
      this.downStationId = request.getDownStationId();
    }

    if (request.getUpStationId() != null) {
      this.upStationId = request.getUpStationId();
    }
  }
}

