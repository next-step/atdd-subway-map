package subway.line;

import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import subway.line.section.LineSection;
import subway.line.section.LineSections;
import subway.section.Section;
import subway.station.Station;

@Entity
@Table(name = "LINE")
@NoArgsConstructor
@Getter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private LineSections lineSections = new LineSections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        LineSection lineSection = new LineSection(this, section);

        lineSections.addLineSection(lineSection);
    }

    public void removeLineSection(LineSection lineSection) {
        lineSections.removeLineSection(lineSection);
    }

    public void modify(LineModifyRequest lineModifyRequest) {
        String modifiedName = lineModifyRequest.getName();
        if (StringUtils.isNotBlank(modifiedName)) {
            this.name = modifiedName;
        }

        String modifiedColor = lineModifyRequest.getColor();
        if (StringUtils.isNotBlank(modifiedColor)) {
            this.color = modifiedColor;
        }
    }

    public Station getUpStation() {
        return lineSections.getUpStation();
    }

    public Station getDownStation() {
        return lineSections.getDownStation();
    }

    public Optional<LineSection> getLastLineSection() {
        return lineSections.getLastLineSection();
    }

    public int getLineSectionCount() {
        return lineSections.size();
    }
}
