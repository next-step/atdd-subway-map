package subway.line;

import subway.line.exception.LineException;
import subway.section.Section;
import subway.section.SectionException;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int distance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station downStation;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();
    public Line() {
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
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

    public int getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(downStation);

        return stations;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void generateSection(Section section) {

        verifyDownStation(section);

        verifyAlreadyStation(section);

        this.downStation = section.getDownStation();
        this.distance = this.distance + section.getDistance();
        sections.add(section);
    }

    private void verifyDownStation(Section section) {
        boolean isNotLineDownStation = !this.downStation.getId().equals(section.getUpStation().getId());

        if (isNotLineDownStation) {
            throw new LineException("등록할 구간의 상행역이 노선에 등록되어있는 하행종점역이 아닌 경우 구간 등록이 불가능합니다.");
        }
    }

    private void verifyAlreadyStation(Section section) {
        boolean isAlreadyStation = sections.stream().anyMatch(s ->
                s.getUpStation().getId().equals(section.getDownStation().getId()) ||
                        s.getDownStation().getId().equals(section.getDownStation().getId()));

        if (isAlreadyStation) {
            throw new LineException("이미 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    public Section deleteSection(Long stationId) {
        verifySectionCount();

        Section deleteSection = verifyDeleteDownStation(stationId);

        this.downStation = deleteSection.getUpStation();
        this.distance = this.distance - deleteSection.getDistance();
        sections.remove(deleteSection);

        return deleteSection;
    }

    private void verifySectionCount() {
        if (sections.size() <= 1) {
            throw new LineException("구간이 1개인 노선의 구간은 삭제할 수 없습니다.");
        }
    }

    private Section verifyDeleteDownStation(Long stationId) {
        Section deleteSection = sections.stream().filter(s -> s.getDownStation().getId().equals(stationId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 구간입니다."));

        if (!deleteSection.getDownStation().getId().equals(downStation.getId())) {
            throw new LineException("노선의 하행종점역만 제거할 수 있습니다.");
        }
        return deleteSection;
    }
}
