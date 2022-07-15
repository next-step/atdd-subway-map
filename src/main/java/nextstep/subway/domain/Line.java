package nextstep.subway.domain;

import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToOne
    @JoinColumn(name = "upStationId")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "downStationId")
    private Station downStation;

    private Integer distance;

    @OneToMany(mappedBy = "line")
    private List<Section> stations = new ArrayList<>();

    public Line(final String name,
                final String color,
                final Station upStation,
                final Station downStation,
                final Integer distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    protected Line() {
    }

    public void update(String name, String color){
        this.name = StringUtils.hasText(name) ? name : this.name;
        this.color = StringUtils.hasText(color) ? color : this.color;
    }
}
