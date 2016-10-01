//
//  ViewController.swift
//  favorites
//
//  Created by Ali on 9/27/16.
//  Copyright © 2016 Apress Inc. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    
    @IBOutlet weak var titleLabel: UILabel!
    
    @IBOutlet weak var authorLabel: UILabel!
    
    @IBOutlet weak var genreLabel: UILabel!
    
    @IBOutlet weak var descLabel: UILabel!
    
    @IBOutlet weak var itemButton: UIBarButtonItem!

    var user=Favorite()
    
    @IBAction func tapBarButton(sender: UIBarButtonItem) {
        performSegueWithIdentifier("ConnectTo", sender: itemButton)
    }
    
    @IBAction func saveDetails(segue:UIStoryboardSegue) {
        titleLabel.text = user.favBook
        authorLabel.text = user.favAuthor
        genreLabel.text = user.genre
        descLabel.text = user.desc
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

