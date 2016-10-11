//
//  ViewController.swift
//  TrackUrWork
//
//  Created by Ali on 9/27/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

/*
 References used for this project are listed below.
 For TableView - https://developer.apple.com/library/content/referencelibrary/GettingStarted/DevelopiOSAppsSwift/Lesson7.html
 For date and time manipulation - https://developer.apple.com/reference/foundation/nscalendar
 Multiview infromation - Class notes (http://creative.colorado.edu/~apierce/MAD/)
 */

import UIKit

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var showHours: UILabel!
    
    @IBOutlet weak var hoursAlowedStepper: UIStepper!
    
    @IBOutlet weak var welcomeLabel: UILabel!
    
    @IBOutlet weak var toDoBtn: UIButton!
    
    @IBOutlet weak var doTodayBtn: UIButton!
    
    @IBOutlet weak var inprogressBtn: UIButton!
    
    @IBOutlet weak var doneBtn: UIButton!
    
    @IBOutlet weak var hoursAllowedPicker: UIDatePicker!
    
    @IBOutlet weak var hoursWorkedLabel: UILabel!
    
    @IBOutlet weak var hoursLeftLabel: UILabel!
    
    @IBAction func TapToDoButton(sender: UIButton) {
            performSegueWithIdentifier("TaskWindow", sender: toDoBtn)
    }
    
    @IBAction func TapDoTodayButton(sender: UIButton) {
            performSegueWithIdentifier("TaskWindow", sender: doTodayBtn)
    }
    
    @IBAction func TapInProgressButton(sender: UIButton) {
             performSegueWithIdentifier("TaskWindow", sender: inprogressBtn)
    }
    
    @IBAction func TapDoneButton(sender: UIButton) {
            performSegueWithIdentifier("TaskWindow", sender: doneBtn)
    }
    
    func createAlert(msg : String){
        let alert=UIAlertController(title:"Warning", message:msg, preferredStyle: UIAlertControllerStyle.Alert)
        
        let cancelAction=UIAlertAction(title:"Cancel",style: UIAlertActionStyle.Cancel, handler: nil)
        
        alert.addAction(cancelAction)
        let okAction = UIAlertAction(title: "Ok", style: UIAlertActionStyle.Default, handler: nil)
        
        alert.addAction(okAction)
        presentViewController(alert, animated:true, completion:nil)
    }
    
    @IBAction func updateHours(sender: UIStepper) {
        showHours.text = "\(Int(hoursAlowedStepper.value))"
        timeAllowed.hours = Int(hoursAlowedStepper.value)
        
        var hoursLeft = 0
        var minsLeft = 0
        
        let workedTimeInMins = (timeWorked.hours * 60) + timeWorked.mins
        let allowedTimeInMins = (timeAllowed.hours * 60) + timeAllowed.mins
        hoursLeft = (allowedTimeInMins - workedTimeInMins) / 60
        minsLeft = (allowedTimeInMins - workedTimeInMins) % 60
        
        hoursLeftLabel.text = String(hoursLeft)+"H:"+String(minsLeft)+"M"
        if (hoursLeft < 0 || (hoursLeft == 0 && minsLeft < 0)){
            createAlert("Number of hours worked exceeded allowed hours")
        }
        else {
            timeLeft.hours = hoursLeft
            timeLeft.mins = minsLeft
        }
        print("time Allowed: \(timeLeft.hours) \(timeLeft.mins)")
    }
    
    //Data sharing
    
    func updateHoursLabels(){
        print("hoursWorked 1: \(timeWorked.hours) : \(timeWorked.mins)")
        let totalHoursWorked = String(timeWorked.hours)+"H:"+String(timeWorked.mins)+"M"
        
        hoursWorkedLabel.text = totalHoursWorked
        
        print("hoursWorked 1: \(timeLeft.hours) : \(timeLeft.mins)")
        let totalHoursLeft = String(timeLeft.hours)+"H:"+String(timeLeft.mins)+"M"
        
        hoursLeftLabel.text = totalHoursLeft
    }
    
    @IBAction func saveDetails(segue:UIStoryboardSegue) {
        updateHoursLabels()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "TaskWindow"{
            
            pressedButtonTag = sender!.tag;
            //print("test \(pressedButtonTag!)")
        }
        
        if segue.identifier == "showDetailsWindow"{
            let currentTask = userSelectedTasks.tasks[0]
            print("Prepare for segue, selected task name: \(currentTask.name)")
        }
    }
    
    //table view
    
    @IBOutlet weak var toDoTableView: UITableView!
    
    @IBOutlet weak var doTodayTableView: UITableView!
    
    @IBOutlet weak var inprogressTableView: UITableView!
    
    @IBOutlet weak var doneTableView: UITableView!
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        var rowsCount = 0
        
        if (tableView == toDoTableView) {
            rowsCount = toDoTasks.tasks.count
        }

        if (tableView == doTodayTableView) {
            rowsCount = doTodayTasks.tasks.count
        }

        if (tableView == inprogressTableView) {
            rowsCount = inprogressTasks.tasks.count
        }

        if (tableView == doneTableView) {
            rowsCount = doneTasks.tasks.count
        }
        
        //print("rows \(rowsCount)")
        return rowsCount
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        var cell:UITableViewCell?
        
        print("choice \(pressedButtonTag!)")

        if (tableView == toDoTableView) {
            let textCellIdentifier = "toDoCells"
            cell = toDoTableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath)
            let currentTask = toDoTasks.tasks[indexPath.row]
            
            cell!.textLabel?.text = currentTask.name
            cell!.detailTextLabel?.text = currentTask.desc
            cell!.textLabel?.textColor = UIColor.blackColor()
            cell!.detailTextLabel?.textColor = UIColor.blueColor()
            
            print("test \(currentTask.name)")
            print("test \(currentTask.desc)")
            return cell!
        }

        if (tableView == doTodayTableView) {
            let textCellIdentifier = "doTodayCells"
            cell = doTodayTableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath)
            let currentTask = doTodayTasks.tasks[indexPath.row]
            print("test"+textCellIdentifier)
            cell!.textLabel?.text = currentTask.name
            cell!.detailTextLabel?.text = currentTask.desc
            cell!.textLabel?.textColor = UIColor.blackColor()
            cell!.detailTextLabel?.textColor = UIColor.blueColor()
            
            //print("test \(currentTask.name)")
            //print("test \(currentTask.desc)")
            return cell!
        }

        if (tableView == inprogressTableView) {
            let textCellIdentifier = "inprogressCells"
            cell = inprogressTableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath)
            let currentTask = inprogressTasks.tasks[indexPath.row]
            
            print("test"+textCellIdentifier)
            cell!.textLabel?.text = currentTask.name
            cell!.detailTextLabel?.text = currentTask.desc
            cell!.textLabel?.textColor = UIColor.blackColor()
            cell!.detailTextLabel?.textColor = UIColor.blueColor()
            
            //print("test \(currentTask.name)")
            //print("test \(currentTask.desc)")
            return cell!
        }

        if (tableView == doneTableView) {
            let textCellIdentifier = "doneCells"
            cell = doneTableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath)
            let currentTask = doneTasks.tasks[indexPath.row]
            
            print("test"+textCellIdentifier)
            cell!.textLabel?.text = currentTask.name
            cell!.detailTextLabel?.text = currentTask.desc
            cell!.textLabel?.textColor = UIColor.blackColor()
            cell!.detailTextLabel?.textColor = UIColor.blueColor()
            
            //print("test \(currentTask.name)")
            //print("test \(currentTask.desc)")
            return cell!
        }
        
        return cell!
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        
        userSelectedRow = indexPath.row
        userSelectedTasks.removeAllTasks()
        
        if userSelectedRow != nil {
            if (tableView == toDoTableView) {
                tableViewId = 1
                let currentTask = toDoTasks.tasks[userSelectedRow!]
                userSelectedTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                //print(" selected task name: \(currentTask.name)")
                toDoTasks.printTask(userSelectedRow!)
            }
            if (tableView == doTodayTableView) {
                tableViewId = 2
                let currentTask = doTodayTasks.tasks[userSelectedRow!]
                userSelectedTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                //print(" selected task name: \(currentTask.name)")
            }
            if (tableView == inprogressTableView) {
                tableViewId = 3
                let currentTask = inprogressTasks.tasks[userSelectedRow!]
                userSelectedTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                //print(" selected task name: \(currentTask.name)")
            }
            if (tableView == doneTableView) {
                tableViewId = 4
                let currentTask = doneTasks.tasks[userSelectedRow!]
                userSelectedTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                //print(" selected task name: \(currentTask.name)")
            }
        }
        performSegueWithIdentifier("showDetailsWindow", sender: toDoTableView)
    }
    
    func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
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
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        //print("swipe to delete fn called")
        if editingStyle == .Delete {
            let indexToDelete = indexPath.row

            switch (tableView) {
            case toDoTableView:
                let currentTask = toDoTasks.tasks[indexToDelete]
                if currentTask.switchOn {
                    cancelHours(currentTask.startTime, endDate: currentTask.endTime)
                    updateHoursLeft()
                    updateHoursLabels()
                }
                toDoTasks.removeTask(indexToDelete)
                toDoTableView.reloadData()
            case doTodayTableView:
                let currentTask = doTodayTasks.tasks[indexToDelete]
                if currentTask.switchOn {
                    cancelHours(currentTask.startTime, endDate: currentTask.endTime)
                    updateHoursLeft()
                    updateHoursLabels()
                }
                doTodayTasks.removeTask(indexToDelete)
                doTodayTableView.reloadData()
            case inprogressTableView:
                let currentTask = inprogressTasks.tasks[indexToDelete]
                if currentTask.switchOn {
                    cancelHours(currentTask.startTime, endDate: currentTask.endTime)
                    updateHoursLeft()
                    updateHoursLabels()
                }
                inprogressTasks.removeTask(indexToDelete)
                inprogressTableView.reloadData()
            case doneTableView:
                let currentTask = doneTasks.tasks[indexToDelete]
                if currentTask.switchOn {
                    cancelHours(currentTask.startTime, endDate: currentTask.endTime)
                    updateHoursLeft()
                    updateHoursLabels()
                }
                doneTasks.removeTask(indexToDelete)
                doneTableView.reloadData()
            default:
                print("selected table view id: \(tableViewId!) is invalid")
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("view did load called")
        toDoTableView.delegate = self
        toDoTableView.dataSource = self
        doTodayTableView.delegate = self
        doTodayTableView.dataSource = self
        inprogressTableView.delegate = self
        inprogressTableView.dataSource = self
        doneTableView.delegate = self
        doneTableView.dataSource = self
        
        let currentComponents = NSCalendar.currentCalendar().components([.Weekday, .Day], fromDate: NSDate())
        if (currentComponents.weekday == 1){
            hoursWorkedLabel.text = "0H:0M"
        }
        
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        let nav = self.navigationController?.navigationBar
        nav?.barStyle = UIBarStyle.Black
        
        if (selectedMoveToIndex != nil){
            print("selectedMoveToIndex view did appear called \(selectedMoveToIndex!)")
            let currentTask = userSelectedTasks.tasks[0]
            switch(tableViewId!){
            case 1:
                toDoTasks.updateTask(userSelectedRow!, name: currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                print("updated name: \(currentTask.name)")
                toDoTasks.printTask(userSelectedRow!)
                switch(selectedMoveToIndex!){
                case 0:
                    toDoTasks.removeTask(userSelectedRow!)
                    doTodayTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 1:
                    toDoTasks.removeTask(userSelectedRow!)
                    inprogressTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 2:
                    toDoTasks.removeTask(userSelectedRow!)
                    doneTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                default:
                    print("selected MoveTo Index: \(selectedMoveToIndex!) is invalid")
                }
            case 2:
                doTodayTasks.updateTask(userSelectedRow!, name: currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                print("updated name: \(currentTask.name)")
                switch(selectedMoveToIndex!){
                case 0:
                    doTodayTasks.removeTask(userSelectedRow!)
                    toDoTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 1:
                    doTodayTasks.removeTask(userSelectedRow!)
                    inprogressTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 2:
                    doTodayTasks.removeTask(userSelectedRow!)
                    doneTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                default:
                    print("selected MoveTo Index: \(selectedMoveToIndex!) is invalid")
                }
            case 3:
                inprogressTasks.updateTask(userSelectedRow!, name: currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                print("updated name: \(currentTask.name)")
                switch(selectedMoveToIndex!){
                case 0:
                    inprogressTasks.removeTask(userSelectedRow!)
                    toDoTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 1:
                    inprogressTasks.removeTask(userSelectedRow!)
                    doTodayTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 2:
                    inprogressTasks.removeTask(userSelectedRow!)
                    doneTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                default:
                    print("selected MoveTo Index: \(selectedMoveToIndex!) is invalid")
                }
            case 4:
                doneTasks.updateTask(userSelectedRow!, name: currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                print("updated name: \(currentTask.name)")
                switch(selectedMoveToIndex!){
                case 0:
                    doneTasks.removeTask(userSelectedRow!)
                    toDoTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 1:
                    doneTasks.removeTask(userSelectedRow!)
                    doTodayTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                case 2:
                    doneTasks.removeTask(userSelectedRow!)
                    inprogressTasks.addTask(currentTask.name, desc: currentTask.desc, date: currentTask.date, startTime: currentTask.startTime, endTime: currentTask.endTime, switchOn: currentTask.switchOn)
                default:
                    print("selected MoveTo Index: \(selectedMoveToIndex!) is invalid")
                }
            default:
                print("selected table view id: \(tableViewId!) is invalid")
            }
            selectedMoveToIndex = nil
        }
        
        toDoTableView.reloadData()
        doTodayTableView.reloadData()
        inprogressTableView.reloadData()
        doneTableView.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

