package p20.e10insurance.e10insurance.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import p20.e10insurance.e10insurance.Beans.SessionBean;

@Controller
public class MenuController {

    @Autowired
    private SessionBean sessionBean; 

    // shows messages prefixed by asterisk only.

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String ShowMenu()
    {
        // explaination: we want success and system error messages to show up
        // on the other hand edit messages should not so we use * to hide the
        // edit messages.
        var message = sessionBean.GetMessage();
        var nothing = "";
        final var MINIMUM_LENGTH = 5; // arbitrary number!
        final var HIDE_INDICATOR = "*";
        if(message != null && !message.equals(""))
        {
            if(message.length() > MINIMUM_LENGTH && message.startsWith(HIDE_INDICATOR))
            {
                message = nothing; // if message has (*) suppress it.
            } 

        }
        else
        {
            message = nothing;
        }

        sessionBean.SetMessage(message);
        sessionBean.SetRedirect("/menu");
        return "layout";

    }
    
}
