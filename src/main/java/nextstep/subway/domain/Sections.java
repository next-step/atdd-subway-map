package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.hibernate.annotations.OrderBy;

@Getter
@Embeddable
public class Sections {

  @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy(clause = "id asc")
  private List<Section> sections = new ArrayList<>();

  public void addSection(Section section) {
    this.sections.add(section);
  }
}
