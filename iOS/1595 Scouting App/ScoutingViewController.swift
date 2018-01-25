//
//  ScoutingViewController.swift
//  1595 Scouting App
//
//  Created by Stephen Ogden on 1/24/18.
//  Copyright © 2018 1595 Dragons. All rights reserved.
//

import UIKit

class ScoutingViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func cancel() {
        self.performSegue(withIdentifier: "toMain", sender: self)
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
