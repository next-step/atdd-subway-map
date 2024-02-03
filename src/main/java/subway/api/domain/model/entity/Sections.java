package subway.api.domain.model.entity;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.SortNatural;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Sections 일급 컬렉션으로 리팩토링하여 Line 엔티티와의 관계를 관리합니다.
 * Sections 내에서 Section 엔티티들의 생명주기를 관리하며,
 * Line 엔티티와의 연관관계를 효율적으로 관리하기 위해 사용됩니다.
 *
 * @author : Rene Choi
 * @since : 2024/02/02
 */
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sections implements Iterable<Section> {
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
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "line_id")
	@SortNatural
	private SortedSet<Section> sections = new TreeSet<>();

	@Override
	public Iterator<Section> iterator() {
		return sections.iterator();
	}

	public void addSection(Section section) {
		this.sections.add(section);
	}

	public boolean isSizeBelow(long size) {
		return this.sections.size() <= size;
	}

	public void removeLastSection() {
		this.sections.remove(lastSection());
	}

	public boolean isDownEndStation(Long stationId) {
		return lastSection().isDownEndStation(stationId);
	}

	public boolean isContainsAnyStation(Long stationId) {
		return this.sections.stream().anyMatch(section -> section.isAnyStation(stationId));
	}

	public List<Station> parseStations() {
		return this.sections.stream()
			.flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
			.distinct()
			.collect(Collectors.toList());
	}

	private Section lastSection() {
		return this.sections.last();
	}
}
