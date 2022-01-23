package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Station extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	protected Station() {
	}

	protected Station(String name) {
		this.name = name;
	}

	public static Station from(String name) {
		return new Station(name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
