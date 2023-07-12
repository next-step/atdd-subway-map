package subway.route.dto;

import subway.route.domain.Route;

public class RouteRequest {

    private Long id;
    private String name;

    public Route toEntity() {
        return new Route(id, name);
    }

    public void saveId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
