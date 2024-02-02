package subway.api.domain.model.entity;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.domain.dto.inport.LineCreateCommand;
import subway.api.interfaces.dto.request.LineCreateRequest;
import subway.common.exception.SectionDeletionNotValidException;
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

	/**
	 * 현실 세계의 도메인 컨텍스트를 반영할 때 지하철 노선도에서 '라인' 부분이 중복될 수는 없음을 고려하여
	 * Set 자료구조로 선택.
	 * 또한 향후 정렬 요구 사항의 필요성 대응을 고려하여 SortedSet으로 선택.
	 * <p>
	 * Cascade Option 적용 이슈에 대해서
	 * - Line과 Section 사이 -> 지하철 노선이 없어진다면, 그 노선에 속한 구건(Section)도 더 이상 존재할 이유가 없으므로 이 경우 CascadeType.ALL은 적절한 옵션이다.
	 * - Line과 Station 사이 -> 노선이 삭제된다고 하더라도 연관된 지하철 역이 사라져서는 안된다. 따라서 Section 와 Station 사이에서는 Cascade 옵션을 적용하면 안 된다.
	 * => 결론적으로 현재 필드에 CascadeType.ALL 옵션 적용은 불필요하다.
	 */
	@OneToMany(mappedBy = "line", fetch = FetchType.LAZY)
	@OrderBy("id ASC")
	@JsonManagedReference
	private SortedSet<Section> sections = new TreeSet<>();

	public static Line from(LineCreateCommand createCommand) {
		return ModelMapperBasedObjectMapper.convert(createCommand, Line.class);
	}

	public void updateLink(Section section) {
		this.sections.add(section);
	}

	public Line updateName(String name) {
		this.name = name;
		return this;
	}

	public Line updateColor(String color) {
		this.color = color;
		return this;
	}

	public boolean isDownEndStation(Long stationId) {
		return sections.last().fetchDownStationId().equals(stationId);
	}

	public boolean notContainsStation(Long stationId) {
		return !containsStation(stationId);
	}

	public boolean containsStation(Long stationId) {
		return sections.stream().anyMatch(s -> s.fetchUpStationId().equals(stationId) || s.fetchDownStationId().equals(stationId));
	}

	public void addSection(Section section) {
		sections.add(section);
	}

	public boolean sectionCountUnder(long size) {
		return this.getSections().size() <= size;
	}

	public void removeSection(Long stationId) {
		if (sections.isEmpty()) { // || sections.size() == 1 -> 비즈니스 정책에 따른 검증은 다른 곳에서 하고 있으므로 여기서 하지 않는다
			throw new SectionDeletionNotValidException();
		}

		Section lastSection = sections.last();
		if (!lastSection.fetchDownStationId().equals(stationId)) {
			throw new SectionDeletionNotValidException();
		}

		sections.remove(lastSection);
	}

}
