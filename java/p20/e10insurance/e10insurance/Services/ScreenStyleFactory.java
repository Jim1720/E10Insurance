package p20.e10insurance.e10insurance.Services; 
import java.util.List; 
import org.springframework.stereotype.Service; 
import p20.e10insurance.e10insurance.Models.ScreenStyle; 

@Service 
public class ScreenStyleFactory {
    
     // creates and modifies ScreenStyle objects 

     // css class values to change div characteristics to show
     // picture, outline or solid colors.
   
 
    List<String> cssStyle = List.of ( "bg-style", "bg-image", "bg-outline", "bg-solid" );
    // 
    //                      
    int styleIndex = 0;
    final int STYLE_MAX = cssStyle.size();
    // 
    List<String> internalStyle =  List.of ("none", "picture", "outline", "solid");
    //
    // click 'color' link to cycle to next color
    List<String> userColors =  List.of (
      "white","red","pink",
      "blue","aqua", "yellow", "green", 
      "lawngreen","gold", "goldenrod");
    //                      
    int colorIndex = 0;
    final int COLOR_MAX = userColors.size();

    // 
    //  Keep these in sync with the color array as the are referenced together.
    //
    // apply if solid  
    List<String> headerColors =  List.of (
    "black","white","red",
    "white","blue", "black", "white", 
    "black","brown", "brown");

    // apply if solid only  
    List<String> labelColors =  List.of (  
    "black","white","red",
    "white","blue", "black", "white", 
    "black","brown", "brown"); 

      // message colors
      List<String> messageColors =  List.of (  
        "black","white","red",
        "white","blue", "black", "white", 
        "black","brown", "brown"); 

    // defaults
    String defaultUserColor = "white";
    String defaultLabelColor = "dodgerblue";
    String defaultHeaderColor = "burleywood";
    String defaultMessageColor = "burleywoood";
    String white = "white";
    
    // create new screen style
    public ScreenStyle CreateScreenStyleObject(String screenName)
    { 
        ScreenStyle s =  new ScreenStyle();
        s.setScreenName(screenName);  
        s.setDivClass("bg-image");
        s.setDivClassInternal("picture"); 
        //
        s.setUserColor(white);
        s.setLabelColor(white);
        s.setHeaderColor(white);
        s.setMessageColor(white);  
        return s;
    }

    private void setColors(ScreenStyle s, int index)
    {

        // updates all colors in ScreenStyleObject from current color index.
           
        var labelColor = labelColors.get(index);
        var headerColor = headerColors.get(index); 
        var messageColor = messageColors.get(index);
        var userColor = userColors.get(index); 

        s.setUserColor(userColor);
        s.setLabelColor(labelColor);
        s.setHeaderColor(headerColor);
        s.setMessageColor(messageColor); 
    } 
 
    private int FindNextStyleIndex(int index)
    {
        int value = index + 1;
        if (value == STYLE_MAX)
        {
            value = 0;
        }
        return value;
   
    }

    private int FindNextColorIndex(int index)
    {
        int value = index + 1;
        if (value == COLOR_MAX)
        {
            value = 0;
        }
        return value;
    }

    // update screen style when user clicks links for style or color 
    // called from sessionBean
    public void ModifyScreenStyleObject(ScreenStyle s,
                 String linkAction) // nextColor, nextStyle 
                  
    {
       
        var nextStyleClicked = "nextStyle"; 
        var nextColorClicked = "nextColor";

        if(linkAction.equals(nextStyleClicked)) 
        { 

                // find next Internal Style
                String divStyle = s.getDivClassInternal();
                int index = internalStyle.indexOf(divStyle);
                int nextIndex = FindNextStyleIndex(index);
                var nextStyle = internalStyle.get(nextIndex);
                s.setDivClassInternal(nextStyle);

                // update matching external style 'bg-something' etc.
                var nextCSSstyle = cssStyle.get(nextIndex);   
                s.setDivClass(nextCSSstyle); 
                // set colors based on style selected
                switch(nextStyle) {

                    case "none" :
                             s.setUserColor(defaultUserColor);
                             s.setLabelColor(defaultLabelColor);
                             s.setHeaderColor(defaultHeaderColor);
                             s.setMessageColor(defaultMessageColor); 
                             break;
                    case "picture" :
                            var white = "white";
                            s.setUserColor(white);
                            s.setLabelColor(white);
                            s.setHeaderColor(white);
                            s.setMessageColor(white); 
                            break;
                    case "outline" :
                            s.setUserColor(defaultUserColor);
                            s.setLabelColor(defaultLabelColor);
                            s.setHeaderColor(defaultHeaderColor);
                            s.setMessageColor(defaultMessageColor); 
                            break;
                     case "solid" :  
                            int first = 0;
                            setColors(s, first);
                            break;
                    default: break;

                } 

        }

        // if the style was changed to SOLID or OUTLINE
        // set the inital color in the list above

        if(linkAction.equals(nextColorClicked))
        {
             String Outline = "outline";
             String internalClass = s.getDivClassInternal(); 
             String currentUserColor = s.getUserColor();
             int index = userColors.indexOf(currentUserColor);
             int nextIndex = FindNextColorIndex(index);  
             //
             if(internalClass.equals(Outline))
             {
                // just change border : leave header , messages , labels at default colors
                // for outline style.
                var userColor = userColors.get(nextIndex); 
                s.setUserColor(userColor); 
             }
             else
             {  
                // change labels, header, message and background  
                setColors(s, nextIndex); 
             } 
         
        } 
    } 
}
