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

    override func viewDidLoad() {
        super.viewDidLoad()
		let my_uuid = UUID().uuidString
		// let btSocket = CBSocket
		print(globals.data)
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
