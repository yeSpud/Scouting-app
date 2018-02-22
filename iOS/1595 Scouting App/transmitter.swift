//
//  transmitter.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/26/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit
import CoreBluetooth

class transmitter: UIViewController {
	
	
	var peripheral:CBPeripheral!
	// let SCRATCH_UUID = UUID.init(uuidString: "00001101-0000-1000-8000-00805F9B34FB")
	let SERVICE_UUID = CBUUID(string: "0x1800")
	
	override func viewDidLoad() {
		super.viewDidLoad()
		// Define manager
		globals.manager = CBCentralManager(delegate: self, queue: nil)
		print(globals.data)
		// Do any additional setup after loading the view.
	}
	
	@IBOutlet weak var console: UILabel!
	
	
	override func didReceiveMemoryWarning() {
		super.didReceiveMemoryWarning()
		// Dispose of any resources that can be recreated.
	}
	
	override var preferredStatusBarStyle: UIStatusBarStyle {
		return .lightContent
	}
	
	/*
	// MARK: - Navigation
	
	// In a storyboard-based application, you will often want to do a little preparation before navigation
	override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
	// Get the new view controller using segue.destinationViewController.
	// Pass the selected object to the new view controller.
	}
	*/
	
}
extension transmitter: CBCentralManagerDelegate {
	func centralManagerDidUpdateState(_ central: CBCentralManager) {
		switch central.state {
		case .unknown:
			print("central.state is .unknown")
		case .resetting:
			print("central.state is .resetting")
		case .unsupported:
			let alert = UIAlertController(title: "Bluetooth unavalible", message: "Bluetooth is unavalibe for this device, as it is unsupported", preferredStyle: UIAlertControllerStyle.alert)
			alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
			self.present(alert, animated: true, completion: nil)
			print("central.state is .unsupported")
		case .unauthorized:
			print("central.state is .unauthorized")
		case .poweredOff: do {
			let alert = UIAlertController(title: "Bluetooth unavalible", message: "Bluetooth is unavalibe for this device, as it is not turned on", preferredStyle: UIAlertControllerStyle.alert)
			alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
			self.present(alert, animated: true, completion: nil)
			print("central.state is .poweredOff")
			}
		case .poweredOn:
			print("central.state is .poweredOn")
			globals.manager.scanForPeripherals(withServices: nil)
		}
	}
	func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
		print(peripheral)
		if peripheral.name != nil {
			console.text = peripheral.name
		} else {
			console.text = peripheral.identifier.uuidString
		}
	}
}
