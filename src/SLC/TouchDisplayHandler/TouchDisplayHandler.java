package SLC.TouchDisplayHandler;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// TouchDisplayHandler
public class TouchDisplayHandler extends HWHandler {
    //------------------------------------------------------------
    // TouchDisplayHandler
    public TouchDisplayHandler(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
    } // TouchDisplayHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case TD_MouseClicked:
                slc.send(new Msg(id, mbox, Msg.Type.TD_MouseClicked, msg.getDetails()));
                break;
            case TD_UpdateDisplay:
                handleUpdateDisplay(msg);
                slc.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, msg.getDetails()));
                break;
            case TD_UpdatePasscodeInput:
                handleUpdatePasscodeInput(msg);
                break;
            case BR_BarcodeRead:
                handleUpdateBarcodeInput(msg);
                break;
            case SLS_GetDeliveryOrder:
                //Send barcode data from handler to SLC
                slc.send(new Msg(id, mbox, Msg.Type.SLS_GetDeliveryOrder, msg.getDetails()));
                break;
            case SLS_ReplyDeliveryOrderForGui:
                log.info("Handler receive replay from SLC " + msg.getDetails());
                handleUpdateServerReply(msg);
                break;

            case passCode_wrong:
                log.info(msg.getDetails());
                handleWrongPasscode(msg);
                break;
            case SLS_ReplyOpenLocker:
                log.info("Handler show which locker has opened :"+msg.getDetails());
                handleUpdateOpenLockerDoorDisplay(msg);
                handleUpdateOpenLockerDoorTitleDetail("Store");
                break;
            case TD_PassCodeOpenLocker:
                log.info("Handler show which locker has opened :"+msg.getDetails());
                handleUpdateOpenLockerDoorDisplay(msg);
                handleUpdateOpenLockerDoorTitleDetail("Pick");
                break;
            case TD_UpdateAdminPage:
                log.info("Handler update admin status");
                handleAdminPageGetLockerStatus();
                break;
            case Locker_st:
                // Check locker status 1by1 request by admin page
                slc.send(new Msg(id, mbox, Msg.Type.Locker_st, msg.getDetails()));
                break;
            case Locker_st_c:
                handleUpdateAdminLockerStatus(msg);
                break;
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
	log.info(id + ": update display -- " + msg.getDetails());
    } // handleUpdateDisplay


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll

    protected void handleUpdatePasscodeInput(Msg msg){
        log.info(id + ": update passcode input -- " + msg.getDetails());
    }// Handle update passcode input box value when user click the number on the display screen
    protected void handleUpdateBarcodeInput(Msg msg){
        log.info(id + ": update barcode input -- " + msg.getDetails());
    }// Handle update barcode input when SLC send Barcode to TD
    protected void handleUpdateServerReply(Msg msg){
        log.info(id + ": Server reply:  " + msg.getDetails());
    }

    protected void handleWrongPasscode(Msg msg) {
        log.info(id + ": passcode is wrong and show on the display. Display: " + msg.getDetails());
    }
    protected void handleUpdateOpenLockerDoorDisplay(Msg msg){
        log.info(id + ": update opened locker door:  " + msg.getDetails());
    }
    protected void handleUpdateOpenLockerDoorTitleDetail(String storeOrPick){
        log.info(id + ": update opened locker door title type:  " + storeOrPick);
    }
    protected void handleShowPaymentError(Msg msg){
        log.info(id + ": update payment error and show  " + msg.getDetails());
    }
    protected void handleUpdateAdminLockerStatus(Msg msg){
       log.info(id+": update admin page locker state"+ msg.getDetails());
    }
    protected void handleAdminPageGetLockerStatus(){
        log.info(id+": get admin page locker status");
    }
} // TouchDisplayHandler
