package nextstep.subway.line.domain;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.common.BaseEntity;
import javax.persistence.*;
import nextstep.subway.common.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }


    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(Station upStation,Station downStation,int distance) {
        validateSection(this,upStation,downStation);
        sections.add(new Section(this,upStation,downStation,distance));
    }

    private void validateSection(Line line,Station upStation, Station downStation){
        boolean isValidUpStation =  !line.getSections().get(line.getSections().size()-1)
            .getDownStation().equals(upStation);
        boolean isValidDownStation = line.getSections().stream()
            .anyMatch(section -> section.getUpStation().equals(downStation) || section.getDownStation().equals(downStation));
        if(line.getSections().size() == 0) return;
        if(isValidUpStation) { throw new InvalidSectionException("상행역은 현재 노선의 하행 종점역이어야 합니다.");}
        if(isValidDownStation){ throw new InvalidSectionException("하행역은 노선에 이미 등록되어 있습니다."); }
    }

    public void removeSection(int index) {
        sections.remove(index);
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
        return sections;
    }
}