//
//  TaskDetailsViewController.swift
//  TrackUrWork
//
//  Created by Ali on 9/28/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class TaskDetailsViewController {
    class var sharedInstance: TaskDetailsViewController {
        struct Static {
            static var instance: TaskDetailsViewController?
            static var token: dispatch_once_t = 0
        }
        
        dispatch_once(&Static.token) {
            Static.instance = TaskDetailsViewController()
        }
        
        return Static.instance!
    }

    struct task {
        var name = "Name"
        var desc = "Description"
    }
    
    var tasks = [task]()
    
    func addTask(name: String, desc: String){
        tasks.append(task(name: name, desc: desc))
    }    

}
