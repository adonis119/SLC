package SLC.LockerHandler.Emulator;

import AppKickstarter.misc.Msg;
import SLC.LockerHandler.LockerHandler;
import SLC.SLCStarter;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// LockerEmulator
public class LockerEmulator extends LockerHandler {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private LockerEmulatorController lockerEmulatorController;

    //------------------------------------------------------------
    // LockerEmulator
    public LockerEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    } // LockerEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "LockerEmulator.fxml";
        loader.setLocation(LockerEmulator.class.getResource(fxmlName));
        root = loader.load();
        lockerEmulatorController = (LockerEmulatorController) loader.getController();
        lockerEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 800, 700));
        myStage.setTitle("Locker");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    } // LockerEmulator

    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        // fixme
        super.handleGoActive();
        //LockerEmulatorController.appendTextArea("Octopus Reader Activated");
        //LockerEmulatorController.goActive();
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        // fixme
        super.handleGoStandby();
        //LockerEmulatorController.appendTextArea("Octopus Reader Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (lockerEmulatorController.getPollResp()) {
            case "ACK":
                slc.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                break;

            case "NAK":
                slc.send(new Msg(id, mbox, Msg.Type.PollNak, id + " is down!"));
                break;

            case "Ignore":
                // Just ignore.  do nothing!!
                break;
        }

    } // handlePoll

    protected void checkLockerStatus(Msg msg){
        lockerEmulatorController.lockerStatus(msg.getDetails());
    }

    protected void openLockerDoor(Msg msg){
        lockerEmulatorController.openLocker(msg.getDetails());
    }
} // LockerEmulator
