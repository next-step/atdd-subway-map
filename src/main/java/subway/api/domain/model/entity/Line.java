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
	 */
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
