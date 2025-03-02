package com.example.madfour;
//private static final String CONNECTION_STRING = "mongodb://localhost:27017";
//private static final String DATABASE_NAME = "projectk";

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MongoDBHelper {
    //private static final String CONNECTION_STRING = "mongodb+srv://vvsl180504:Nimisha2010@cluster.myvf8.mongodb.net/?retryWrites=true&w=majority&appName=Cluster";
    //private static final String DATABASE_NAME = "Projectone";
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "projectk";
    private static final String USERS_COLLECTION = "users";
    private static final String MEDICINES_COLLECTION = "medicines";
    private static final String ORDERS_COLLECTION = "orders";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;
    private MongoCollection<Document> medicinesCollection;
    private MongoCollection<Document> ordersCollection;

    public MongoDBHelper() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            usersCollection = database.getCollection(USERS_COLLECTION);
            medicinesCollection = database.getCollection(MEDICINES_COLLECTION);
            ordersCollection = database.getCollection(ORDERS_COLLECTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String username, String password, String role) {
        try {
            Document user = usersCollection.find(new Document("username", username)
                    .append("password", password)
                    .append("role", role)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void registerUser(String username, String password, String email, String phone, String address) {
        try {
            Document newUser = new Document("username", username)
                    .append("password", password)
                    .append("email", email)
                    .append("phone", phone)
                    .append("address", address)
                    .append("role", "customer");
            usersCollection.insertOne(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Medicine> getMedicinesByCategory(String category) {
        List<Medicine> medicines = new ArrayList<>();
        try {
            FindIterable<Document> documents = medicinesCollection.find(new Document("category", category));
            for (Document doc : documents) {
                medicines.add(new Medicine(
                        doc.getString("name"),
                        doc.getString("description"),
                        doc.getString("category"),
                        doc.getString("imageUrl")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public void addMedicine(String name, String description, String category, String imageUrl) {
        try {
            Document newMedicine = new Document("name", name)
                    .append("description", description)
                    .append("category", category)
                    .append("imageUrl", imageUrl);
            medicinesCollection.insertOne(newMedicine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeOrder(String username, List<Medicine> cartItems) {
        try {
            List<Document> items = new ArrayList<>();
            for (Medicine medicine : cartItems) {
                items.add(new Document("name", medicine.getName())
                        .append("description", medicine.getDescription())
                        .append("category", medicine.getCategory())
                        .append("imageUrl", medicine.getImageUrl()));
            }
            Document order = new Document("username", username)
                    .append("items", items)
                    .append("status", "pending");
            ordersCollection.insertOne(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public List<Order> getOrdersByUsername(String username) {
//        List<Order> orders = new ArrayList<>();
//        try {
//            FindIterable<Document> documents = ordersCollection.find(new Document("username", username));
//            for (Document doc : documents) {
//                List<Medicine> items = new ArrayList<>();
//                List<Document> itemDocs = (List<Document>) doc.get("items");
//                for (Document itemDoc : itemDocs) {
//                    items.add(new Medicine(
//                            itemDoc.getString("name"),
//                            itemDoc.getString("description"),
//                            itemDoc.getString("category"),
//                            itemDoc.getString("imageUrl")
//                    ));
//                }
//                orders.add(new Order(
//                        doc.getObjectId("_id").toString(),
//                        doc.getString("username"),
//                        items,
//                        doc.getString("status")
//                ));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return orders;
//    }

    public User getUserDetails(String username) {
        try {
            Document doc = usersCollection.find(new Document("username", username)).first();
            if (doc != null) {
                return new User(
                        doc.getString("username"),
                        doc.getString("password"),
                        doc.getString("email"),
                        doc.getString("phone"),
                        doc.getString("address")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public List<Order> getConfirmedOrders() {
//        List<Order> orders = new ArrayList<>();
//        try {
//            FindIterable<Document> documents = ordersCollection.find(new Document("status", "pending"));
//            for (Document doc : documents) {
//                List<Medicine> items = new ArrayList<>();
//                List<Document> itemDocs = (List<Document>) doc.get("items");
//                for (Document itemDoc : itemDocs) {
//                    items.add(new Medicine(
//                            itemDoc.getString("name"),
//                            itemDoc.getString("description"),
//                            itemDoc.getString("category"),
//                            itemDoc.getString("imageUrl")
//                    ));
//                }
//                orders.add(new Order(
//                        doc.getObjectId("_id").toString(),
//                        doc.getString("username"),
//                        items,
//                        doc.getString("status")
//                ));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return orders;
//    }

    public void updateOrderStatus(String orderId, String newStatus) {
        try {
            Document query = new Document("_id", new ObjectId(orderId));
            Document update = new Document("$set", new Document("status", newStatus));
            ordersCollection.updateOne(query, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}