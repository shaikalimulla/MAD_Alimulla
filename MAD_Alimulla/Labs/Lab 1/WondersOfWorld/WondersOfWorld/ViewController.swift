//
//  ViewController.swift
//  WondersOfWorld
//
//  Created by Ali on 9/6/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    var imageTracker = 0
    
    @IBOutlet weak var displayPic: UIImageView!
    
    @IBOutlet weak var infoMsg: UILabel!
    
    @IBAction func chooseArt(sender: UIButton) {
        infoMsg.text = ""
        
        if let title = sender.currentTitle {
            switch title {
            case "ChichenItza":
                displayPic.image=UIImage(named: "ChichenItza.jpg")
                imageTracker = 1
                
            case "Christ Statue":
                displayPic.image=UIImage(named: "Christ.jpg")
                imageTracker = 2
                
            case "Colosseum":
                displayPic.image=UIImage(named: "Colosseum.jpg")
                imageTracker = 3
                
            case "Great Wall":
                displayPic.image=UIImage(named: "greatWallOfChina.jpg")
                imageTracker = 4
                
            case "MachuPicchu":
                displayPic.image=UIImage(named: "MachuPicchu.jpg")
                imageTracker = 5
                
            case "PetraJordan":
                displayPic.image=UIImage(named: "PetraJordan.jpg")
                imageTracker = 6
                
            case "Taj Mahal":
                displayPic.image=UIImage(named: "Tajmahal.jpg")
                imageTracker = 7
                
            default:
                imageTracker = 0
                infoMsg.text = ""
                
            }
        }
    }
    
    
    @IBAction func info(sender: UIButton) {
        
        if sender.currentTitle=="Info" {
            switch imageTracker{
            case 0:
                infoMsg.text = "This is Taj Mahal"
                
            case 1:
                infoMsg.text = "This is Pyramid at Chichén Itzá"
                
            case 2:
                infoMsg.text = "This is Christ Redeemer"
              
            case 3:
                infoMsg.text = "This is Roman Colosseum"
                
            case 4:
                infoMsg.text = "This is Great wall of China"
                
            case 5:
                infoMsg.text = "This is Machu Picchu at Peru"
                
            case 6:
                infoMsg.text = "This is Petra at Jordan"
                
            case 7:
                infoMsg.text = "This is Taj Mahal"
                
            default:
                infoMsg.text = "This is Taj Mahal"
            }
        }
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

