package SLC.OctopusReaderDriver;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import SLC.HWHandler.HWHandler;


//======================================================================
// OctopusReaderDriver
public class OctopusReaderDriver extends HWHandler {
    //------------------------------------------------------------
    // OctopusReaderDriver
    public OctopusReaderDriver (String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    }


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case OR_OctopusCardRead:
                slc.send(new Msg(id, mbox, Msg.Type.OR_OctopusCardRead, msg.getDetails()));
                break;

            case OR_PaymentFailed:
                slc.send((new Msg(id, mbox, Msg.Type.OR_PaymentFailed, msg.getDetails())));
                break;

            case OR_GoActive:
                handleGoActive();
                break;

            case OR_GoStandby:
                handleGoStandby();
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        log.info(id + ": Go Active");
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        log.info(id + ": Go Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll
} // OctopusReaderDriver

