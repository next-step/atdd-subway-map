package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    public Station getUpStation() {
        return this.sections.stream().findFirst().get().getUpStation();
    }

    public Station getDownStation() {
        return this.sections.stream().findFirst().get().getDownStation();
    }

    public void update(final String name, final String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }
}
