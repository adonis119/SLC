package SLC.BarcodeReaderDriver.Emulator;

import AppKickstarter.misc.Msg;
import SLC.SLCStarter;
import SLC.BarcodeReaderDriver.BarcodeReaderDriver;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// BarcodeReaderEmulator
public class BarcodeReaderEmulator extends BarcodeReaderDriver {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private BarcodeReaderEmulatorController barcodeReaderEmulatorController;

    //------------------------------------------------------------
    // BarcodeReaderEmulator
    public BarcodeReaderEmulator(String id, SLCStarter slcStarter) {
	super(id, slcStarter);
	this.slcStarter = slcStarter;
	this.id = id;
    } // BarcodeReaderEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "BarcodeReaderEmulator.fxml";
	loader.setLocation(BarcodeReaderEmulator.class.getResource(fxmlName));
	root = loader.load();
	barcodeReaderEmulatorController = (BarcodeReaderEmulatorController) loader.getController();
	barcodeReaderEmulatorController.initialize(id, slcStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, 350, 470));
	myStage.setTitle("Barcode Reader");
    // Set Pop up position of scene
    myStage.setX(1100);
    myStage.setY(300);
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    slcStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // BarcodeReaderEmulator


    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        // fixme
	super.handleGoActive();
	barcodeReaderEmulatorController.appendTextArea("Barcode Reader Activated");
    barcodeReaderEmulatorController.goActive();
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        // fixme
	super.handleGoStandby();
	barcodeReaderEmulatorController.appendTextArea("Barcode Reader Standby");
	barcodeReaderEmulatorController.goStandby();
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();
        switch (barcodeReaderEmulatorController.getPollResp()) {
            case "ACK":
                log.info("sending ack");
                slc.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                break;
            case "NAK":
                log.info("sending nak");
                slc.send(new Msg(id, mbox, Msg.Type.PollNak, id + " is down!"));
                break;
            case "Ignore":
                // Just ignore.  do nothing!!
                break;
        }
    } // handlePoll
} // BarcodeReaderEmulator
