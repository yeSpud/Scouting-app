//
//  ViewController.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/16/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit
import CoreBluetooth

class ViewController: UIViewController {
	
	var manager:CBCentralManager!
	
	override func viewDidLoad() {
		super.viewDidLoad()
		manager = CBCentralManager()
		manager.delegate = self as? CBCentralManagerDelegate
		if (globals.manager != nil) {
			if (globals.manager.isScanning) {
				globals.manager.stopScan()
			}
		}
		// Do any additional setup after loading the view, typically from a nib.
	}
	
	override func didReceiveMemoryWarning() {
		super.didReceiveMemoryWarning()
		// Dispose of any resources that can be recreated.
	}
	
	@IBAction func startButtonClicked() {
		if (globals.macAddress != "") {
			let alertController = UIAlertController(title: "Enter team number", message: "Enter the team number to start scouting", preferredStyle: .alert)
			let confirmAction = UIAlertAction(title: "Enter", style: .default) { (_) in
				globals.teamNumber =  Int((alertController.textFields?[0].text)!)!
				self.performSegue(withIdentifier: "toScout", sender: self)
				
			}
			confirmAction.isEnabled = false
			let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
			alertController.addAction(confirmAction)
			alertController.addAction(cancelAction)
			alertController.addTextField { (textField) in
				textField.placeholder = "Enter team number"
				textField.keyboardType = UIKeyboardType.decimalPad
				
				NotificationCenter.default.addObserver(forName: .UITextFieldTextDidChange, object: textField, queue: OperationQueue.main, using:
					{_ in
						let textIsNotEmpty = textField.text?.isEmpty
						confirmAction.isEnabled = !textIsNotEmpty!
				})
			}
			self.present(alertController, animated: true, completion: nil)
		} else {
			let alert = UIAlertController(title: "No MAC Address", message: "No MAC Address has been entered, and the app will not be able to send data to the server. Please enter a MAC Address", preferredStyle: UIAlertControllerStyle.alert)
			alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
			self.present(alert, animated: true, completion: nil)
		}
	}
	
	@IBAction func settingsButtonClicked() {
		let alertController = UIAlertController(title: "Enter MAC Address", message: "Enter the MAC address of the server", preferredStyle: .alert)
		let confirmAction = UIAlertAction(title: "Enter", style: .default) { (_) in
			globals.macAddress = (alertController.textFields?[0].text)!
		}
		confirmAction.isEnabled = false
		let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { (_) in }
		alertController.addTextField { (textField) in
			textField.placeholder = "Enter MAC Address"
			alertController.addAction(confirmAction)
			alertController.addAction(cancelAction)
			NotificationCenter.default.addObserver(forName: .UITextFieldTextDidChange, object: textField, queue: OperationQueue.main, using:
				{_ in
					let textIsNotEmpty = textField.text?.isEmpty
					confirmAction.isEnabled = !textIsNotEmpty!
			})
		}
		self.present(alertController, animated: true, completion: nil)
	}
	override var preferredStatusBarStyle: UIStatusBarStyle {
		return .lightContent
	}
}
