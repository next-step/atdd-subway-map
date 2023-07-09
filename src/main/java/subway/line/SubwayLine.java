package subway.line;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.StringUtils;
import subway.line.section.LineSection;

@Entity
@Table(name = "subway_line")
@Builder
@AllArgsConstructor
public class SubwayLine {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long lineId;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false)
  private String name;

  @OneToMany
  @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
  @Builder.Default
  private Set<LineSection> sections = new HashSet<>();

  public SubwayLine() {}

  public Long getLineId() {
    return lineId;
  }

  public String getColor() {
    return color;
  }

  public String getName() {
    return name;
  }

  public void editLine(SubwayLineEditRequest request) {
    if (StringUtils.hasText(request.getName())) {
      this.name = request.getName();
    }

    if (StringUtils.hasText(request.getColor())) {
      this.color = request.getColor();
    }
  }
}

