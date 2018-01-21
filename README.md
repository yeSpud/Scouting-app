# 1595-Scouting

## What is this?
This is the 1595 Scouting app for Android and (comming soon, maybe) iOS.

## Installation and prerequisites

### Android
For android, the prerequisites are that you have a device running on Andorid KitKat or newer (Thats api 19 and up), and that device must have bluetooth. 

To install, simply [download][1] the apk from here, and when finished, run the apk to install it. 

If you have issues with your device not installing becasue its from an unknown source, simply go into Settings>Security, then enable the option titled `Unknown sources`.

### iOS

For iOS... well... the app is still being developed, so please just be patient in the meantime.

## Using the app
When first launching the app, and attempting to scout a team, you may get a message saying to enter a MAC address first, and if youve gotten this message, then chances are you forgot to pair your device to the laptop/server that will be recieving the data.

To pair your device with the laptop/server, go to Settings>Bluetooth (Make sure its enabled aswell), and then select the laptop/server from the menu once it shows up.

On the laptop/servers end, make sure that device is set to discoverable, and then be sure to accept when the device requests to pair with it.

For setting the laptops/servers MAC address in the app, first go back to the app, and then click on the button that says `ENTER MAC ADDRESS`. Then enter the laptops/servers MAC address into the text field. The MAC address can be found by running the [Scouting-app-server][2] program on the laptop/server, and it will print out the MAC address once finishing setup. The other way to find it is by right-clicking on the bluetooth icon in the taskbar of the laptop/server, selecting `Open settings`, going to the `Hardware` tab, selecting `Bluetooth radio` or `Generic bluetooth radio`, clicking properties, and then sekecting advanced. The MAC address should be listed in the section marked `Address`. It usually looks like a few letters and numbers seperated by colons every two characters, such as `AC:BC:32:8E:CC:1A`.

Note: When entering the MAC address, be sure to make all the letters capitalized, and seperate every two characters with a colon.

After entering the MAC address, just press the back button to return, and then select `START SCOUTING` to start. It will then prompt you for a team number to scout. Once you enter the number and press Start Scouting, you will recieve several other options to enter for Autonomous and TeleOp. Once done, just press submit, and it will then send the data to the laptop/server, and return to the main page, where you can start scouing once more. 

Note: After you ever the MAC address, you will not need to enter it again unless the app crashes, you restart the device, or you quit the app.

## Vidoes
\[I will hopefully have some videos up soon-ish to help explain setup better, and how to use it\]

[1]: https://github.com/1595Dragons/Scouting-app/raw/master/app-release.apk
[2]: https://github.com/1595Dragons/Scouting-app-server
