package nextstep.subway.domain.Entity;

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

	protected Section() {
		//
	}

	public Section(
					Line line,
					Station upStation,
					Station downStation,
					int distance
	) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public int getDistance() {
		return this.distance;
	}

	public static Section of(Line line, Station upStation, Station downStation, int distance) {

		return new Section(line, upStation, downStation, distance);
	}
}
