package com.View;

import com.Helper.Config;
import com.Helper.DBConnector;
import com.Helper.Helper;
import com.Model.Hotel;
import com.Model.ReservationInfo;
import com.Model.Room;
import com.Model.RoomProperties;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservationGUI extends JFrame {
    private JPanel wrapper;
    private JPanel pnl_reservation;
    private JPanel pnl_top;
    private JPanel pnl_client_info;
    private JTextField fld_client_name;
    private JTextField fld_client_phone;
    private JTextField fld_client_mail;
    private JTextArea txtArea_client_note;
    private JButton btn_add_reservation;
    private JTextField fld_hotel_name;
    private JTextArea txtArea_hotel_address;
    private JTextField fld_hotel_phone;
    private JTextArea txtArea_hotel_property;
    private JTextField fld_room_type;
    private JTextArea txtArea_room_property;
    private JTextField fld_adult_numb;
    private JTextField fld_child_numb;
    private JTextField fld_check_in_date;
    private JTextField fld_check_out_date;
    private JTextField fld_total_price2;
    private JTextField fld_total_price;

    private final Room room;
    private int adult_numb = 0;
    private int child_numb = 0;
    private String check_in;
    private String check_out;
    private int total_price;
    private int total_price2;

    public ReservationGUI(Room room, int adult_numb, int child_numb, String check_in, String check_out){
        this.room = room;
        this.adult_numb = adult_numb;
        this.child_numb = child_numb;
        this.check_in = check_in;
        this.check_out = check_out;
        add(wrapper);
        setSize(1200,700);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        Hotel hotel = Hotel.getFetch(room.getHotel_id());
        RoomProperties roomProperty = RoomProperties.getFetch(room.getId());

        fld_hotel_name.setText(hotel.getName());
        txtArea_hotel_address.setText(hotel.getAddress());
        fld_hotel_phone.setText(hotel.getPhone());
        txtArea_hotel_property.setText(hotel.getProperty());
        fld_room_type.setText(room.getRoom_type());
        txtArea_room_property.setText(roomProperty.getProperty() + "\n" + roomProperty.getBed() + "\n" + roomProperty.getArea());
        fld_adult_numb.setText(Integer.toString(adult_numb));
        fld_child_numb.setText(Integer.toString(child_numb));
        fld_check_in_date.setText(check_in);
        fld_check_out_date.setText(check_out);

        total_price2 = 2* (   (room.getAdult_price() * adult_numb) + (room.getChild_price() * child_numb)  );
        fld_total_price2.setText(total_price2 + " TL");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date check_in_date = null;
        Date check_out_date = null;
        try {
            check_in_date = formatter.parse(check_in);
            check_out_date = formatter.parse(check_out);
        } catch (ParseException ex) {

        }

        long diff = check_out_date.getTime() - check_in_date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        total_price = (int) (days * (   (room.getAdult_price() * adult_numb) + (room.getChild_price() * child_numb)   ));
        fld_total_price.setText(String.valueOf(total_price) + " TL");

        btn_add_reservation.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_client_name) || Helper.isFieldEmpty(fld_client_phone) || Helper.isFieldEmpty(fld_client_mail) || Helper.isAreaEmpty(txtArea_client_note)){
                Helper.showMsg("Lütfen Rezervasyon bilgilerini doldurunuz.");
            }
            else{
                String client_name = fld_client_name.getText();
                String client_phone = fld_client_phone.getText();
                String client_email = fld_client_mail.getText();
                String client_note = txtArea_client_note.getText();
                int room_id = room.getId();

                if (ReservationInfo.add(client_name, client_phone, client_email, client_note, room_id, check_in, check_out, adult_numb, child_numb, total_price)){
                    Helper.showMsg("Rezervasyon işlemi yapıldı.");
                     int newStock = room.getStock() - 1;
                     updateRoomStock(newStock, room_id);
                }
            }
        });
    }

    public static boolean updateRoomStock (int stock, int id){
        String query = "UPDATE room SET stock = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,stock);
            pr.setInt(2,id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
