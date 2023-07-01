package subway;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import subway.controller.ApiRequestBody;

import java.util.Map;


public final class AcceptanceTestBuilder {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private RequestSpecification requestSpecification;
    private Response response;


    private AcceptanceTestBuilder() {
        this.requestSpecification = RestAssured.given().log().all();
    }

    private AcceptanceTestBuilder(String baseUrl) {
        this.requestSpecification = RestAssured.given().baseUri(baseUrl).log().all();
    }

    public static AcceptanceTestBuilder given() {
        return new AcceptanceTestBuilder();
    }

    public static AcceptanceTestBuilder given(String baseUrl) {
        return new AcceptanceTestBuilder(baseUrl);
    }

    public <T extends ApiRequestBody> AcceptanceTestBuilder body(T requestBody) {
        String jsonString = convertToJsonString(requestBody);
        this.requestSpecification = this.requestSpecification.body(jsonString);
        return this;
    }

    public <M extends Map> AcceptanceTestBuilder body(M params) {
        this.requestSpecification = this.requestSpecification.body(params);
        return this;
    }

    public AcceptanceTestBuilder when(HttpMethod httpMethod, String uri) {
        this.requestSpecification = this.requestSpecification.when();
        switch (httpMethod) {
            case POST:
                this.response = this.requestSpecification.post(uri);
                break;
            case PUT:
                this.response = this.requestSpecification.put(uri);
                break;
            case PATCH:
                this.response = this.requestSpecification.patch(uri);
                break;
            case GET:
                this.response = this.requestSpecification.get(uri);
                break;
            case DELETE:
                this.response = this.requestSpecification.delete(uri);
                break;
            default:
                break;
        }
        return this;
    }

    public AcceptanceTestBuilder when(HttpMethod httpMethod) {
        this.requestSpecification = this.requestSpecification.when();
        switch (httpMethod) {
            case POST:
                this.response = this.requestSpecification.post();
                break;
            case PUT:
                this.response = this.requestSpecification.put();
                break;
            case PATCH:
                this.response = this.requestSpecification.patch();
                break;
            case GET:
                this.response = this.requestSpecification.get();
                break;
            case DELETE:
                this.response = this.requestSpecification.delete();
                break;
            default:
                break;
        }
        return this;
    }

    public AcceptanceTestBuilder contentType(ContentType contentType) {
        this.requestSpecification = requestSpecification.contentType(contentType);
        return this;
    }

    public ValidatableResponse then(HttpStatus httpStatus) {
        return this.response.then().log().all().assertThat().statusCode(httpStatus.value());
    }


    public static <T extends ApiRequestBody> String convertToJsonString(T requestBody) {
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
