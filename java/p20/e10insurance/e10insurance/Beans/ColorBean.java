package p20.e10insurance.e10insurance.Beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


import p20.e10insurance.e10insurance.Models.ScreenStyle; 
import p20.e10insurance.e10insurance.Services.ScreenStyleFactory;

/* documentation : 

    1. creates style objects for each screen when style is clicked.
       before that the screen defaults to normal display.
    2. once the style object is created styles are 'cycled'
       through picture, color-outline, color-sold and back to none = no changes.
    3. The form div calls colorBean.getStyleClass to set the forms' div
       style to show a picture, outline or solid background as selected.
       
    4. id=userColor is set to the current screen object user color

    5. When the class is 'bg-outline' or 'bg-solid' the javascript 'claimpage.js'
       will set the CSS color variables from color values loaded in
       id=userColor div value field. Same for label and header colors which
       are selected by table values for best display values.

    6. added enviornment variable to turn styles on/off

*/

@Component
@SessionScope 
public class ColorBean { 

    @Autowired 
    SessionBean sessionBean;

    @Autowired 
    ScreenStyleFactory screenStyleFactory; 

    @Value("${E10UseStyles:notSet}")
    String useStyles;
     
    private List<ScreenStyle> screenStyleList;  
    List<String> supportedStyleScrens; 
    private final String OUTLINE = "outline";
    private final String SOLID = "solid"; 

    ColorBean()
    {
        screenStyleList = new ArrayList<ScreenStyle>();  
        supportedStyleScrens = List.of("/update","/claim");  
    }
 

    public void setStyle(String screenName, String linkAction)
    {
        // a. gets called by LinkController when user clicks style/color link

        // maintains a list of syles/colors for screens.
        // 1. link clicked at top will change screen color/picture 
        // 2. settings can store/load specific group or resetore defaults.

        var found = false;
        var index = 0;
        for(ScreenStyle s: screenStyleList)
        {
            var screen = s.getScreenName(); 
            if(screen.equals(screenName))
            {
                 // - - - - - - modify existing entry - - - - - - 
                 // - - - - - - when user clicks a link
                 // - - - - - - controller will check before screen display
                 // - - - - - - special screen id's required for activation
                 screenStyleFactory.ModifyScreenStyleObject(s, linkAction);
                 // replace Streen style
                
                 // todo: replace screens style object
                 screenStyleList.set(index,s); // replace element in list. 
                 // 
                // dumpStyleList("style list replaced: " + screenName); 
                 // todo: mark found.
                 found = true;
                 break; // exit loop.
            }
            index = index + 1;
        }
        // - - - - - add new entry - - - - - - - - - - - - - - - 
        if(!found)
        {
            ScreenStyle screenStyle = screenStyleFactory.CreateScreenStyleObject(screenName);   
            screenStyleList.add(screenStyle);
            // dumpStyleList("style list added: " + screenName);
        }

        // e. reset the links on the screen 
        

    }   

    public void dumpStyleList(String tag) 
    {
        int i = 0;
        System.out.println("----- screen style list ------ ");
        for(ScreenStyle s: screenStyleList)
        {
            i++;
            System.out.println(i);
            System.out.println(tag);
            System.out.println(s.getScreenName());
            System.out.println(s.getDivClassInternal()); 
            System.out.println(" ");
        }
    }

    public void ClearScreenStyles()
    {
        // reset all screen styles to orignal values.
        screenStyleList.clear();
    }

    // d. link control 
    public boolean showStyleLink()
    {

        var notFound = -1;
        // check screen to see if style is used on that screen.
        var currentScreen = sessionBean.GetRedirect();

        // check environment variable to allowStyles 
        var YesItDoes= "Y";
        var stylesAllowed = useStyles.toUpperCase().equals(YesItDoes);

        // todo : future turn this on to limit links to screens
        var onSupportedScreen = supportedStyleScrens.indexOf(currentScreen) > notFound; 
        // 
        var result = onSupportedScreen && stylesAllowed;
        //
        return result;   
    }

