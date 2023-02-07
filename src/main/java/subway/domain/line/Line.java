package subway.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.color.Color;
import subway.domain.station.Station;
import subway.dto.domain.AddSectionVo;
import subway.dto.domain.CreateSectionVo;
import subway.dto.domain.DeleteSectionVo;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color", referencedColumnName = "id")
    private Color color;

    @Embedded
    private Sections sections;

    public Line(String name, Color color) {
        this.name = new Name(name);
        this.color = color;
        this.sections = new Sections();
    }

    public void createSection(CreateSectionVo createSectionVo) {
        sections.createSection(createSectionVo);
    }

    public void deleteAllSection() {
        sections.deleteAllSection();
    }

    public void deleteSection(DeleteSectionVo deleteSectionVo) {
        sections.deleteSection(deleteSectionVo);
    }

    public void addSection(AddSectionVo addSectionVo) {
        sections.addSection(addSectionVo);
    }

    public List<Station> getStationsByAscendingOrder() {
        return sections.getStationsByAscendingOrder();
    }

    public void updateNameAndColor(String name, Color color) {
        this.name = new Name(name);
        this.color = color;
    }

    public void checkLineExtendValid(Station upStation, Station downStation) {
        sections.checkSectionExtendValid(upStation, downStation);
    }

    public void checkLineReduceValid(Station station) {
        sections.checkSectionReduceValid(station);
    }

    public String getNameValue() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return Objects.equals(getId(), line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
