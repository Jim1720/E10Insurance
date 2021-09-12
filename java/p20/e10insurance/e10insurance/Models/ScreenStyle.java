package p20.e10insurance.e10insurance.Models;

import org.springframework.stereotype.Component;

@Component
public class ScreenStyle {

    // ScreenStyle - represets color and div class settings for one screen
    // header and label colors are used for best display viewing.

    private String screenName;
    private String divClass;
    private String divClassInternal;
    private String userColor;
    private String labelColor;
    private String headerColor; 
    private String messageColor;

    public ScreenStyle()
    {

    }


    public void setScreenName(String screenName)
    {
        this.screenName = screenName;
    }

    public String getScreenName()
    {
        return this.screenName;
    }

    public void setDivClass(String divClass)
    {
        this.divClass = divClass;
    }

    public String getDivClass()
    {
        return this.divClass;
    }

    public void setDivClassInternal(String divClassInternal)
    {
        this.divClassInternal = divClassInternal;
    }

    public String getDivClassInternal()
    {
        return this.divClassInternal;
    }


    public void setUserColor(String userColor)
    {
        this.userColor = userColor;
    }

    public String getUserColor()
    {
        return this.userColor;
    }

    public void setLabelColor(String labelColor)
    {
        this.labelColor = labelColor;
    }

    public String getLabelColor()
    {
        return this.labelColor;
    }

    public void setHeaderColor(String headerColor)
    {
        this.headerColor = headerColor;
    }

    public String  getHeaderColor()
    {
        return this.headerColor;
    }

    public void setMessageColor(String color)
    {
        this.messageColor = color;
    }

    public String getMessageColor()
    {
        return this.messageColor;
    }
 
    
}
