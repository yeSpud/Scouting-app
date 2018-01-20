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
	func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
		if (segue.identifier == "toSettings") {
			let abcViewController = segue.destination
			abcViewController.title = "Main"
		} else if (segue.identifier == "toMainPage") {
			let abcViewController = segue.destination
			abcViewController.title = "Settings"
		}
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
				textField.keyboardType = .numberPad
			})
			self.teamNumber =  Int((alertController.textFields?[0].text)!)!

		}
		//the cancel action doing nothing
		let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
		//adding textfields to our dialog box
		alertController.addTextField { (textField) in
			textField.placeholder = "Enter team number"
		}
		//adding the action to dialogbox
		alertController.addAction(confirmAction)
		alertController.addAction(cancelAction)
		//finally presenting the dialog box
		self.present(alertController, animated: true, completion: nil)
	}

	
	func showMACDialog() {
		//Creating UIAlertController and
		//Setting title and message for the alert dialog
		let alertController = UIAlertController(title: "Enter server MAC address", message: "Enter the team number to start scouting", preferredStyle: .alert)
		//the confirm action taking the inputs
		let confirmAction = UIAlertAction(title: "Enter", style: .default) { (_) in
			//getting the input values from user
			self.serverMAC = (alertController.textFields?[0].text)!
			
		}
		//the cancel action doing nothing
		let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
		
		//adding textfields to our dialog box
		alertController.addTextField { (textField) in
			textField.placeholder = "Enter server MAC address"
		}
		
		// Debug option
		let autoOption = UIAlertAction(title: "Lol :P", style: .destructive) { (_) in
			self.serverMAC = "AC:BC:32:8E:CC:1A"
		}
		
		//adding the action to dialogbox
		alertController.addAction(confirmAction)
		alertController.addAction(cancelAction)
		alertController.addAction(autoOption)
		//finally presenting the dialog box
		self.present(alertController, animated: true, completion: nil)
	}
    @IBAction func startButtonClicked() {
        showScoutingDialog()
    }
    @IBAction func settingsButtonClicked() {
        //performSegue(withIdentifier: "toSettings", sender: nil)
		showMACDialog()
    }

	
}

