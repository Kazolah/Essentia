package com.essentia.left_drawer;

import java.io.Serializable;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class NavigationListItems implements Serializable{
    public int icon;
    public String title;

    public NavigationListItems(){
        super();
    }

    public NavigationListItems(int icon, String title){
        super();
        this.icon = icon;
        this.title = title;
    }
}
