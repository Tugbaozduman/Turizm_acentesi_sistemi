package com.Model;

import com.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelSeason {

    private int id;
    private String season_start;
    private String season_end;
    private int hotel_id;

    private Hotel hotel;

    public HotelSeason(int id, String season_start, String season_end, int hotel_id) {
        this.id = id;
        this.season_start = season_start;
        this.season_end = season_end;
        this.hotel_id = hotel_id;
        this.hotel = Hotel.getFetch(hotel_id);
    }

    public HotelSeason() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeason_start() {
        return season_start;
    }

    public void setSeason_start(String season_start) {
        this.season_start = season_start;
    }

    public String getSeason_end() {
        return season_end;
    }

    public void setSeason_end(String season_end) {
        this.season_end = season_end;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public static HotelSeason getFetch(int id) {
        HotelSeason obj = null;
        String query = "SELECT * FROM season WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()){
                obj = new HotelSeason(rs.getInt("id"), rs.getString("season_start"), rs.getString("season_end"), rs.getInt("hotel_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obj;
    }


    public static ArrayList<HotelSeason> getList(){
        ArrayList<HotelSeason> hotelSeasonList = new ArrayList<>();
        HotelSeason obj;
        String query = "SELECT * FROM season";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery(query);
            while (rs.next()){
                obj = new HotelSeason();
                obj.setSeason_start(rs.getString("season_start"));
                obj.setSeason_end(rs.getString("season_end"));
                hotelSeasonList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelSeasonList;
    }

//konaklama sezonlarının hotel id sine db den alan metod
    public static ArrayList<HotelSeason> getListByHotelID( int id){
        ArrayList<HotelSeason> hotelSeasonList = new ArrayList<>();
        HotelSeason obj;
        String query = "SELECT * FROM season WHERE hotel_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                obj = new HotelSeason();
                obj.setId(rs.getInt("id"));
                obj.setSeason_start(rs.getString("season_start"));
                obj.setSeason_end(rs.getString("season_end"));
                obj.setHotel_id(rs.getInt("hotel_id"));
                hotelSeasonList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotelSeasonList;
    }

    public static boolean add(String season_start, String season_end, int hotel_id){
        String query = "INSERT INTO season (season_start, season_end, hotel_id) VALUES (?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1,season_start);
            pr.setString(2,season_end);
            pr.setInt(3, hotel_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
