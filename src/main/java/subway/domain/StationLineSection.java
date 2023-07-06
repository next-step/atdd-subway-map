package subway.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@EqualsAndHashCode(of = "sectionId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationLineSection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sectionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "line_id")
	private StationLine line;

	@JoinColumn(name = "up_station_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Station upStation;

	@JoinColumn(name = "down_station_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Station downStation;

	@Column
	private BigDecimal distance;

	@Builder
	public StationLineSection(Station upStation, Station downStation, BigDecimal distance) {
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	//associate util method
	public void apply(StationLine line) {
		this.line = line;
	}
}
