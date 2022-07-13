package nextstep.subway.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@ToString
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Long upStationId;
    @Column(nullable = false)
    private Long downStationId;
    @Column(nullable = false)
    private Integer distance;

    public Line(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line() {}

    public void modifyLine(String name, String color){
        this.name = name;
        this.color = color;
    }

    public void modifyLineValidation(String name, String color){
        if(!StringUtils.hasText(name)){
            throw new IllegalArgumentException("name을 입력하여 주십시오.");
        }
        if(!StringUtils.hasText(color)){
            throw new IllegalArgumentException("color을 입력하여 주십시오.");
        }
    }
}
