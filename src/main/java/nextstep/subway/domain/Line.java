package nextstep.subway.domain;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private Set<Section> sections = new LinkedHashSet<>();

    private String name;
    private String color;

    protected Line() {
    }
}
