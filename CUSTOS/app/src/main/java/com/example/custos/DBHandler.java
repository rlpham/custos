package com.example.custos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DBHandler {

    public JSONObject getUser() throws JSONException {
        JSONObject user;

        try {
            user = new JSONObject();
            JSONObject coordinates = new JSONObject();
            coordinates.put("longitude", "25.7617");
            coordinates.put("latitude", "80.1918");

            user.put("username", "kennytran123");
            user.put("phone", "123-456-7890");
            user.put("home_location", coordinates);

            return user;

        } catch (JSONException e) {
            System.out.println(e);
        }
        return null;
    }

    public JSONObject getEvent() throws JSONException {
        JSONObject event;
        try {
            event = new JSONObject();
            JSONArray guests = new JSONArray();
            guests.put("Ryan Pham");
            guests.put("Dale Reyes");
            guests.put("Hoang Pham");
            guests.put("Rahul Datta");

            JSONObject location = new JSONObject();
            location.put("longitude", "25.7617");
            location.put("latitude", "80.1918");

            event.put("id","E1234");
            event.put("owner", "kennytran123");
            event.put("name", "Kennys Stunna 22nd Birthday Party");
            event.put("location", location);
            event.put("description", "kennys 22nd birthday, bring cake please, and friends.");
            event.put("date/time", "01/06/2021 08:00PM EST");
            event.put("guests", guests);

            return event;

        } catch(JSONException e) {
            System.out.println(e);
        }
        return null;
    }

    public JSONArray getNotifications() throws JSONException {
        JSONArray notifications;
        try {
            notifications = new JSONArray();
            JSONObject n1 = new JSONObject();
            JSONObject n2 = new JSONObject();
            JSONObject n3 = new JSONObject();

            n1.put("id","E1234");
            n1.put("sender", "user23324");
            n1.put("type", "invite");
            n1.put("message", "kennytran123 has invited you an event");

            n2.put("id","D1234");
            n2.put("sender", "xxDragonSlayer_3.14");
            n2.put("type", "alert");
            n2.put("message", "xxDragonSlayer_3.14 might be in danger");

            n3.put("id","L1234");
            n3.put("sender", "DFA987");
            n3.put("type", "alert");
            n3.put("message", "DFA987 is now sharing their location with you for event: Kennys Stunna 22nd Birthday Party");

            notifications.put(n1);
            notifications.put(n2);
            notifications.put(n3);

            return notifications;

        } catch(JSONException e) {
            System.out.println(e);
        }
        return null;
    }

    public JSONArray getEventsList() throws JSONException {
        JSONArray events;
        try {
            events = new JSONArray();
            JSONObject e1 = new JSONObject();
            JSONObject e2 = new JSONObject();
            JSONObject e3 = new JSONObject();

            e1.put("id", "E1234");
            e1.put("name", "Kennys Stunna 22nd Birthday Party");
            e2.put("id", "E5815");
            e2.put("name", "Dinner");
            e3.put("id", "E8951");
            e3.put("name", "Study Session");

            events.put(e1);
            events.put(e2);
            events.put(e3);
            return events;

        } catch(JSONException e) {
            System.out.println(e);
        }
        return null;
    }

    public JSONArray getContacts() throws JSONException {
        JSONArray contacts;
        try {



            contacts = new JSONArray();



            contacts.put("Fred Toadey");
            contacts.put("Bob Saget");
            contacts.put("Chris Paul");
            contacts.put("Angel Lansacre");
            contacts.put("Dixie Man");
            contacts.put("Joey Jooper");
            contacts.put("Hugh Yassaw");
            contacts.put("Henry Tasad");
            contacts.put("Sally Awad");
            contacts.put("Chad Brubaker");
            contacts.put("Archie Vega");
            contacts.put("Dennis Jordan");
            contacts.put("Jospeh McArna");
            contacts.put("Hope Posat");
            contacts.put("Hilary Oiusian");
            contacts.put("Sam Fradeg");



            return contacts;

        }catch(Exception e) {
            System.out.println(e);
        }
        return null;



}
}
