package SLC.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.logging.Logger;


//======================================================================
// TouchDisplayEmulatorController
public class TouchDisplayEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private TouchDisplayEmulator touchDisplayEmulator;
    private MBox touchDisplayMBox;
    private MBox slServerMbox;
    private String selectedScreen;
    private String pollResp;
    public ChoiceBox screenSwitcherCBox;
    public ChoiceBox pollRespCBox;
    public Text passcodeInputBox;
    public Text barcodeInputBox;
    public Text replyInputBox;
    public Text wrongPasscodeResponse;
    public Rectangle touchDisplayLocker1;
    public Rectangle touchDisplayLocker2;
    public Rectangle touchDisplayLocker3;
    public Rectangle touchDisplayLocker4;
    public Rectangle touchDisplayLocker5;
    public Rectangle touchDisplayLocker6;
    public Rectangle touchDisplayLocker7;
    public Rectangle touchDisplayLocker8;
    public Rectangle touchDisplayLocker9;
    public Rectangle touchDisplayLocker10;
    public Rectangle touchDisplayLocker11;
    public Rectangle touchDisplayLocker12;
    public Rectangle touchDisplayLockerLeft;
    public Text touchDisplayLockerLeftText;
    public Rectangle touchDisplayLockerRight;
    public Text touchDisplayLockerRightText;
    public Text touchDisplayLockerDetailText;
    public  Text touchDisplayLockerContent;
    public Text touchDisplayLockerTitle;
    public  Text touchDisplayPaymentError;
    public  Text touchDisplayPaymentError2;
    private String[] rightLockerIDList = {"lockerID5", "lockerID6", "lockerID13", "lockerID14", "lockerID21", "lockerID22", "lockerID23", "lockerID24",
            "lockerID15", "lockerID16", "lockerID7", "lockerID8"};
    private String[] leftLockerIDList = {"lockerID1", "lockerID2", "lockerID9", "lockerID10", "lockerID17", "lockerID18", "lockerID19", "lockerID20",
            "lockerID11", "lockerID12", "lockerID3", "lockerID4"};

    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator, String pollRespParam) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.touchDisplayEmulator = touchDisplayEmulator;
        this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        this.pollResp = pollRespParam;
        this.pollRespCBox.setValue(this.pollResp);
        this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
            }
        });
        this.screenSwitcherCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectedScreen = screenSwitcherCBox.getItems().get(newValue.intValue()).toString();
                switch (selectedScreen) {
                    case "Blank":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
                        break;

                    case "Main Menu":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                        break;

                    case "Confirmation":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
                        break;
                    case "Store Delivery":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "StoreDelivery"));
                        break;
                    case "Maintenance":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Maintenance"));
                        break;
                    case "Admin Page":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "AdminPage"));
                        break;
                    case "Payment":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Payment"));
                        break;
                    case "Open Locker Door":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "OpenLockerDoor"));
                        break;
                }
            }
        });
        this.selectedScreen = screenSwitcherCBox.getValue().toString();
    } // initialize


    //------------------------------------------------------------
    // getSelectedScreen
    public String getSelectedScreen() {
        return selectedScreen;
    } // getSelectedScreen


    //------------------------------------------------------------
    // getPollResp
    public String getPollResp() {
        return pollResp;
    } // getPollResp


    //------------------------------------------------------------
    // td_mouseClick
    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
        //send back to driver
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick

    public void td_updatePasscodeInput(String passcodeInput) {
        this.passcodeInputBox.setText(passcodeInput);
    } // update passcode input when slc send a msg to update it

    public void td_updateBarcodeInput(String barcodeInput) {
        // Prevent user send barcode before the store delivery loaded
        if (this.selectedScreen.compareTo("Store Delivery") == 0) {
            this.barcodeInputBox.setText(barcodeInput);
            //Pass barcode data from Controller to Handler
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.SLS_GetDeliveryOrder, barcodeInputBox.getText()));
        }
    } // update barcode input when slc send a msg to update it

    public void td_updateServerReply(String reply) {
        this.replyInputBox.setText(reply);

    }

    public void td_updateOpenLockerDoorDisplay(String lockerId) {
        String rightOrLeftLocker = "R";
        String openedLockerId = "1";
        log.info(lockerId);
        if (Arrays.asList(rightLockerIDList).contains(lockerId)) {
            log.info("This locker is Right!" + lockerId);
            rightOrLeftLocker = "R";
            this.touchDisplayLockerRight.setFill(Color.RED);
        } else if (Arrays.asList(leftLockerIDList).contains(lockerId)) {
            log.info("This locker is Left! " + lockerId);
            rightOrLeftLocker = "L";
            this.touchDisplayLockerLeft.setFill(Color.RED);
        } else {
            log.info("This locker is not a valid locker ID");
        }
        switch (lockerId) {
            case "lockerID1":
            case "lockerID5":
                // Locker 1 and locker 5 is the number 1 locker of left/right
                openedLockerId = "1";
                this.touchDisplayLocker1.setFill(Color.RED);
                break;
            case "lockerID2":
            case "lockerID6":
                openedLockerId = "2";
                this.touchDisplayLocker2.setFill(Color.RED);
                break;
            case "lockerID9":
            case "lockerID13":
                openedLockerId = "3";
                this.touchDisplayLocker3.setFill(Color.RED);
                break;
            case "lockerID10":
            case "lockerID14":
                openedLockerId = "4";
                this.touchDisplayLocker4.setFill(Color.RED);
                break;
            case "lockerID17":
            case "lockerID21":
                openedLockerId = "5";
                this.touchDisplayLocker5.setFill(Color.RED);
                break;
            case "lockerID18":
            case "lockerID22":
                openedLockerId = "6";
                this.touchDisplayLocker6.setFill(Color.RED);
                break;
            case "lockerID19":
            case "lockerID23":
                openedLockerId = "7";
                this.touchDisplayLocker7.setFill(Color.RED);
                break;
            case "lockerID20":
            case "lockerID24":
                openedLockerId = "8";
                this.touchDisplayLocker8.setFill(Color.RED);
                break;
            case "lockerID11":
            case "lockerID15":
                openedLockerId = "9";
                this.touchDisplayLocker9.setFill(Color.RED);
                break;
            case "lockerID12":
            case "lockerID16":
                openedLockerId = "10";
                this.touchDisplayLocker10.setFill(Color.RED);
                break;
            case "lockerID3":
            case "lockerID7":
                openedLockerId = "11";
                this.touchDisplayLocker11.setFill(Color.RED);
                break;
            case "lockerID4":
            case "lockerID8":
                openedLockerId = "12";
                this.touchDisplayLocker12.setFill(Color.RED);
                break;
            default:
                break;
        }
        String detailLockerPosition = "Locker "+openedLockerId+" at ";
        if(rightOrLeftLocker.compareTo("R")==0){
            this.touchDisplayLockerRightText.setText("R"+openedLockerId);
            this.touchDisplayLockerLeftText.setText("");
            detailLockerPosition+="Right hand side";
        }else if (rightOrLeftLocker.compareTo("L")==0){
            this.touchDisplayLockerLeftText.setText("L"+openedLockerId);
            this.touchDisplayLockerRightText.setText("");
            detailLockerPosition+="Left hand side";
        }

        this.touchDisplayLockerDetailText.setText(detailLockerPosition);


    }

    public void td_wrongPasscode(String wrongPasscodeMsg){
        this.wrongPasscodeResponse.setText(wrongPasscodeMsg);
    }

    public void td_updateOpenLockerDoorTitleDetail(String storeOrPick){
        if(storeOrPick.compareTo("Store")==0){
        this.touchDisplayLockerContent.setText("Please store your delivery at");
        this.touchDisplayLockerTitle.setText("Store delivery");
        }else if (storeOrPick.compareTo("Pick")==0){
            this.touchDisplayLockerContent.setText("Please pick up your delivery at");
            this.touchDisplayLockerTitle.setText("Pick up delivery");
        }
    }

    public void td_showPaymentError(String errorType){
        if(errorType.equals("AmountNotEnough")) {
            this.touchDisplayPaymentError.setOpacity(100);
            this.touchDisplayPaymentError2.setOpacity(100);
        }
    }

    public void td_updateAdminPageAllLockerStatus(){

    }

} // TouchDisplayEmulatorController
