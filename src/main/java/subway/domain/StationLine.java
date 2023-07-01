package subway.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.StationLineCreateException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "lineId")
public class StationLine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lineId;

	@Column
	private String name;

	@Column
	private String color;

	@ManyToOne(fetch = FetchType.LAZY)
	private Station upStation;

	@ManyToOne(fetch = FetchType.LAZY)
	private Station downStation;

	@Column
	private BigDecimal distance;

	@Builder
	public StationLine(String name, String color, Station upStation, Station downStation, BigDecimal distance) {
		if (upStation.equals(downStation)) {
			throw new StationLineCreateException("upStation and downStation can't be equal");
		}

		this.name = name;
		this.color = color;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}
}
