package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;


//======================================================================
// SLC
public class SLC extends AppThread {
    private int pollingTime;
    private MBox barcodeReaderMBox;
    private MBox touchDisplayMBox;
	private String currentScene = "Blank";
	private String passcodeInput ="";

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

	for (boolean quit = false; !quit;) {
	    Msg msg = mbox.receive();

	    log.fine(id + ": message received: [" + msg + "].");

	    switch (msg.getType()) {
		case TD_MouseClicked:
		    log.info("MouseCLicked: " + msg.getDetails());
		    processMouseClicked(msg);
		    break;

		case TimesUp:
		    Timer.setTimer(id, mbox, pollingTime);
		    log.info("Poll: " + msg.getDetails());
		    barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
		    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
		    break;

		case PollAck:
		    log.info("PollAck: " + msg.getDetails());
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
			case "Blank":
				// If user click the blank scene, direct to MainMenu, and set currentScene to "MainMenu"
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
				currentScene = "MainMenu";
				break;
			case "MainMenu":
				log.info("Clicked on MainMenu");
				// Click on Pick up x= 0~300(left=0,width=300), y= 270~340(bottom=140, height=70)
				if(clickedPositionX>=0&&clickedPositionX<=300&&clickedPositionY >=270 && clickedPositionY<=340){
					log.info("Clicked Pick up delivery");
					touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
					currentScene = "Confirmation";
				} else if(clickedPositionX>=340&&clickedPositionX<=640&&clickedPositionY >=270 && clickedPositionY<=340) {
					// Click on Pick up x= 340~640(right=0,width=300), y= 270~340(bottom=140, height=70)
					log.info("Clicked Store delivery");
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
				// Check clicked number input
				// Check the clicked of del first, since only del can click when passcode already have 8 digits
				if(clickedPositionY>=315&&clickedPositionY<=355&&clickedPositionX>=382&&clickedPositionX<=442)
				{
						log.info("Clicked Del");
						if(passcodeInput!="") {
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
			default:
				break;
		}




    } // processMouseClicked
} // SLC
