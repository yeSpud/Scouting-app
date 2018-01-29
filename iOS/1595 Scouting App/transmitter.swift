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
	let NAME = "Spud"
	let SCRATCH_UUID = CBUUID(string: "a495ff21-c5b1-4b44-b512-1370f02d74de")
	let SERVICE_UUID = CBUUID(string: "00001101-0000-1000-8000-00805F9B34FB")
	
    override func viewDidLoad() {
        super.viewDidLoad()
		manager = CBCentralManager(delegate: self, queue: nil)
		print(globals.data)
        // Do any additional setup after loading the view.
    }
	
	func centralManagerDidUpdateState(_ central: CBCentralManager) {
		if central.state == CBManagerState.poweredOn {
			central.scanForPeripherals(withServices: nil, options: nil)
		} else {
			print("Bluetooth not available.")
		}
	}
	
	private func centralManager(central: CBCentralManager, didDiscoverPeripheral peripheral: CBPeripheral, advertisementData: [String : AnyObject], RSSI: NSNumber) {
		let device = (advertisementData as NSDictionary).object(forKey: CBAdvertisementDataLocalNameKey) as? NSString
		
		if device?.contains(NAME) == true {
			self.manager.stopScan()
			
			self.peripheral = peripheral
			self.peripheral.delegate = self
			
			manager.connect(peripheral, options: nil)
		}
	}
	
	func centralManager(central: CBCentralManager, didConnectPeripheral peripheral: CBPeripheral) {
		peripheral.discoverServices(nil)
	}
	
	private func peripheral(peripheral: CBPeripheral,didDiscoverServices error: NSError?) {
		for service in peripheral.services! {
			let thisService = service as CBService
			
			if service.uuid == SERVICE_UUID {
				peripheral.discoverCharacteristics(
					nil,
					for: thisService
				)
			}
		}
	}
	private func peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService service: CBService, error: NSError?) {
		for characteristic in service.characteristics! {
			let thisCharacteristic = characteristic as CBCharacteristic
			
			if thisCharacteristic.uuid == SCRATCH_UUID {
				self.peripheral.setNotifyValue(true, for: thisCharacteristic)
			}
		}
	}
	
	private func peripheral(peripheral: CBPeripheral, didUpdateValueForCharacteristic characteristic: CBCharacteristic,error: NSError?) {
		var count:UInt32 = 0;
		
		if characteristic.uuid == SCRATCH_UUID {
			characteristic.value!.getBytes(&count, length: sizeof(UInt32))
			labelCount.text = NSString(format: "%llu", count) as String
		}
	}
	
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
