package SLC.OctopusReaderDriver.Emulator;

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
// OctopusReaderEmulatorController
public class OctopusReaderEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private OctopusReaderEmulator octopusReaderEmulator;
    private MBox octopusReaderMBox;
    private String activationResp;
    private String standbyResp;
    private String pollResp;
    public TextField octopusCardField;
    public TextField octopusReaderStatusField;
    public TextField octopusAmountField;
    public TextField requestedAmountField;
    public TextArea octopusReaderTextArea;
    public ChoiceBox standbyRespCBox;
    public ChoiceBox activationRespCBox;
    public ChoiceBox pollRespCBox;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, OctopusReaderEmulator octopusReaderEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.octopusReaderEmulator = octopusReaderEmulator;
        this.octopusReaderMBox = appKickstarter.getThread("OctopusReaderDriver").getMBox();
        this.activationRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                activationResp = activationRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Activation Response set to " + activationRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.standbyRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                standbyResp = standbyRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Standby Response set to " + standbyRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Poll Response set to " + pollRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.activationResp = activationRespCBox.getValue().toString();
        this.standbyResp = standbyRespCBox.getValue().toString();
        this.pollResp = pollRespCBox.getValue().toString();
        this.goStandby();
        requestedAmountField.setText(appKickstarter.getProperty("OctopusReader.Requested.Amount"));
    } // initialize


    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        switch (btn.getText()) {
            case "Octopus card 1":
                octopusCardField.setText(appKickstarter.getProperty("OctopusReader.OctopusCard1"));
                octopusAmountField.setText(appKickstarter.getProperty("OctopusReader.OctopusCard1.Amount"));
                break;

            case "Octopus card 2":
                octopusCardField.setText(appKickstarter.getProperty("OctopusReader.OctopusCard2"));
                octopusAmountField.setText(appKickstarter.getProperty("OctopusReader.OctopusCard2.Amount"));
                break;

            case "Octopus card 3":
                octopusCardField.setText(appKickstarter.getProperty("OctopusReader.OctopusCard3"));
                octopusAmountField.setText(appKickstarter.getProperty("OctopusReader.OctopusCard3.Amount"));
                break;

            case "Reset":
                octopusCardField.setText("");
                octopusAmountField.setText("");
                break;

            case "Send Octopus card":
                // TO-DO cal the amount enough to get fee? If ok return success to get $$ message?
                if (processPayment()) {
                    // if payment success
                    octopusReaderMBox.send(new Msg(id, octopusReaderMBox, Msg.Type.OR_OctopusCardRead, octopusCardField.getText()));
                    octopusReaderTextArea.appendText("Sending Octopus Card NO. :" + octopusCardField.getText() + "\n");
                } else {
                    // if payment fail
                }
                break;

            case "Activate/Standby":
                octopusReaderMBox.send(new Msg(id, octopusReaderMBox, Msg.Type.OR_GoActive, octopusCardField.getText()));
                octopusReaderTextArea.appendText("Removing card\n");
                break;

            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed

    //------------------------------------------------------------
    // Payment handling
    private boolean processPayment() {
        // change price text to double
        double requestedAmount, octopusAmount;
        String parseRequest, parseOctopus;
        parseRequest = requestedAmountField.getText().substring(1);
        parseOctopus = octopusAmountField.getText().substring(1);
        requestedAmount = Double.parseDouble(parseRequest);
        octopusAmount = Double.parseDouble(parseOctopus);

        if (octopusAmount - requestedAmount < -50.0) {
            return false; // if not enough money
        }
        // if enough money
        octopusAmountField.setText("$ " + String.format("%.2f", (octopusAmount - requestedAmount)));

        return true;
    } // Payment handling

    //------------------------------------------------------------
    // getters
    public String getActivationResp() {
        return activationResp;
    }

    public String getStandbyResp() {
        return standbyResp;
    }

    public String getPollResp() {
        return pollResp;
    }


    //------------------------------------------------------------
    // goActive
    public void goActive() {
        updateOctopusReaderStatus("Active");
    } // goActive


    //------------------------------------------------------------
    // goStandby
    public void goStandby() {
        updateOctopusReaderStatus("Standby");
    } // goStandby


    //------------------------------------------------------------

    private void updateOctopusReaderStatus(String status) {
        octopusReaderStatusField.setText(status);
    }


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        octopusReaderTextArea.appendText(status + "\n");
    } // appendTextArea
}
