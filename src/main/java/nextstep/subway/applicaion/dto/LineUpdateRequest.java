package nextstep.subway.applicaion.dto;

public class LineUpdateRequest {
    private Long id;
    private String name;
    private String color;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
