package subway.line;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import subway.line.section.Section;
import subway.line.section.Sections;
import subway.station.Station;

/**
 * 지하철 노선을 의미하는 객체
 * Line을 생성하면 바로 initStations()를 실행해 Stations를 초기화할 수 있도록 한다.
 *
 * @author JerryK026
 * @date 2023-07-13
 */
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Sections sections;
    private int distance;

    public Line() {}

    public Line(String name, String color, int distance) {
        this(null, name, color, null, distance);
    }

    public Line(Long id, String name, String color, Sections sections, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // line이 생성되고 나서 stations를 주입받는다. Section이 내부에 Line을 가지기 때문에 양방향 관계를 가지기 위한 조치
    public void initStations(Station upStation, Station downStation) {
        this.sections = new Sections(this, upStation, downStation);
    }

    public void addSection(Station upStation, Station downStation) {
        this.sections.add(this, upStation, downStation);
    }

    public Section getLastSection() {
        return this.sections.getLastSection();
    }

    public void validateSection(Station upStation, Station downStation) {
        this.sections.validate(upStation, downStation);
    }

    public void deleteSection(Station station) {
        this.sections.delete(station);
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

    public List<Station> getStations() {
        return this.sections.getStations();
    }

    public List<Section> getSections() {
        return this.sections.getSections();
    }

    public int getDistance() {
        return distance;
    }
}
