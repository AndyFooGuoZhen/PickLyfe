package picklyfe.registration;

import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import picklyfe.registration.Announcement.Announcement;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.User.UserRepository;
import picklyfe.registration.Events.Event;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class Testing {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Autowired
    UserRepository userRepository;

    //=====================================================================================================================================
    //User Test
    //=====================================================================================================================================
    @Test
    public void testCreateUser() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                post("/user/post/test_user@gmail.com/test_user_name/test_user_password");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.asString();
        assertEquals("Successful creating user: test_user_name", returnString);
    }

    @Test
    public void testUserEmailChange() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                put("/user/updateEmail/1/name1@gmail.com");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("Successful updated email to: name1@gmail.com", returnString);
    }

    @Test  // run this for each test-case in the above collection
    public void testUserNameChange() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/user/updateName/1/newTestName");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("Successful updated username to: newTestName", returnString);
    }

    @Test  // run this for each test-case in the above collection
    public void testUserPasswordChange() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/user/updatePassword/1/newTestPassword/newTestPassword");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("Successful updated user password", returnString);
    }

    @Test  // run this for each test-case in the above collection
    public void testUserRoleChange() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/user/updateRole/1/Admin");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("Successful updated user role", returnString);
    }

    @Test
    public void testGetAllUser() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/user/all");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        JSONArray userArr = new JSONArray(returnString);
        assertEquals(5 , userArr.length());
    }

    @Test
    public void testDeleteUser() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                delete("/user/delete/name5/password5");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        assertEquals("Successful deleted user", returnString);
    }

    //=====================================================================================================================================
    //UserProfile Test - Chen Yu Goh
    //=====================================================================================================================================
    @Test
    public void createUserProfileTest() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                post("/userprofile/post/test_user_profile");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Successful creating user: test_user_profile", returnString);
    }

    @Test
    public void TestUserProfileNameChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/name/editedName");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("newName edited to editedName", returnString);
    }

    @Test
    public void TestUserProfileHighScoreChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/hs/2000");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("highScore edited to 2000", returnString);
    }

    @Test
    public void TestUserProfileLongestEventChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/les/6");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("LongestEventSurvived edited to 6", returnString);

    }

    @Test
    public void TestUserProfileAboutMeChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/am/reeee");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("aboutMe edited to reeee", returnString);
    }

    @Test
    public void TestUserProfileHoursPlayedChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/hp/2.5");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("hoursPlayed edited to 2.5", returnString);

    }

    @Test
    public void testGetAllUserProfile() throws Throwable {

        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/userprofile/all");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        JSONArray userArr = new JSONArray(returnString);
        assertEquals(5, userArr.length());
    }

    @Test
    public void TestUserProfilePerkChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/perk/11");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("User was given Apple", returnString);
    }

    @Test
    public void TestUserProfilePerkEquippedChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/userprofile/1/perkID/11");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("User has equipped Apple", returnString);
    }


    //=====================================================================================================================================
    //Perks Test - Chen Yu Goh
    //=====================================================================================================================================

    @Test
    public void TestPerk1Detail() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                get("/perk/use/1");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Successful selection", returnString);
    }

    @Test
    public void TestPerk2Detail() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                get("/perk/use/2");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Successful selection", returnString);
    }

    @Test
    public void TestPerk3Detail() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                get("/perk/use/3");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Successful selection", returnString);
    }

    @Test
    public void TestPerk12Detail() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                get("/perk/use/12");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Successful selection", returnString);
    }

    @Test
    public void TestPerkNameChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/name/1/PerkTest");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's name at 1 successfully updated to PerkTest", returnString);
    }

    @Test
    public void TestPerkRarityChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/rarity/1/5");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's rarity at 1 successfully updated to 5 rarity", returnString);
    }

    @Test
    public void TestPerkDescriptionChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/description/1/testing");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's description at 1 successfully updated to testing", returnString);
    }

    @Test
    public void TestPerkScoreMultiplierChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/scoremultiplier/1/1.9");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's score multiplier at 1 successfully updated to 1.9", returnString);
    }

    @Test
    public void TestPerkStatusMultiplierChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/statusmultiplier/1/0.5");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's status multiplier at 1 successfully updated to 0.5", returnString);
    }

    @Test
    public void TestPerkReviveChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/revive/1/3");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's revive at 1 successfully updated to 3", returnString);
    }

    @Test
    public void TestPerkIgnoreDeathChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/perk/ignoredeath/1/4");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk's ignore death at 1 successfully updated to 4", returnString);
    }

    @Test
    public void TestPerkDeleteChange() {
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                delete("/perk/delete/4");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Perk at 4 successfully deleted", returnString);

    }


    //=====================================================================================================================================
    //Settings Test - Chen Yu Goh
    //=====================================================================================================================================

    @Test
    public void TestSettingsVolumeChange() throws Throwable{
        // Send request and receive response
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                put("/setting/volume/1/2");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Volume setting successfully changed to 2", returnString);
    }

    //=====================================================================================================================================
    //Events Test
    //=====================================================================================================================================
    @Test
    public void testNewEvent() throws Throwable {
//        Response response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                post("/event/post/testEvent/testEventDesc/testOption1Desc/10,10,-10,-10/testOption2Desc/-10,-10,10,10");

        Response response = RestAssured.given().post("/event/post/testEvent/testEventDesc/testOption1Desc/10,10,-10,-10/testOption2Desc/-10,-10,10,10");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.body().asString();
        assertTrue(returnString.contains("testEvent"));
        assertTrue(returnString.contains("testEventDesc"));
        assertTrue(returnString.contains("10,10,-10,-10"));
        assertTrue(returnString.contains("testOption1Desc"));
        assertTrue(returnString.contains("-10,-10,10,10"));
        assertTrue(returnString.contains("testOption2Desc"));
    }

    @Test  // run this for each test-case in the above collection
    public void testEventDelete() throws Throwable {
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                delete("/event/delete/12");


        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body for correct response
        String returnString = response.getBody().asString();
        assertEquals("Successful deleted event", returnString);
    }

    //=====================================================================================================================================
    //Announcement Test
    //=====================================================================================================================================
    @Test  // run this for each test-case in the above collection
    public void CreateAnnouncementByString() throws Throwable {
        Response Announcementresponse = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                post("/ann/newString/FirstTest/JustTesting");

        // Check status code
        int statusCode1 = Announcementresponse.getStatusCode();
        assertEquals(200, statusCode1);

        // Check response body for correct response
        String returnString = Announcementresponse.getBody().asString();
        try {

            Response response1 = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    put("/ann/editText/1/EditedText").
                    then().
                    extract().response();

            int statusCode2 = response1.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString2 = response1.getBody().asString();
            assertTrue(returnString2.contains("Successfully edited an announcement"));

            Response response2 = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    put("/ann/editTitle/1/EditedTitle").
                    then().
                    extract().response();

            int statusCode3 = response2.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString3 = response2.getBody().asString();
            assertTrue(returnString3.contains("Successfully edited an announcement"));

            Response end = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    delete("/ann/delete/1").
                    then().
                    extract().response();

            int endStatusCode = end.getStatusCode();
            assertEquals(200, endStatusCode);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    //=====================================================================================================================================
    //Announcement Test
    //=====================================================================================================================================
    @Test  // run this for each test-case in the above collection
    public void CreatePatchByString() throws Throwable {
        Response Announcementresponse = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("hello").
                when().
                post("/patch/newString/FirstTest/JustTesting");

        // Check status code
        int statusCode1 = Announcementresponse.getStatusCode();
        assertEquals(200, statusCode1);

        // Check response body for correct response
        String returnString = Announcementresponse.getBody().asString();
        try {

            Response response1 = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    put("/patch/editText/1/EditedText").
                    then().
                    extract().response();

            int statusCode2 = response1.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString2 = response1.getBody().asString();
            assertTrue(returnString2.contains("Successfully edited a patch"));

            Response response2 = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    put("/patch/editTitle/1/EditedTitle").
                    then().
                    extract().response();

            int statusCode3 = response2.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString3 = response2.getBody().asString();
            assertTrue(returnString3.contains("Successfully edited a patch"));

            Response end = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    delete("/ann/delete/1").
                    then().
                    extract().response();

            int endStatusCode = end.getStatusCode();
            assertEquals(200, endStatusCode);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    //=====================================================================================================================================
    //Game Test
    //=====================================================================================================================================
    @Test
    public void testNewGame() {
        Response userResponse = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("Hello").
                when().
                get("/user/name/test_user_name");

        int statusCode1 = userResponse.getStatusCode();
        assertEquals(200, statusCode1);

        String returnString = userResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);

            JSONObject returnObj2 = new JSONObject(returnObj.get("userProfile").toString());

            Response response = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body(returnObj2.toString()).
                    when().
                    post("/game/post/2").
                    then().
                    extract().response();

            int statusCode2 = response.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString2 = response.getBody().asString();
            assertTrue(returnString2.contains("Successful created game for user: "));

            Response end = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body(returnObj2.toString()).
                    when().
                    put("/game/end").
                    then().
                    extract().response();

            int endStatusCode = end.getStatusCode();
            assertEquals(200, endStatusCode);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
        @Test
        public void testGameGetEvent() {
            Response userResponse = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    body("Hello").
                    when().
                    get("/user/1");

            int statusCode1 = userResponse.getStatusCode();
            assertEquals(200, statusCode1);

            String returnString = userResponse.getBody().asString();
            try {
                JSONObject returnObj = new JSONObject(returnString);

                JSONObject returnObj2 = new JSONObject(returnObj.get("userProfile").toString());

                Response response = RestAssured.given().
                        header("Content-Type", "application/json").
                        header("charset", "utf-8").
                        and().
                        body(returnObj2.toString()).
                        when().
                        post("/game/post/2").
                        then().
                        extract().response();

                int statusCode2 = response.getStatusCode();
                assertEquals(200, statusCode2);


                Response response2 = RestAssured.given().
                        header("Content-Type", "application/json").
                        header("charset", "utf-8").
                        and().
                        body(returnObj2.toString()).
                        when().
                        put("/game/getEvent").
                        then().
                        extract().response();

                int statusCode3 = response2.getStatusCode();
                assertEquals(200, statusCode3);
                assertTrue(response2.getBody() != null);

                Response end = RestAssured.given().
                        header("Content-Type", "application/json").
                        header("charset", "utf-8").
                        and().
                        body(returnObj2.toString()).
                        when().
                        put("/game/end").
                        then().
                        extract().response();

                int endStatusCode = end.getStatusCode();
                assertEquals(200, endStatusCode);

            } catch (Exception e) {
                e.printStackTrace();
                fail(e.toString());
            }
        }

    @Test
    public void testGameUpdateAndGetScore() {
        Response userResponse = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("Hello").
                when().
                get("/user/1");

        int statusCode = userResponse.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = userResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);

            JSONObject returnObj2 = new JSONObject(returnObj.get("userProfile").toString());

            String userProfileID = returnObj2.get("id").toString();

            Response response = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body(returnObj2.toString()).
                    when().
                    post("/game/post/2").
                    then().
                    extract().response();
            statusCode = response.getStatusCode();
            assertEquals(200, statusCode);

            Response response2 = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    put("/game/updateScore/"+ userProfileID +"/10" + userProfileID);
            statusCode = response2.getStatusCode();
            assertEquals(200, statusCode);
            assertTrue(response2.getBody().asString().contains("Successful updated score to: 10"));

            Response response3 = RestAssured.given().
                    header("Content-Type", "text/plain").
                    header("charset", "utf-8").
                    and().
                    body("").
                    when().
                    get("/game/score/" + userProfileID);
            statusCode = response3.getStatusCode();
            assertEquals(200, statusCode);
            assertTrue(response3.getBody() != null);

            Response end = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body(returnObj2.toString()).
                    when().
                    put("/game/end").
                    then().
                    extract().response();

            int endStatusCode = end.getStatusCode();
            assertEquals(200, endStatusCode);


        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    //=====================================================================================================================================
    // Friends Test
    //=====================================================================================================================================
    @Test
    public void testNewFriend() {
        Response userResponse = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("Hello").
                when().
                get("/user/1");

        int statusCode1 = userResponse.getStatusCode();
        assertEquals(200, statusCode1);

        String returnString = userResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);

            JSONObject returnObj2 = new JSONObject(returnObj.get("userProfile").toString());

            Response addResponse = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body(returnObj2.toString()).
                    when().
                    put("/user/add/name2").
                    then().
                    extract().response();

            int statusCode2 = addResponse.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString2 = addResponse.getBody().asString();
            assertTrue(returnString2.contains("Successfully added friend"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

    @Test
    public void testGetFriendsList() {
        Response userResponse = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("Hello").
                when().
                get("/user/1");

        int statusCode1 = userResponse.getStatusCode();
        assertEquals(200, statusCode1);

        String returnString = userResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);

            JSONObject returnObj2 = new JSONObject(returnObj.get("userProfile").toString());
            String userProfileID = returnObj2.get("id").toString();

            Response addResponse = RestAssured.given().
                    header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    and().
                    body(returnObj2.toString()).
                    when().
                    get("/user/friendList/" + userProfileID).
                    then().
                    extract().response();

            int statusCode2 = addResponse.getStatusCode();
            assertEquals(200, statusCode2);

            String returnString2 = addResponse.getBody().asString();
            assertFalse(returnString2.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }
}