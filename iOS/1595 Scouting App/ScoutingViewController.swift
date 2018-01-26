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

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet weak var cubeTextStepper: UIStepper!
    @IBOutlet weak var cubeText: UILabel!
    @IBAction func cubeNumberChange(_ sender: Any) {
        cubeText.text = String(Int(cubeTextStepper.value));
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
