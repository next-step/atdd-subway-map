package subway.route.dto;

import subway.route.domain.Route;

public class RouteResponse {

    private Long id;

    private String name;

    public RouteResponse(Route route) {
        this.id = route.getId();
        this.name = route.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
