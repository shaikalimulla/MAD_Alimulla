//
//  ViewController.swift
//  Lab2
//
//  Created by Ali on 9/12/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var displayPic: UIImageView!
    
    @IBOutlet weak var titleInfo: UILabel!
    
    @IBOutlet weak var segmentInfo: UISegmentedControl!
    
    @IBOutlet weak var switchInfo: UISwitch!
    
    @IBOutlet weak var sliderInfo: UISlider!
    
    @IBOutlet weak var fontSizeLabel: UILabel!
    
    @IBOutlet weak var colorSegment: UISegmentedControl!
    
    func updatePic() {
        if segmentInfo.selectedSegmentIndex == 0 {
            titleInfo.text="Nokia 6110 Mobile Phone"
            displayPic.image=UIImage(named: "nokia")
        }
        else if segmentInfo.selectedSegmentIndex == 1 {
            titleInfo.text="iPhone 6s Smart Phone"
            displayPic.image=UIImage(named: "iphone")
        }
    }
    
    func updateFont() {
        if switchInfo.on {
            titleInfo.text=titleInfo.text?.uppercaseString
        } else {
            titleInfo.text=titleInfo.text?.lowercaseString
        }
    }
    
    func changeColor() {
        switch colorSegment.selectedSegmentIndex {
            case 0:
                titleInfo.textColor=UIColor.redColor()
            case 1:
                titleInfo.textColor=UIColor.greenColor()
            case 2:
                titleInfo.textColor=UIColor.blueColor()
            case 3:
                titleInfo.textColor=UIColor.orangeColor()
            case 4:
                titleInfo.textColor=UIColor.cyanColor()
            default:
                titleInfo.textColor=UIColor.redColor()
            }
    }
    
    @IBAction func updateImage(sender: UISegmentedControl) {
        updatePic()
        updateFont()
    }
    
    @IBAction func updateCase(sender: UISwitch) {
        updateFont()
    }
    
    @IBAction func updateFontSize(sender: UISlider) {
        let fontSize=sender.value //float
        fontSizeLabel.text=String(format: "%.0f", fontSize) //convert float to String
        let fontSizeCGFloat=CGFloat(fontSize) //convert float to CGFloat
        titleInfo.font=UIFont.systemFontOfSize(fontSizeCGFloat) //create a UIFont object and assign to the font property
    }
    
    @IBAction func updateColor(sender: UISegmentedControl) {
        changeColor()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

