//
//  dataStorage.swift
//  TrackUrWork
//
//  Created by Ali on 10/1/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import Foundation

var pressedButtonTag:Int?
var tableViewId:Int?
var userSelectedRow:Int?
var selectedMoveToIndex:Int?
var AlertOn = false

struct time {
    var hours = 0
    var mins = 0
}

var timeWorked = time()
var timeAllowed = time(hours:40, mins:0)
var timeLeft = time(hours:40, mins:0)

var toDoTasks: dataStorage = dataStorage()
var doTodayTasks: dataStorage = dataStorage()
var inprogressTasks: dataStorage = dataStorage()
var doneTasks: dataStorage = dataStorage()
var userSelectedTasks: dataStorage = dataStorage()

class dataStorage {
    
    struct task {
        var name = "Name"
        var desc = "Description"
        var date = NSDate.init()
        var startTime = NSDate.init()
        var endTime = NSDate.init()
        var switchOn = false
    }
    
    var tasks = [task]()
    
    func addTask(name: String, desc: String, date: NSDate, startTime: NSDate, endTime: NSDate, switchOn: Bool){
        tasks.append(task(name: name, desc: desc, date: date, startTime: startTime, endTime: endTime, switchOn: switchOn))
    }
    func removeAllTasks(){
        tasks.removeAll()
    }
    func removeTask(indexPos:Int){
        tasks.removeAtIndex(indexPos)
    }
    func updateTask(index:Int, name: String, desc: String, date: NSDate, startTime: NSDate, endTime: NSDate, switchOn: Bool){
        tasks[index] = task(name: name, desc: desc, date: date, startTime: startTime, endTime: endTime, switchOn: switchOn)
    }
    func printTask(index:Int){
        print("\(tasks[index])")
    }
}