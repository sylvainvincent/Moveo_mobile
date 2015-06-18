package fr.moveoteam.moveomobile.menu;

/**
 * Classe métier pour les elements du menu (Titre, icônes et compteur pour les nouveaux elements)
 * Created by Sylvain on 27/04/15.
 */
public class MenuItems {
    private String title;
    private int icon;
    private String count = "0";
    // Permet d'afficher le compteur (true)
    private boolean isCounterVisible = false;

    public MenuItems(){}

    public MenuItems(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public MenuItems(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
