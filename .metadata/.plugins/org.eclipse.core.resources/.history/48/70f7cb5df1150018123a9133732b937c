import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class HttpMethods {


	public HttpMethods() {
		//contructor
	}

	public  boolean buildMessageForViewStore(String filter) throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
		JsonObject requestGetItems = new JsonObject(); //Build the Json object that will be the body of the http message
		requestGetItems.addProperty("version", "1.0");
		requestGetItems.addProperty("methodName", "getItems");
		
		if(filter.isEmpty()) {
			requestGetItems.add("params", null);
		} else {
			JsonArray array = new JsonArray();
			array.add(filter);
			requestGetItems.add("params", array); //add the json array (parameters) to the json object that will be body of http message
		}
	
		String json = gson.toJson(requestGetItems);
		System.out.println("JSON REPRESENTATION OF BODY MESSAGE: " + json);
		
		//Send the request
		HttpPost request = createHTTPMessage(requestGetItems);
		HttpResponse httpResponse = sendHttpMessage("10.1.130.217", request); //get the response
		parseViewStoreResponse(httpResponse); 
		
		return true;
	}
	
public void parseViewStoreResponse(HttpResponse response) throws ParseException, IOException {
		
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	String content = EntityUtils.toString(response.getEntity());
	System.out.println(content);

	JsonElement element = gson.fromJson(content, JsonElement.class); // Convert the entity string to a JsonElement
	JsonObject jsonObj = element.getAsJsonObject(); // Now we can convert the JsonElement to a JsonObject.  The entity is now a json object

	ArrayList<Item> itemsToReturn = new ArrayList<Item>();

	JsonArray t = jsonObj.get("return").getAsJsonArray();

	// turn each object in the json array into an 'Item'.  Put each one in an array list 
	for(int i = 0; i < t.size(); i++) {
		Item tempItem = gson.fromJson(t.get(i).toString(), Item.class);
		itemsToReturn.add(tempItem);
	}
	
	for(Item tempItem : itemsToReturn) { //Go through each item from the server
		System.out.println("SERVER RESPONSE " + tempItem.toString());
	} 
	
}

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
		
		HttpPost request = createHTTPMessage(requestPurchaseItems); //Create the http message
		HttpResponse httpResponse = sendHttpMessage("10.1.130.217", request); //Send the Http message
		double successPrice = parsePurchaseResponse(httpResponse); //get the response
		return successPrice;
	}
	
	
public double parsePurchaseResponse(HttpResponse httpResponse) throws ParseException, IOException {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		//Get the entity body
		HttpEntity json = httpResponse.getEntity();
		String content = EntityUtils.toString(json);
		System.out.println(content);
		
		//convert to json object
		JsonElement element = gson.fromJson(content, JsonElement.class);

		//Now we can convert the JsonElement to a JsonObject
		JsonObject jsonObj = element.getAsJsonObject();
		
		double responseSuccess = jsonObj.get("return").getAsDouble();
		
		return responseSuccess;
	}
	
public HttpPost createHTTPMessage(JsonObject jsonRequest) throws UnsupportedEncodingException {
		HttpPost httpRequest = new HttpPost("/"); //Create the HTTP Post object
		httpRequest.addHeader("Host", "Logan's mac"); //Add host header
		httpRequest.addHeader("Content-Type", "application/json"); //add content-type header
		StringEntity entity = new StringEntity(jsonRequest.toString()); //get the json request body as an entity
		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpRequest.setEntity(entity);
		return httpRequest;
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