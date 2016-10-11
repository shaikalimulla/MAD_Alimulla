//
//  ShowDetailsViewController.swift
//  TrackUrWork
//
//  Created by Ali on 10/6/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ShowDetailsViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var showNameField: UITextField!
    
    @IBOutlet weak var showDescField: UITextField!
    
    @IBOutlet weak var moveCellSegmentCtl: UISegmentedControl!
    
    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var startTimePicker: UIDatePicker!
    @IBOutlet weak var endTimePicker: UIDatePicker!
    
    @IBOutlet weak var trackTimeSwitch: UISwitch!
    
    var startDate = NSDate.init()
    var endDate = NSDate.init()
    var selectedDate = NSDate.init()
    var trackTimeSwitchValueChanged = false
    var timeValueChanged = false
    var dateValueChanged = false

    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func dateChanged(sender: UIDatePicker) {
        selectedDate = datePicker.date
        if trackTimeSwitch.on {
            dateValueChanged = true
        }
    }
    
    @IBAction func startTimeChanged(sender: UIDatePicker) {
        startDate = startTimePicker.date
        if trackTimeSwitch.on {
            timeValueChanged = true
        }
    }
    
    @IBAction func endTimeChange(sender: UIDatePicker) {
        endDate = endTimePicker.date
        if trackTimeSwitch.on {
            timeValueChanged = true
        }
    }
    

    @IBAction func trackTimeSwitchChanged(sender: UISwitch) {
        trackTimeSwitchValueChanged = true
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
    
    func calculateTimeEdited() -> Bool {
        let currentTask = userSelectedTasks.tasks[0]
        let calendar = NSCalendar.currentCalendar()
        let initialTimeComponentsDiff = calendar.components([.Hour, .Minute], fromDate: currentTask.startTime, toDate: currentTask.endTime, options: [])
        
        let initialHoursWorked = initialTimeComponentsDiff.hour
        let initialMinsWorked = initialTimeComponentsDiff.minute
        print("initial Hours:Mins: \(initialHoursWorked) : \(initialMinsWorked)")
        
        let timeComponentsDiff = calendar.components([.Hour, .Minute], fromDate: startDate, toDate: endDate, options: [])
        
        let hoursWorked = timeComponentsDiff.hour
        let minsWorked = timeComponentsDiff.minute
        print("New Hours:Mins: \(hoursWorked) : \(minsWorked)")
        
        let initialTotalHoursInMins = ((initialHoursWorked) * 60) + initialMinsWorked
        let newTotalHoursInMins = ((hoursWorked) * 60) + minsWorked
        let diffInMins = initialTotalHoursInMins - newTotalHoursInMins
        var timWorkedTotalHoursInMins = ((timeWorked.hours) * 60) + timeWorked.mins
        
        if diffInMins < 0 {
            timWorkedTotalHoursInMins = timWorkedTotalHoursInMins - diffInMins
            let allowedTimeInMins = (timeAllowed.hours * 60) + timeAllowed.mins
            print("time calc: \(timWorkedTotalHoursInMins) \(allowedTimeInMins)")
            if (timWorkedTotalHoursInMins >= allowedTimeInMins){
                createAlert("Number of hours worked exceeded allowed hours")
                AlertOn = true
                return false
            }
            else {
                timeWorked.hours = timWorkedTotalHoursInMins / 60
                timeWorked.mins = timWorkedTotalHoursInMins % 60
                print("hoursWorked2: \(timeWorked.hours) : \(timeWorked.mins)")
            }
        } else {
            timWorkedTotalHoursInMins = timWorkedTotalHoursInMins - diffInMins
            
            if ((timWorkedTotalHoursInMins / 60) < 0 || (timWorkedTotalHoursInMins % 60) < 0) {
                createAlert("Number of hours or minutes worked should be greater than 0")
                AlertOn = true
                return false
            } else {
                timeWorked.hours = timWorkedTotalHoursInMins / 60
                timeWorked.mins = timWorkedTotalHoursInMins % 60
            
                print("hoursWorked2: \(timeWorked.hours) : \(timeWorked.mins)")
            }
        }
        return true
    }
    
    override func shouldPerformSegueWithIdentifier(identifier: String, sender: AnyObject!) -> Bool {
        if identifier == "doneShowDetails" {
            if trackTimeSwitchValueChanged {
                
                if trackTimeSwitch.on {
                    if checkDateSelected(selectedDate) == false || updateHoursWorked(startDate, endDate: endDate) == false {
                        return false
                    }
                } else {
                    print("show details time switch off ")
                    cancelHours(startDate, endDate: endDate)
                }
                if updateHoursLeft() == false {
                    return false
                }
                
                return true
            }
            if dateValueChanged {
                if checkDateSelected(selectedDate) == false {
                    return false
                }
                
                return true
            }
            if timeValueChanged {
                if calculateTimeEdited() == false {
                    return false
                }
                if updateHoursLeft() == false {
                    return false
                }
                
                return true
            }
            if AlertOn {
                return false
            }
            return true
        }
        
        // by default, transition
        return true
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "doneShowDetails"{
            if showNameField.text!.isEmpty == false && showDescField.text!.isEmpty == false {
                userSelectedTasks.updateTask(0, name: showNameField.text!, desc: showDescField.text!, date: selectedDate, startTime: startDate, endTime: endDate, switchOn: trackTimeSwitch.on)
                print("updated name: \(userSelectedTasks.tasks[0].name) \(selectedDate)")
            }
            
            selectedMoveToIndex = moveCellSegmentCtl.selectedSegmentIndex
            print("move to index: \(selectedMoveToIndex!)")
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        showNameField.delegate=self
        showDescField.delegate=self
        
        let currentTask = userSelectedTasks.tasks[0]
        userSelectedTasks.printTask(0)
        if (currentTask.name.isEmpty == false && currentTask.desc.isEmpty == false){
            showNameField.text = currentTask.name
            showDescField.text = currentTask.desc
            datePicker.date = currentTask.date
            selectedDate = datePicker.date
            startTimePicker.date = currentTask.startTime
            startDate = startTimePicker.date
            endTimePicker.date = currentTask.endTime
            endDate = endTimePicker.date
            if currentTask.switchOn {
                trackTimeSwitch.on = true
            } else {
                trackTimeSwitch.on = false
            }
            
            print("show details view, selected task name: \(currentTask.name) \(datePicker.date)")
            
        }
        
        if tableViewId != nil {
            switch (tableViewId!){
            case 1:
                moveCellSegmentCtl.setTitle("DoToday", forSegmentAtIndex:0)
                moveCellSegmentCtl.setTitle("InProgress", forSegmentAtIndex:1)
                moveCellSegmentCtl.setTitle("Done", forSegmentAtIndex:2)
            case 2:
                moveCellSegmentCtl.setTitle("ToDo", forSegmentAtIndex:0)
                moveCellSegmentCtl.setTitle("InProgress", forSegmentAtIndex:1)
                moveCellSegmentCtl.setTitle("Done", forSegmentAtIndex:2)
            case 3:
                moveCellSegmentCtl.setTitle("ToDo", forSegmentAtIndex:0)
                moveCellSegmentCtl.setTitle("DoToday", forSegmentAtIndex:1)
                moveCellSegmentCtl.setTitle("Done", forSegmentAtIndex:2)
            case 4:
                moveCellSegmentCtl.setTitle("ToDo", forSegmentAtIndex:0)
                moveCellSegmentCtl.setTitle("DoToday", forSegmentAtIndex:1)
                moveCellSegmentCtl.setTitle("InProgress", forSegmentAtIndex:2)
            default:
                moveCellSegmentCtl.setTitle("DoToday", forSegmentAtIndex:0)
                moveCellSegmentCtl.setTitle("InProgress", forSegmentAtIndex:1)
                moveCellSegmentCtl.setTitle("Done", forSegmentAtIndex:2)
            }
        }
        
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
