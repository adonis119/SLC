package SLC.SLServer.Emulator;

import AppKickstarter.misc.Msg;
import SLC.SLCStarter;
import SLC.SLServer.SLServerHandler;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// SLServerEmulator
public class SLServerEmulator extends SLServerHandler {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private SLServerEmulatorController slServerEmulatorController;

    //------------------------------------------------------------
    // SLServerEmulator
    public SLServerEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    }


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "SLServerEmulator.fxml";
        loader.setLocation(SLServerEmulator.class.getResource(fxmlName));
        root = loader.load();
        slServerEmulatorController = (SLServerEmulatorController) loader.getController();
        slServerEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 470));
        myStage.setTitle("SL Server");
        // Set Pop up position of scene
        myStage.setX(700);
        myStage.setY(30);
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    }


    //------------------------------------------------------------
    //  Since SL server is not a device of SLC, It should not handle standby,active...etc.. from SLC, but because we test SL server by GUI, using HWhandler to emulate
    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (slServerEmulatorController.getPollResp()) {
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

    protected  void fetchAmount(Msg msg){
        slServerEmulatorController.fetchAmount(msg.getDetails());
    }
    protected void verifyBarCode(Msg msg) {
        slServerEmulatorController.verifyOrder(msg.getDetails());
    } // handlePoll

}

