package subway;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.web.server.LocalServerPort;

@AcceptanceTest
@Disabled
public class BaseAcceptance {

    protected static RequestSpecification REQUEST_SPEC;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void setRequestSpec() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.setPort(port);
        REQUEST_SPEC = reqBuilder.build();
    }

}
