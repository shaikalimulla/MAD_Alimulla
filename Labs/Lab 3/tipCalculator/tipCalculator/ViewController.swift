//
//  ViewController.swift
//  tipCalculator
//
//  Created by Ali on 9/20/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var amountVal: UITextField!
    
    @IBOutlet weak var tipVal: UITextField!
    
    @IBOutlet weak var peopleCount: UITextField!
    
    @IBOutlet weak var calTip: UILabel!
    
    @IBOutlet weak var calTotal: UILabel!
    
    @IBOutlet weak var calTotalPeople: UILabel!
    
    let charsAcceptable = "0123456789."
    
    func calTips(){
        var amount:Float
        var perc:Float
        
        if amountVal.text!.isEmpty{
            amount = 0.0
        } else {
            amount = Float(amountVal.text!)!
        }
        if tipVal.text!.isEmpty{
            perc = 0.0
        } else {
            perc = Float(tipVal.text!)!/100
        }
        
        let numOfPeople = Int(peopleCount.text!)
        let tip = amount*perc
        let total = amount+tip
        var personTotal:Float = 0.0
        
        if numOfPeople != nil {
            if numOfPeople! > 0 {
                personTotal = total / Float(numOfPeople!)
            } else {
                let alert=UIAlertController(title:"Warning", message:"Number of people should be greater than 1", preferredStyle: UIAlertControllerStyle.Alert)
                
                let cancelAction=UIAlertAction(title:"Cancel",style: UIAlertActionStyle.Cancel, handler: nil)
                
                alert.addAction(cancelAction)
                let okAction = UIAlertAction(title: "Ok", style: UIAlertActionStyle.Default, handler: {action in self.peopleCount.text="1"
                    self.calTips()})
                
                alert.addAction(okAction)
                presentViewController(alert, animated:true, completion:nil)
                
            }
        }
        
        let currencyFormatter = NSNumberFormatter()
        currencyFormatter.numberStyle=NSNumberFormatterStyle.CurrencyStyle
        calTip.text = currencyFormatter.stringFromNumber(tip)
        calTotal.text = currencyFormatter.stringFromNumber(total)
        calTotalPeople.text = currencyFormatter.stringFromNumber(personTotal)
    }
    
    func textFieldDidEndEditing(textField: UITextField) {
        calTips()
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        if (string.characters.count == 0) {
            return true
        }
        
        let charset = NSCharacterSet(charactersInString: self.charsAcceptable)
        let filtered = string.componentsSeparatedByCharactersInSet(charset).filter {  !$0.isEmpty }
        let str = filtered.joinWithSeparator("")
        return (string != str)
    }
    
    // Called when the user click on the view, outside the UITextField.
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    override func viewDidLoad() {
        amountVal.delegate=self
        tipVal.delegate=self
        peopleCount.delegate=self
        super.viewDidLoad()
        
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
