package nextstep.subway.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@ToString
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    private Long upStationId;
    private Long downStationId;
    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        nameAndColorValidation(name, color);
        this.name = name;
        this.color = color;
        this.upStationId = upStation.getId();
        this.downStationId = downStation.getId();
        this.sections = new Sections(this, distance, upStation, downStation);
    }

    public List<Station> getStations() {
        return sections.getStation();
    }

    public void modifyLine(String name, String color){
        nameAndColorValidation(name, color);
        this.name = name;
        this.color = color;
    }

    public void nameAndColorValidation(String name, String color){
        if(!StringUtils.hasText(name)){
            throw new IllegalArgumentException("name을 입력하여 주십시오.");
        }
        if(!StringUtils.hasText(color)){
            throw new IllegalArgumentException("color을 입력하여 주십시오.");
        }
    }

}
