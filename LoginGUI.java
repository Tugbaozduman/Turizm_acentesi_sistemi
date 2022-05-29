package com.View;

import com.Helper.Config;
import com.Helper.Helper;
import com.Model.Admin;
import com.Model.Operator;
import com.Model.User;

import javax.swing.*;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wbottom;
    private JPanel wtop;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_pass;
    private JButton btn_login;

    public LoginGUI(){
        add(wrapper);
        setSize(400,400);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

//login ekranı giriş yap butonu metodu
        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)){
                Helper.showMsg("fill");
            }
            else{
                User u = User.getFetch(fld_user_uname.getText(), fld_user_pass.getText());
                if (u==null){
                    Helper.showMsg("Kullanıcı bulunamadı. Kullanıcı adı veya şifre hatalı.");
                }
                else{
                    switch (u.getType()){
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "admin":
                            AdminGUI adGUI = new AdminGUI((Admin) u);
                            break;
                    }
                    dispose();
                }
            }
        });
    }

    public static void main(String[] args){
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}
