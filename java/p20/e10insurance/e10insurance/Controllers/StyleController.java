package p20.e10insurance.e10insurance.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import p20.e10insurance.e10insurance.Beans.ColorBean;
import p20.e10insurance.e10insurance.Beans.SessionBean;   

@Controller
public class StyleController {

    
    @Autowired
    SessionBean sessionBean; 

    @Autowired
    ColorBean colorBean;
      

    @RequestMapping(value = "/style", method = RequestMethod.GET)  
    public String Style(Model model) 
    { 
         var screen = sessionBean.GetRedirect(); // do not clear rdirect field.
         if(!colorBean.showStyleLink())
         {
             return "layout";
         }
         var action = "nextStyle";    
         colorBean.setStyle(screen, action);
         // set redirect for controllers 
         sessionBean.SetRedirect(screen);
         // redirect back to current screen so it's controller can be called to
         // load screen model.
         var destination = "redirect:" + screen;
         return destination; 
    }

    
    @RequestMapping(value = "/color", method = RequestMethod.GET)  
    public String Color(Model model) 
    {
        String screen = sessionBean.GetRedirect(); // do not clear rdirect field.
        String action = "nextColor";     
        colorBean.setStyle(screen, action);
         // redirect back to current screen so it's controller can be called to
         // load screen model.
         var destination = "redirect:" + screen;
         return destination;
    } 
    
}
