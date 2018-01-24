//
//  ViewController.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/16/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
	var teamNumber = 0
	var serverMAC = ""
	

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
		
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
	
	func showScoutingDialog() {
		//Creating UIAlertController and
		//Setting title and message for the alert dialog
		let alertController = UIAlertController(title: "Enter team number", message: "Enter the team number to start scouting", preferredStyle: .alert)
		//the confirm action taking the inputs
		let confirmAction = UIAlertAction(title: "Enter", style: .default) { (_) in
			//getting the input values from user
			alertController.addTextField(configurationHandler: { textField in
				textField.keyboardType = UIKeyboardType.numberPad
			})
			self.teamNumber =  Int((alertController.textFields?[0].text)!)!

		}
		confirmAction.isEnabled = false
		//the cancel action doing nothing
		let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
		//adding the action to dialogbox
		alertController.addAction(confirmAction)
		alertController.addAction(cancelAction)
		//adding textfields to our dialog box
		alertController.addTextField { (textField) in
			textField.placeholder = "Enter team number"
			
			// Observe the UITextFieldTextDidChange notification to be notified in the below block when text is changed
			NotificationCenter.default.addObserver(forName: .UITextFieldTextDidChange, object: textField, queue: OperationQueue.main, using:
				{_ in
					// Being in this block means that something fired the UITextFieldTextDidChange notification.
					
					// Access the textField object from alertController.addTextField(configurationHandler:) above and get the character count of its non whitespace characters
					let textIsNotEmpty = textField.text?.isEmpty
					
					// If the text contains non whitespace characters, enable the OK Button
					confirmAction.isEnabled = !textIsNotEmpty!
					
			})
			
		}
		
		//finally presenting the dialog box
		self.present(alertController, animated: true, completion: nil)
	}

	
	func showMACDialog() {
        //Creating UIAlertController and
        //Setting title and message for the alert dialog
        let alertController = UIAlertController(title: "Enter MAC Address", message: "Enter the MAC address of the server", preferredStyle: .alert)
        //the confirm action taking the inputs
        let confirmAction = UIAlertAction(title: "Enter", style: .default) { (_) in
            self.serverMAC = (alertController.textFields?[0].text)!
            
        }
		confirmAction.isEnabled = false
        //the cancel action doing nothing
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
        //adding textfields to our dialog box
        alertController.addTextField { (textField) in
            textField.placeholder = "Enter MAC Address"
			
			//adding the action to dialogbox
			alertController.addAction(confirmAction)
			alertController.addAction(cancelAction)
			
			NotificationCenter.default.addObserver(forName: .UITextFieldTextDidChange, object: textField, queue: OperationQueue.main, using:
				{_ in
					// Being in this block means that something fired the UITextFieldTextDidChange notification.
					
					// Access the textField object from alertController.addTextField(configurationHandler:) above and get the character count of its non whitespace characters
					let textIsNotEmpty = textField.text?.isEmpty
					
					// If the text contains non whitespace characters, enable the OK Button
					confirmAction.isEnabled = !textIsNotEmpty!
					
			})
        }
		
        //finally presenting the dialog box
        self.present(alertController, animated: true, completion: nil)
	}
    
    func noMAC() {
        // create the alert
        let alert = UIAlertController(title: "No MAC Address", message: "No MAC Address has been entered, and the app will not be able to send data to the server. Please enter a MAC Address", preferredStyle: UIAlertControllerStyle.alert)
        
        // add an action (button)
        alert.addAction(UIAlertAction(title: "k fine", style: UIAlertActionStyle.default, handler: nil))
        
        // show the alert
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func startButtonClicked() {
        if (self.serverMAC != "") {
        showScoutingDialog()
        } else {
            noMAC()
        }
    }
    @IBAction func settingsButtonClicked() {
		showMACDialog()
    }

	
}

