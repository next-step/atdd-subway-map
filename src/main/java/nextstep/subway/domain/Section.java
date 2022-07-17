package nextstep.subway.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Section {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "downStation_id")
  private Station downStation;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "upStation_id")
  private Station upStation;

  private int distance;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "line_id")
  private Line line;

  @Builder
  public Section(Station upStation, Station downStation, int distance, Line line) {
    this.downStation = downStation;
    this.upStation = upStation;
    this.distance = distance;
    this.line = line;
  }

  public List<Station> getSectionInStation() {
    return Arrays.asList(downStation, upStation);
  }
}
