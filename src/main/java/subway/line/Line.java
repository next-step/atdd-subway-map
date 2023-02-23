package subway.line;

import org.springframework.http.HttpStatus;
import subway.exception.CustomException;
import subway.exception.ErrorDto;
import subway.station.Station;
import subway.station.StationService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    //@Column(nullable = true)
    private Long upStationId;

    //@Column(nullable = true)
    private Long downStationId;

    //@Column(nullable = true)
    private Long distance;
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId= upStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Station upStation, Station downStation, int distance){
        this.getSections().add(new Section(this, upStation, downStation, distance));
    }

    public Boolean checkStationExist(Station station){
        if (!this.getSections().get(this.getSections().size() - 1).getDownStation().equals(station)) {
            throw new CustomException(
                    new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ this.getId() +") 가 존재 하지 않습니다.")
            );
        } else {
            return false;
        }
    }

    public Boolean isStationExist(Station station){
        Boolean isExist = false;
        Long stationId = station.getId();

        for(int i=0; i<this.getSections().size(); ++i){
            Station upStation = this.getSections().get(i).getUpStation();
            Station downStation = this.getSections().get(i).getDownStation();
            Long upStationId = upStation.getId();
            Long downStationId = downStation.getId();

            if(upStation.getId() == station.getId() || downStation.getId() == station.getId()){
                isExist = true;
            }
        }

        if (isExist) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isLastSection(Long stationId){
        List<Section> sections = this.getSections();
        Section lastSection = sections.get(sections.size()-1);

        if(lastSection.getDownStation().getId() == stationId){
            return true;
        } else {
            return false;
        }
    }

    public Boolean isOnlySection(){
        List<Section> sections = this.getSections();
        if(sections.size() == 1){
            return true;
        } else {
            return false;
        }
    }
}
