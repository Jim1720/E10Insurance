package p20.e10insurance.e10insurance.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Models.AppAlert;

// adding @controller caused other errors:investigate 
public class AlertController {

    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;
    
    @Autowired
    private SessionBean sessionBean; 

    
    @RequestMapping(value = "/alert", method = RequestMethod.GET)
    public String alert(Model m, @ModelAttribute AppAlert appAlert)
    {  
        // show serious error conditions by alert.html

        appAlert.message = sessionBean.GetMessage();
        appAlert.detailMessage = sessionBean.getMessageDetail();  
        sessionBean.SetRedirect("/appalert");
        return "layout";
    }

    
}
