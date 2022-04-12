package SLC.SLServer.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


//======================================================================
// SLServerEmulatorController
public class SLServerEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private SLServerEmulator sLServerEmulator;
    private MBox sLServerMBox;
    private String pollResp;
    public TextField sLServerDeliveryOrderIDField;
    public TextField sLServerDeliveryOrderBarcodeField;
    public TextField sLServerStatusField;
    public TextArea sLServerTextArea;
    public ChoiceBox pollRespCBox;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, SLServerEmulator sLServerEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.sLServerEmulator = sLServerEmulator;
        this.sLServerMBox = appKickstarter.getThread("SLServer").getMBox();
        // No need handle standby and activate by SLC since SL Server is not a device of SLC
        this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Poll Response set to " + pollRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.pollResp = pollRespCBox.getValue().toString();
    } // initialize

    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Delivery Order 1":
                sLServerDeliveryOrderIDField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder1"));
                break;
            case "Delivery Order 2":
                sLServerDeliveryOrderIDField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder2"));
                break;
            case "Delivery Order 3":
                sLServerDeliveryOrderIDField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder3"));
                break;
            case "Reset":
                sLServerDeliveryOrderIDField.setText("");
                break;
            case "Send Delivery Order to SLC":
                sLServerMBox.send(new Msg(id, sLServerMBox, Msg.Type.SLS_GetDeliveryOrder, sLServerDeliveryOrderIDField.getText()));
                sLServerTextArea.appendText("Sending Delivery order NO. :" + sLServerDeliveryOrderIDField.getText()+"\n");
                break;
            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed


    //------------------------------------------------------------
    // getters
    public String getPollResp()       { return pollResp; }


    private void updateSLServerStatus(String status) {
        sLServerStatusField.setText(status);
    }


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        sLServerTextArea.appendText(status+"\n");
    } // appendTextArea
}
