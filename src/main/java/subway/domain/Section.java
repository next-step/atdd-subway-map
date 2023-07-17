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
import subway.dto.SectionRequest;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lineId;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;


    public static Section from(Line line) {
        return Section.builder()
            .lineId(line.getId())
            .upStationId(line.getUpStationId())
            .downStationId(line.getDownStationId())
            .distance(line.getDistance())
            .build();
    }

    public static Section from(Long lineId, SectionRequest request) {
        return Section.builder()
            .lineId(lineId)
            .upStationId(request.getUpStationId())
            .downStationId(request.getDownStationId())
            .distance(request.getDistance())
            .build();
    }
}
