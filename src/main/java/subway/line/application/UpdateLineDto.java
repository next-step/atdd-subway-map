package subway.line.application;

import subway.line.presentation.UpdateLineRequest;

public class UpdateLineDto {
    private String name;
    private String color;

    public UpdateLineDto(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static UpdateLineDto from(UpdateLineRequest lineRequest) {
        return new UpdateLineDto(
                lineRequest.getName(),
                lineRequest.getColor()
        );
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