    public boolean showColorLink()
    {

        // check screen to see if style is used on that screen.
        //var currentScreen = this.Redirect;
        //List<String> supportedStyleScrens = 
        //List.of("Update","Register","Claim");
        // todo : future turn this on to limit links to screens
        //var onSupportedScreen = supportedStyleScrens.indexOf(currentScreen) > -1; 

        // not on styled screen
        if(!showStyleLink())
        {
            return false;
        }

        ScreenStyle s = FindCurrentStyle();  
        if (s == null) return false;
        var divClassIntenal = s.getDivClassInternal();
        var show = divClassIntenal.equals("solid") 
                   ||
                   divClassIntenal.equals("outline");
        return show; 
    }

    public String getStyleLink()
    {
        
        ScreenStyle s = FindCurrentStyle(); 
        if (s == null) return "Style"; // no style set.
        var divClassIntenal = s.getDivClassInternal(); 
        var value = ""; // no styles applied - default
        switch(divClassIntenal)
        {
            case "outline" : value = "Outline"; break;
            case "solid"   : value = "Solid"; break;
            case "picture" : value = "Picture";  break;
            case "none"    : value = "Style"; break;
        } 
        return value;
    }


    public String getColorLink()
    {
        // if visible this is the color to show = UserColor.
        ScreenStyle s = FindCurrentStyle();  
        if(s == null) return "White";
        var color = s.getUserColor();
        return color;
    }

  
   

    // b. called by controller to set div fields
    //    java script will read those fields and
    //    set color properties. 
    

    public String haveColors()
    {
        ScreenStyle s = FindCurrentStyle();
        if( s == null) return "no";
        var style = s.getDivClassInternal();
        // colors only valid for SOLID or OUTLINE style settings.
        return (style.equals(SOLID) || style.equals(OUTLINE)) ? "yes" : "no";
        
    }

    public String getUserColor()
    {
        ScreenStyle s = FindCurrentStyle();
        
        if( s == null) return "white";
        return s.getUserColor();
    }

    public String getLabelColor()
    {
        ScreenStyle s = FindCurrentStyle(); 
        if( s == null) return "dodgerblue";
        return s.getLabelColor();
        
    }

    public String getHeaderColor()
    {
        ScreenStyle s = FindCurrentStyle(); 
        if( s == null) return "goldenrod";
        return s.getHeaderColor();

    }

    public String getMessageColor()
    {
        ScreenStyle s = FindCurrentStyle(); 
        if( s == null) return "burleywood";
        return s.getMessageColor();

    }

    public String forControllersGetDivClassInternal()
    {
        // called by controllers
        ScreenStyle s = FindCurrentStyle();
        return s.getDivClassInternal();
    }

    private ScreenStyle FindCurrentStyle()
    {
        var currentScreenName = sessionBean.GetRedirect();
        ScreenStyle foundStyle = null;
        for(ScreenStyle s: screenStyleList)
        {
            if (s.getScreenName().equals(currentScreenName))
            {
                foundStyle = s;
            } 
        } 
        return foundStyle;
    }

    
     // c. gets called by html fragments to set div and color values.
 
     public boolean isClassSet()
     {

         // call me first to see if class set to something
         boolean classSet = false;
        // called directly from html to set div class for picture display.
        String currentScreenName = sessionBean.GetRedirect();
        for(ScreenStyle s: screenStyleList)
        {
            if (s.getScreenName().equals(currentScreenName))
            {

                if(s.getDivClassInternal().equals(OUTLINE) 
                     ||
                   s.getDivClassInternal().equals(SOLID)) 
                { 
                    classSet = true; 
                }
            } 
        } 
        return classSet;
     } 

     public String getFirstClass()
     {
         return "none";
     }

     public String getFirstClassInternal()
     {
         return "none";
     }

     public String getStyleClass()
     {
        ScreenStyle s = FindCurrentStyle();  
        if(s == null) return "bg-style";
        var externalCSSclass = s.getDivClass();
        return externalCSSclass;
     }
 

}
