package subway.test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class LineResponseHelper {
    private final ExtractableResponse<Response> response;

    public LineResponseHelper(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public List<Long> extractIds() {
        return this.response.jsonPath().getList("id", Long.class);
    }

    public Long extractId() {
        return this.response.jsonPath().getLong("id");
    }


    public String extractName() {
        return this.response.jsonPath().getString("name");
    }

    public String extractColor() {
        return this.response.jsonPath().getString("color");
    }

    public Integer getStatusCode() {
        return this.response.statusCode();
    }
}
