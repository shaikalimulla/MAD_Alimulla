//
//  TaskViewController.swift
//  TrackUrWork
//
//  Created by Ali on 9/27/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

var taskMgr: TaskDetailsViewController = TaskDetailsViewController()
let shareData = TaskDetailsViewController.sharedInstance

class TaskViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var descField: UITextField!
    @IBOutlet weak var doneBtn: UIButton!

    struct task {
        var name = "Name"
        var desc = "Description"
    }
    var taskInfo = task()
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    

    // Called when the user click on the view, outside the UITextField.
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    func passValues(){
        if nameField.text!.isEmpty{
            
        } else {
            taskInfo.name = nameField.text!
        }
        if descField.text!.isEmpty{
            
        } else {
            taskInfo.desc = descField.text!
        }
    }

    @IBAction func TapOnDone(sender: UIButton) {
        passValues()
        
    }

    func textFieldDidEndEditing(textField: UITextField) {
        //passValues()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        nameField.text = taskInfo.name
        descField.text = taskInfo.desc
        
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
