//
//  ScoutingViewController.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/24/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit

class ScoutingViewController: UIViewController {

    @IBOutlet weak var HeaderText: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        HeaderText.text = "Team to scout: " + String(globals.teamNumber)
        HeaderText.textColor = UIColor.white
        // Do any additional setup after loading the view.
    }
    @IBOutlet weak var hasAuto: UISwitch!
    @IBOutlet weak var autoSwitch: UISwitch!
    @IBOutlet weak var autoScale: UISwitch!
    @IBOutlet weak var teleScale: UISwitch!
    @IBOutlet weak var teleSwitch: UISwitch!
    @IBOutlet weak var endClimb: UISwitch!
    @IBOutlet weak var endClimbAssist: UISwitch!
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet weak var cubeTextStepper: UIStepper!
    @IBOutlet weak var cubeText: UILabel!
    @IBAction func cubeNumberChange(_ sender: Any) {
        cubeText.text = String(Int(cubeTextStepper.value));
    }
	
    @IBAction func submit() {
		var string:String;
        string = String(String(globals.teamNumber) + ", ").uppercased()
		string.append(String(String(hasAuto.isOn) + ", ").uppercased())
		string.append(String(String(autoSwitch.isOn) + ", ").uppercased())
		string.append(String(String(autoScale.isOn) + ", ").uppercased())
		string.append(String(String(teleSwitch.isOn) + ", ").uppercased())
		string.append(String(String(teleScale.isOn) + ", ").uppercased())
		string.append(String(String(cubeText.text!) + ", ").uppercased())
		string.append(String(String(endClimb.isOn) + ", ").uppercased())
		string.append(String(String(endClimbAssist.isOn)).uppercased())
		globals.data = string
		self.performSegue(withIdentifier: "submit", sender: self)

    }
	override var preferredStatusBarStyle: UIStatusBarStyle {
		return .lightContent
	}
}
