package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.LineUpdateRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String color;

    private long upStationId;

    private long downStationId;

    @Getter(AccessLevel.NONE)
    private long distance;

    @Builder
    public Line(String name, String color, long upStationId, long downStationId, long distance ) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void update(LineUpdateRequest request) {
        this.name = request.getName();
        this.color = request.getColor();
    }
}
