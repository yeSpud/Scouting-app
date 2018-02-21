//
//  globals.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/25/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import Foundation
import CoreBluetooth
//class globals {
	//var macAddress = String();
	//var teamNumber = Int();
	
	//func setMacAddress(macAddr:String) {
		//macAddress = macAddr
	//}
struct globals {
	static var macAddress = "";
	static var data = "Err";
	static var teamNumber = Int()
	static var manager:CBCentralManager! 
}
