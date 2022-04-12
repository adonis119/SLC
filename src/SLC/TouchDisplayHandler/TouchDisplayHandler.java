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
            case SLS_ReplyDeliveryOrder:{
                log.info("Handler receive replay from SLC " + msg.getDetails());
                handleUpdateServerReply(msg);
                break;
            }
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
} // TouchDisplayHandler
