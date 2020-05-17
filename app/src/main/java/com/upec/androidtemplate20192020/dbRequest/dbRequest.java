package com.upec.androidtemplate20192020.dbRequest;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.upec.androidtemplate20192020.Balance;
import com.upec.androidtemplate20192020.Expense;
import com.upec.androidtemplate20192020.SessionManager;
import com.upec.androidtemplate20192020.SplitCount;
import com.upec.androidtemplate20192020.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class dbRequest {

    private Context context;
    private RequestQueue queue;
    private SessionManager sessionManager;


    public dbRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void signup(final String username, final String email, final String password, final String password2, final SignupCallback callback){

        String url = "https://pw.lacl.fr/~u21717956/splitCount/signup.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String,String> errors = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // User signed up successfully !
                        callback.onSuccess("Signed up successfully !");
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("username")){
                            errors.put("username", messages.getString("username"));
                        }
                        if (messages.has("email")){
                            errors.put("email", messages.getString("email"));
                        }
                        if (messages.has("password")){
                            errors.put("password", messages.getString("password"));
                        }
                        callback.inputError(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("email", email);
                map.put("password", password);
                map.put("password2", password2);

                return map;
            }
        };

        queue.add(request);

    }

    public void login(final String username, final String password, final LoginCallback callback){
        String url = "https://pw.lacl.fr/~u21717956/splitCount/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if (!error){
                        String uid = json.getString("uid");
                        String username = json.getString("username");
                        String email = json.getString("email");
                        callback.onSuccess(uid, username, email);
                    }else{
                        callback.onError(json.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onError("An error has occurred, please try again later.");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try again later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);

                return map;
            }
        };

        queue.add(request);


    }

    public void createSplitcount(final String name, final String description, final SessionManager sessionManager, final SplitcountCreateCallback callback){
        this.sessionManager = sessionManager;
        final String uid = sessionManager.getId();
        String url = "https://pw.lacl.fr/~u21717956/splitCount/addSP.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String,String> errors = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // Splitcount created successfully
                        callback.onSuccess(json.getString("message"));
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("name")){
                            errors.put("name", messages.getString("name"));
                        }
                        if (messages.has("query")){
                            errors.put("query", messages.getString("query"));
                        }
                        callback.inputError(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("description", description);
                map.put("uid", uid);

                return map;
            }
        };

        queue.add(request);

    }

    public void addExpenditure(final String title, final int sid, final String uid, final double amount, final AddExpenditureCallback callback){
        String url = "https://pw.lacl.fr/~u21717956/splitCount/addExpenditure.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // Expenditure added successfully
                        callback.onSuccess(json.getString("message"));
                    }else{
                        JSONObject messages = json.getJSONObject("message");

                        callback.onError(json.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("sid", String.valueOf(sid));
                map.put("amount", String.valueOf(amount));
                map.put("uid", uid);
                map.put("title", title);

                return map;
            }
        };

        queue.add(request);

    }

    public void joinSplitcount(final String sp_code, final SessionManager sessionManager, final SplitcountJoinCallback callback){
        this.sessionManager = sessionManager;
        final String uid = sessionManager.getId();
        String url = "https://pw.lacl.fr/~u21717956/splitCount/joinSP.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String errors;
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // Splitcount created successfully
                        callback.onSuccess(json.getString("message"));
                    }else{
                        errors = json.getString("message");
                        callback.inputError(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("sp_code", sp_code);
                map.put("uid", uid);

                return map;
            }
        };

        queue.add(request);

    }

    public void leaveSplitcount(final String Sid, final String Uid, final LeaveSplitcountCallback callback){
        final String uid = Uid;
        final String sid = Sid;

        String url = "https://pw.lacl.fr/~u21717956/splitCount/quit.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String errors;
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // Splitcount created successfully
                        callback.onSuccess(json.getString("message"));
                    }else{
                        errors = json.getString("message");
                        callback.onError(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("sid", sid);
                map.put("uid", uid);

                return map;
            }
        };

        queue.add(request);

    }

    public void listSplitCount(final SessionManager sessionManager, final SplitcountListCallback callback){

        String url = "https://pw.lacl.fr/~u21717956/splitCount/usermain.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();
                try {
                    ArrayList<SplitCount> splitcounts = new ArrayList<>();
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("splitcounts");
                    System.out.println(jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean error = jsonObject.getBoolean("error");

                        if (!error) {
                            String name = jsonObject.getString("name");
                            String description = jsonObject.getString("description");
                            int sid = jsonObject.getInt("sid");
                            String sp_code = jsonObject.getString("sp_code");
                            splitcounts.add(new SplitCount(sp_code, name, description, sid));
                            if (splitcounts.size() == jsonArray.length()){
                                callback.onSuccess(splitcounts);
                            }

                        } else {
                            callback.onError(json.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("uid", sessionManager.getId());
                return map;
            }
        };

        queue.add(request);
    }

    public void spUsersList(final String sid, final spUsersListCallback callback){
        String url = "https://pw.lacl.fr/~u21717956/splitCount/spUsersList.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();
                try {
                    ArrayList<User> spUsers = new ArrayList<>();
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("spUsersList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            String uid = jsonObject.getString("uid");
                            String username = jsonObject.getString("username");
                            spUsers.add(new User(uid, username));
                            if (spUsers.size() == jsonArray.length()){
                                callback.onSuccess(spUsers);
                            }
                        } else {
                            callback.onError(json.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("sid", sid);
                return map;
            }
        };

        queue.add(request);
    }

    public void updateKeep(final String username, final String email, final String paswordConfirm, final SessionManager sessionManager, final UpdateProfileKeepCallback callback){
        this.sessionManager = sessionManager;
        final String uid = sessionManager.getId();
        final String old_username = sessionManager.getUsername();
        final String old_email = sessionManager.getEmail();
        String url = "https://pw.lacl.fr/~u21717956/splitCount/updateUser.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String,String> errors = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // User signed up successfully !
                        callback.onSuccess(json.getString("message"));
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("username")){
                            errors.put("username", messages.getString("username"));
                        }
                        if (messages.has("email")){
                            errors.put("email", messages.getString("email"));
                        }
                        if (messages.has("password")){
                            errors.put("password", messages.getString("password"));
                        }
                        callback.inputError(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("uid", uid);
                map.put("username", username);
                map.put("email", email);
                map.put("passwordConfirm", paswordConfirm);
                map.put("old_username", old_username);
                map.put("old_email", old_email);


                return map;
            }
        };

        queue.add(request);

    }

    public void updateChange(final String username, final String email, final String password, final String password2, final String passwordConfirm, final SessionManager sessionManager, final UpdateProfileChangeCallback callback){
        this.sessionManager = sessionManager;
        final String uid = sessionManager.getId();
        final String old_username = sessionManager.getUsername();
        final String old_email = sessionManager.getEmail();
        String url = "https://pw.lacl.fr/~u21717956/splitCount/updateUser.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String,String> errors = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // User signed up successfully !
                        callback.onSuccess(json.getString("message"));
                    }else{
                        JSONObject messages = json.getJSONObject("message");
                        if (messages.has("username")){
                            errors.put("username", messages.getString("username"));
                        }
                        if (messages.has("email")){
                            errors.put("email", messages.getString("email"));
                        }
                        if (messages.has("password")){
                            errors.put("password", messages.getString("password"));
                        }
                        if (messages.has("passwordConfirm")){
                            errors.put("passwordConfirm", messages.getString("passwordConfirm"));
                        }
                        callback.inputError(errors);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("email", email);
                map.put("password2", password2);
                map.put("password", password);
                map.put("passwordConfirm", passwordConfirm);
                map.put("old_username", old_username);
                map.put("old_email", old_email);
                map.put("uid", uid);


                return map;
            }
        };

        queue.add(request);

    }

    public void listExpenses(final String sid, final SessionManager sessionManager, final ExpensesListCallback callback){

        String url = "https://pw.lacl.fr/~u21717956/splitCount/operationsList.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();
                try {
                    ArrayList<Expense> expenses = new ArrayList<>();
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("operations");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            String eid = jsonObject.getString("eid");
                            String title = jsonObject.getString("title");
                            String amount = jsonObject.getString("amount");
                            String payingUser = jsonObject.getString("from");
                            String date = jsonObject.getString("datetime");
                            expenses.add(new Expense(eid, title, amount, date, payingUser));
                            if (expenses.size() == jsonArray.length()){
                                callback.onSuccess(expenses);
                            }

                        } else {
                            callback.onError(json.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("sid", sid);
                return map;
            }
        };
        queue.add(request);
    }

    public void deleteExpenses(final String eid, final SessionManager sessionManager, final DeleteExpenseCallback callback){

        String url = "https://pw.lacl.fr/~u21717956/splitCount/deleteExpenditure.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean error = json.getBoolean("error");
                    if (!error){
                        // Expense deleted successfully
                        callback.onSuccess(json.getString("message"));
                    }else{
                        callback.onError(json.getString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("eid", eid);
                return map;
            }
        };
        queue.add(request);
    }

    public void listDebts(final String sid, final SessionManager sessionManager, final DebtListCallback callback){

        String url = "https://pw.lacl.fr/~u21717956/splitCount/balance.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();
                try {
                    ArrayList<Balance> balances = new ArrayList<>();
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonArray = json.getJSONArray("balance");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Boolean error = jsonObject.getBoolean("error");
                        if (!error) {
                            String usernameFrom = jsonObject.getString("from");
                            String usernameTo = jsonObject.getString("to");
                            String debt = jsonObject.getString("amount");
                            balances.add(new Balance(usernameFrom, usernameTo, debt));

                            if (balances.size() == jsonArray.length()){
                                callback.onSuccess(balances);
                            }

                        } else {
                            callback.onError(json.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError){
                    callback.onError("Connection failed, please check your internet.");
                }else if (error instanceof VolleyError){
                    callback.onError("An error has occurred, please try later.");
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("sid", sid);
                return map;
            }
        };
        queue.add(request);
    }



    public interface LoginCallback{
        void onSuccess(String uid, String username, String email);
        void onError(String message);
    }

    public interface SignupCallback{
        void onSuccess(String message);
        void inputError(Map<String,String> errors);
        void onError(String message);
    }

    public interface spUsersListCallback{
        void onSuccess(ArrayList<User> spUsers);
        void onError(String message);
    }

    public interface UpdateProfileKeepCallback{
        void onSuccess(String message);
        void inputError(Map<String,String> errors);
        void onError(String message);
    }

    public interface UpdateProfileChangeCallback{
        void onSuccess(String message);
        void inputError(Map<String,String> errors);
        void onError(String message);
    }

    public interface AddExpenditureCallback{
        void onSuccess(String message);
        void onError(String message);
    }

    public interface LeaveSplitcountCallback{
        void onSuccess(String message);
        void onError(String message);
    }

    public interface SplitcountCreateCallback{
        void onSuccess(String message);
        void inputError(Map<String,String> errors);
        void onError(String message);
    }

    public interface SplitcountJoinCallback{
        void onSuccess(String message);
        void inputError(String errors);
        void onError(String message);
    }

    public interface SplitcountListCallback{
        void onSuccess(ArrayList<SplitCount> info);
        void onError(String message);
    }

    public interface ExpensesListCallback{
        void onSuccess(ArrayList<Expense> expenses);
        void onError(String message);
    }

    public interface DeleteExpenseCallback{
        void onSuccess(String message);
        void onError(String message);
    }

    public interface DebtListCallback{
        void onSuccess(ArrayList<Balance> balances);
        void onError(String message);
    }
}