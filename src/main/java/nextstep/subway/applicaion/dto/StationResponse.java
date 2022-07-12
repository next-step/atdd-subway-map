package nextstep.subway.applicaion.dto;

public class StationResponse {
    private Long id;
    private String name;

    private StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(Long id, String name) {
        return new StationResponse(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
