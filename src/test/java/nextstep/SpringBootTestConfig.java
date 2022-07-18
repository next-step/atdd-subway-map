package nextstep;


import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import nextstep.subway.acceptance.util.DataBaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class SpringBootTestConfig {
    @LocalServerPort
    int port;

    @Autowired
    private DataBaseCleaner dataBaseCleaner;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }

   @AfterEach
   void tearDown(){
        dataBaseCleaner.execute();
   }
}
