package subway.line.dto;

public class UpdateLineDto {
    private String name;
    private String color;

    private UpdateLineDto(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public static UpdateLineDto from(UpdateLineRequest updateLineRequest) {
        return new UpdateLineDto(
                updateLineRequest.getName(),
                updateLineRequest.getColor()
        );
    }
}
