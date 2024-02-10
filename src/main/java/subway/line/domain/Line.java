package subway.line.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import subway.exception.AlreadyExistDownStationException;
import subway.exception.InvalidSectionUpStationException;
import subway.section.domain.Section;
import subway.station.domain.Station;
import subway.station.service.StationDto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE line SET deleted_at = CURRENT_TIMESTAMP where line_id = ?")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    @Column
    private Integer distance;

    @Column
    private Timestamp deleted_at;

    protected Line() {
    }

    private Line(String name, String color, Integer distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public static Line from(String name, String color, Integer distance) {
        return new Line(name, color, distance);
    }

    public void updateLine(String color, Integer distance) {
        this.color = color;
        this.distance = distance;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();

        for (Section section : this.sections) {
            stations.add(section.getUpStation());
        }

        stations.add(this.sections.get(this.sections.size()-1).getDownStation());

        return stations;
    }

    public void registerSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }

    public void addSection(Section section) {
        validAddSection(section);
        registerSection(section);
    }

    private void validAddSection(Section section) {
        if (!this.sections.get(this.sections.size() - 1).checkAddStation(section.getUpStation())) {
            throw new InvalidSectionUpStationException();
        }

        boolean isExistDownStation = getStations().stream()
                .anyMatch(st ->
                        st.equals(section.getDownStation())
                );

        if (isExistDownStation) {
            throw new AlreadyExistDownStationException();
        }
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

    public Integer getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Line line = (Line) o;
        return Objects.equals(id, line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
