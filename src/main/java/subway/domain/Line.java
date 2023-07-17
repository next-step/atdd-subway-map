package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.dto.LineRequest;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    public static Line from(LineRequest request) {
        return Line.builder()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .upStationId(request.getUpStationId())
            .downStationId(request.getDownStationId())
            .build();
    }

    public void update(LineRequest request) {
        this.name = request.getName().isBlank() ? this.name : request.getName();
        this.color = request.getColor().isBlank() ? this.color : request.getColor();
        this.distance = request.getDistance() == null ? this.distance : request.getDistance();
    }

    public void updateDownStation(Section section) {
        this.downStationId = section.getDownStationId();
    }

}
