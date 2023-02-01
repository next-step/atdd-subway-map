package subway.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long upStationId;

	private Long downStationId;

	private int distance;

	public Section(Long upStationId, Long downStationId, int distance) {
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}
}
