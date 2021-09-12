package p20.e10insurance.e10insurance.Controllers; 
import p20.e10insurance.e10insurance.Beans.SessionBean; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping; 
 

@Controller
public class GeneralControllers { 
 
    /* topmost menu driver for app */ 

    //https://exceptionshub.com/why-is-my-spring-autowired-field-null.html

    @Autowired
    private SessionBean sessionBean;

    @RequestMapping(value = "/")
    public String splash() 
    {  
        sessionBean.SetRedirect("/");
        return "layout";
    }

    @RequestMapping(value = "/start")
    public String start() 
    {
        sessionBean.SetSignedIn(false);
        sessionBean.SetRedirect("/start");
        return "layout";
    }

    
    @RequestMapping(value = "/menu")
    public String menu() 
    { 
        sessionBean.SetRedirect("/menu");
        return "layout";
    }

    @RequestMapping(value = "/classic")
    public String classic() 
    { 
        sessionBean.SetRedirect("/classic");
        return "layout";
    }

    @RequestMapping(value = "/fullmenu")
    public String fullmenu() 
    { 
        sessionBean.SetRedirect("/fullmenu");
        return "layout";
    }

    @RequestMapping(value = "/info")
    public String info() 
    {
        
        sessionBean.SetRedirect("/info");
        return "layout";
    }

    @RequestMapping(value = "/settings")
    public String settings() 
    {
        
        sessionBean.SetRedirect("/settings");
        return "layout";
    }
 

    @RequestMapping(value = "/about")
    public String about() 
    {
        
        sessionBean.SetRedirect("/about");
        return "layout";
    }

   

    @RequestMapping(value = "/signout")
    public String signout() 
    { 
        sessionBean.SetCustomerNames("", "");
        sessionBean.SetSignedIn(false); 
        sessionBean.SetRedirect("/start");
        return "layout";
    }





}