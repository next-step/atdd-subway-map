package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineModifyRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

	public SubwayLine(Long id, String name, String color, Long upStationId, Long downStationId, Integer distance) {
		this.id = id;
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
}
