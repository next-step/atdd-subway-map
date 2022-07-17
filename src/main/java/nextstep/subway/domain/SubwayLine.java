package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineModifyRequest;
import nextstep.subway.exception.AlreadyRegisterException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.exception.SameUpStationException;

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

	@OneToMany(mappedBy = "subwayLine")
	private List<Section> sectionList = new ArrayList<>();

	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public SubwayLine(Long id, Section section, String name, String color, Long upStationId, Long downStationId, Integer distance) {
		this.id = id;
		saveSection(section);
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public void modify(SubwayLineModifyRequest request) {
		this.name = request.getName();
		this.color = request.getColor();
	}

	public void saveSection(Section section) {
		sectionList.add(section);
		section.addSubwayLine(this);
		this.downStationId = section.getDownStationId();
	}

	public Section deleteSection(Long stationId) {
		Section section = sectionList.stream()
				.filter(s -> s.getDownStationId().equals(stationId))
				.findAny()
				.orElseThrow(NoSuchElementException::new);

		this.downStationId = section.getUpStationId();
		sectionList.remove(section);
		return section;
	}

	public void validate(Section section) {
		if (hasSameStation(section)) {
			throw new AlreadyRegisterException(ErrorCode.ALREADY_REGISTER_SECTION.getMessage());
		}

		if (isSameUpStation(section)) {
			throw new SameUpStationException(ErrorCode.CANNOT_REGISTER_WITH_UP_STATION.getMessage());
		}
	}

	private boolean hasSameStation(Section section) {
		return this.getDownStationId().equals(section.getDownStationId()) ||
				this.getUpStationId().equals(section.getDownStationId());
	}

	private boolean isSameUpStation(Section section) {
		return this.upStationId.equals(section.getUpStationId());
	}
}
