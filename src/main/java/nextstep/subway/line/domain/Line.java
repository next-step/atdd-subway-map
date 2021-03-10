package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.exception.InvalidSectionException;
import nextstep.subway.line.exception.NoSectionException;
import nextstep.subway.line.exception.NoSuchStationException;
import nextstep.subway.station.domain.Station;
import org.graalvm.compiler.nodes.calc.IntegerDivRemNode;
import org.springframework.data.util.Optionals;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade ={CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<Section> getSection() {
        return this.sections;
    }

    public void addSection(Section section) {
        if (!isEmptySections() && !isAppendableSection(section)) {
            throw new InvalidSectionException("Input section is invalid");
        }
        this.sections.add(section);
        section.setLine(this);
    }

    private Boolean isEmptySections() {
        return (this.sections.size() == 0);
    }

    private Boolean isAppendableSection(final Section section){
        return getLastSection().getDownStation().equals(section.getUpStation());
    }

    public Section getLastSection() {
        return this.sections
                .stream()
                .skip(this.sections.size() - 1)
                .findFirst()
                .orElseThrow(()-> new NoSectionException("No registered Section"));
    }

    public Station getUpStation() {
        return this.sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        return this.sections.get(this.sections.size()-1).getDownStation();
    }

    public Integer getLineDistance() {
        return this.sections.stream().
                reduce(0, (TotalDistance, section) -> TotalDistance + section.getDistance(), Integer::sum);
    }
}
