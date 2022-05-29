package com.View;

import com.Helper.Config;
import com.Helper.Helper;
import com.Helper.Item;
import com.Model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoomAddGUI extends JFrame {
    private JPanel wrapper;
    private JPanel pnl_room_add_top;
    private JPanel pnl_room_add_left;
    private JPanel pnl_room_add_right;
    private JComboBox cmb_room_hotelname;
    private JComboBox cmb_room_type;
    private JTextField fld_room_stock;
    private JComboBox cmb_room_hotel_type;
    private JTextField fld_room_area;
    private JTextField fld_room_bed;
    private JComboBox cmb_season;
    private JTextField fld_adult_price;
    private JTextField fld_child_price;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JRadioButton radioButton5;
    private JButton btn_room_add;

    private String select_room_type;
    private int addedRoom_id;

    private final Admin admin;

    public RoomAddGUI(Admin admin) {
        this.admin = admin;
        add(wrapper);
        setSize(800, 400);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        radioButton1.setText(Helper.roomProperty("1"));
        radioButton2.setText(Helper.roomProperty("2"));
        radioButton3.setText(Helper.roomProperty("3"));
        radioButton4.setText(Helper.roomProperty("4"));
        radioButton5.setText(Helper.roomProperty("5"));

        loadHotelNameCombo();
        loadHotelTypeCombo();
        loadSeasonCombo();

//oda ekleme sayfasında seçilen odaya göre dinamik olarak pansiyon tipini getiren metod
        cmb_room_hotelname.addActionListener(event -> {
            loadHotelTypeCombo();
            loadSeasonCombo();
            cmb_room_type.setSelectedIndex(0);
            cmb_season.setSelectedIndex(0);
        });

//oda ekle butonu kodları başlangıcı
        btn_room_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_room_stock) || Helper.isFieldEmpty(fld_adult_price) || Helper.isFieldEmpty(fld_child_price) ||
                    Helper.isFieldEmpty(fld_room_bed) || Helper.isFieldEmpty(fld_room_area) ||
                    cmb_room_type.getSelectedItem().toString().equals("") || cmb_room_hotel_type.getSelectedItem() == null ||
                    cmb_season.getSelectedItem() == null || cmb_room_hotelname == null ||
                        (!radioButton1.isSelected() && !radioButton2.isSelected() && !radioButton3.isSelected() &&
                        !radioButton4.isSelected() && !radioButton5.isSelected())){
                Helper.showMsg("fill");
            }
            else {
                String room_type = cmb_room_type.getSelectedItem().toString();
                int stock = Integer.parseInt(fld_room_stock.getText());
                int season_id=0;
                int adult_price = Integer.parseInt(fld_adult_price.getText().toString());
                int child_price = Integer.parseInt(fld_child_price.getText().toString());
                Item hotelTypeItem = (Item) cmb_room_hotel_type.getSelectedItem();
                int hotel_type_id = hotelTypeItem.getKey();
                Item hotelItem = (Item) cmb_room_hotelname.getSelectedItem();
                int hotel_id = hotelItem.getKey();
                for (HotelSeason obj : HotelSeason.getListByHotelID(hotel_id)){ //season ıd yi çekmek için yazıldı
                    String season = (obj.getSeason_start().toString() + "  -  " + obj.getSeason_end().toString());
                    if (season.equals(cmb_season.getSelectedItem().toString())){
                        season_id = obj.getId();
                        break;
                    }
                }

                if (Room.add(room_type, stock, season_id, adult_price, child_price, hotel_type_id, hotel_id)){
                    ArrayList<Room> roomList = Room.getList();
                    Room addedRoom = roomList.get(Room.getList().size()-1);
                    addedRoom_id = addedRoom.getId();
                    String room_properties = "";
                    for (int i = 1; i<=7; i++){  //room property ekleme
                        switch (i){
                            case 1:
                                if (radioButton1.isSelected()){
                                    room_properties += radioButton1.getText();
                                }
                                break;
                            case 2:
                                if (radioButton2.isSelected()){
                                    room_properties += "\n"+radioButton2.getText();
                                }
                                break;
                            case 3:
                                if (radioButton3.isSelected()){
                                    room_properties += "\n"+radioButton3.getText();
                                }
                                break;
                            case 4:
                                if (radioButton4.isSelected()){
                                    room_properties += "\n" + radioButton4.getText();
                                }
                                break;
                            case 5:
                                if (radioButton5.isSelected()){
                                    room_properties += "\n" + radioButton5.getText();
                                }
                                break;
                        }
                    }

                    RoomProperties.add(room_properties, addedRoom_id, fld_room_bed.getText(), Integer.parseInt(fld_room_area.getText().toString()) );
                    Helper.showMsg("done");
                    cmb_room_hotelname.setSelectedIndex(0);
                    cmb_room_type.setSelectedIndex(0);
                    fld_room_stock.setText(null);
                    cmb_room_hotel_type.setSelectedIndex(0);
                    cmb_season.setSelectedIndex(0);
                    fld_adult_price.setText(null);
                    fld_child_price.setText(null);
                    fld_room_bed.setText(null);
                    fld_room_area.setText(null);
                    radioButton1.setSelected(false);
                    radioButton2.setSelected(false);
                    radioButton3.setSelected(false);
                    radioButton4.setSelected(false);
                    radioButton5.setSelected(false);
                }
            }
        });
//oda ekle butonu kodları bitişi

    }

//Hotel isimlerini combo box a aktaran metod
    public void loadHotelNameCombo(){
        cmb_room_hotelname.removeAllItems();
        cmb_room_hotelname.addItem(new Item(0,null));
        for (Hotel obj : Hotel.getList()){
            cmb_room_hotelname.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

//oda ekleme sayfasında seçilen otele göre pansiyon türlerini combo box a aktaran metod
    private void loadHotelTypeCombo() {
        Item hotelItem = (Item) cmb_room_hotelname.getSelectedItem();
        cmb_room_hotel_type.removeAllItems();
        cmb_room_hotel_type.addItem(new Item(0,null));
        for (HotelType obj : HotelType.getListByHotelID(hotelItem.getKey())){

            cmb_room_hotel_type.addItem(new Item(obj.getId(), obj.getType()));
        }
    }

//oda ekleme sayfasında seçilen otele göre sezon türlerini combo box a aktaran metod
    private void loadSeasonCombo() {
        Item hotelItem = (Item) cmb_room_hotelname.getSelectedItem();
        cmb_season.removeAllItems();
        cmb_season.addItem(new Item(0,null));
        for (HotelSeason obj : HotelSeason.getListByHotelID(hotelItem.getKey())){
            cmb_season.addItem(new Item(obj.getId(), (obj.getSeason_start() + "  -  " + obj.getSeason_end())));
        }
    }
}
