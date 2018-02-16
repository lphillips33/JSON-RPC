import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

/*
 * https://stackoverflow.com/questions/14024625/
 * how-to-get-httpclient-returning-status-code-
 * and-response-body
 *
 * 
 * https://stackoverflow.com/questions/16701521/how-
 * can-i-tell-when-httpclient-execute-is-finished-fetching
 * -all-content-in-a-l
 * 
 */


/*
 * https://stackoverflow.com/questions/12364555/how-i-can-i-display
 * -all-the-http-headers-when-using-the-defaulthttpclient
 */

/*
 * 
 * https://stackoverflow.com/questions/7181534/http-post-using-json-in-java
 */

public class HttpMethods {


	public HttpMethods() {
		//contructor
	}

	//change name of parameter to filter
	public  boolean buildMessageForViewStore(String itemToGet) throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		HttpPost request = new HttpPost("/");//Create the HTTP Post object
		request.addHeader("Host", "Logan's mac"); //Add host header
		request.addHeader("Content-Type", "application/json"); //add content-type header
		
		System.out.println(request.getRequestLine().getMethod());
		System.out.println(request.getRequestLine().getUri());
		System.out.println(request.getRequestLine().getProtocolVersion());
		System.out.println(request.getRequestLine().toString());

		JsonObject requestGetItems = new JsonObject(); //Build the Json object that will be the body of the http message
		requestGetItems.addProperty("version", "1.0");
		requestGetItems.addProperty("methodName", "getItems");
		
		//Send as array for nate
		
		JsonArray array = new JsonArray();
		array.add(itemToGet);
		requestGetItems.add("params", array);
		
		
		//requestGetItems.addProperty("params", itemToGet); // Add that item we want as the parameter.  If the string is empty, we get back everything.  Had this BEFORE talking to nate

		//put the json object into a string representation to check
		String json = gson.toJson(requestGetItems);
		System.out.println("JSON REPRESENTATION OF BODY MESSAGE: " + json);

		List<Header> httpHeaders = Arrays.asList(request.getAllHeaders()); //turn the array of headers into a list
		for(Header header : httpHeaders) { // go through headers of our HttpPost request object
			System.out.println("HEADERS:" + header.getName() + ":" + header.getValue());
		}

		//Should it be HttpEntity entity?
		StringEntity entity = new StringEntity(requestGetItems.toString());
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		request.setEntity(entity);
		System.out.println("ENTITY:" + "" + request.getEntity());

		//Send the request

		HttpResponse httpResponse = sendHttpMessage("10.1.131.68", request);
		
		httpResponse.getStatusLine().getStatusCode();

		//server comes back, create an arraylist of items to display 
		System.out.println("ITEMS FROM SERVER ARE COMING!!");

		//HANDLE RESPONSE

		
		List<Header> responseHeaders = Arrays.asList(httpResponse.getAllHeaders()); //Get the headers from the Server.  Put the Header array into an array list
		for(Header header : responseHeaders) { //Go through each response headers
			System.out.println("SERVER HEADERS:" + header.getName() + ":" + header.getValue());
		}

		System.out.println("THE ENTITY OF THE RESPONSE IS COMING!!!");
		String jsonString = httpResponse.getEntity().toString();

		
		JsonElement element = gson.fromJson(jsonString, JsonElement.class); // Convert the entity string to a JsonElement
		JsonObject jsonObj = element.getAsJsonObject(); // Now we can convert the JsonElement to a JsonObject.  The entity is now a json object

		ArrayList<Item> itemsFromServer = parseGetItemsServerResponse(jsonObj); // Go through the server response (json entity).  Convert each Json object to an Item
		for(Item tempItem : itemsFromServer) { //Go through each item from the server
			System.out.println("SERVER RESPONSE " + tempItem.toString());
		} 
		
		
		
//		JsonReader reader = new JsonReader(new StringReader(jsonString));
//		reader.setLenient(true);
//		Item item = gson.fromJson(reader, Item.class);
		
		return true;
	}


	//ArrayList<Item> itemsToPurchase as parameter???
	public double buildMessageForPurchase(String name, int count) throws ClientProtocolException, IOException {
		System.out.println("PURCHASING ITEMS");
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonObject requestPurchaseItems = new JsonObject(); //Create json object that will represent the item we want to purchase
		requestPurchaseItems.addProperty("version", "1.0"); 
		requestPurchaseItems.addProperty("methodName", "purchase");

		JsonArray purchaseArray = new JsonArray(); //json array to hold the name and quantity of what we want to buy
		purchaseArray.add(name); //add name to Json Array
		purchaseArray.add(count); //add quantity to Json Array

		requestPurchaseItems.add("params", purchaseArray); //Add json array to our object 

		
		String purchaseJson = gson.toJson(requestPurchaseItems); // put json object to string to check 
		System.out.println(purchaseJson);

		// Send the http message
		
		
		
		HttpPost request = new HttpPost("/"); //Create http post object
	
		request.addHeader("Host", "Logan's mac"); //Add host header
		request.addHeader("Content-Type", "application/json"); //Add content type header
		
		StringEntity entity = new StringEntity(requestPurchaseItems.toString());
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		request.setEntity(entity);

		HttpResponse httpResponse = sendHttpMessage("10.1.131.68", request);
		httpResponse.getStatusLine().getStatusCode();

		//Get the headers from the Server.  Put the Header array into an array list
		List<Header> responseHeaders = Arrays.asList(httpResponse.getAllHeaders());

		//Go through each response headers
		for(Header header : responseHeaders) {
			System.out.println("SERVER HEADERS:" + header.getName() + ":" + header.getValue());
		}

		System.out.println("THE ENTITY OF THE RESPONSE IS COMING!!!");

		String jsonString = httpResponse.getEntity().toString();

		//convert to json object
		JsonElement element = gson.fromJson(jsonString, JsonElement.class);

		//Now we can convert the JsonElement to a JsonObject
		JsonObject jsonObj = element.getAsJsonObject();

		//Get the double
		double responseSuccess = jsonObj.getAsDouble();

		return responseSuccess;
	}

	public  ArrayList<Item> parseGetItemsServerResponse(JsonObject response) {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		ArrayList<Item> itemsToShow = new ArrayList<Item>();

		JsonArray t = response.get("params").getAsJsonArray();

		// turn each object in the json array into an 'Item'.  Put each one in an array list 
		for(int i = 0; i < t.size(); i++) {
			//System.out.println(t.get(i).toString());

			Item tempItem = gson.fromJson(t.get(i).toString(), Item.class);

			//System.out.println(tempItem.toString());
			
			itemsToShow.add(tempItem);
		}
		return itemsToShow;
	}

	public HttpResponse sendHttpMessage(String ipAddress, HttpRequest request) throws ClientProtocolException, IOException {
		InetAddress address = null;
		address = InetAddress.getByName(ipAddress);
		HttpHost target = new HttpHost(address, 6666);		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpResponse httpResponse = httpclient.execute(target, request); 
		return httpResponse;
	}
}