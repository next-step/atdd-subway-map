package subway.line.presentation;

import javax.validation.constraints.NotEmpty;

public class LinePatchRequest {

    @NotEmpty(message = "노선 이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "노선 색을 입력해주세요.")
    private String color;

    public LinePatchRequest() {
    }

    public LinePatchRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
