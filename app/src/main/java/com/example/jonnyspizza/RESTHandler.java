package com.example.jonnyspizza;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jonnyspizza.CustomObjects.Address;
import com.example.jonnyspizza.CustomObjects.Carryout;
import com.example.jonnyspizza.CustomObjects.Customer;
import com.example.jonnyspizza.CustomObjects.Delivery;
import com.example.jonnyspizza.CustomObjects.Order;
import com.example.jonnyspizza.CustomObjects.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RESTHandler {

    public static final String BASE_URL = "https://dev4.jonathanbasom.repl.co";
    public static final String USERS_RESOURCE = "/users";
    private static final String EXISTING_USERS_RESOURCE = "/existingAccounts";
    private static final String NEW_USERS_RESOURCE = "/newAccounts";
    public static final String ORDERS_RESOURCE = "/orders";

    public static final String POST_ORDER_URL = "/postOrder";

    private Context context;
    private RequestQueue queue;

    /**
     * Constructor
     * @param context
     */
    public RESTHandler(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(this.context);
    }

    /**
     * POST "/users/<userID>/orders" - adds a new order for the user "userID"
     * @param order
     * @return
     */
    public void addOrder(Order order){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String orderJSONString = objectMapper.writeValueAsString(order);

            JSONObject jsonBody = new JSONObject(orderJSONString);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + USERS_RESOURCE + "/" + order.getUserID() + ORDERS_RESOURCE, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String orderID = "";
                            try {
                                orderID = response.getString(DB_Util.ORDER_ID_KEY);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((PlaceOrderActivity) context).createOrderPlacedDialog(orderID);

                        /*try{
                            if(response.getString("status").equals("true")){
                                JSONArray jsonArray=response.getJSONArray("data");


                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name=jsonObject.getString("name");

                                }
                            }
                        }
                        catch (JSONException error){
                            //Log.e("TAGJE", error.getMessage());
                        } */
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e("TAGE", error.getMessage(), error);
                }

            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }

            };

            // Request a string response from the provided URL.
        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "/postOrder",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        })
            {
                @Override
                protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", "test1");
                params.put("message", "hello");
                //params.put("order", order.toString());

                return params;
                }
            }; */

            // Add the request to the RequestQueue.
            queue.add(request);

            /**
             mPostCommentResponse.requestStarted();
             RequestQueue queue = Volley.newRequestQueue(context);
             StringRequest sr = new StringRequest(Request.Method.POST,"http://api.someservice.com/post/comment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            mPostCommentResponse.requestCompleted();
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            mPostCommentResponse.requestEndedWithError(error);
            }
            }){
            @Override
            protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("user",userAccount.getUsername());
            params.put("pass",userAccount.getPassword());
            params.put("comment", Uri.encode(comment));
            params.put("comment_post_ID",String.valueOf(postId));
            params.put("blogId",String.valueOf(blogId));

            return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> params = new HashMap<String, String>();
            params.put("Content-Type","application/x-www-form-urlencoded");
            return params;
            }
            };
             queue.add(sr);
             */
        }
        catch (JsonProcessingException e){
            //return "JSON Processing Error";
        }
        catch (JSONException e){
            //return "JSON Exception";
        }
    }

    /**
     * GET "/users/<userID>/orders" - gets the most recent orders for user "userID" from the server
     * @param userID ID of the user account for which the recent orders are associated
     */
    public void getRecentOrders(int userID){

        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + USERS_RESOURCE + "/" + userID + ORDERS_RESOURCE, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);

                    ArrayList<ContentValues> resultsList;

                    try {
                        JSONArray jsonArray = response.getJSONArray("orders");
                        resultsList = parseRecentOrdersJSON(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultsList = new ArrayList<>();
                    }

                    ((MainActivity) context).createNewOrderHistoryDialog(resultsList);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Indicate server error
                    String errorMessage = "The server is temporarily unavailable. Please try again later.";
                    ((UserAccountActivity) context).createErrorDialog(errorMessage);
                }
            });
            queue.add(jsonObjectRequest);

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Parses the JSON response from "/orders" for the recent history display
     * @param ordersJSON JSONArray - body of the response with a list of recent orders
     * @return ArrayList<ContentValues> Contains the parsed recent orders
     * @throws JSONException
     */
    private ArrayList<ContentValues> parseRecentOrdersJSON(JSONArray ordersJSON) throws JSONException {

        ArrayList<ContentValues> resultsList = new ArrayList<>();

        for (int i = 0; i < ordersJSON.length(); ++i){
            JSONObject order = ordersJSON.getJSONObject(i);

            String orderID = order.getString(DB_Util.ORDER_PK);
            String orderType = order.getString(DB_Util.ORDER_TYPE);
            String orderCost = order.getString(DB_Util.ORDER_COST);
            String orderDate = order.getString(DB_Util.ORDER_DATE);

            ContentValues values = new ContentValues();
            values.put(DB_Util.ORDER_PK, orderID);
            values.put(DB_Util.ORDER_TYPE, orderType);
            values.put(DB_Util.ORDER_COST, orderCost);
            values.put(DB_Util.ORDER_DATE, orderDate);

            resultsList.add(values);
        }

        return resultsList;
    }

    /**
     * GET "/order/<orderID>" - gets the details for the specified order
     * @param orderID
     * @param orderType
     */
    public void getOrder(String orderID, String orderType){

        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + ORDERS_RESOURCE + "/" + orderID, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Order order = parseOrderJSON(response);
                    // Exception means that Order unable to be parsed -- TODO: Set an error popup for user
                    if (order != null){
                        int userId = ((MainActivity) context).getUserAccount().getId();
                        order.setUserID(userId);
                        ((MainActivity) context).viewOrderSummary(order);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Indicate server error
                    String errorMessage = "The server is temporarily unavailable. Please try again later.";
                    ((UserAccountActivity) context).createErrorDialog(errorMessage);
                }
            });

            queue.add(jsonObjectRequest);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Takes the JSON from an Order and deserializes the Order object
     * @param orderJSON Resposne from the server
     * @return Order
     */
    private Order parseOrderJSON(JSONObject orderJSON) {

        try {
            Customer customer = parseCustomerJSON(orderJSON);
            Payment payment = parsePaymentJSON(orderJSON);
            Cart cart = parseItemsJSON(orderJSON);

            Order order = createOrder(orderJSON, cart, customer, payment);

            return order;
        }
        catch (Exception e) {
            // Log Order unable to be successfully parsed from JSON from server
            return null;
        }
    }

    /**
     * Takes the JSON from an Order and deserializes the Customer object
     * @param orderJSON Response from the server
     * @return Customer with the customer info
     * @throws JSONException
     * @throws JsonProcessingException
     */
    private Customer parseCustomerJSON(JSONObject orderJSON) throws JSONException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer customer = null;

        if (orderJSON.has(DB_Util.CUSTOMER_KEY)) {
            String customerJSONString = orderJSON.getString(DB_Util.CUSTOMER_KEY);
            customer = objectMapper.readValue(customerJSONString, Customer.class);
        }

        return customer;
    }

    /**
     * Takes the JSON from an Order and deserializes the Payment object
     * @param orderJSON Response from the server
     * @return Payment with the payment info
     * @throws JSONException
     * @throws JsonProcessingException
     */
    private Payment parsePaymentJSON(JSONObject orderJSON) throws JSONException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Payment payment = null;

        if (orderJSON.has(DB_Util.PAYMENT_KEY)) {
            String paymentJSONString = orderJSON.getString(DB_Util.PAYMENT_KEY);
            payment = objectMapper.readValue(paymentJSONString, Payment.class);
        }

        return payment;
    }

    /**
     * Takes the JSON from an Order and deserializes the Cart object
     * @param orderJSON Response from the server
     * @return Cart with the Order Items
     * @throws JSONException
     * @throws JsonProcessingException
     */
    private Cart parseItemsJSON(JSONObject orderJSON) throws JSONException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JSONArray cartJSON = orderJSON.getJSONArray(DB_Util.CART_KEY);

        Cart cart = new Cart();

        for (int i = 0; i < cartJSON.length(); ++i) {
            JSONObject itemJSON = cartJSON.getJSONObject(i);
            Item item;
            if (itemJSON.has(DB_Util.PIZZA_KEY)) {
                String itemJSONString = itemJSON.getString(DB_Util.PIZZA_KEY);
                item = objectMapper.readValue(itemJSONString, Pizza.class);
                cart.addItem(item);
            } else if (itemJSON.has(DB_Util.SUB_KEY)) {
                String itemJSONString = itemJSON.getString(DB_Util.SUB_KEY);
                item = objectMapper.readValue(itemJSONString, Sub.class);
                cart.addItem(item);
            } else if (itemJSON.has(DB_Util.WINGS_KEY)) {
                String itemJSONString = itemJSON.getString(DB_Util.WINGS_KEY);
                item = objectMapper.readValue(itemJSONString, Wings.class);
                cart.addItem(item);
            } else if (itemJSON.has(DB_Util.DRINK_KEY)) {
                String itemJSONString = itemJSON.getString(DB_Util.DRINK_KEY);
                item = objectMapper.readValue(itemJSONString, Drink.class);
                cart.addItem(item);
            }
        }
            return cart;
    }

    /**
     * Assembles an Order object with the previously abstracted data from the server
     * @param orderJSON Response from server(determines the type of order)
     * @param cart Cart
     * @param customer Customer
     * @param payment Payment
     * @return Order
     * @throws JSONException
     * @throws JsonProcessingException
     */
    private Order createOrder(JSONObject orderJSON, Cart cart, Customer customer, Payment payment) throws JSONException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order;

        if (orderJSON.has(DB_Util.DELIVERY_ADDRESS_KEY)) {
            String addressJSONString = orderJSON.getString(DB_Util.DELIVERY_ADDRESS_KEY);
            Address address = objectMapper.readValue(addressJSONString, Address.class);
            order = new Delivery(cart, customer, payment, address);
        }
        else {
            order = new Carryout(cart, customer, payment);
        }

        return order;
    }

    /**
     * POST "/users/newAccounts" - adds a new user account to the system
     * @param username
     * @param password
     */
    public void addUser(String username, String password){
        try{
            UserAccount userAccount = new UserAccount(username, password);
            ObjectMapper objectMapper = new ObjectMapper();
            String orderJSONString = objectMapper.writeValueAsString(userAccount);
            JSONObject jsonBody = new JSONObject(orderJSONString);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + USERS_RESOURCE + NEW_USERS_RESOURCE, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int userID = -1;
                            try {
                                userID = Integer.parseInt(response.getString(DB_Util.USER_ID_KEY));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((UserAccountActivity) context).completeSignIn(userID, username, password);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage;
                    // Indicate replicate, try again
                    if (error.networkResponse.statusCode == HttpURLConnection.HTTP_CONFLICT){
                        errorMessage = "Username " + username + " already exists.  Please try a new username!";
                    }
                    // Indicate server error
                    else {
                        errorMessage = "The server is temporarily unavailable. Please try again later.";
                    }
                    ((UserAccountActivity) context).createErrorDialog(errorMessage);
                }

            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }

            };

            queue.add(request);
        }
        catch (Exception e){

        }
    }

    /**
     * POST "/users/existingAccounts" - determines if the username and password are a valid match
     * @param username
     * @param password
     */
    public void signInUser(String username, String password){
        try{
            UserAccount userAccount = new UserAccount(username, password);
            ObjectMapper objectMapper = new ObjectMapper();
            String orderJSONString = objectMapper.writeValueAsString(userAccount);
            JSONObject jsonBody = new JSONObject(orderJSONString);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + USERS_RESOURCE + EXISTING_USERS_RESOURCE, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            int userID = -1;
                            try {
                                userID = Integer.parseInt(response.getString(DB_Util.USER_ID_KEY));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((UserAccountActivity) context).completeSignIn(userID, username, password);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage;
                    // Indicate invalid credentials
                    if (error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                        errorMessage = "Username and password do not match.  Please try again!";
                    }
                    // Indicate server error
                    else {
                        errorMessage = "The server is temporarily unavailable. Please try again later.";
                    }
                    ((UserAccountActivity) context).createErrorDialog(errorMessage);
                }

            }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }

            };

            queue.add(request);
        }
        catch (Exception e){

        }
    }
}
