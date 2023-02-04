package subway.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.common.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LineRequest {

    @Comment("노선 이름")
    @NotBlank
    private String name;

    @Comment("노선 색")
    @NotBlank
    private String color;

    @Comment("노선 시작역")
    @NotNull
    private Long upStationId;

    @Comment("노선 종료역")
    @NotNull
    private Long downStationId;

    @Comment("노선 거리")
    @NotNull
    private Integer distance;
}
