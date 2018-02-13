//include the source docs for gson 2.6.2
//include the source docs for apache http
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

import java.util.Iterator;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpRequest;

import com.google.gson.*;



/*
HeaderElementIterator it = new BasicHeaderElementIterator(
	    request.headerIterator("Set-Cookie"));

	while (it.hasNext()) {
	    HeaderElement elem = it.nextElement(); 
	    System.out.println(elem.getName() + " = " + elem.getValue());
	    NameValuePair[] params = elem.getParameters();
	    for (int i = 0; i < params.length; i++) {
	        System.out.println(" " + params[i]);
	    }
	}
*/


/*
List<Header> httpHeaders = Arrays.asList(request.getAllHeaders());

for(Header header : httpHeaders) {
	System.out.println(header.getName() + ":" + header.getValue());
}
*/


/**
 * https://static.javadoc.io/com.google.code.gson/gson/
 * 2.6.2/com/google/gson/JsonArray.html#add-java.lang.String-
 * 
 */


/*
 * 
 * 
 * https://stackoverflow.com/questions/9389842/how-to-send-a-json-object
 * -over-httpclient-request-with-android
 */



public class RPCClientMain {

	public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
			
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// read the response back from the server
		
		JsonObject response = new JsonObject();
		
		response.addProperty("version", 1.0);
		
		//each element is a json object consisting of name, price, stock
		JsonArray itemDescriptions = new JsonArray();
		
		JsonObject item1Info = new JsonObject();
		item1Info.addProperty("name", "Rant");
		item1Info.addProperty("price", 13.99);
		item1Info.addProperty("stock", 5);
		
		JsonObject item2Info = new JsonObject();
		item2Info.addProperty("name", "MAGA");
		item2Info.addProperty("price", 19.99);
		item2Info.addProperty("stock", 500);
		
		itemDescriptions.add(item1Info);
		itemDescriptions.add(item2Info);
		
		response.add("params", itemDescriptions);
		
		String json = gson.toJson(response);
		System.out.println(json);
		
		//Parse the json object and create new items
		
		ArrayList<Item> itemsToShow = new ArrayList<Item>();
		
		JsonArray t = response.get("params").getAsJsonArray();
		
		for(int i = 0; i < t.size(); i++) {
			//System.out.println(t.get(i).toString());
			
			Item tempItem = gson.fromJson(t.get(i).toString(), Item.class);
			
			System.out.println(tempItem.toString());
			
			itemsToShow.add(tempItem);
		}
		
	
	}

	
	//buildMessage(methodName)
	
	//change name of parameter to filter
	public boolean buildMessageForViewStore(ArrayList<String> itemsToGet) throws ClientProtocolException, IOException {
		
	
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		HttpPost request = new HttpPost("/");
		
		request.addHeader("Host", "Logan's mac");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Content-Length","322");
		
		System.out.println(request.getRequestLine().getMethod());
		System.out.println(request.getRequestLine().getUri());
		System.out.println(request.getRequestLine().getProtocolVersion());
		System.out.println(request.getRequestLine().toString());
		
		
		//Build the json object that will be the body of the http messagse
		JsonObject requestGetItems = new JsonObject();
		requestGetItems.addProperty("version", "1.0");
		requestGetItems.addProperty("methodName", "getItems");
		
		JsonArray jArray = new JsonArray();
		
		Iterator<String> iterator = itemsToGet.iterator();
		while(iterator.hasNext()) {
			jArray.add(new String(iterator.next()));
		}
			
	
		requestGetItems.add("params", jArray);
		
		String json = gson.toJson(requestGetItems);
		System.out.println(json);
		
		List<Header> httpHeaders = Arrays.asList(request.getAllHeaders());
		
		for(Header header : httpHeaders) {
			System.out.println("HEADERS:" + header.getName() + ":" + header.getValue());
		}
		
		HttpEntity entity = new StringEntity(requestGetItems.toString());
		request.setEntity(entity);
		System.out.println("ENTITY:" + "" + request.getEntity());
				
		
		//Send the request
		
		InetAddress address = null;
		address = InetAddress.getByName("localhost");
		HttpHost target = new HttpHost(address);
				
		CloseableHttpClient httpclient = HttpClients.createDefault();
				
		HttpResponse httpResponse = httpclient.execute(target, request); 
				
		
		return false;
	}
	
	//ArrayList<Item> itemsToPurchase as parameter???
	public boolean buildMessageForPurchase(String name, int count) {
		
		/*
		System.out.println("PURCHASING ITEMS");
		
		JsonObject requestPurchaseItems = new JsonObject();
		requestPurchaseItems.addProperty("version", "1.0");
		requestPurchaseItems.addProperty("methodName", "purchase");
		
		JsonArray purchaseArray = new JsonArray();
		purchaseArray.add(new String("book"));
		purchaseArray.add(new Integer(5));
		
		requestPurchaseItems.add("params", purchaseArray);
		
		String purchaseJson = gson.toJson(requestPurchaseItems);
		System.out.println(purchaseJson);
		*/
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonObject requestPurchaseItems = new JsonObject();
		requestPurchaseItems.addProperty("version", "1.0");
		requestPurchaseItems.addProperty("methodName", "purchase");
		
		JsonArray purchaseArray = new JsonArray();
		
		purchaseArray.add(name);
		purchaseArray.add(count);
			
		requestPurchaseItems.add("params", purchaseArray);
		
		String purchaseJson = gson.toJson(requestPurchaseItems);
		System.out.println(purchaseJson);
	
		// read the response back from the server.  Create a method that 
		// will return a json object to do this
		
		
		JsonObject response = new JsonObject();
		
		response.addProperty("version", 1.0);
		
		//each element is a json object consisting of name, price, stock
		JsonArray returnArray = new JsonArray();
		
		JsonObject item1Info = new JsonObject();
		item1Info.addProperty("name", "Rant");
		item1Info.addProperty("price", 13.99);
		item1Info.addProperty("stock", 5);
		
		JsonObject item2Info = new JsonObject();
		
		
		returnArray.add(item1Info);
		
		return false;
	}
	
	
}