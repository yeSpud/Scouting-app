//
//  BluetoothList.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 3/28/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit
import CoreBluetooth

class BluetoothList: UIViewController {
	
	var peripheral:CBPeripheral!
	// let SCRATCH_UUID = UUID.init(uuidString: "00001101-0000-1000-8000-00805F9B34FB")
	let SERVICE_UUID = CBUUID(string: "0x1800")
	
	@IBOutlet weak var text: UILabel!
	
	override func viewDidLoad() {
		super.viewDidLoad()
		// Define manager
		globals.manager = CBCentralManager(delegate: self as CBCentralManagerDelegate, queue: nil)
		// Do any additional setup after loading the view.
		
		
	}
	
	override var preferredStatusBarStyle: UIStatusBarStyle {
		return .lightContent
	}
}

extension BluetoothList: CBCentralManagerDelegate {
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
			// This is where we define the GATT service to san for!
			globals.manager.scanForPeripherals(withServices: [SERVICE_UUID])
		}
	}
	func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
		// print(peripheral)
		print(peripheral.name ?? "Name not avalible")
		if peripheral.name != nil {
			if (text.text?.contains(peripheral.name!))! {
				// Dont write it
			} else {
				text.text?.append("\n")
				text.text?.append(peripheral.name!)
			}
		}
	}
	
}
