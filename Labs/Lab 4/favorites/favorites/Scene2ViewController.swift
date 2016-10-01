//
//  Scene2ViewController.swift
//  favorites
//
//  Created by Ali on 9/27/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class Scene2ViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var titleField: UITextField!
    
    @IBOutlet weak var author: UITextField!
    
    @IBOutlet weak var desc: UITextField!
    
    @IBOutlet weak var genre: UITextField!
    
    @IBOutlet weak var submit: UIButton!
    
    @IBAction func tapButton(sender: UIButton) {
        //performSegueWithIdentifier("ConnectTo", sender: submit)
        
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "doneFavs"{
            let sceneViewController = segue.destinationViewController as! ViewController
            //check to see that text was entered in the textfields
            if titleField.text!.isEmpty == false{
                sceneViewController.user.favBook=titleField.text
            }
            if author.text!.isEmpty == false{
                sceneViewController.user.favAuthor=author.text
            }
            if genre.text!.isEmpty == false{
                sceneViewController.user.genre=genre.text
            }
            if desc.text!.isEmpty == false{
                sceneViewController.user.desc=desc.text
            }
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        titleField.delegate=self
        author.delegate=self
        genre.delegate=self
        desc.delegate=self
        
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
