package subway.line;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import subway.exception.CannotRemoveLineSectionException;
import subway.line.section.LineSection;
import subway.line.station.LineStation;
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

    private int distance;

    @OneToMany(mappedBy = "line")
    private List<LineSection> lineSections = new ArrayList<>();

    @OneToMany(mappedBy = "line")
    private List<LineStation> lineStations = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downLineStationId")
    private Station downStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upLineStationId")
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lastLineSectionId")
    private LineSection lastLineSection;

    public Line(String name, String color, int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public LineSection getLastLineSection() {
        return lastLineSection;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public void addLineStation(LineStation lineStation) {
        if (!lineStations.contains(lineStation)) {
            lineStations.add(lineStation);
            lineStation.changeLine(this);
        }
    }

    public void removeLineStation(LineStation lineStation) {
        if (lineStations.contains(lineStation)) {
            lineStations.remove(lineStation);
            lineStation.changeLine(null);
        }
    }

    public void addLineSection(LineSection lineSection) {
        if (isAbleToRegister(lineSection)) {
            lineSection.setNumber(nextSectionNumber());
            lineSection.setLine(this);
            downStation = lineSection.getDownStation();
            lastLineSection = lineSection;
            lineSections.add(lineSection);
        }
    }

    public void removeLineSection(LineSection lineSection) {
        if (isNotAbleToRemove(lineSection)) {
            throw new CannotRemoveLineSectionException();
        }

        lineSections.remove(lineSection);
        lineSection.setLine(null);
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

    private int nextSectionNumber() {
        return lineSections.stream()
            .mapToInt(LineSection::getNumber)
            .max().orElse(0) + 1;
    }

    private boolean isAbleToRegister(LineSection lineSection) {
        LineSection lastLineSection = getLastLineSection();
        return Objects.isNull(lastLineSection) || isConnected(lineSection);
    }

    private boolean isNotAbleToRemove(LineSection lineSection) {
        return !isAbleToRemove(lineSection);
    }

    private boolean isAbleToRemove(LineSection lineSection) {
        LineSection lastLineSection = getLastLineSection();

        return Objects.isNull(lastLineSection)
            || lastLineSection == lineSection;
    }

    private boolean isConnected(LineSection lineSection) {
        LineSection lastLineSection = getLastLineSection();

        return Objects.nonNull(lastLineSection)
            && lastLineSection.isConnected(lineSection);
    }
}
