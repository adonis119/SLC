package SLC.SLServer.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;

import java.util.ArrayList;
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
    public TextField sLServerDeliverySizeField;
    public TextField sLServerDeliveryAmount;
    public TextField sLServerStatusField;
    public TextArea sLServerTextArea;
    public ChoiceBox pollRespCBox;
    public ArrayList<deliveryOrder> deliveryOrderArrayList = new ArrayList<>();


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
                sLServerDeliverySizeField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder1.size"));
                sLServerDeliveryAmount.setText(appKickstarter.getProperty("SLServer.DeliveryOrder1.amount"));
                break;
            case "Delivery Order 2":
                sLServerDeliveryOrderIDField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder2"));
                sLServerDeliverySizeField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder2.size"));
                sLServerDeliveryAmount.setText(appKickstarter.getProperty("SLServer.DeliveryOrder2.amount"));
                break;
            case "Delivery Order 3":
                sLServerDeliveryOrderIDField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder3"));
                sLServerDeliverySizeField.setText(appKickstarter.getProperty("SLServer.DeliveryOrder3.size"));
                sLServerDeliveryAmount.setText(appKickstarter.getProperty("SLServer.DeliveryOrder3.amount"));
                break;
            case "Reset":
                sLServerDeliveryOrderIDField.setText("");
                sLServerDeliverySizeField.setText("");
                sLServerDeliveryAmount.setText("");
                break;


            case "Create Delivery Order to SL Server":
                if (sLServerDeliveryOrderIDField.getText().equals("") || sLServerDeliverySizeField.getText().equals("") || sLServerDeliveryAmount.getText().equals("")) {
                    sLServerTextArea.appendText("Please fill in all the input box! \n");
                    return;
                }

                if (deliveryOrderArrayList.isEmpty()) {
                    deliveryOrderArrayList.add(new deliveryOrder(sLServerDeliveryOrderIDField.getText(), Integer.parseInt(sLServerDeliverySizeField.getText()),
                            Integer.parseInt(sLServerDeliveryAmount.getText())));
                    sLServerTextArea.appendText("Created Delivery order :\n");
                    sLServerTextArea.appendText("Delivery ID :" + sLServerDeliveryOrderIDField.getText() + "\n");
                    sLServerTextArea.appendText("Delivery Size :" + sLServerDeliverySizeField.getText() + "\n");
                    sLServerTextArea.appendText("Delivery Amount :" + sLServerDeliveryAmount.getText() + "\n");
                } else {
                    for (int i = 0; i < deliveryOrderArrayList.size(); i++) {
                        if (deliveryOrderArrayList.get(i).orderId.compareTo(sLServerDeliveryOrderIDField.getText()) == 0) {
                            sLServerTextArea.appendText("This delivery order ID is already exits ! \n");
                            return;
                        }
                    }
                    deliveryOrderArrayList.add(new deliveryOrder(sLServerDeliveryOrderIDField.getText(), Integer.parseInt(sLServerDeliverySizeField.getText()),
                            Integer.parseInt(sLServerDeliveryAmount.getText())));
                    sLServerTextArea.appendText("Created Delivery order :\n");
                    sLServerTextArea.appendText("Delivery ID :" + sLServerDeliveryOrderIDField.getText() + "\n");
                    sLServerTextArea.appendText("Delivery Size :" + sLServerDeliverySizeField.getText() + "\n");
                    sLServerTextArea.appendText("Delivery Amount :" + sLServerDeliveryAmount.getText() + "\n");

                }
                break;
            case "Send Delivery Order to SLC":
                sLServerMBox.send(new Msg(id, sLServerMBox, Msg.Type.SLS_GetDeliveryOrder, sLServerDeliveryOrderIDField.getText()));
                sLServerTextArea.appendText("Sending Delivery order NO. :" + sLServerDeliveryOrderIDField.getText() + "\n");

                break;
            default:
                log.warning(id + ": unknown button: [" + btn.getText() + "]");
                break;
        }
    } // buttonPressed


    //------------------------------------------------------------
    // getters
    public String getPollResp() {
        return pollResp;
    }


    private void updateSLServerStatus(String status) {
        sLServerStatusField.setText(status);
    }


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        sLServerTextArea.appendText(status + "\n");
    } // appendTextArea

    public void verifyOrder(String orderID){
        String tempOrderID = orderID.split("-")[0]+orderID.split("-")[1] ;
        if(!deliveryOrderArrayList.isEmpty()){
            for(int i =0;i<deliveryOrderArrayList.size();i++){
                if(deliveryOrderArrayList.get(i).orderId.equals(tempOrderID)){
                    sLServerMBox.send(new Msg(id, sLServerMBox, Msg.Type.SLS_ReplyDeliveryOrderForGui, "This barcode is available!"));
                    sLServerMBox.send(new Msg(id, sLServerMBox, Msg.Type.SLS_ReplyOpenLocker, String.valueOf(deliveryOrderArrayList.get(i).size)));
                    return;
                }
            }
        }
        sLServerMBox.send(new Msg(id, sLServerMBox, Msg.Type.SLS_ReplyDeliveryOrderForGui, "This barcode is not available!"));
    }

    private class deliveryOrder {
        String orderId = "";
        int size = 1;
        double amount = 0;

        deliveryOrder(String orderID, int size, double amount) {
            this.orderId = orderID;
            this.size = size;
            this.amount = amount;
        }
    }

    //------------------------------------------------------------
    // fetch data
    public void fetchAmount(String deliveryID) {
        // send amount
        for (int i = 0; i < deliveryOrderArrayList.size(); i++) {
            deliveryOrder current = deliveryOrderArrayList.get(i);
            if (current.orderId.equals(deliveryID)) {
                sLServerMBox.send(new Msg(id, sLServerMBox, Msg.Type.SLS_ReplyAmount, ("$ " + current.amount)));
            }

        }
    } // fetch data
}
