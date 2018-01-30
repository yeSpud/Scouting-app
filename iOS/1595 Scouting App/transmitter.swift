//
//  transmitter.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/26/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit
import CoreBluetooth

class transmitter: UIViewController, CBCentralManagerDelegate, CBPeripheralDelegate {

	var manager:CBCentralManager!
	var peripheral:CBPeripheral!
	let SCRATCH_UUID = UUID.init(uuidString: "00001101-0000-1000-8000-00805F9B34FB")
	let SERVICE_UUID = CBUUID(string: "00001101-0000-1000-8000-00805F9B34FB")
	
    override func viewDidLoad() {
        super.viewDidLoad()
		// Define manager
		manager = CBCentralManager(delegate: self, queue: nil)
		print(globals.data)
        // Do any additional setup after loading the view.
    }
	
    @IBOutlet weak var console: UILabel!
    
    
    
	// Check if teh bluetooth is enabled
	func centralManagerDidUpdateState(_ central: CBCentralManager) {
		if central.state == CBManagerState.poweredOn {
			central.scanForPeripherals(withServices:nil, options: nil)
			print (central.isScanning)
			console.text = String(describing: central.retrievePeripherals(withIdentifiers: [SCRATCH_UUID!]))
		} else {
			//print("Bluetooth not available.")
			let alert = UIAlertController(title: "Bluetooth unavalible", message: "Bluetooth is unavalibe for this device. Is it even turned on?", preferredStyle: UIAlertControllerStyle.alert)
			alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
			self.present(alert, animated: true, completion: nil)
		}
	}
	
	// Pair with device....
	// TODO: Change to be based on MAC Address instead of name
	private func centralManager(central: CBCentralManager, didDiscoverPeripheral peripheral: CBPeripheral, advertisementData: [String : AnyObject], RSSI: NSNumber) {
		let device = (advertisementData as NSDictionary).object(forKey: CBAdvertisementDataLocalNameKey) as? NSString
		// console.text = peripheral.name
		/*
		if device?.contains(globals.macAddress) == true {
			self.manager.stopScan()
			
			self.peripheral = peripheral
			self.peripheral.delegate = self
			
			manager.connect(peripheral, options: nil)
		}
*/
	}
	
	// Once you are connected to a device, you can get a list of services on that device.
	func centralManager(central: CBCentralManager, didConnectPeripheral peripheral: CBPeripheral) {
		peripheral.discoverServices(nil)
	}
	
	// Once you get a list of the services offered by the device, you will want to get a list of the characteristics. You can get crazy here, or limit listing of characteristics to just a specific service. If you go crazy watch for threading issues.
	private func peripheral(peripheral: CBPeripheral,didDiscoverServices error: NSError?) {
		for service in peripheral.services! {
			let thisService = service as CBService
			
			if service.uuid == SERVICE_UUID {
				peripheral.discoverCharacteristics(nil, for: thisService)
			}
		}
	}
	
	// There are different ways to approach getting data from the BLE device. One approach would be to read changes incrementally. Another approach, the approach I used in my application, would be to have the BLE device notify you whenever a characteristic value has changed.
	private func peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService service: CBService, error: NSError?) {
		for characteristic in service.characteristics! {
			let thisCharacteristic = characteristic as CBCharacteristic
			
			if thisCharacteristic.uuid == SERVICE_UUID {
				self.peripheral.setNotifyValue(true, for: thisCharacteristic)
			}
		}
	}
	
	// This is an optional step, but hey, let us be good programmers and clean up after ourselves. Also a good place to start scanning all over again.
	private func centralManager(central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: NSError?) {
		central.scanForPeripherals(withServices: nil, options: nil)
	}

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
