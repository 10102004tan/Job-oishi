package com.datto.demo_android.models;

public class InforProfileAdd {
    private String title;
    private String description;
    private String buttonTitle;
    private String icon;

    public InforProfileAdd(String title, String description, String buttonTitle, String icon) {
        this.title = title;
        this.description = description;
        this.buttonTitle = buttonTitle;
        this.icon = icon;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }
}
