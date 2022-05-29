package com.Model;

import com.Helper.DBConnector;
import com.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Room {
    private int id;
    private String room_type;
    private int stock;
    private int season_id;
    private int adult_price;
    private int child_price;
    private int hotel_type_id;
    private int hotel_id;


    private Hotel hotel;
    private HotelSeason hotelSeason;
    private HotelType hotelType;

    public Room() {

    }

    public Room(int id, String room_type, int stock, int season_id, int adult_price, int child_price, int hotel_type_id, int hotel_id) {
        this.id = id;
        this.room_type = room_type;
        this.stock = stock;
        this.season_id = season_id;
        this.adult_price = adult_price;
        this.child_price = child_price;
        this.hotel_type_id = hotel_type_id;
        this.hotel_id = hotel_id;
        this.hotel = Hotel.getFetch(hotel_id);
        this.hotelSeason = HotelSeason.getFetch(season_id);
        this.hotelType = HotelType.getFetch(hotel_type_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public int getAdult_price() {
        return adult_price;
    }

    public void setAdult_price(int adult_price) {
        this.adult_price = adult_price;
    }

    public int getChild_price() {
        return child_price;
    }

    public void setChild_price(int child_price) {
        this.child_price = child_price;
    }

    public int getHotel_type_id() {
        return hotel_type_id;
    }

    public void setHotel_type_id(int hotel_type_id) {
        this.hotel_type_id = hotel_type_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }


    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public HotelSeason getHotelSeason() {
        return hotelSeason;
    }

    public void setHotelSeason(HotelSeason hotelSeason) {
        this.hotelSeason = hotelSeason;
    }

    public HotelType getHotelType() {
        return hotelType;
    }

    public void setHotelType(HotelType hotelType) {
        this.hotelType = hotelType;
    }

    public static ArrayList<Room> getList(){
        ArrayList<Room> roomList = new ArrayList<>();
        String query = "SELECT * FROM room";
        Room obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                obj = new Room();
                obj.setId(rs.getInt("id"));
                obj.setRoom_type(rs.getString("room_type"));
                obj.setStock(rs.getInt("stock"));
                obj.setSeason_id(rs.getInt("season_id"));
                obj.setAdult_price(rs.getInt("adult_price"));
                obj.setChild_price(rs.getInt("child_price"));
                obj.setHotel_type_id(rs.getInt("hotel_type_id"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                roomList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomList;
    }

//oda özelliklerini oda id ye göre yazmak için gerekli metod
    public static Room getFetch(int id) {
        Room obj = null;
        String query = "SELECT * FROM room WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Room(rs.getInt("id"), rs.getString("room_type"), rs.getInt("stock"), rs.getInt("season_id"), rs.getInt("adult_price"), rs.getInt("child_price"), rs.getInt("hotel_type_id"), rs.getInt("hotel_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static boolean add(String room_type, int stock, int season_id, int adult_price, int child_price, int hotel_type_id, int hotel_id) {
        String query = "INSERT INTO room (room_type, stock, season_id, adult_price, child_price, hotel_type_id, hotel_id) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,room_type);
            pr.setInt(2,stock);
            pr.setInt(3,season_id);
            pr.setInt(4,adult_price);
            pr.setInt(5,child_price);
            pr.setInt(6,hotel_type_id);
            pr.setInt(7,hotel_id);

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

    public static Room getFetchByHotelID(int id) {
        Room obj = null;
        String query = "SELECT * FROM room WHERE hotel_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Room(rs.getInt("id"), rs.getString("room_type"), rs.getInt("stock"), rs.getInt("season_id"), rs.getInt("adult_price"), rs.getInt("child_price"), rs.getInt("hotel_type_id"), rs.getInt("hotel_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Room getFetchByHotelIDSeasonID(int season_id, int hotel_id) {
        String query = "SELECT * FROM room WHERE season_id = ? AND hotel_id = ?";
        Room obj=null;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, season_id);
            pr.setInt(2, hotel_id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new Room();
                obj.setId(rs.getInt("id"));
                obj.setRoom_type(rs.getString("room_type"));
                obj.setStock(rs.getInt("stock"));
                obj.setSeason_id(rs.getInt("season_id"));
                obj.setAdult_price(rs.getInt("adult_price"));
                obj.setChild_price(rs.getInt("child_price"));
                obj.setHotel_type_id(rs.getInt("hotel_type_id"));
                obj.setHotel_id(rs.getInt("hotel_id"));  }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static ArrayList<Room> searchRoomList(int hotel_id){
        ArrayList<Room> roomList = new ArrayList<>();
        String query = "SELECT * FROM room WHERE hotel_id = ?";
        Room obj;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, hotel_id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                obj = new Room();
                obj.setId(rs.getInt("id"));
                obj.setRoom_type(rs.getString("room_type"));
                obj.setStock(rs.getInt("stock"));
                obj.setSeason_id(rs.getInt("season_id"));
                obj.setAdult_price(rs.getInt("adult_price"));
                obj.setChild_price(rs.getInt("child_price"));
                obj.setHotel_type_id(rs.getInt("hotel_type_id"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                roomList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomList;
    }
}
