package com.example.alimu.icecreamfinder;

/**
 * Created by alimu on 12/8/2016.
 */

public class searchIceCream {

    private String icecreamShop;
    private String icecreamShopURL;

    private void setCoffeeInfo(Integer flavor){
        switch (flavor){
            case 0:
                icecreamShop="Glacier";
                icecreamShopURL="http://www.glaciericecream.com/";
                break;
            case 1:
                icecreamShop="Sweet Cow";
                icecreamShopURL="http://www.sweetcowicecream.com/";
                break;
            case 2:
                icecreamShop="Fior di Latte";
                icecreamShopURL="http://fiordilattegelato.com/";
                break;
            default:
                icecreamShop="none";
                icecreamShopURL="https://www.google.com/#q=boulder+ice+cream+shops";
        }
    }

    public void seticecreamShop(Integer flavor){

        setCoffeeInfo(flavor);
    }
/*
    public void seticecreamShopURL(Integer coffeeCrowd){

        setCoffeeInfo(coffeeCrowd);
    }
*/
    public String geticecreamShop(){
        return icecreamShop;
    }

    public String geticecreamShopURL(){
        return icecreamShopURL;
    }
}
