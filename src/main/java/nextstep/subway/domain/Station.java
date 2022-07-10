package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@OneToMany(mappedBy = "upStation", fetch = FetchType.LAZY)
	private List<Line> upStationList = new ArrayList<>();

	@OneToMany(mappedBy = "downStation", fetch = FetchType.LAZY)
	private List<Line> downStationList = new ArrayList<>();

	public Station() {
	}

	public Station(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Line> getUpStationList() {
		return upStationList;
	}

	public List<Line> getDownStationList() {
		return downStationList;
	}
}
