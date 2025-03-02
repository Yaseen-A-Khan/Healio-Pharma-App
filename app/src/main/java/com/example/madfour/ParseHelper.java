package com.example.madfour;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.DeleteCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseHelper {

    public boolean authenticateUser(String username, String password, String role) {
        try {
            ParseUser.logIn(username, password);
            ParseUser user = ParseUser.getCurrentUser();
            return user != null && role.equals(user.getString("role"));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerUser(String username, String password, String email, String phone, String address, SaveCallback callback) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("phone", phone);
        user.put("address", address);
        user.put("role", "customer");
        user.signUpInBackground((SignUpCallback) callback);
    }

    public void addMedicine(String name, String description, String category, String imageUrl,String price, SaveCallback callback) {
        ParseObject medicine = new ParseObject("Medicine");
        medicine.put("name", name);
        medicine.put("description", description);
        medicine.put("category", category);
        medicine.put("imageUrl", imageUrl);
        medicine.put("price", price);
        medicine.saveInBackground(callback);
    }

    public List<Medicine> getMedicinesByCategory(String category) {
        List<Medicine> medicines = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Medicine");
        query.whereEqualTo("category", category);
        try {
            List<ParseObject> results = query.find();
            for (ParseObject obj : results) {
                medicines.add(new Medicine(
                        obj.getString("name"),
                        obj.getString("description"),
                        obj.getString("category"),
                        obj.getString("imageUrl"),
                        obj.getString("price")
                ));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public List<MedicineAdmin> getMedicinesByCategorys(String category) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Medicine");
        List<MedicineAdmin> medicines = new ArrayList<>();
        query.whereEqualTo("category", category);
        try {
            List<ParseObject> results = query.find();

            Log.d(TAG, "getMedicinesByCategory: Found " + results.size() + " medicines for category " + category);

            for (ParseObject result : results) {
                String id = result.getObjectId();
                String name = result.getString("name");
                String description = result.getString("description");
                String imageUrl = result.getString("imageUrl");
                String price = result.getString("price");
                medicines.add(new MedicineAdmin(id, name, description, category, imageUrl, price));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public void storeOrder(String username, Map<Medicine, Integer> cartItems, SaveCallback callback) {
        ParseObject order = new ParseObject("Order");
        double totalAmount=0.0;
        order.put("username", username);
        List<String> items = new ArrayList<>();
        for (Map.Entry<Medicine, Integer> entry : cartItems.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            items.add(medicine.getName() + " [ Rs."+medicine.getPrice()+" x" + quantity+" ]");
            totalAmount+=(Double.parseDouble(medicine.getPrice()))*quantity;
        }
        order.put("items", items);
        order.put("status", "pending");
        order.put("totalAmount",new Double(totalAmount).toString());
        order.saveInBackground(callback);
    }

    private static final String TAG = "ParseHelper";

    public List<Order> getConfirmedOrders() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("status", "Confirmed");
        List<ParseObject> results = query.find();

        Log.d(TAG, "getConfirmedOrders: Found " + results.size() + " orders");

        List<Order> orders = new ArrayList<>();
        for (ParseObject result : results) {
            String id = result.getObjectId();
            String username = result.getString("username");
            List<String> items = result.getList("items");
            String status = result.getString("status");
            orders.add(new Order(id, username, items, status));
        }
        return orders;
    }

    public List<Order> getOrdersByUsername(String username) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        String username1=username;
        query.whereEqualTo("username", username1);
        List<ParseObject> results = query.find();

        Log.d(TAG, "getOrdersByUsername: Found " + results.size() + " orders for username " + username1);

        List<Order> orders = new ArrayList<>();
        for (ParseObject result : results) {
            String id = result.getObjectId();
            String user = result.getString("username");
            List<String> items = result.getList("items");
            String status = result.getString("status");
            String totalAmount = result.getString("totalAmount");
            orders.add(new Order(id, user, items, status, totalAmount));
        }
        return orders;
    }

    public void updateOrderStatus(String orderId, String newStatus, SaveCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.getInBackground(orderId, (object, e) -> {
            if (e == null) {
                object.put("status", newStatus);
                object.saveInBackground(e2 -> {
                    if (e2 == null) {
                        Log.d(TAG, "Order " + orderId + " updated to status: " + newStatus);
                    } else {
                        Log.e(TAG, "Error updating order " + orderId + ": " + e2.getMessage());
                    }
                    callback.done(e2);
                });
            } else {
                Log.e(TAG, "Error fetching order " + orderId + ": " + e.getMessage());
                callback.done(e);
            }
        });
    }

    public List<Order> getNonDeliveredOrders() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereNotEqualTo("status", "Delivered"); // Exclude delivered orders
        List<ParseObject> results = query.find();

        Log.d(TAG, "getNonDeliveredOrders: Found " + results.size() + " orders");

        List<Order> orders = new ArrayList<>();
        for (ParseObject result : results) {
            String id = result.getObjectId();
            String username = result.getString("username");
            List<String> items = result.getList("items");
            String status = result.getString("status");
            String totalAmount = result.getString("totalAmount");
            orders.add(new Order(id, username, items, status,totalAmount));
        }
        return orders;
    }

    public interface UpdateStatusCallback {
        void onComplete(Exception e);
    }
    public void deleteMedicine(String medicineId, DeleteCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Medicine");
        query.getInBackground(medicineId, (object, e) -> {
            if (e == null) {
                object.deleteInBackground(callback);
            } else {
                callback.done(e);
            }
        });
    }
}