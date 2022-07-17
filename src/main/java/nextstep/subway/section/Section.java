package nextstep.subway.section;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.exception.CustomException;

@Entity
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long lineId;

	private Long upStationId;

	private Long downStationId;

	private int distance;

	protected Section() {
	}

	public Section(Long lineId, Long upStationId, Long downStationId, int distance) {
		this.lineId = lineId;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public void validateAppendUpStationId(Long appendUpStationId) {
		if (!this.id.equals(appendUpStationId)) {
			throw new CustomException(SectionErrorCode.INVALID_UP_STATION);
		}
	}

	public void validateAppendDownStationId(Long appendDownStationId) {
		if (this.id.equals(appendDownStationId)) {
			throw new CustomException(SectionErrorCode.INVALID_DOWN_STATION);
		}
	}

	public boolean isIncludedStation(Long stationId) {
		return upStationId.equals(stationId) || downStationId.equals(stationId);
	}

	public void validateDeleteStationId(Long deleteStationId) {
		if (!this.id.equals(deleteStationId)) {
			throw new CustomException(SectionErrorCode.INVALID_DOWN_STATION);
		}
	}
}
