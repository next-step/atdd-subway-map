package subway.line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import subway.line.section.Section;
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
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "line")
    private List<Station> stations = new ArrayList<>();

    public Line(String name, String color, int distance) {
        this(name, color, distance, new ArrayList<>());
    }

    public Line(String name, String color, int distance, List<Station> stations) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        stations.forEach(this::addStation);
    }

    public List<Long> getStationIds() {
        return stations.stream().map(Station::getId)
            .collect(Collectors.toList());
    }
    public void removeStation(Station station) {
        if (stations.contains(station)) {
            stations.remove(station);
            station.changeLine(null);
        }
    }

    public void addStation(Station station) {
        if (!stations.contains(station)) {
            stations.add(station);
            station.changeLine(this);
        }
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

    public void registerSection(Section section) {
        if (isAbleToRegister(section)) {
            section.setNumber(nextSectionNumber());
            section.setLine(this);
            sections.add(section);
        }
    }

    private int nextSectionNumber() {
        return sections.stream()
            .mapToInt(Section::getNumber)
            .max().orElse(0) + 1;
    }

    private boolean isAbleToRegister(Section section) {
        return !sections.contains(section) && isConnected(section);
    }

    private boolean isConnected(Section section) {
        return getLastSection()
            .map(lastSection -> lastSection.isConnected(section))
            .orElse(true);
    }

    private Optional<Section> getLastSection() {
        return sections.stream()
            .max(Comparator.comparing(Section::getNumber));
    }
}
