package subway;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(
    value = { AcceptanceTestExecutionListener.class },
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface AcceptanceTest {
}
