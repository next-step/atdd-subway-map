package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineModifyRequest;
import nextstep.subway.exception.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "subwayLine", cascade = CascadeType.ALL)
	private List<Section> sectionList = new ArrayList<>();

	private String name;
	private String color;

	public SubwayLine(Long id, Section section, String  name, String color) {
		this.id = id;
		saveSection(section);
		this.name = name;
		this.color = color;
	}

	public void modify(SubwayLineModifyRequest request) {
		this.name = request.getName();
		this.color = request.getColor();
	}

	public void saveSection(Section section) {
		sectionList.add(section);
		section.addSubwayLine(this);
	}

	public Section deleteSection(Long stationId) {
		Section section = sectionList.stream()
				.filter(s -> s.getDownStationId().equals(stationId))
				.findAny()
				.orElseThrow(NoSuchElementException::new);

		sectionList.remove(section);
		return section;
	}

	public void validateOnSave(Section section) {
		if (hasSameStation(section)) {
			throw new AlreadyRegisterException(ErrorCode.ALREADY_REGISTER_SECTION.getMessage());
		}

		if (isSameUpStation(section)) {
			throw new SameUpStationException(ErrorCode.CANNOT_REGISTER_WITH_UP_STATION.getMessage());
		}
	}

	public void validateOnDelete(Long stationId) {
		if (isSectionSizeEqualsOne()) {
			throw new CannotDeleteUniqueSectionException(ErrorCode.CANNOT_DELETE_UNIQUE_SECTION.getMessage());
		}

		if (isNotSameDownStationId(stationId)) {
			throw new CannotDeleteException(ErrorCode.CANNOT_DELETE_WITH_NOT_SAME_DOWN_STATION.getMessage());
		}
	}

	private boolean hasSameStation(Section section) {
		return downStationIdOfLastSection().equals(section.getDownStationId()) ||
				upStationIdOfLastSection().equals(section.getDownStationId());
	}

	private boolean isSameUpStation(Section section) {
		return upStationIdOfLastSection().equals(section.getUpStationId());
	}

	private boolean isSectionSizeEqualsOne() {
		return this.getSectionList().size() == 1;
	}

	private boolean isNotSameDownStationId(Long stationId) {
		return !downStationIdOfLastSection().equals(stationId);
	}

	private Long downStationIdOfLastSection() {
		return lastSection().getDownStationId();
	}

	private Long upStationIdOfLastSection() {
		return lastSection().getUpStationId();
	}

	private Section lastSection() {
		return this.getSectionList().get(sectionList.size() - 1);
	}
}
