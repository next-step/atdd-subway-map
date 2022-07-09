package nextstep.subway.domain.subwayLineColor;

import nextstep.subway.domain.subwayLine.SubwayLine;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

public class SubwayLineColor {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "subway_line_color_id")
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @OneToMany(mappedBy = "color", fetch = LAZY)
    private List<SubwayLine> subwayLines;

    public SubwayLineColor() {
    }

    public SubwayLineColor(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
