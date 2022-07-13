package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class LineRequest {
    @NotBlank(message = "name 공백 입력 불가")
    private String name;
    @NotBlank(message = "color 공백 입력 불가")
    private String color;

    private Long upStationId;

    private Long downStationId;

    @Min(value = 1, message = "1~30 사이의 값을 입력해주세요.")
    @Max(value = 30 , message = "distance는 30보다 클 수 없습니다.")
    private Integer distance;
}
