package SLC.OctopusReaderDriver.Emulator;

import AppKickstarter.misc.Msg;
import SLC.SLCStarter;
import SLC.OctopusReaderDriver.OctopusReaderDriver;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// OctopusReaderDriver
public class OctopusReaderEmulator extends OctopusReaderDriver {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private OctopusReaderEmulatorController octopusReaderEmulatorController;

    //------------------------------------------------------------
    // OctopusReaderEmulator
    public OctopusReaderEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    } // OctopusReaderEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "OctopusReaderEmulator.fxml";
        loader.setLocation(OctopusReaderEmulator.class.getResource(fxmlName));
        root = loader.load();
        octopusReaderEmulatorController = (OctopusReaderEmulatorController) loader.getController();
        octopusReaderEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 600));
        myStage.setTitle("Octopus Reader");
        // Set Pop up position of scene
        myStage.setX(1100);
        myStage.setY(30);
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    } // start


    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        // fixme
        super.handleGoActive();
        octopusReaderEmulatorController.appendTextArea("Octopus Reader Activated");
        octopusReaderEmulatorController.goActive();
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        // fixme
        super.handleGoStandby();
        octopusReaderEmulatorController.appendTextArea("Octopus Reader Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (octopusReaderEmulatorController.getPollResp()) {
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

    //------------------------------------------------------------
    // Accept payment amount
    protected void updateAmount(Msg msg) {
        octopusReaderEmulatorController.updateAmount(msg.getDetails());
    } // Accept payment amount
}

