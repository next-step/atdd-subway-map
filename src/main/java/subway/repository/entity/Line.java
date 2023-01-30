package subway.repository.entity;

import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("노선 이름")
    @Column(length = 20)
    private String name;

    @Comment("노선 색")
    @Column(length = 20)
    private String color;

    @Comment("노선 시작역")
    @Column(length = 20)
    private Long upStationId;

    @Comment("노선 종료역")
    private Long downStationId;

    @Comment("노선 거리")
    private int distance;

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
}
