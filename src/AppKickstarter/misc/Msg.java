package AppKickstarter.misc;


//======================================================================
// Msg
public class Msg {
    private String sender;
    private MBox senderMBox;
    private Type type;
    private String details;

    //------------------------------------------------------------
    // Msg
    /**
     * Constructor for a msg.
     * @param sender id of the msg sender (String)
     * @param senderMBox mbox of the msg sender
     * @param type message type
     * @param details details of the msg (free format String)
     */
    public Msg(String sender, MBox senderMBox, Type type, String details) {
	this.sender = sender;
	this.senderMBox = senderMBox;
	this.type = type;
	this.details = details;
    } // Msg


    //------------------------------------------------------------
    // getSender
    /**
     * Returns the id of the msg sender
     * @return the id of the msg sender
     */
    public String getSender()     { return sender; }


    //------------------------------------------------------------
    // getSenderMBox
    /**
     * Returns the mbox of the msg sender
     * @return the mbox of the msg sender
     */
    public MBox   getSenderMBox() { return senderMBox; }


    //------------------------------------------------------------
    // getType
    /**
     * Returns the message type
     * @return the message type
     */
    public Type   getType()       { return type; }


    //------------------------------------------------------------
    // getDetails
    /**
     * Returns the details of the msg
     * @return the details of the msg
     */
    public String getDetails()    { return details; }


    //------------------------------------------------------------
    // toString
    /**
     * Returns the msg as a formatted String
     * @return the msg as a formatted String
     */
    public String toString() {
	return sender + " (" + type + ") -- " + details;
    } // toString


    //------------------------------------------------------------
    // Msg Types
    /**
     * Message Types used in Msg.
     * @see Msg
     */
    public enum Type {
        /** Terminate the running thread */	Terminate,
	/** Generic error msg */		Error,
	/** Set a timer */			SetTimer,
	/** Set a timer */			CancelTimer,
	/** Timer clock ticks */		Tick,
	/** Time's up for the timer */		TimesUp,
	/** Health poll */			Poll,
	/** Health poll +ve acknowledgement */	PollAck,
        /** Health poll -ve acknowledgement */	PollNak,
	/** Update Display */			TD_UpdateDisplay,
	/** Mouse Clicked */			TD_MouseClicked,
        /** Barcode Reader Go Activate */	BR_GoActive,
        /** Barcode Reader Go Standby */	BR_GoStandby,
        /** Card inserted */			BR_BarcodeRead,
       /**Part of we added Msg*/
       /** Update passcode Input*/ TD_UpdatePasscodeInput,
        /** Octopus Reader Go Activate */	OR_GoActive,
        /** Octopus Reader Go Standby */	OR_GoStandby,
        /** Octopus Payment Amount */		OR_PaymentAmount,
        /** Octopus Card inserted */		OR_OctopusCardRead,
        /** Octopus Payment Failed */ OR_PaymentFailed,
        /** SLServer send delivery order*/ SLS_GetDeliveryOrder,
        /** SLServer reply delivery order*/ SLS_ReplyDeliveryOrderForGui,
        /** SLServer reply to open new locker*/ SLS_ReplyOpenLocker,
        /** SLServer request for octopus amount*/ SLS_RequestAmount,
        /** SLServer reply for octopus amount*/ SLS_ReplyAmount,
        /** call locker to open locker*/        OpenLocker,
        /** Locker opened */		Locker_op,
        /** Locker closed */		Locker_cl,
        /** Locker check status */		Locker_st,
        /** Locker status is closed */		Locker_st_c,
        /** Locker status is opened*/		Locker_st_o,
        /** PassCode is not correct, show to user */ passCode_wrong,
        /** When passcode is correct, call to open correspond door*/ TD_PassCodeOpenLocker,
        /** Update the admin lockers status*/ TD_UpdateAdminPage,
        /** When Octopus function is called, call to open correspond page*/ TD_OctopusPage,
        /** Send passcode to server when a delivery stored on locker*/SLS_SendPasscodeData,
        /** Send Delivery Order ID to SL server when it stored to our locker */ SLS_SendDeliveryData,
    } // Type
} // Msg
