package subway.line.section;

import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;

@Entity
public class Section {
//    @EmbeddedId
//    private SectionId sectionId;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lineId")
    private Line line;
    @ManyToOne
    @JoinColumn(name = "upStationId")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "downStationId")
    private Station downStation;

    @Column
    private Long distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, Long distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    //    public Section(Long distance, Station upStation, Line line) {
//        this.distance = distance;
//        this.upStation = upStation;
//        this.line = line;
//    }
    //    public Section(Line line, Station station, Long distanceFromPrev) {
//        this(new SectionId(line.getId(), station.getId()), distanceFromPrev);
//    }

//    public Section(SectionId sectionId, Long distanceFromPrev) {
//        this.sectionId = sectionId;
//        this.distanceFromPrev = distanceFromPrev;
//    }
//
//    public static List<Section> firstSectionsOf(Line line) throws CannotAddSectionException {
//        Section up = new Section(line, line.getUpStation(), 0L);
//        Section down = new Section(line, line.getDownStation(), line.getDistance());
//
//        up.setLine(line);
//        down.setLine(line);
//        up.setStation(line.getUpStation());
//        down.setStation(line.getDownStation());
//        return List.of(up, down);
//    }
//
//    public SectionId getSectionId() {
//        return sectionId;
//    }
//
//    public Long getDistanceFromPrev() {
//        return distanceFromPrev;
//    }
//
//    @Override
//    public Object getId() {
//        return sectionId;
//    }
//
//    public Station getStation() {
//        return station;
//    }
//
//    public Line getLine() {
//        return line;
//    }
//
//    @Override
//    public boolean isNew() {
//        return createdAt == null;
//    }
//
//    public void setLine(@NotNull Line line) throws CannotAddSectionException {
//        if (this.line != null) {
//            this.line.getSections().remove(line);
//        }
//        this.line = line;
//
//        List<Section> sections = line.getSections();
//        if (sections.contains(this)) {
//            throw new CannotAddSectionException("이미 등록된 역은 구간으로 추가할 수 없습니다.");
//        }
//        sections.add(this);
//    }
//
//    public void setStation(Station station) {
//        this.station = station;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Section section = (Section) o;
//        return Objects.equals(sectionId, section.sectionId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(sectionId);
//    }
//
//    @Override
//    public String toString() {
//        return "Section{" +
//                "sectionId=" + sectionId +
//                ", distanceFromPrev=" + distanceFromPrev +
//                ", createdAt=" + createdAt +
//                '}';
//    }
}
