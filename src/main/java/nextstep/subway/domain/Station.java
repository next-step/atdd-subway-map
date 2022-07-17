package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne()
    @JoinColumn(name = "section_id")
    private Section section;

    protected Station() {
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

	public void addSection(Station downStation, Integer distance) {
        this.section = new Section(downStation, distance);
	}

    public Section getSection() {
        return section;
    }
}
