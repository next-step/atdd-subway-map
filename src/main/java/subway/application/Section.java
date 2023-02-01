package subway.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.InvalidSectionDistanceException;
import subway.exception.SectionErrorCode;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

	private static final int INVALID_SECTION_DISTANCE = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long upStationId;

	private Long downStationId;

	private int distance;

	public Section(Long upStationId, Long downStationId, int distance) {
		if (distance <= INVALID_SECTION_DISTANCE) {
			throw new InvalidSectionDistanceException(SectionErrorCode.INVALID_SECTION_DISTANCE);
		}

		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}
}
