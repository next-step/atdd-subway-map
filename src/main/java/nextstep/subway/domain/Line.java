package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nextstep.subway.exception.SubwayException;

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
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
	private List<Section> sections = new ArrayList<>();

	@Builder
	public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
		this.name = name;
		this.color = color;
		addSection(upStationId, downStationId, distance);
	}

	public void update(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void addSection(Long upStationId, Long downStationId, Long distance) {
		validateAddSection(upStationId);
		this.sections.add(new Section(this, upStationId, downStationId, distance));
	}

	private void validateAddSection(Long upStationId) {
		if (this.sections.isEmpty()) {
			return;
		}

		if (!Objects.equals(upStationId, lastStationId())) {
			throw new SubwayException("not the last station id");
		}
	}

	public Section subSection(Long downStationId) {
		validateSubSection(downStationId);

		int lastIndex = this.sections.size() - 1;
		Section lastSection = this.sections.get(lastIndex);
		this.sections.remove(lastSection);

		return lastSection;
	}

	private void validateSubSection(Long downStationId) {
		if (this.sections.size() <= 1) {
			throw new SubwayException("no longer delete sections");
		}

		if (!Objects.equals(downStationId, lastStationId())) {
			throw new SubwayException("not the last station id");
		}
	}

	private Long lastStationId() {
		return this.sections.get(this.sections.size() - 1).getDownStationId();
	}
}
