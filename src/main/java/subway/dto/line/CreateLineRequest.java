package subway.dto.line;


import lombok.Getter;

@Getter
public class CreateLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

}
