package subway.line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import subway.exception.CannotRemoveLineSectionException;
import subway.line.section.LineSection;
import subway.line.station.LineStation;

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

    public Line(String name, String color, int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public List<Long> getStationIds() {
        return lineStations.stream().map(subway.line.station.LineStation::getStationId)
            .collect(Collectors.toList());
    }


    public void addLineStation(LineStation lineStation) {
        if (!lineStations.contains(lineStation)) {
            lineStations.add(lineStation);
            lineStation.changeLine(this);
        }
    }

    public void addLineStations(List<LineStation> lineStations) {
        lineStations.forEach(this::addLineStation);
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
            lineSections.add(lineSection);
        }
    }

    public void removeLineSection(Long downStationId) {
        if (isNotAbleToRemove(downStationId)) {
            throw new CannotRemoveLineSectionException();
        }

        removeLastLineSection();
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

    private boolean isAbleToRegister(LineSection section) {
        return !lineSections.contains(section) && isConnected(section);
    }

    private boolean isNotAbleToRemove(LineSection lineSection) {
        return !isAbleToRemove(lineSection);
    }

    private boolean isNotAbleToRemove(Long downStationId) {
        return !isAbleToRemove(downStationId);
    }

    private boolean isAbleToRemove(LineSection lineSection) {
        Optional<LineSection> optionalLastLineSection = getLastLineSection();

        return lineSections.contains(lineSection)
            && optionalLastLineSection.isPresent()
            && optionalLastLineSection.get() == lineSection;
    }

    private boolean isAbleToRemove(Long downStationId) {
        Optional<LineSection> optionalLastLineSection = getLastLineSection();

        return optionalLastLineSection.isPresent()
            && Objects.equals(optionalLastLineSection.get().getDownStationId(), downStationId);
    }

    private boolean isConnected(LineSection lineSection) {
        return getLastLineSection()
            .map(lastSection -> lastSection.isConnected(lineSection))
            .orElse(true);
    }

    public Optional<LineSection> getLastLineSection() {
        return lineSections.stream()
            .max(Comparator.comparing(LineSection::getNumber));
    }

    private void removeLastLineSection() {
        getLastLineSection()
            .ifPresent(this::removeLineSection);
    }
}
