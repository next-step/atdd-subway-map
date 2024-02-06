package subway.domain;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
}
