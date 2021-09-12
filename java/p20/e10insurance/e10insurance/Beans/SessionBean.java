package p20.e10insurance.e10insurance.Beans;  
  
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
 
//import p20.e10insurance.e10insurance.Models.ScreenStyle; 
//import p20.e10insurance.e10insurance.Services.ScreenStyleFactory;

import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
 
import java.time.LocalDateTime;   
 
@Component
@SessionScope
public class SessionBean  
{ 
    /*  1. Tracks state (signedIn) to decide which links to display.
        2. Holds customer names to display when signed in.
        3. Holds non-default message to display for screens in 'message field'.
        4. Gets current date for splash screen.
        5. holds style instructions for screens.
        6. holds jwt  verification values
    */

    /* style logic description 

        1. no styling colors by default - black background 

        2. user clicks 'style' link and then 'color' link
           that causes a 'SceeenStyle' object to be registered for that screen
           o screen reloads with new values by two seperate sets of code:
             a. outline, solid, picture is read from html directly to set divClass.
                 th:if th:unless will set one of two divs.
             b. (outline, solid) color sent to html via hidden fields
                when screen (resets colors/style or reloads)
                and java script takes care of reading the hidden fields
                to apply the color changes.
                
        3. screen initial display will
           use a and b above to properly set screen colors.
        4. settins screen: default will clear all screen style 
           objets for a once again black background. 
           CSS variables set by java script:
           --user-color  ; background/border
           --label-color ; appropriate label color for background
           --h-color     ; header color best suited to background selected.
    */
 
    private boolean SignedIn;
    private boolean AdminSignedIn;
    private String CustomerFirstName;
    private String CustomerLastName;  
    private String Message; 
    public String Redirect;
    private String custId;
    private String adjustedId;
    private String adjustingId;
    private String copyClaimId;
    private String MessageDetail; 
    private boolean plansLoaded;
    private boolean servicesLoaded; 
    private String pass1; 
    private String token; 
    private String tokenHeaderName;

    @Value("${E10TokenHeaderName:notSet}")
    String E10TokenHeaderName; 

    SessionBean()
    {
        this.SignedIn = false;
        this.Message = "";
        this.Redirect = "";
        this.CustomerFirstName = "";
        this.CustomerLastName = ""; 
        this.plansLoaded = false;
        this.servicesLoaded = false; 
        this.token = "";
        this.tokenHeaderName = E10TokenHeaderName;
    } 
 
    
    public String GetCurrentDate()
    {
        // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd yyyy - HH:mm"); 
        LocalDateTime now = LocalDateTime.now();  
        String date = dtf.format(now);   
        return date;
    } 

    public String GetTokenHeaderName()
    {
        return this.tokenHeaderName;
    }
    

    public String GetRedirect()
    {
        // return rdirect and 'clear' field if true passed. allows reading first.

        String value = this.Redirect; 
       
        return value;
    }
    public void SetRedirect(String value)
    {
        this.Redirect = value;
    }

    public void SetMessage(String value)
    {
        this.Message = value;
    }

    public void SetMessageFirst(String value)
    {
        // used in admin only set the first error message
        // in series of edits - helper function - 
        if(this.Message.equals(""))
        {
            this.Message = value;
        }
    }

    public String GetMessage()
    {
        return this.Message;
    }

    public boolean GetSignedIn()
    {
        return SignedIn;
    }

    public boolean GetSignedOut()
    {
        return !SignedIn;
    }

    public void SetSignedIn(boolean SignedIn)
    {
        this.SignedIn = SignedIn;
        if(SignedIn == false)
        {
            this.CustomerFirstName = "";
            this.CustomerLastName = "";
        }
    }

    public void SetCustomerNames(String FirstName, String LastName)
    {
        this.CustomerFirstName = FirstName;
        this.CustomerLastName = LastName;
    }

    public String GetCustomerName()
    {
        return this.CustomerFirstName + " " + this.CustomerLastName;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getCustId()
    {
        return custId;
    }

    public String getAdjustedId()
    {
        return adjustedId;
    }

    public void setAdjustedId(String adjustedId)
    {
        this.adjustedId = adjustedId;
    }

    public String getAdjustingId()
    {
        return adjustingId;
    }

    public void setAdjustingId(String adjustingId)
    {
        this.adjustingId = adjustingId;
    }

    public void setMessageDetail(String messageDetail)
    {
        this.MessageDetail = messageDetail;
    }

    public String getMessageDetail()
    {
        return MessageDetail;
    } 
 

    public void setPlansLoaded(boolean value)
    {
        plansLoaded = value;
    }

    public boolean  arePlansLoaded()
    {
        return plansLoaded;
    }

    public void setSevicesLoaded(boolean value)
    {
        servicesLoaded = value;
    }

    public boolean areServicesLoaded()
    {
        return servicesLoaded;
    }

    public void setCopyClaimId(String claimId)
    {
        this.copyClaimId = claimId;
    }

    public String getCopyClaimId()
    {
        return this.copyClaimId;
    }
 
    public void setAdminSignedIn(boolean signedin)
    {
        this.AdminSignedIn = signedin;
    }

    public boolean isAdminSignedIn()
    {
        return this.AdminSignedIn;
    }

    public void setPass1(String pass1)
    {
        this.pass1 = pass1;
    }

    public String getPass1()
    {
        return pass1;
    }
 
    public void setToken(String token)
    {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }

   
}