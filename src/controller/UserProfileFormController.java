package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import security.Principal;
import security.SecurityContextHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserProfileFormController {
    public TextField txtName;
    public TextField txtUsername;
    public TextField txtRole;
    public TextField txtPassword;
    public TextField txtConfirmPassword;
    public Button btnUpdate;

    public void initialize(){
        Principal principal = SecurityContextHolder.getPrincipal();
        txtName.setText(principal.getName());
        txtUsername.setText(principal.getUsername());
        txtRole.setText(String.valueOf(principal.getRole()));


    }
    public void btnUpdate_OnAction(ActionEvent actionEvent) throws SQLException {
        if(txtPassword.getText().isEmpty()||txtConfirmPassword.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please enter a password").show();
            return;
        }
        if(!txtPassword.getText().equals(txtConfirmPassword.getText())){
            new Alert(Alert.AlertType.ERROR,"Password and Confirm password does not match").show();
            return;
        }
        else{
            Connection connection= DBConnection.getInstance().getConnection();
            PreparedStatement pst = connection.prepareStatement("UPDATE user SET username=? ,password=? WHERE dep8_student_attendance.user.name=?");
            pst.setString(1,txtUsername.getText());
            pst.setString(2,txtConfirmPassword.getText());
            pst.setString(3,txtName.getText());
            int rst = pst.executeUpdate();
            if(rst!=1){
                new Alert(Alert.AlertType.ERROR,"Faile To Update").show();
            }else{
                new Alert(Alert.AlertType.INFORMATION,"Successfully Updated!").show();
            }



        }
    }

    public void btnBaclOn_Action(ActionEvent actionEvent) {
    }
}
