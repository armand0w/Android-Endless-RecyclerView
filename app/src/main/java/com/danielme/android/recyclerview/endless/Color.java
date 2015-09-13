package com.danielme.android.recyclerview.endless;


/**
 * @author danielme.com
 */
public class Color extends Item{

    private String name;
    private String hex;


    public Color(String name, String hex) {
        this.hex = hex;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
