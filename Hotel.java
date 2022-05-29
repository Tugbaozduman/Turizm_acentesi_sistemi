package com.Model;

import com.Helper.DBConnector;
import com.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Hotel {
    private int id;
    private String name;
    private String star;
    private String property;
    private String address;
    private String phone;
    private String email;



    public Hotel(int id, String name, String star, String property, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.property = property;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Hotel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ArrayList<Hotel> getList(){
        ArrayList<Hotel> hotelList = new ArrayList<>();
        String query = "SELECT * FROM hotel";
        Hotel obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Hotel();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setStar(rs.getString("star"));
                obj.setProperty(rs.getString("property"));
                obj.setAddress(rs.getString("address"));
                obj.setPhone(rs.getString("phone"));
                obj.setEmail(rs.getString("email"));
                hotelList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelList;
    }

//hotel özelliklerini id ye göre yazmak için gerekli metod
    public static Hotel getFetch(int id) {
        Hotel obj = null;
        String query = "SELECT * FROM hotel WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Hotel(rs.getInt("id"), rs.getString("name"), rs.getString("star"), rs.getString("property"), rs.getString("address"), rs.getString("phone"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

//yeni Otel eklerken 2. defa ekleme olmaması için email e göre kontrol
    public static Hotel getFetch(String email) {
        Hotel obj = null;
        String query = "SELECT * FROM hotel WHERE email = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, email);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Hotel(rs.getInt("id"), rs.getString("name"), rs.getString("star"), rs.getString("property"), rs.getString("address"), rs.getString("phone"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static boolean add(String name, String star, String property, String address, String phone, String email){
        String query = "INSERT INTO hotel (name, star, property, address, phone, email) VALUES (?,?,?,?,?,?)";
        Hotel findHotel = Hotel.getFetch(email);
        if (findHotel !=null){
            Helper.showMsg("Bu otel daha önce sisteme kaydedilmiştir.");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,name);
            pr.setString(2,star);
            pr.setString(3,property);
            pr.setString(4,address);
            pr.setString(5,phone);
            pr.setString(6,email);

            int response = pr.executeUpdate();

            if (response == -1){
                Helper.showMsg("error");
            }
            return response != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

// girilen anahtar kelimeye göre search metodu
    public static ArrayList<Hotel> searchHotelList(String query){
        ArrayList<Hotel> hotelList = new ArrayList<>();
        Hotel obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Hotel();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setStar(rs.getString("star"));
                obj.setProperty(rs.getString("property"));
                obj.setAddress(rs.getString("address"));
                obj.setPhone(rs.getString("phone"));
                obj.setEmail(rs.getString("email"));
                hotelList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelList;
    }
}
