import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.TestData;
import service.YandexSpellerInputs;
import service.YandexSpellerRest;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

public class YandexSpellerTest {
    @DataProvider
    public Object[][] testCases1() {
        return new Object[][]{
                {"00", YandexSpellerInputs.Language.RU.getLanguage(), YandexSpellerInputs.Format.PLAIN.getFormat()},
                {"01", YandexSpellerInputs.Language.RU.getLanguage(), YandexSpellerInputs.Format.HTML.getFormat()},
                {"10", YandexSpellerInputs.Language.UK.getLanguage(), YandexSpellerInputs.Format.PLAIN.getFormat()},
                {"11", YandexSpellerInputs.Language.UK.getLanguage(), YandexSpellerInputs.Format.HTML.getFormat()},
                {"20", YandexSpellerInputs.Language.EN.getLanguage(), YandexSpellerInputs.Format.PLAIN.getFormat()},
                {"21", YandexSpellerInputs.Language.EN.getLanguage(), YandexSpellerInputs.Format.HTML.getFormat()},
        };
    }

    @Test(description = "test 1 - lowercase language and format", dataProvider = "testCases1")
    void emptyResponse1(String Number, String currentLanguage, String currentFormat) {
        RestAssured
                .given()
                .queryParam(YandexSpellerInputs.TEXT, TestData.CORRECT_TEXT)
                .queryParam(YandexSpellerInputs.LANG, currentLanguage)
                .queryParam(YandexSpellerInputs.FORMAT, currentFormat)
                .when()
                .log().everything() //logs sent stuff
                .get(YandexSpellerRest.YANDEX_SPELLER_API_URI)
                .prettyPeek() //logs received stuff
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .time(lessThan(10000L)) //ms
                .body("", Matchers.hasSize(0));
    }

    @DataProvider
    public Object[][] testCases2() {
        return new Object[][]{
                {"00", YandexSpellerInputs.Language.RU + "", YandexSpellerInputs.Format.PLAIN + ""},
                {"01", YandexSpellerInputs.Language.RU + "", YandexSpellerInputs.Format.HTML + ""},
                {"10", YandexSpellerInputs.Language.UK + "", YandexSpellerInputs.Format.PLAIN + ""},
                {"11", YandexSpellerInputs.Language.UK + "", YandexSpellerInputs.Format.HTML + ""},
                {"20", YandexSpellerInputs.Language.EN + "", YandexSpellerInputs.Format.PLAIN + ""},
                {"21", YandexSpellerInputs.Language.EN + "", YandexSpellerInputs.Format.HTML + ""},
        };
    }

    @Test(description = "test 2 - uppercase language and format", dataProvider = "testCases2")
    void emptyResponse2(String number, String currentLanguage, String currentFormat) {
        RestAssured
                .given()
                .queryParam(YandexSpellerInputs.TEXT, TestData.CORRECT_TEXT)
                .queryParam(YandexSpellerInputs.LANG, currentLanguage)
                .queryParam(YandexSpellerInputs.FORMAT, currentFormat)
                .when()
                .log().everything() //logs sent stuff
                .get(YandexSpellerRest.YANDEX_SPELLER_API_URI)
                .prettyPeek() //logs received stuff
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .time(lessThan(10000L)) //ms
                .body("", Matchers.hasSize(0));
    }

    @DataProvider
    public Object[][] testCases3() {
        return new Object[][]{
                {"1", TestData.WRONG_TEXT_1, TestData.RETURNED_TEXT_1},
                {"2", TestData.WRONG_TEXT_2, TestData.RETURNED_TEXT_2},
                {"3", TestData.WRONG_TEXT_3, TestData.RETURNED_TEXT_3},
        };
    }

