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
				break;
			default:
				break;
		}




    } // processMouseClicked
} // SLC
