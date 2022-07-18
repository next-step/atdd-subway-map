package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.exception.ExceptionMessages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void changeNameAndColor(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
    }

    public void checkRegisterEndpointId(long upStationId, long downEndpointStationId) {
        if (upStationId != downEndpointStationId) {
            throw new IllegalArgumentException(
                    ExceptionMessages.getNotEndpointInputExceptionMessage(upStationId, downEndpointStationId));
        }
    }

    public void checkRemoveEndPointId(long stationId, long downEndpointStationId) {
        if (downEndpointStationId != stationId) {
            throw new IllegalArgumentException(
                    ExceptionMessages.getNotEndpointInputExceptionMessage(stationId, downEndpointStationId));
        }
    }
}