    @Test(description = "test 3", dataProvider = "testCases3")
    void getResults(String number, String wrongText, String[] returnedText) {
        RestAssured
                .given()
                .queryParam(YandexSpellerInputs.TEXT, wrongText)
                .queryParam(YandexSpellerInputs.LANG, YandexSpellerInputs.Language.EN.getLanguage())
                .queryParam(YandexSpellerInputs.FORMAT, YandexSpellerInputs.Format.PLAIN)
                .when()
                .log().everything() //logs sent stuff
                .get(YandexSpellerRest.YANDEX_SPELLER_API_URI)
                .prettyPeek() //logs received stuff
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .time(lessThan(15000L)) //ms
                .body("word", is(asList(wrongText)))
                .body("s", is(asList(asList(returnedText))));
//                      .extract().response();

//        List<Object> nse = resp.jsonPath().getList("word");
    }

    @DataProvider
    public Object[][] testCases31() {
        return new Object[][]{
                {"1", TestData.WRONG_TEXT_1, TestData.RETURNED_TEXT_1},
//                {"2", TestData.WRONG_TEXT_2, TestData.RETURNED_TEXT_2},
//                {"3", TestData.WRONG_TEXT_3, TestData.RETURNED_TEXT_3},
        };
    }
    @Test(description = "test 31 - use functions and jsonPath()", dataProvider = "testCases31")
    void getResults31(String number, String wrongText, String[] returnedText) {

        HashMap<String, Object> params = new HashMap<>();
        params.put(YandexSpellerInputs.TEXT, wrongText);
        params.put(YandexSpellerInputs.LANG, YandexSpellerInputs.Language.EN);
        params.put(YandexSpellerInputs.FORMAT, YandexSpellerInputs.Format.PLAIN);

        Response resp = new YandexSpellerRest()
                .getWithParams(params);

        System.out.println(resp.jsonPath().getList("$"));
        System.out.println("hz1");
        System.out.println(resp.jsonPath().getList("$.."));
        System.out.println(resp.jsonPath().getString("code"));
        System.out.println(resp.jsonPath().getString("pos"));
        System.out.println(resp.jsonPath().getString("word"));
        System.out.println("hz2");
        System.out.println(resp.jsonPath().getString("s"));

//        assertEquals(resp.jsonPath().getString("word"), wrongText);
//        assertEquals(resp.jsonPath().getList("word").toString(), wrongText);
//        assertEquals(resp.jsonPath().getList("s"), TestData.RETURNED_TEXT_1);
//
//        List<Object> nse = resp.jsonPath().getList("word");

        System.out.println("hz");
        System.out.println(resp.jsonPath().getList("word"));
        System.out.println("hz");
        System.out.println(resp.jsonPath().getList("s"));
    }

    @Test(description = "test 4 - the whole response")
    void getXZ() {
        Response response =
                YandexSpellerRest.doGetExtract(
                        YandexSpellerRest.YANDEX_SPELLER_API_URI,
                        TestData.WRONG_TEXT_1,
                        YandexSpellerInputs.Language.EN,
                        YandexSpellerInputs.Format.PLAIN);

        List<String> jsonResponse = response.jsonPath().getList("$");
//        System.out.println(jsonResponse);
        assertEquals(jsonResponse.size(), 1, "Incorrect jsonResponse's size");
        assertEquals(jsonResponse.toString(), "[{col=0, code=1, s=[torture, tortured, torturer], len=8, pos=0, row=0, word=torturee}]");
    }

//    @Test(description = "test 5 ")
//    void getXZ2() {
//        Response response =
//                YandexSpellerRest.doGetExtract(
//                        YandexSpellerRest.YANDEX_SPELLER_API_URI,
//                        "LOnDon",
//                        YandexSpellerInputs.Language.EN,
//                        YandexSpellerInputs.Format.PLAIN);
//
//        List<String> jsonResponse = response.jsonPath().getList("$");
//        System.out.println(jsonResponse);
//        assertEquals(jsonResponse.size(), 1, "Incorrect jsonResponse's size");
////        assertEquals(jsonResponse.toString(), "[{col=0, code=1, s=[torture, tortured, torturer], len=8, pos=0, row=0, word=torturee}]");
//    }




}
