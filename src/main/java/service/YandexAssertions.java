package service;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat; // cool asserts

public class YandexAssertions {

    public static void checkTimeStatusJson(Response resp) {
        assertThat(resp.time()).describedAs("time").isLessThan(10000);
        assertThat(resp.statusCode()).describedAs("status code").isEqualTo(HttpStatus.SC_OK);
        assertThat(resp.contentType()).describedAs("Content-Type").contains(ContentType.JSON.toString());
    }
}
