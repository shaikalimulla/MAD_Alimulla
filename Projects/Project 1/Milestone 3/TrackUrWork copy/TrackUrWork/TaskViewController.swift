//
//  TaskViewController.swift
//  TrackUrWork
//
//  Created by Ali on 9/27/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class TaskViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var descField: UITextField!
    @IBOutlet weak var doneBtn: UIButton!
    @IBOutlet weak var trackTimeSwitch: UISwitch!
    
    var startDate = NSDate.init()
    var endDate = NSDate.init()
    var selectedDate = NSDate.init()
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }

    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }

    @IBAction func TapOnDone(sender: UIButton) {
        //passValues()
        
    }

    func textFieldDidEndEditing(textField: UITextField) {
        //passValues()
    }
    
    func createAlert(msg : String){
        let alert=UIAlertController(title:"Warning", message:msg, preferredStyle: UIAlertControllerStyle.Alert)
        
        let cancelAction=UIAlertAction(title:"Cancel",style: UIAlertActionStyle.Cancel, handler: nil)
        
        alert.addAction(cancelAction)
        let okAction = UIAlertAction(title: "Ok", style: UIAlertActionStyle.Default, handler: nil)
        
        alert.addAction(okAction)
        presentViewController(alert, animated:true, completion:nil)
    }
    
    func updateHoursWorked(startDate: NSDate, endDate: NSDate) -> Bool {
        let calendar = NSCalendar.currentCalendar()
        let timeComponentsDiff = calendar.components([.Hour, .Minute], fromDate: startDate, toDate: endDate, options: [])
        
        let hoursWorked = timeComponentsDiff.hour
        let minsWorked = timeComponentsDiff.minute
        print("New Hours:Mins: \(hoursWorked) : \(minsWorked)")
        
        if (hoursWorked < 0 || minsWorked < 0) {
            createAlert("Number of hours or minutes worked should be greater than 0")
            AlertOn = true
            return false
        }
        else {
            let totalHoursBeforeAdding = ((timeWorked.hours + hoursWorked) * 60) + timeWorked.mins + minsWorked
            let allowedTimeInMins = (timeAllowed.hours * 60) + timeAllowed.mins
            print("time calc: \(totalHoursBeforeAdding) \(allowedTimeInMins)")
            if (totalHoursBeforeAdding >= allowedTimeInMins){
                createAlert("Number of hours worked exceeded allowed hours")
                AlertOn = true
                return false
            }
            else {
                timeWorked.hours = timeWorked.hours + hoursWorked
                timeWorked.mins = timeWorked.mins + minsWorked
                if(timeWorked.mins >= 60) {
                    timeWorked.hours = timeWorked.hours + (timeWorked.mins/60)
                    timeWorked.mins = timeWorked.mins % 60
                }
                print("hoursWorked2: \(timeWorked.hours) : \(timeWorked.mins)")
            }
        }
        return true
    }
    
    func cancelHours(startDate: NSDate, endDate: NSDate) -> Bool {
        let calendar = NSCalendar.currentCalendar()
        let timeComponentsDiff = calendar.components([.Hour, .Minute], fromDate: startDate, toDate: endDate, options: [])
        
        let hoursWorked = timeComponentsDiff.hour
        let minsWorked = timeComponentsDiff.minute
        print("New Hours:Mins: \(hoursWorked) : \(minsWorked)")
        
        if (hoursWorked < 0 || minsWorked < 0) {
            createAlert("Number of hours or minutes worked should be greater than 0")
            AlertOn = true
            return false
        }
        else {
            
            timeWorked.hours = timeWorked.hours - hoursWorked
            timeWorked.mins = timeWorked.mins - minsWorked
            if(timeWorked.mins < 0) {
                timeWorked.hours = timeWorked.hours - 1
                timeWorked.mins = 60 + timeWorked.mins
            }
            print("hoursWorked2: \(timeWorked.hours) : \(timeWorked.mins)")
            
        }
        AlertOn = false
        return true
    }
    
    func updateHoursLeft() -> Bool{
        var hoursLeft = 0
        var minsLeft = 0
        
        let workedTimeInMins = (timeWorked.hours * 60) + timeWorked.mins
        let allowedTimeInMins = (timeAllowed.hours * 60) + timeAllowed.mins
        hoursLeft = (allowedTimeInMins - workedTimeInMins) / 60
        minsLeft = (allowedTimeInMins - workedTimeInMins) % 60
        
        if (hoursLeft < 0 || (hoursLeft == 0 && minsLeft < 0)){
            createAlert("Number of hours worked exceeded allowed hours")
            AlertOn = true
            return false
        }
        else {
            timeLeft.hours = hoursLeft
            timeLeft.mins = minsLeft
        }
        return true
    }
    
    func checkDateSelected(selectedDate: NSDate) -> Bool{
        let calendar = NSCalendar.currentCalendar()
        let currentComponents = NSCalendar.currentCalendar().components([.Weekday, .Day], fromDate: NSDate())
        let selectedComponents = calendar.components([.Weekday, .Day], fromDate: selectedDate)
        let currentWeekDay = currentComponents.weekday
        let currentDay = currentComponents.day
        let selectedWeekDay = selectedComponents.weekday
        let selectedDay = selectedComponents.day
        print("Day: \(currentWeekDay) \(currentDay) : \(selectedWeekDay) \(selectedDay)")
        let daysLeft = 7 - currentWeekDay
        print("daysLEft: \(daysLeft)")
        if (selectedDay > daysLeft + currentDay || selectedDay < abs(currentWeekDay - currentDay)+1){
            createAlert("Please add hours for the current week only!")
            AlertOn = true
            return false
        }
        return true
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject!) -> Bool {
        if identifier == "doneTasks" {
            
            if trackTimeSwitch.on {
                if checkDateSelected(selectedDate) == false {
                    return false
                }
                if updateHoursWorked(startDate, endDate: endDate) == false {
                    return false
                }
            }
            if updateHoursLeft() == false {
                return false
            }
            return true
        }
        return true
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "doneTasks"{

            print("view \(pressedButtonTag!)")
            if nameField.text!.isEmpty == false && descField.text!.isEmpty == false {
                if pressedButtonTag != nil {
                    switch (pressedButtonTag!){
                    case 1:
                        toDoTasks.addTask(nameField.text!, desc: descField.text!, date: selectedDate, startTime: startDate, endTime: endDate, switchOn: trackTimeSwitch.on)
                    case 2:
                        doTodayTasks.addTask(nameField.text!, desc: descField.text!, date: selectedDate, startTime: startDate, endTime: endDate, switchOn: trackTimeSwitch.on)
                    case 3:
                        inprogressTasks.addTask(nameField.text!, desc: descField.text!, date: selectedDate, startTime: startDate, endTime: endDate, switchOn: trackTimeSwitch.on)
                    case 4:
                        doneTasks.addTask(nameField.text!, desc: descField.text!, date: selectedDate, startTime: startDate, endTime: endDate, switchOn: trackTimeSwitch.on)
                    default:
                        toDoTasks.addTask(nameField.text!, desc: descField.text!, date: selectedDate, startTime: startDate, endTime: endDate, switchOn: trackTimeSwitch.on)
                    }
                }
            }
        }
    }
    
    //Calculate hours
    
    @IBOutlet weak var startTimePicker: UIDatePicker!
    
    @IBOutlet weak var endTimePicker: UIDatePicker!
    
    @IBOutlet weak var datePicker: UIDatePicker!
    
    @IBAction func dateSelected(sender: UIDatePicker) {
        selectedDate = datePicker.date
    }
    @IBAction func startTimeSelected(sender: UIDatePicker) {
        startDate = startTimePicker.date
    }
    
    @IBAction func endTimeSelected(sender: UIDatePicker) {
        endDate = endTimePicker.date
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        nameField.delegate=self
        descField.delegate=self

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
