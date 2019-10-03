package service;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerRest {//checkText()
    public static final String YANDEX_SPELLER_API_URI =
            "https://speller.yandex.net/services/spellservice.json/checkText";

    private RequestSpecification REQUEST_SPECIFICATION;

    public YandexSpellerRest() { //for all requests
        REQUEST_SPECIFICATION = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter()) //logs
                .addFilter(new ResponseLoggingFilter()).build();
    }

    public Response getWithParams(Map<String, Object> params) {
        RequestSpecification specification = given(REQUEST_SPECIFICATION);

        for (Map.Entry<String, Object> param : params.entrySet())
            specification.param(param.getKey(), param.getValue());

        return specification.get(YANDEX_SPELLER_API_URI);
    }



    public static Response doGetExtract(String uri,
                                        String textToTest,
                                        YandexSpellerInputs.Language lang,
                                        YandexSpellerInputs.Format format) {
        RestAssured.defaultParser = Parser.JSON;
        return
                RestAssured
                        .given()
                        .queryParam(YandexSpellerInputs.TEXT, textToTest)
                        .queryParam(YandexSpellerInputs.LANG, lang)
                        .queryParam(YandexSpellerInputs.FORMAT, format)
                        .when()
                        .log().everything() //logs sent stuff
                        .get(uri)
                        .prettyPeek() //logs received stuff
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .contentType(ContentType.JSON)
                        .time(lessThan(10000L)) //ms
                        .extract().response();
    }

}
