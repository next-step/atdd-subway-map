package subway;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Disabled
public class BaseAcceptance {

    protected static RequestSpecification REQUEST_SPEC;

    @BeforeEach
    public void setUp() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(ContentType.JSON);
        REQUEST_SPEC = reqBuilder.build();
    }


}
