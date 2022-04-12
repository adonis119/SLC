package SLC.LockerHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;

import java.io.IOException;
import java.lang.invoke.SwitchPoint;
import java.util.logging.Logger;

import AppKickstarter.misc.Msg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

//======================================================================
// BarcodeReaderEmulatorController
public class LockerEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private LockerEmulator lockerEmulator;
    private MBox lockerMBox;
    private String activationResp;
    private String standbyResp;
    private String pollResp;
    public Button lockerID1;
    public Button lockerID2;
    public Button lockerID3;
    public Button lockerID4;
    public Button lockerID5;
    public Button lockerID6;
    public Button lockerID7;
    public Button lockerID8;
    public Button lockerID9;
    public Button lockerID10;
    public Button lockerID11;
    public Button lockerID12;
    public Button lockerID13;
    public Button lockerID14;
    public Button lockerID15;
    public Button lockerID16;
    public Button lockerID17;
    public Button lockerID18;
    public Button lockerID19;
    public Button lockerID20;
    public Button lockerID21;
    public Button lockerID22;
    public Button lockerID23;
    public Button lockerID24;
    //public TextField lockerNumField;
    //public TextField lockerStatusField;
    //public TextArea barcodeReaderTextArea;
    //public ChoiceBox standbyRespCBox;
    //public ChoiceBox activationRespCBox;
    public ChoiceBox pollRespCBox;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, LockerEmulator lockerEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.lockerEmulator = lockerEmulator;
        this.lockerMBox = appKickstarter.getThread("LockerHandler").getMBox();
        /*
            this.activationRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    activationResp = activationRespCBox.getItems().get(newValue.intValue()).toString();
                    //appendTextArea("Activation Response set to " + activationRespCBox.getItems().get(newValue.intValue()).toString());
                }
            });
            this.standbyRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    standbyResp = standbyRespCBox.getItems().get(newValue.intValue()).toString();
                    //appendTextArea("Standby Response set to " + standbyRespCBox.getItems().get(newValue.intValue()).toString());
                }
            });
            */
            this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
                    //appendTextArea("Poll Response set to " + pollRespCBox.getItems().get(newValue.intValue()).toString());
                }
            });
            //this.activationResp = activationRespCBox.getValue().toString();
            //this.standbyResp = standbyRespCBox.getValue().toString();
            this.pollResp = pollRespCBox.getValue().toString();
        this.goStandby();

    } // initialize

    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) throws IOException {
        Button btn = (Button) actionEvent.getSource();
        lockerMBox.send(new Msg(id, lockerMBox, Msg.Type.Locker_st, btn.getId()));
        if(!btn.getText().equals("Closed")){
            btn.setText("Closed");
            lockerMBox.send(new Msg(id, lockerMBox, Msg.Type.Locker_cl, btn.getId()));
        }
    } // buttonPressed

    public void lockerStatus(String lockerId){
        String status = "";
        switch(lockerId){
            case "lockerID1":
                status = lockerID1.getText();
                break;
            case "lockerID2":
                status = lockerID2.getText();
                break;
            case "lockerID3":
                status = lockerID3.getText();
                break;
            case "lockerID4":
                status = lockerID4.getText();
                break;
            case "lockerID5":
                status = lockerID5.getText();
                break;
            case "lockerID6":
                status = lockerID6.getText();
                break;
            case "lockerID7":
                status = lockerID7.getText();
                break;
            case "lockerID8":
                status = lockerID8.getText();
                break;
            case "lockerID9":
                status = lockerID9.getText();
                break;
            case "lockerID10":
                status = lockerID10.getText();
                break;
            case "lockerID11":
                status = lockerID11.getText();
                break;
            case "lockerID12":
                status = lockerID12.getText();
                break;
            case "lockerID13":
                status = lockerID13.getText();
                break;
            case "lockerID14":
                status = lockerID14.getText();
                break;
            case "lockerID15":
                status = lockerID15.getText();
                break;
            case "lockerID16":
                status = lockerID16.getText();
                break;
            case "lockerID17":
                status = lockerID17.getText();
                break;
            case "lockerID18":
                status = lockerID18.getText();
                break;
            case "lockerID19":
                status = lockerID19.getText();
                break;
            case "lockerID20":
                status = lockerID20.getText();
                break;
            case "lockerID21":
                status = lockerID21.getText();
                break;
            case "lockerID22":
                status = lockerID22.getText();
                break;
            case "lockerID23":
                status = lockerID23.getText();
                break;
            case "lockerID24":
                status = lockerID24.getText();
                break;
        }
        if(status.equals("Closed")){
            lockerMBox.send(new Msg(id, lockerMBox, Msg.Type.Locker_st_c, lockerId+" is closed."));
        }
        else{
            lockerMBox.send(new Msg(id, lockerMBox, Msg.Type.Locker_st_o, lockerId+" is opened."));
        }
        //this.passcodeInputBox.setText(lockerId);
    }

    public void openLocker(String lockerId){
        switch(lockerId){
            case "lockerID1":
                lockerID1.setText("Opened");
                break;
            case "lockerID2":
                lockerID2.setText("Opened");
                break;
            case "lockerID3":
                lockerID3.setText("Opened");
                break;
            case "lockerID4":
                lockerID4.setText("Opened");
                break;
            case "lockerID5":
                lockerID5.setText("Opened");
                break;
            case "lockerID6":
                lockerID6.setText("Opened");
                break;
            case "lockerID7":
                lockerID7.setText("Opened");
                break;
            case "lockerID8":
                lockerID8.setText("Opened");
                break;
            case "lockerID9":
                lockerID9.setText("Opened");
                break;
            case "lockerID10":
                lockerID10.setText("Opened");
                break;
            case "lockerID11":
                lockerID11.setText("Opened");
                break;
            case "lockerID12":
                lockerID12.setText("Opened");
                break;
            case "lockerID13":
                lockerID13.setText("Opened");
                break;
            case "lockerID14":
                lockerID14.setText("Opened");
                break;
            case "lockerID15":
                lockerID15.setText("Opened");
                break;
            case "lockerID16":
                lockerID16.setText("Opened");
                break;
            case "lockerID17":
                lockerID17.setText("Opened");
                break;
            case "lockerID18":
                lockerID18.setText("Opened");
                break;
            case "lockerID19":
                lockerID19.setText("Opened");
                break;
            case "lockerID20":
                lockerID20.setText("Opened");
                break;
            case "lockerID21":
                lockerID21.setText("Opened");
                break;
            case "lockerID22":
                lockerID22.setText("Opened");
                break;
            case "lockerID23":
                lockerID23.setText("Opened");
                break;
            case "lockerID24":
                lockerID24.setText("Opened");
                break;
        }
        lockerMBox.send(new Msg(id, lockerMBox, Msg.Type.Locker_op, lockerId));

    }


    //------------------------------------------------------------
    // getters
    public String getActivationResp() { return activationResp; }
    public String getStandbyResp()    { return standbyResp; }
    public String getPollResp()       { return pollResp; }


    //------------------------------------------------------------
    // goActive
    public void goActive() {
        updateBarcodeReaderStatus("Active");
    } // goActive


    //------------------------------------------------------------
    // goStandby
    public void goStandby() {
        updateBarcodeReaderStatus("Standby");
    } // goStandby


    //------------------------------------------------------------
    // updateBarcodeReaderStatus
    private void updateBarcodeReaderStatus(String status) {
       // barcodeReaderStatusField.setText(status);
    } // updateBarcodeReaderStatus


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        //barcodeReaderTextArea.appendText(status+"\n");
    } // appendTextArea

} // BarcodeReaderEmulatorController
