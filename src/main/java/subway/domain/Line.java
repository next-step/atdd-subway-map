package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "LINE")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String name;
    @Column(length = 20)
    private String color;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        this.name = name;
        this.color = color;

        final Section section = new Section(this, upStation, downStation, distance);
        this.sections.add(section);
    }

    protected Line() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        Station lastUpStation = this.sections.stream().findFirst().get().getUpStation();
        while (true) {
            final Station finalUpStation = lastUpStation;
            final Optional<Section> findSection = this.sections.stream()
                    .filter(section -> section.getDownStation().equals(finalUpStation))
                    .findAny();
            if (findSection.isEmpty()) break;
            lastUpStation = findSection.get().getUpStation();
        }
        final List<Station> stations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stations.add(0, lastUpStation);
        return stations;
    }

    public void update(final String name, final String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        final Section section = new Section(this, upStation, downStation, distance);
        this.sections.add(section);
    }
}
