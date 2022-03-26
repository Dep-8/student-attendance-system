package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import util.DepAlert;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class BackupAndRestoreFormController {
    public Button btnBackup;

    public void btnBackup_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose backup location");
        fileChooser.setInitialFileName(LocalDate.now() + "-sas-bak");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup files (*.dep8bak)", "*.dep8bak"));
        File file = fileChooser.showSaveDialog(btnBackup.getScene().getWindow());

        if (file != null) {
            ProcessBuilder mysqlDumpProcessBuilder = new ProcessBuilder("mysqldump",
                    "-h", "localhost",
                    "--port", "3306",
                    "-u", "root",
                    "-p12345678",
                    "--add-drop-database",
                    "--databases", "dep8_student_attendance");

            mysqlDumpProcessBuilder.redirectOutput(System.getProperty("os.name").equalsIgnoreCase("windows") ? file : new File(file.getAbsolutePath() + ".dep8bak"));
            try {
                Process mysqlDump = mysqlDumpProcessBuilder.start();
                int exitCode = mysqlDump.waitFor();

                if (exitCode == 0) {
                    new DepAlert(Alert.AlertType.INFORMATION, "Backup process succeeded",
                            "Success", ButtonType.OK).show();
                } else {
                    new DepAlert(Alert.AlertType.ERROR, "Backup process failed, try again!",
                            "Backup failed", "Error", ButtonType.OK).show();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnRestore_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Select Backup File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Backup files (*.dep8bak)", "*.dep8bak"));
        File file = fileChooser.showOpenDialog(btnBackup.getScene().getWindow());

        if(file!=null){
            ProcessBuilder mysqlDumpProcessBuilder = new ProcessBuilder("mysql",
                    "-h", "localhost",
                    "--port", "3306",
                    "-u", "root",
                    "-p12345678"
                    );
            mysqlDumpProcessBuilder.redirectInput(file);

            try {
                Process mysqlRestore = mysqlDumpProcessBuilder.start();
                int exitCode = mysqlRestore.waitFor();
                if (exitCode == 0) {
                    new DepAlert(Alert.AlertType.INFORMATION, "Restore process succeeded",
                            "Success", ButtonType.OK).show();
                } else {
                    new DepAlert(Alert.AlertType.ERROR, "Restore process failed, try again!",
                            "Backup failed", "Error", ButtonType.OK).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}
