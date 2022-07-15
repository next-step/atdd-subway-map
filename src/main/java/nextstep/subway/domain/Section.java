package nextstep.subway.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * @author a1101466 on 2022/07/13
 * @project subway
 * @description
 */
@Entity
@ToString
@Getter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @OneToOne
    public Station upStation;

    @OneToOne
    public Station downStation;

    private Integer distance;

    protected Section() {
    }

    public Section(Integer distance, Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("상행종점역과 하행종점역의 아이디는 같을 수 없습니다.");
        }
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }


    public void setLine(Line line) {
        this.line = line;
    }

    public List<Station> getStations() {
        return List.of(upStation, downStation);
    }

}
