package subway.subwayline;

public class ModifySubwayLineRequest {
    private String name;
    private String color;

    public ModifySubwayLineRequest(String name, String color) {
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
