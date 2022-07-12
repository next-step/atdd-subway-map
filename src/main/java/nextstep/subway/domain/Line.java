package nextstep.subway.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "지하철 노선의 색은 필수 값 입니다.")
    @Length(min = 1, max = 100, message = "지하철 노선 색의 길이를 확인해주세요.")
    private String color;

    @OneToOne
    @JoinColumn(name = "upstation_id")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "downStation_id")
    private Station downStation;

    @Column(nullable = false)
    @Min(value = 1, message = "지하철 노선은 최소 1미터 이상이어야합니다.")
    private Long distance;

    protected Line() {
    }

    @Builder
    public Line(String name, String color, Long distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
