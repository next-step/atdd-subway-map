package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Getter
@NoArgsConstructor
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String color;
	@NonNull
	private Long upStationId;
	@NonNull
	private Long downStationId;
	@NonNull
	private Long distance;

	@Builder
	public Line(String name, String color, Long upStationId, Long downStationId, Long distance){
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public void update(String name, String color) {
		this.name = name;
		this.color = color;
	}
}
