package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubwayLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String color;

	private Long upStationId;

	private Long downStationId;

	@OneToMany(mappedBy = "subwayLine", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<SubwayLineStationGroup> subwayLineStationGroups = new ArrayList<>();

	public SubwayLine(
		String name,
		String color,
		Long upStationId,
		Long downStationId,
		List<Station> stations) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;

		createSubwayLineStationGroups(stations);
	}

	private void createSubwayLineStationGroups(List<Station> stations) {
		for (Station station : stations) {
			SubwayLineStationGroup subwayLineStationGroup = new SubwayLineStationGroup(station, this);
			this.subwayLineStationGroups.add(subwayLineStationGroup);
		}
	}

	public void updateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}
}
