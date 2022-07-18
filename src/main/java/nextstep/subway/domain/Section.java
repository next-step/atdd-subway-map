package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "line_id")
	private Line line;
	@NonNull
	private Long upStationId;
	@NonNull
	private Long downStationId;
	@NonNull
	private Long distance;

	public Section(Line line, Long upStationId, Long downStationId, Long distance){
		this.line = line;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}
}
