package SLC.TouchDisplayHandler.Emulator;

import SLC.SLCStarter;
import SLC.TouchDisplayHandler.TouchDisplayHandler;
import AppKickstarter.misc.Msg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// TouchDisplayEmulator
public class TouchDisplayEmulator extends TouchDisplayHandler {
    private final int WIDTH = 680;
    private final int HEIGHT = 570;
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private TouchDisplayEmulatorController touchDisplayEmulatorController;

    //------------------------------------------------------------
    // TouchDisplayEmulator
    public TouchDisplayEmulator(String id, SLCStarter slcStarter) throws Exception {
	super(id, slcStarter);
	this.slcStarter = slcStarter;
	this.id = id;
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	// Parent root;
	myStage = new Stage();
	reloadStage("TouchDisplayEmulator.fxml");
	myStage.setTitle("Touch Display");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    slcStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // reloadStage
    private void reloadStage(String fxmlFName) {
        TouchDisplayEmulator touchDisplayEmulator = this;

        Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    log.info(id + ": loading fxml: " + fxmlFName);

		    // get the latest pollResp string, default to "ACK"
		    String pollResp = "ACK";
		    if (touchDisplayEmulatorController != null) {
		        pollResp = touchDisplayEmulatorController.getPollResp();
                    }

		    Parent root;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlFName));
		    root = loader.load();
		    touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
		    touchDisplayEmulatorController.initialize(id, slcStarter, log, touchDisplayEmulator, pollResp);
		    myStage.setScene(new Scene(root, WIDTH, HEIGHT));
		} catch (Exception e) {
		    log.severe(id + ": failed to load " + fxmlFName);
		    e.printStackTrace();
		}
	    }
	});
    } // reloadStage


    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());

        switch (msg.getDetails()) {
            case "BlankScreen":
                reloadStage("TouchDisplayEmulator.fxml");
                break;
            case "MainMenu":
                reloadStage("TouchDisplayMainMenu.fxml");
                break;
            case "Confirmation":
                reloadStage("TouchDisplayConfirmation.fxml");
                break;
            case "StoreDelivery":
                reloadStage("TouchDisplayStoreDelivery.fxml");
                break;
            case "Maintenance":
                reloadStage("TouchDisplayMaintenance.fxml");
                break;
            case "OpenLockerDoor":
                reloadStage("TouchDisplayOpenLockerDoor.fxml");
                break;
            case "AdminPage":
                reloadStage("TouchDisplayAdminPage.fxml");
                break;
            case "Payment":
                reloadStage("TouchDisplayPayment.fxml");
                break;
            default:
                log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
                break;
        }
    } // handleUpdateDisplay


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (touchDisplayEmulatorController.getPollResp()) {
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
    protected void handleUpdatePasscodeInput(Msg msg){
        touchDisplayEmulatorController.td_updatePasscodeInput(msg.getDetails());
    }// Handle update passcode input box value when user click the number on the display screen
    protected void handleUpdateBarcodeInput(Msg msg){
       touchDisplayEmulatorController.td_updateBarcodeInput(msg.getDetails());
    }//// Handle update barcode input when SLC send Barcode to TD
    protected void handleUpdateServerReply(Msg msg){
        touchDisplayEmulatorController.td_updateServerReply(msg.getDetails());
    }//

    protected void handleWrongPasscode(Msg msg) {
        touchDisplayEmulatorController.td_wrongPasscode(msg.getDetails());
    }
    protected void handleUpdateOpenLockerDoorDisplay(Msg msg){
        touchDisplayEmulatorController.td_updateOpenLockerDoorDisplay(msg.getDetails());
    }
    protected void handleUpdateOpenLockerDoorTitleDetail(String storeOrPick){
        touchDisplayEmulatorController.td_updateOpenLockerDoorTitleDetail(storeOrPick);
    }
    protected void handleShowPaymentError(Msg msg){
        touchDisplayEmulatorController.td_showPaymentError(msg.getDetails());
    }

    protected void handleUpdateOctopusDisplay(String amount) {
        touchDisplayEmulatorController.td_openOctopusDisplay(amount);
    }
    protected void handleUpdateAdminLockerStatus(Msg msg){
        touchDisplayEmulatorController.td_adminPageHandleUpdateLockerStatus(msg.getDetails());
    }
    protected void handleAdminPageGetLockerStatus(){
        touchDisplayEmulatorController.td_adminPageGetLockerStatus();
    }
} // TouchDisplayEmulator
