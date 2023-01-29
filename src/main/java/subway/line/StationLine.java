package subway.line;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * StationLine
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(length = 5, nullable = false)
    private Long upStationId;

    @Column(length = 5, nullable = false)
    private Long downStationId;

    @Column(length = 10, nullable = false)
    private Integer distance;

}
