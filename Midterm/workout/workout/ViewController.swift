//
//  ViewController.swift
//  workout
//
//  Created by Ali on 10/13/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var workoutTime: UITextField!
    
    @IBOutlet weak var weeklySwitch: UISwitch!
    
    @IBOutlet weak var workoutPerWeek: UISlider!
    
    @IBOutlet weak var workoutType: UISegmentedControl!
    
    @IBOutlet weak var workoutDone: UIButton!
    
    @IBOutlet weak var workoutsSliderLabel: UILabel!
    
    @IBOutlet weak var milesLabel: UILabel!
    
    @IBOutlet weak var calariesLabel: UILabel!
    
    @IBOutlet weak var picView: UIImageView!
    
    var workoutsVal:Float?
    var switchOn = false
    
    @IBAction func updateImage(sender: UISegmentedControl) {
        if workoutType.selectedSegmentIndex == 0 {
            picView.image=UIImage(named: "run")
        }
        else if workoutType.selectedSegmentIndex == 1 {
            picView.image=UIImage(named: "swim")
        } else if workoutType.selectedSegmentIndex == 2 {
            picView.image=UIImage(named: "bike")
        }
        
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    func calWorkout(){
        var cal:Float?
        var miles:Float?
        if (workoutTime.text != nil) {
            miles = Float(workoutTime.text!)!/10
            cal = Float(workoutTime.text!)!*10
            milesLabel.text = String(format: "%.2f", miles!) + " miles"
            calariesLabel.text = String(format: "%.2f", cal!) + " Calaries"
        }
    }
    
    @IBAction func workoutClicked(sender: UIButton) {
         calWorkout()
    }
    
    @IBAction func weeklyWorkoutValue(sender: UISlider) {
        workoutsVal = Float(sender.value)
        workoutsSliderLabel.text=String(format: "%.2f", workoutsVal!)
        var cal:Float?
        var miles:Float?
        
        if switchOn {
            if (workoutsVal != nil) {
                miles = (Float(workoutTime.text!)!/10) * workoutsVal!
                cal = (Float(workoutTime.text!)!*10) * workoutsVal!
                milesLabel.text = String(format: "%.2f", miles!) + " miles"
                calariesLabel.text = String(format: "%.2f", cal!) + " Calaries"
            }
        } else {
            if (workoutTime.text != nil) {
                miles = Float(workoutTime.text!)!/10
                cal = Float(workoutTime.text!)!*10
                milesLabel.text = String(format: "%.2f", miles!) + " miles"
                calariesLabel.text = String(format: "%.2f", cal!) + " Calaries"
            }
        }
    }
    
    @IBAction func showWeeklySelected(sender: UISwitch) {
        
        
        if weeklySwitch.on {
            switchOn = true
            
        } else {
            switchOn = false
            
        }
    }
    override func viewDidLoad() {
        workoutTime.delegate=self
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

