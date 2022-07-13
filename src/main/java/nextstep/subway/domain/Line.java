package nextstep.subway.domain;

import lombok.Getter;
import lombok.ToString;
import nextstep.subway.exception.BusinessException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Objects;

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

    public void vlidationSectionStation(Long reqUpstationId, Long reqDownStationId){
        if(this.downStationId != reqUpstationId)
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다");

    }

    public void modifyDownStationId(Long reqUpstationId){
        this.downStationId = reqUpstationId;
    }

    public void addSection(Section section) {
        sections.addSection(this,section);
    }

    public Section validationAndSectionDelete(Line line, Long stationId){
        return sections.validationAndSectionDelete(line, stationId);
    }

}
