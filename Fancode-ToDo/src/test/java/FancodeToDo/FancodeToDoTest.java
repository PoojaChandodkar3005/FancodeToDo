package FancodeToDo;

import java.util.Iterator;
import java.util.List;
import static io.restassured.RestAssured.given;
import  io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
public class FancodeToDoTest {
	

	    private static final String BASE_URL = "http://jsonplaceholder.typicode.com";

	    @Test
	    public void testUserTodoCompletion() {
	        // Step 1: Fetch all users
	        Response usersResponse = given()
	        		.when()
	                .get(BASE_URL + "/users")
	                .then()
	                .statusCode(200)
	                .extract().response();
	    //  System.out.println("Printinting All the users");
	       // usersResponse.prettyPrint();
	        List<Integer> userIds = usersResponse.jsonPath().getList("id");

	        for (Integer userId : userIds) {
	            // Step 2: Fetch user details
	            Response userResponse = given()
	                    .when()
	                    .get(BASE_URL + "/users/" + userId)
	                    .then()
	                    .statusCode(200)
	                    .extract().response();
	            //System.out.println("Printing UsersID ");
	           // userResponse.prettyPrint();

	            // Get user location
	            double lat = userResponse.jsonPath().getDouble("address.geo.lat");
	            double lng = userResponse.jsonPath().getDouble("address.geo.lng");
System.out.println(lat);
System.out.println(lng);
	            // Check if the user belongs to FanCode city
	            if (lat >= -40 && lat <= 5 && lng >= 5 && lng <= 100) {
	                // Step 3: Fetch user's todos
	                Response todosResponse = given()
	                        .when()
	                        .get(BASE_URL + "/todos?userId=" + userId)
	                        .then()
	                        .statusCode(200)
	                        .extract().response();
	               // todosResponse.prettyPrint();

	                List<Boolean> completionStatus = todosResponse.jsonPath().getList("completed");
//Iterator i = completionStatus.iterator();
//if(i.hasNext()) {
//	System.out.println(i.next());
//}
//	                long completedCount = completionStatus.stream().filter(Boolean::booleanValue).count();
	             // Count completed tasks
	                long completedCount = 0;
	                for (boolean status : completionStatus) {
	                    if (status) {
	                        completedCount++;
	                    }
	                }

	                double completionPercentage = (double) completedCount / completionStatus.size() * 100;

	                // Step 4: Assert that the completion percentage is greater than 50%
	                //System.out.println(completionPercentage+ "User ID "+userId);
	                Assert.assertTrue(completionPercentage > 50, "User ID " + userId + " has a completion percentage of " + completionPercentage);
	            }
	        }
	    }
	}

