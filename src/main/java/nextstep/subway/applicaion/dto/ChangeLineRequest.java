package nextstep.subway.applicaion.dto;

public class ChangeLineRequest {
    private String name;
    private String color;

    protected ChangeLineRequest() {
    }

    public ChangeLineRequest(String name, String color) {
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
