package com.example.jonnyspizza;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jonnyspizza.CustomObjects.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RESTHandler {

    public static final String BASE_URL = "https://dev4.jonathanbasom.repl.co";
    public static final String POST_ORDER_URL = BASE_URL + "/postOrder";
    public static final String GET_RECENT_ORDERS_URL = BASE_URL + "/orders";

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
     * "/postOrder"
     * @param order
     * @return
     */
    public String addOrder(Order order){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String orderJSONString = objectMapper.writeValueAsString(order);

            JSONObject jsonBody = new JSONObject(orderJSONString);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, POST_ORDER_URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

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

            return "123";       //TODO: Change this to the actual order ID
        }
        catch (JsonProcessingException e){
            return "JSON Processing Error";
        }
        catch (JSONException e){
            return "JSON Exception";
        }
    }

    /**
     * Get "/orders" - gets the most recent orders from the server
     * @param displayContext Context of the dialog builder for the order history popup
     * @param view View for the Order History Popup
     */
    public void getRecentOrders(Context displayContext, View view){

        //ArrayList<ContentValues> resultsList = new ArrayList<>();
        System.out.println("This is a test");

        try{
            //JSONObject jsonObject = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_RECENT_ORDERS_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("Response Received");
                    System.out.println(response);

                    ArrayList<ContentValues> resultsList;

                    try {
                        JSONArray jsonArray = response.getJSONArray("orders");
                        resultsList = parseOrdersJSON(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultsList = new ArrayList<>();
                    }

                    ((MainActivity) context).populateOrderHistoryDialog(displayContext, view, resultsList);

                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

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
    private ArrayList<ContentValues> parseOrdersJSON(JSONArray ordersJSON) throws JSONException {

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
}
