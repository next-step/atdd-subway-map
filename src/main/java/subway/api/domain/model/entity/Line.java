package subway.api.domain.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.interfaces.dto.LineCreateRequest;
import subway.common.mapper.ModelMapperBasedVoMapper;

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
	 *
	 * Cascade Option 적용 이슈에 대해서
	 * - Line과 Link 사이 -> 지하철 노선이 없어진다면, 그 노선에 속한 구건(Link)도 더 이상 존재할 이유가 없으므로 이 경우 CascadeType.ALL은 적절한 옵션이다.
	 * - Line과 Station 사이 -> 노선이 삭제된다고 하더라도 연관된 지하철 역이 사라져서는 안된다. 따라서 Link 와 Station 사이에서는 Cascade 옵션을 적용하면 안 된다.
	 * => 결론적으로 현재 필드에 CascadeType.ALL 옵션 적용은 불필요하다.
	 */
	@OneToMany(mappedBy = "line",  fetch = FetchType.LAZY)
	@OrderBy("id ASC")
	private SortedSet<Link> links = new TreeSet<>();

	public static Line from(LineCreateRequest createRequest) {
		return ModelMapperBasedVoMapper.convert(createRequest,Line.class);
	}

	public void updateLink(Link link) {
		this.links.add(link);
	}

	public Line updateName(String name) {
		this.name = name;
		return this;
	}

	public Line updateColor(String color) {
		this.color = color;
		return  this;
	}
}
