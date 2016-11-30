package com.example.alimu.coffeefinder;

/**
 * Created by alimu on 11/10/2016.
 */

public class searchCoffeeShop {

    private String coffeeShop;
    private String coffeeShopURL;

    private void setCoffeeInfo(Integer coffeeCrowd){
        switch (coffeeCrowd){
            case 0:
                coffeeShop="Starbucks";
                coffeeShopURL="https://www.starbucks.com";
                break;
            case 1:
                coffeeShop="Amante";
                coffeeShopURL="https://www.amantecoffee.com";
                break;
            case 2:
                coffeeShop="Ozo";
                coffeeShopURL="https://ozocoffee.com";
                break;
            case 3:
                coffeeShop="Pekoe";
                coffeeShopURL="http://www.pekoesiphouse.com";
                break;
            case 4:
                coffeeShop="Trident";
                coffeeShopURL="http://www.tridentcafe.com";
                break;
            default:
                coffeeShop="none";
                coffeeShopURL="https://www.google.com/search?q=boulder+coffee+shops&ie=utf-8&oe=utf-8";
        }
    }

    public void setCoffeeShop(Integer coffeeCrowd){

        setCoffeeInfo(coffeeCrowd);
    }

    public void setCoffeeShopURL(Integer coffeeCrowd){

        setCoffeeInfo(coffeeCrowd);
    }

    public String getCoffeeShop(){

        return coffeeShop;
    }

    public String getCoffeeShopURL(){

        return coffeeShopURL;
    }
}
