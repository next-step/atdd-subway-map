package subway.api.domain.model.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.domain.dto.inport.LineCreateCommand;
import subway.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 20, nullable = false)
	private String color;

	@Embedded
	private Sections sections = new Sections();

	public static Line from(LineCreateCommand createCommand) {
		return ModelMapperBasedObjectMapper.convert(createCommand, Line.class);
	}

	public Line updateName(String name) {
		this.name = name;
		return this;
	}

	public Line updateColor(String color) {
		this.color = color;
		return this;
	}

	public boolean isContainsAnyStation(Long stationId) {
		return sections.isContainsAnyStation(stationId);
	}

	public void addSection(Section section) {
		this.sections.addSection(section);
		section.setLine(this);			// 연관관계 매핑
	}

	public boolean isSectionCountBelowThreshold(long size) {
		return sections.isSizeBelow(size);
	}

	public void removeLastSection() {
		sections.removeLastSection();
	}

	public boolean isNotDownEndStation(Long stationId) {
		return !isDownEndStation(stationId);
	}

	public boolean isDownEndStation(Long stationId) {
		return sections.isDownEndStation(stationId);
	}

	public List<Station> parseStations() {
		return this.sections.parseStations();
	}
}
