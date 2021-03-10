package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.exception.ExistDownStationException;
import nextstep.subway.section.Section;
import nextstep.subway.station.domain.Station;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST)
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

    public void addSection(Section section) {
        boolean isExist = isExistDownStation(section.getDownStation());

        checkValidDownStation(isExist);

        sections.add(section);
        section.addLine(this);
    }

    private void checkValidDownStation(boolean isExist) {
        if(isExist) {
            throw new ExistDownStationException();
        }
    }

    private boolean isExistDownStation(Station downStation) {
        return sections.stream()
                .anyMatch(section -> isExistDownStation(section, downStation));
    }

    private boolean isExistDownStation(Section section, Station downStation) {
        return section.isExistStation(downStation);
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

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
