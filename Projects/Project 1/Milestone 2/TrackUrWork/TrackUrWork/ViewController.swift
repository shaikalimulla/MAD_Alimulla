//
//  ViewController.swift
//  TrackUrWork
//
//  Created by Ali on 9/27/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var hoursAllowed: UIStepper!
    
    @IBOutlet weak var showHours: UILabel!
    
    @IBAction func updateHours(sender: UIStepper) {
        showHours.text = "\(Int(hoursAllowed.value))"
    }
    
    @IBOutlet weak var ToDoBtn: UIButton!
    
    func moveToTaskWindow(){
        performSegueWithIdentifier("TaskWindow", sender: ToDoBtn)
    }
    
    @IBAction func TapToDoButton(sender: UIButton) {
        moveToTaskWindow()
    }
    
    @IBAction func TapDoTodayButton(sender: UIButton) {
        moveToTaskWindow()
    }
    
    @IBAction func TapInProgressButton(sender: UIButton) {
        moveToTaskWindow()
    }
    
    @IBAction func TapDoneButton(sender: UIButton) {
        moveToTaskWindow()
    }
    
    //Data sharing
    struct task {
        var name = "Name"
        var desc = "Description"
    }
    
    var tasks = [task]()
    var recieveValues = task()
    
    @IBAction func saveDetails(segue:UIStoryboardSegue) {
        if let sourceViewController = segue.sourceViewController as? TaskViewController {
            recieveValues.name = sourceViewController.taskInfo.name
            recieveValues.desc = sourceViewController.taskInfo.desc
            tasks.append(recieveValues)
            
            
        }
    }
    
    
    @IBOutlet weak var welcomeLabel: UILabel!
    
    //table view
    
    @IBOutlet weak var tableView: UITableView!
    
    let textCellIdentifier = "TextCell"
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        return tasks.count
        
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(textCellIdentifier, forIndexPath: indexPath)
        
        let currentTask = tasks[indexPath.row]
        
        cell.textLabel?.text = currentTask.name
        cell.detailTextLabel?.text = currentTask.desc
        cell.textLabel?.textColor = UIColor.redColor()
        cell.detailTextLabel?.textColor = UIColor.cyanColor()
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        
        print(tasks[indexPath.row].name)
    }
    
    /*
     func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath){
     if (editingStyle == UITableViewCellEditingStyle.Delete){
     
     taskMgr.tasks.removeAtIndex(indexPath.row)
     tableTasks.reloadData()
     }
     }
     */
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        //tableView.reloadData()
        tableView.rowHeight = UITableViewAutomaticDimension
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        
        
        tableView.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

