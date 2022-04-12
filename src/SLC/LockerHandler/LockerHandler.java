package SLC.LockerHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import SLC.HWHandler.HWHandler;


//======================================================================
// OctopusReaderDriver
public class LockerHandler extends HWHandler {
    //------------------------------------------------------------
    // OctopusReaderDriver
    public LockerHandler (String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
    } // BarcodeReaderDriver


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case OpenLocker:
                openLockerDoor(msg);
                break;
            case Locker_op:
                slc.send(new Msg(id, mbox, Msg.Type.Locker_op, msg.getDetails()));
                break;
            case Locker_cl:
                slc.send(new Msg(id, mbox, Msg.Type.Locker_cl, msg.getDetails()));
                break;
            case Locker_st:
                //slc.send(new Msg(id, mbox, Msg.Type.Locker_st, msg.getDetails()));
                checkLockerStatus(msg);
                //checkLockerStatus(msg);
                break;
            case Locker_st_c:
                slc.send(new Msg(id, mbox, Msg.Type.Locker_st_c, msg.getDetails()));
                //checkLockerStatus(msg);
                break;
            case Locker_st_o:
                slc.send(new Msg(id, mbox, Msg.Type.Locker_st_o, msg.getDetails()));
                //checkLockerStatus(msg);
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

    protected void checkLockerStatus(Msg msg){
        log.info(id + ": check Locker Status -- " + msg.getDetails());
    }

    protected void openLockerDoor(Msg msg){
        log.info(id + ": open a locker, size : " + msg.getDetails());
    }
} // OctopusReaderDriver