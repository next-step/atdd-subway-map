package subway;

import lombok.Getter;

@Getter
public class LineCreateRequest {
    private String name;
    private String color;
    private int distance;
    private Long upstationId;
    private Long downstationId;

    public static Line toEntity(LineCreateRequest request, Station upstation, Station downstation) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .distance(request.getDistance())
                .upstation(upstation)
                .downstation(downstation)
                .build();
    }
}
