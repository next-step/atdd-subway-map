package subway.station.web.dto;

public class UpdateLineResponse {

    private String name;
    private String color;

    public UpdateLineResponse(String name, String color) {
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
