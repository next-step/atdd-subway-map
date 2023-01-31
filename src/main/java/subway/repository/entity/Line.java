package subway.repository.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("노선 이름")
    @Column(length = 20)
    private String name;

    @Comment("노선 색상")
    @Column(length = 20)
    private String color;

    @Comment("상행 종점역")
    private Long upStationId;

    @Comment("하행 종점역")
    private Long downStationId;

    @Comment("노선 간 거리")
    private Integer distance;

    @Builder
    private Line(String name,
                 String color,
                 Long upStationId,
                 Long downStationId,
                 Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
}
