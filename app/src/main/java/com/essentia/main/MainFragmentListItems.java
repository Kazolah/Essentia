package com.essentia.main;

/**
 * Created by kyawzinlatt94 on 2/9/15.
 */
public class MainFragmentListItems {
    public int icon1, icon2, icon3, icon4, icon5;
    public String description;
    public String selectedDescription;

    public MainFragmentListItems(String description, String selectedDescription,
                                 int icon1, int icon2, int icon3, int icon4, int icon5){
        this.icon1 = icon1;
        this.icon2 = icon2;
        this.icon3 = icon3;
        this.icon4 = icon4;
        this.icon5 = icon5;
        this.description = description;
        this.selectedDescription = selectedDescription;
    }
}
