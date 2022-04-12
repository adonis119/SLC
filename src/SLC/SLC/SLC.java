package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;

import java.awt.*;
import java.util.Random;


//======================================================================
// SLC
public class SLC extends AppThread {
	private int pollingTime;
	private MBox barcodeReaderMBox;
	private MBox touchDisplayMBox;
	private MBox octopusReaderMBox;
	private MBox sLServerMbox;
	private MBox lockerMBox;
	private String currentScene = "BlankScreen";
	private String passcodeInput = "";
	private locker[] lockers = new locker[24];
	private String barcodeHealth = "ACK";
	private String touchDisplayHealth = "ACK";
	private String octopusReaderHealth = "ACK";
	private String lockerHealth = "ACK";
	private String sLServerHealth = "ACK";

	//------------------------------------------------------------
	// SLC
	public SLC(String id, AppKickstarter appKickstarter) throws Exception {
		super(id, appKickstarter);
		pollingTime = Integer.parseInt(appKickstarter.getProperty("SLC.PollingTime"));
	} // SLC


	//------------------------------------------------------------
	// run
	public void run() {
		Timer.setTimer(id, mbox, pollingTime);
		log.info(id + ": starting...");

		barcodeReaderMBox = appKickstarter.getThread("BarcodeReaderDriver").getMBox();
		touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
		octopusReaderMBox = appKickstarter.getThread("OctopusReaderDriver").getMBox();
		sLServerMbox = appKickstarter.getThread("SLServer").getMBox();
		lockerMBox = appKickstarter.getThread("LockerHandler").getMBox();

		for (int i = 1; i <= 24; i++) {
			//big size locker : id(1-8)
			//medium size locker : id(9-16)
			//small size locker : id(17-22)
			String id = "lockerID" + i;
			if (i < 9) lockers[i - 1] = new locker(id, 3);
			else if (i < 17) lockers[i - 1] = new locker(id, 2);
			else lockers[i - 1] = new locker(id, 1);
		}

		for (boolean quit = false; !quit; ) {
			Msg msg = mbox.receive();

			log.fine(id + ": message received: [" + msg + "].");

			switch (msg.getType()) {
				// Touch Display
				case TD_MouseClicked:
					log.info("MouseCLicked: " + msg.getDetails() + "Barcode health " + barcodeHealth +" Touch Display health " + touchDisplayHealth);
					processMouseClicked(msg);
					break;
				case TD_UpdateDisplay:
					// Update currentScene first (For the right bottom selection hacking scene or normal update display)
					log.info("DisplayUpdated: " + msg.getDetails());
					this.currentScene = msg.getDetails();
					break;
				case TimesUp:
					Timer.setTimer(id, mbox, pollingTime);
					log.info("Poll: " + msg.getDetails());
					barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
					touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
					octopusReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
					sLServerMbox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
					lockerMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
					break;
				// Barcode
				case BR_BarcodeRead:
					log.info("Received Barcode " + msg.getDetails());
					touchDisplayMBox.send(msg);
					break;
				// SL Server
				case SLS_GetDeliveryOrder:
					log.info("SLC receive the barcode from touch display " + msg.getDetails());
					sLServerMbox.send(msg);
					break;
				case SLS_ReplyDeliveryOrder:
					log.info("SLC receive the reply from server " + msg.getDetails());
					touchDisplayMBox.send(msg);
					break;
				case SLS_ReplyOpenLocker:
					log.info("SLC call locker open a empty locker with size: " + msg.getDetails());
					String lockerId = "";
					int index = 0;
					switch (msg.getDetails()) {
						case "1":
							for (int i = 17; i <= 24; i++) {
								if (lockers[i - 1].emptyStatus == 0 && lockers[i - 1].doorStatus == 0 && lockers[i - 1].bookingStatus == 0) {
									lockerId = lockers[i - 1].lockerID;
									index = i - 1;
									break;
								}
							}
							break;
						case "2":
							for (int i = 9; i <= 16; i++) {
								if (lockers[i - 1].emptyStatus == 0 && lockers[i - 1].doorStatus == 0 && lockers[i - 1].bookingStatus == 0) {
									lockerId = lockers[i - 1].lockerID;
									index = i - 1;
									break;
								}
							}
							break;
						case "3":
							for (int i = 1; i <= 8; i++) {
								if (lockers[i - 1].emptyStatus == 0 && lockers[i - 1].doorStatus == 0 && lockers[i - 1].bookingStatus == 0) {
									lockerId = lockers[i - 1].lockerID;
									index = i - 1;
									break;
								}
							}
							break;
					}
					if (msg.getDetails().equals("3") && lockerId.isEmpty()) {
					} else if (lockerId.isEmpty()) {
						log.info("Locker size " + msg.getDetails() + " is full. Try next size.");
						mbox.send(new Msg(id, mbox, Msg.Type.SLS_ReplyOpenLocker, String.valueOf(Integer.parseInt(msg.getDetails()) + 1)));
					} else {
						lockerMBox.send(new Msg(id, mbox, Msg.Type.OpenLocker, lockerId));
						lockers[index].doorStatus = 1;
					}
					break;
				// Octopus
				case OR_OctopusCardRead:
					log.info("Payment success! The octopus card number is " + msg.getDetails());
					// move below line to request octopus payment
					octopusReaderMBox.send((new Msg(id, mbox, Msg.Type.OR_PaymentAmount, "$ 500.00")));
					break;
				case OR_PaymentFailed:
					log.info("Payment Failed! Please make sure to have at least " + msg.getDetails());
					break;
				case OR_PaymentAmount:
					log.info("Payment sent to Octopus Reader:" + msg.getDetails());
					break;
				// Locker
				case Locker_op:
					log.info("The Locker is opened. " + msg.getDetails());
					//lockerMBox.send(msg);
					break;
				case Locker_cl:
					log.info("The Locker is closed.  " + msg.getDetails());
					int lockerIndex = 0;
					for (int i = 0; i < 24; i++) {
						if (lockers[i].lockerID.equals(msg.getDetails())) {
							lockerIndex = i;
							break;
						}
					}
					if (lockers[lockerIndex].emptyStatus == 0) {
						lockers[lockerIndex].emptyStatus = 1;
						Random rnd = new Random();
						lockers[lockerIndex].passCode = String.valueOf(10000000 + rnd.nextInt(90000000));
						log.info("The passCode of " + msg.getDetails() + " is " + lockers[lockerIndex].passCode);
					} else {
						lockers[lockerIndex].emptyStatus = 0;
					}
					//lockerMBox.send(msg);
					break;
				case Locker_st:
					log.info("Check Locker status.  " + msg.getDetails());
					//lockerMBox.send(msg);
					break;
				case Locker_st_c:
					log.info(msg.getDetails());
					break;
				case Locker_st_o:
					log.info(msg.getDetails());
					//lockerMBox.send(msg);
					break;
				case PollAck:
					log.info("PollAck: " + msg.getDetails());
					switch (msg.getSender()){
						case "BarcodeReaderDriver":
							barcodeHealth = "ACK";
							break;
						case "TouchDisplayHandler":
							touchDisplayHealth = "ACK";
							break;
						case "OctopusReaderDriver":
							octopusReaderHealth = "ACK";
							break;
						case "LockerHandler":
							lockerHealth = "ACK";
							break;
						case "SLServer":
							sLServerHealth = "ACK";
							break;
					}
					break;
				case PollNak:
					log.info("PollNak: " + msg.getDetails());
					switch (msg.getSender()){
						case "BarcodeReaderDriver":
							barcodeHealth = "NAK";
							break;
						case "TouchDisplayHandler":
							touchDisplayHealth = "NAK";
							break;
						case "OctopusReaderDriver":
							octopusReaderHealth = "NAK";
							break;
						case "LockerHandler":
							lockerHealth = "NAK";
							//terminate
							break;
						case "SLServer":
							sLServerHealth = "NAK";
							break;
					}
					break;
				case Terminate:
					quit = true;
					break;
				default:
					log.warning(id + ": unknown message type: [" + msg + "]");
			}
		}

	// declaring our departure
	appKickstarter.unregThread(this);
	log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // processMouseClicked
    private void processMouseClicked(Msg msg) {
	// *** process mouse click here!!! ***
		String splitClickedPointed[] = msg.getDetails().split(" ");
		int clickedPositionX = Integer.parseInt(splitClickedPointed[0]);
		int clickedPositionY = Integer.parseInt(splitClickedPointed[1]);
		log.info("MouseX: " + splitClickedPointed[0]);
		log.info("MouseY: " + splitClickedPointed[1]);

		switch(currentScene){
			case "BlankScreen":
				// If user click the blank scene, direct to MainMenu, and set currentScene to "MainMenu"
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                // currentScene = "MainMenu";
				break;
			case "MainMenu":
				log.info("Clicked on MainMenu");
				// Click on Pick up x= 0~300(left=0,width=300), y= 270~340(bottom=140, height=70)
				if(clickedPositionX>=0&&clickedPositionX<=300&&clickedPositionY >=270 && clickedPositionY<=340){
					if(octopusReaderHealth.equals("ACK")) {
						log.info("Clicked Pick up delivery");
						touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
					}
					else  if (octopusReaderHealth.equals("NAK")){
						//octopusReaderMBox.send(new Msg(id, mbox, Msg.Type., ""));
						touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Maintenance"));
					}
				} else if(clickedPositionX>=340&&clickedPositionX<=640&&clickedPositionY >=270 && clickedPositionY<=340) {
					// Click on Pick up x= 340~640(right=0,width=300), y= 270~340(bottom=140, height=70)
					log.info("Clicked Store delivery");
					// Activate Barcode reader for scan barcode
					if(barcodeHealth.equals("ACK")) {
						barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.BR_GoActive, ""));
						touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "StoreDelivery"));
					}
					else if(barcodeHealth.equals("NAK")){
						touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Maintenance"));
					}
				}else if(clickedPositionX>=0&&clickedPositionX<=300&&clickedPositionY >=340 && clickedPositionY<=410) {
					// Click on Admin login x= 0~300(left=0,width=300), y=340~410(bottom=70, height=70)
					log.info("Clicked Admin login");
				}else if(clickedPositionX>=340&&clickedPositionX<=640&&clickedPositionY >=340 && clickedPositionY<=410) {
					// Click on Pick up x= 340~640(right=0,width=300), y= 270~340(bottom=70, height=70)
					log.info("Clicked Refund");
				}
				break;
			case "Confirmation":
				log.info("Clicked on Confirmation");
				// Check footer button
				if(clickedPositionY>=390&&clickedPositionY<=430){
					// Clicked footer
					if (clickedPositionX >= 105 && clickedPositionX <= 265) {
						//Clicked Enter
						//TO-DO check passcode from locker
						log.info("Clicked on Enter");
						verifyPassCode(passcodeInput);
						passcodeInput = "";
					} else if (clickedPositionX >= 380 && clickedPositionX <= 540) {
						//Clicked Back to Menu
						log.info("Clicked on Back");
						passcodeInput = "";
						touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
						currentScene = "MainMenu";
					}
				}
				// Check clicked number input
				// Check the clicked of del first, since only del can click when passcode already have 8 digits
				if(clickedPositionY>=315&&clickedPositionY<=355&&clickedPositionX>=382&&clickedPositionX<=442)
				{
						log.info("Clicked Del");
						if(passcodeInput!=""&&passcodeInput.length()>0) {
							passcodeInput = passcodeInput.substring(0, passcodeInput.length() - 1);
						}
				}
				if(passcodeInput.length()>=8){
					// if already have 8 digits, not check the number click
					return;
				}
				if(clickedPositionY>=150&&clickedPositionY<=190)
				{
					//clicked first row, check x
					if(clickedPositionX>=202&&clickedPositionX<=262){
						log.info("Clicked 1");
						passcodeInput +="1";
					} else if(clickedPositionX>=292&&clickedPositionX<=352){
						log.info("Clicked 2");
						passcodeInput +="2";
					} else if(clickedPositionX>=382&&clickedPositionX<=442){
					log.info("Clicked 3");
						passcodeInput +="3";
					}
				}
				if(clickedPositionY>=205&&clickedPositionY<=245)
				{
					//clicked second row, check x
					if(clickedPositionX>=202&&clickedPositionX<=262){
						log.info("Clicked 4");
						passcodeInput +="4";
					} else if(clickedPositionX>=292&&clickedPositionX<=352){
						log.info("Clicked 5");
						passcodeInput +="5";
					} else if(clickedPositionX>=382&&clickedPositionX<=442){
						log.info("Clicked 6");
						passcodeInput +="6";
					}
				}
				if(clickedPositionY>=260&&clickedPositionY<=300)
				{
					//clicked 3rd row, check x
					if(clickedPositionX>=202&&clickedPositionX<=262){
						log.info("Clicked 7");
						passcodeInput +="7";
					} else if(clickedPositionX>=292&&clickedPositionX<=352){
						log.info("Clicked 8");
						passcodeInput +="8";
					} else if(clickedPositionX>=382&&clickedPositionX<=442){
						log.info("Clicked 9");
						passcodeInput +="9";
					}
				}
				if(clickedPositionY>=315&&clickedPositionY<=355&&clickedPositionX>=292&&clickedPositionX<=352)
				{
						log.info("Clicked 0");
						passcodeInput +="0";
				}
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdatePasscodeInput,passcodeInput));
				log.info(passcodeInput);
				break;
			case "StoreDelivery":{
				//X: 415.0 Y:380.0
				log.info("Store Delivery clicked");
				if(clickedPositionX>=340&&clickedPositionX<=640&&clickedPositionY >=340 && clickedPositionY<=410)
				{
					log.info("Back button clicked!!");
					barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.BR_GoStandby,""));
					touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
					break;
				}

			}
			case "Maintenance":
				// If user click the blank scene, direct to MainMenu, and set currentScene to "MainMenu"
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
				// currentScene = "MainMenu";
				break;
			default:
				break;
		}
	} // processMouseClicked

	private void verifyPassCode(String passcodeInput) {
		for (locker t : lockers) {
			if (t.passCode.equals(passcodeInput)) {
				lockerMBox.send(new Msg(id, mbox, Msg.Type.OpenLocker, t.lockerID));
				t.doorStatus = 1;
				return;
			}
		}
		touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.passCode_wrong, ""));
		log.info("The passcode is not correct");
	}

	private class locker {
		int doorStatus = 0; //0: the door is closed. 1: the door is closed.
		int emptyStatus = 0; //0: the locker is empty. 1: the locker is not empty.
		int bookingStatus = 0; //0: the locker is not booked. 1: the locker is booked.
		String lockerID; // 3 different size (1: small, 2: medium, 3: big)
		String passCode = ""; // after put the product to locker, the locker should generate the passcode of the product.

		int size = 1;

		locker(String lockerID, int size) {
			this.lockerID = lockerID;
			this.size = size;
		}
	}
} // SLC
