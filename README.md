# SLC
COMP4107\
Smart Locker Controlling System

## Group Members:

    CHOW Kwok Lung(20208014)

    TSANG Tsun Sing(20236441)
    
    YIP Cham Sum(20234198)
    
    YIP Chung Kong(20220480)

## Project structure
    -etc
        - SLC.cfg
        - SLC.SLCEmulatorStarter.log
    - src
      - AppKickstarter
        - misc
          - AppThread
          - Lib
          - LogFormatter
          - Mbox
          - Msg
        - timer
          - Appkickstarter
      - SLC
        - BarcodeReaderDriver
          - Emulator
            - BarcodeReaderEmulator
            - BarcodeReaderEmulator.fxml
            - BarcodeReaderEmulatorController
          - BarcodeReaderDriver
          
        - HWHandler
          - HWHandler
          
        - LockerHandler
          - Emulator
            - LockerEmulator
            - LockerEmulator.fxml
            - LockerEmulatorController
          - LockerHandler
          
        - OctopusReaderDriver
          - Emulator
            - OctopusReaderEmulator
            - OctopusReaderEmulator.fxml
            - OctopusReaderEmulatorController
          - OctopusReaderDriver
        - SLC
          - SLC
        - SLServer
          - Emulator
            - SLServerEmulator
            - SLServerEmulator.fxml
            - SLServerEmulatorController
          - SLServerHandler
        - TouchDisplayHandler
          - Emulator
            - TouchDisplayConfirmation.fxml
            - TouchDisplayEmulator
            - TouchDisplayEmulator.fxml
            - TouchDisplayEmulatorController
            - TouchDisplayMainMenu.fxml
            - TouchDisplayMaintenance.fxml
            - TouchDisplayOpenLockerDoor.fxml
            - TouchDisplayStoreDelivery.fxml
          - TouchDisplayHandler
        - SLCEmulatorStarter
        - SLCStarter    
## How to open the SLC?
    To start the system, right click SLCEmulatorStarter then select 
    "Run SLCEmulatorStarter main()". To terminate the system, just close the GUI.

## Which GUIs will display?
    Touch Display
    Barcode Reader
    Octopus Reader
    Locker
    SlServer

## GUI buttons
### Touch Display (No buttons - Use text instead of buttons)
    The landing page, click anywhere and jump to main menu
#### Touch Display (Main Menu)
    Pick up delivery: Go to Pick up delivery Scene
    Store delivery: Go to Store delivery Scene
    Function of this page: Menu for user go to different flow
#### Touch Display (Pick up delivery)
    Numpad(0-9): 9 buttons for user to input number 0-9
    DEL: Delete the previous number input
    Enter: Send input number to SLC check the passcode with all locker
           and open a locker door/ show passcode not correct
    Back to Menu: Go back to Main Menu
    Function of this page: Let user inpput the passcode and pick up delivery
#### Touch Display (Store Delivery)
    Back: Go back to Main Menu
    Function of this page: Activate barcode reader, staff can scan the 
                           barcode to store the delivery to a locker
    

### Barcode Reader
    Barcode 1: Input sample barcode data 1 to Barcode input field
    Barcode 2: Input sample barcode data 2 to Barcode input field
    Barcode 3: Input sample barcode data 3 to Barcode input field
    Reset: Reset the value of input field
    Send Barcode: Send the barcode value to SLServer
    Function of this device: Only function when status is activate, scan
                             barcode to SLC for store delivery
### Octopus Reader 
    Octopus card 1: Input sample Octopus card 1 to Octopus card input field
    Octopus card 2: Input sample Octopus card 2 to Octopus card field
    Octopus card 3: Input sample Octopus card 3 to Octopus card field
    Send Octopus card: Send the Opcopus card with Card No, amount and requested amount to SLServer
    Function of this device: Only function when status is activate, make
                             payment with user if that delivery have to charge
### Locker
    Open/Close: Open/Close the locker (Change the locker status)
    Function of this device: When SLC verify that you are success to pick up/
                             store on a locker. Close the locker if it opened
                             (That means user/staff have store/pick up delivery)

### SLServer
    Delivery Order 1: Input sample Delivery Order 1 data to Delivery Order ID, Size and Amount field.
    Delivery Order 2: Input sample Delivery Order 2 data to Delivery Order ID, Size and Amount field.
    Delivery Order 3: Input sample Delivery Order 3 data to Delivery Order ID, Size and Amount field.
    Reset: Clear the Delivery Order ID, Size and Amount field.
    Create Delivery Order to SL Server: Create a new Delivery Order with Delivery Order ID, Size and Amount and send it to SL Server.
    Function of this device: SLServer is use to add a delivery order on the 
                             Server, SLC will verify the order ID when barcode
                             received
## Common Usage
### How to Pick up delivery?
    Step 1: Click Touch Display GUI to start the system.
    Step 2: Select "Pick up delivery" text on the Main Screen.
    Step 3: Enter 8 digits passcode to pick up delivery
    Step 4: If the code is correct, 
    the system will display the location of the corresponding smart cabinet door.
    Otherwise, Touch Display will display "The passcode is not correct" message.
    Step 5: If the code is correct, SLC will get the amount from SLServer
    Step 6: Tocuh Display show the payment page
    Step 7: Use Octopus readter to pay
    Step 8: Have payment success Touch Display will show which locker have opened
    Step 9: Go to locker GUI close that door(That means you have pick up the delivery)
    Passcode can checked on SLServer GUI, it will send to the SLServer if a delivery has storged to a locker
### How to Store delivery?
    Step 1: Click Touch Display GUI to start the system.
    Step 2: Select "Store delivery" text on the Main Screen.
    Step 3: Go to barcode GUI, input barcode number to the barcode textfield.
    Step 4: Or you can use demo data (3buttons)
    Step 5: Click Send barcode
    Step 6: SLC will pass it to SL Server to verify with orderID
    Step 7: If order not found, Touch display will show the barcode is unavailable
    Step 8: If order verify success, Touch Display will show you which locker have opened
    Step 9: Go to locker GUI close that door(That means you have store the delivery)
### Octopus default setting
    1. Can pay when it has more than $0
    2. Can be negative up to -$50
### How to create a delivery order on server?
    Step 1: Click on SLServer GUI
    Step 2: You can use demo data(3 buttons)/input by yourself
    Step 3: Click create..... button
    After it, if the barcode(without-) is equal order ID, SLC will let you store on a locker
    
## Health Poll
    If some device respon NAK to the SLC of the Poll respon
    SLC will trunoff some related function of the smalllocker
    If Barcode reader NAK
    SLC- store delivery will be disable, the touch screen will show maintance when user direct to store delivery on menu
    If Octopus reader NAK
    SLC- pick up delivery will be disalbe the touch screen will show maintance when user direct to pick up delivery on menu
    If Locker/TouchDisplay NAK
    SLC- all function on touch display will become maintance since locker and display related to all functions.

