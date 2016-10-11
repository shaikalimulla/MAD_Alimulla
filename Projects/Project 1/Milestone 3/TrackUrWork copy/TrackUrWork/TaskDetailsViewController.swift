//
//  TaskDetailsViewController.swift
//  TrackUrWork
//
//  Created by Ali on 9/28/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

var taskMgr: TaskDetailsViewController = TaskDetailsViewController()

struct task {
    var name = "Name"
    var desc = "Description"
}

class TaskDetailsViewController: NSObject {
    var tasks = [task]()
    
    func addTask(name: String, desc: String){
        tasks.append(task(name: name, desc: desc))
    }
}



