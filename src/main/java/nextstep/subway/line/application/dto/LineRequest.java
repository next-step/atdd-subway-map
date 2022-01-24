package nextstep.subway.line.application.dto;

import nextstep.subway.line.domain.Line;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LineRequest {
    @NotBlank(message = "노선 이름은 필수 입니다.")
    @Pattern(regexp = "^[가-힣0-9]{2,30}$", message = "노선 이름은 2자이상 30자 미만 한글과 숫자만 입력이 가능 합니다.")
    private String name;

    @NotBlank(message = "노선 색은 필수 입니다.")
    private String color;

    public LineRequest() {
    }

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
