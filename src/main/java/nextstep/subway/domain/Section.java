package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Section {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "line_id")
  private Line line;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "up_station_id")
  private Station upStation;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "down_station_id")
  private Station downStation;

  private int distance;
}