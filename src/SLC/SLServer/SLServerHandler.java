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
                slc.send(new Msg(id, mbox, Msg.Type.SLS_GetDeliveryOrder, msg.getDetails()));
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

