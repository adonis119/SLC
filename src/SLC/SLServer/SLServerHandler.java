package SLC.SLServer;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import SLC.HWHandler.HWHandler;


//======================================================================
// SLCServerHandler
public class SLServerHandler extends HWHandler {
    //------------------------------------------------------------
    // SLCServerHandler
    public SLServerHandler(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case SLS_GetDeliveryOrder:
                //Receive Data from
                log.info("SLServer receive barcode from SLC " + msg.getDetails());
                if(msg.getDetails().equals("2026-6202")){
                    slc.send(new Msg(id, mbox, Msg.Type.SLS_ReplyDeliveryOrder, "This barcode has been used!"));
                }
                else{
                    slc.send(new Msg(id, mbox, Msg.Type.SLS_ReplyDeliveryOrder, "This barcode is available!"));
                    if(msg.getDetails().equals(appKickstarter.getProperty("BarcodeReader.Barcode1")))
                        slc.send(new Msg(id, mbox, Msg.Type.SLS_ReplyOpenLocker,appKickstarter.getProperty("SLServer.DeliveryOrder1Size")));
                    else if(msg.getDetails().equals(appKickstarter.getProperty("BarcodeReader.Barcode3")))
                        slc.send(new Msg(id, mbox, Msg.Type.SLS_ReplyOpenLocker,appKickstarter.getProperty("SLServer.DeliveryOrder3Size")));
                    else
                        slc.send(new Msg(id, mbox, Msg.Type.SLS_ReplyOpenLocker,"2"));

                }
                break;
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    // Since SL server is not a device of SLC, It should not handle standby,active...etc.. from SLC, but because we test SL server by GUI, using HWhandler to emulate
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll
}

