package subway.common.util;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class RestAssuredBuilder {

    public static RequestSpecification 기본_헤더값_설정() {
        return RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(MediaType.APPLICATION_JSON_VALUE)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
