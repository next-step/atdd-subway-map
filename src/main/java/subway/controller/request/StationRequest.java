package subway.controller.request;

import subway.controller.ApiRequestBody;

@ApiRequestBody
public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }
}
