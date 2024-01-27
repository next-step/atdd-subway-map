package subway.api.domain.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Link implements Comparable<Link> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "up_station_id")
	private Station upStation;

	@ManyToOne
	@JoinColumn(name = "down_station_id")
	private Station downStation;

	@Column(nullable = false)
	private Long distance;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	/**
	 * Link의 동등성은 상행, 하행, 거리로 판단합니다.
	 * 상행역, 하행역, 거리가 모두 같을 경우 해당 Link는 같은 객체로 평가합니다.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Link link = (Link) o;
		return Objects.equals(upStation, link.upStation) &&
			Objects.equals(downStation, link.downStation) &&
			Objects.equals(distance, link.distance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(upStation, downStation, distance);
	}


	@Override
	public int compareTo(Link other) {
		return this.distance.compareTo(other.id);
	}

	public static Link of(Station upStation, Station downStation, Long distance, Line line) {
		return Link.builder()
			.upStation(upStation)
			.downStation(downStation)
			.distance(distance)
			.line(line)
			.build();
	}

	public Long fetchUpStationId() {
		return this.upStation.getId();
	}

	public Long fetchDownStationId() {
		return this.downStation.getId();
	}

	public String  fetchUpStationName() {
		return this.upStation.getName();
	}
	public String  fetchDownStationName() {
		return this.downStation.getName();
	}
}
