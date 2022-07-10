package nextstep.subway.lines.domain;

import nextstep.subway.stations.domain.Station;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "line", uniqueConstraints = {
        @UniqueConstraint(name = "line_name_unique", columnNames = {"name"})
})
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station downStation;

//    @Embedded
    @Column(name = "distance")
    private Distance distance;

    protected Line() {

    }

    private Line(final String name, final String color, final Station upStation, final Station downStation, Distance distance) {
        this.name = name.trim();
        this.color = color.trim();
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Line makeLine(final String name, final String color, final Station upStation, final Station downStation, final int distance) {
        validateName(name);
        validateColor(color);
        isSameStation(upStation, downStation);
        return new Line(name, color, upStation, downStation, new Distance(distance));
    }

    private static void validateName(final String name) {
        if(!StringUtils.hasLength(name.trim())) {
            throw new IllegalArgumentException("지하철 노선명은 null 이거나 빈문자열이 들어올 수 없습니다. [문자열 사이에 공백불가]");
        }
    }

    private static void validateColor(final String color) {
        if(!StringUtils.hasLength(color.trim())) {
            throw new IllegalArgumentException("지하철 노선색은 null 이거나 빈문자열이 들어올 수 없습니다. [문자열 사이에 공백불가]");
        }
    }

    private static void isSameStation(Station upStation, Station downStation) {
        if(upStation.isSame(downStation)) {
            throw new IllegalArgumentException("상행과 하행이 같습니다.");
        }
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public void changeName(final String name) {
        if(Objects.nonNull(name)) {
            validateName(name);
            this.name = name.trim();
        }
    }

    public void changeColor(final String color) {
        if(Objects.nonNull(color)) {
            validateColor(color);
            this.color = color.trim();
        }
    }
}
