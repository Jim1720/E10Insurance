package p20.e10insurance.e10insurance.Controllers; 
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity; 
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate; 

import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.PassedParameters.AdminParm;
import p20.e10insurance.e10insurance.Beans.EditBean;

@Controller
public class AdminController {
 

    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix; 
    
    @Autowired
    RestTemplate restTemplate; 
 

    @Autowired
    SessionBean sessionBean;

    @Autowired
    EditBean editBean;
      
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admSignin(Model model, 
            @RequestParam(required = false)  String message,
            @ModelAttribute AdminParm adminParm)
    {   
        var defaultMessage = "Enter Administrator Id and Password to signin.."; 
        var noEditMessage = message == null || message.equals("");
        var useMessage = noEditMessage ? defaultMessage : message;
        sessionBean.SetMessage(useMessage); 

        model.addAttribute("adminparm", adminParm);
        sessionBean.SetRedirect("/admin");

        return "layout";
    } 

    @RequestMapping(value = "/admin", method = RequestMethod.POST)  
    public String adminProcess(Model model, 
    @ModelAttribute AdminParm adminParm)  
    {

        String adminId =   adminParm.getAdminid();
        String adminPass = adminParm.getAdminpassword();
        String empty = ""; 
        sessionBean.SetMessage("");

       if(adminId.equals(empty))  
       {
           sessionBean.SetMessageFirst("Please enter your Administration Id."); 
       } 
       
       if(adminPass.equals(empty))  
       {
           sessionBean.SetMessageFirst("Please enter your password.");  
       }   

        // - - - - security only allow valid symbols

        if(!editBean.ValidData(adminId, EditBean.dataFieldRequirements.name1))
        {
            sessionBean.SetMessageFirst("Invalid Admin Id. Alphanumeric only."); 
        }
 
        if(!editBean.ValidData(adminPass, EditBean.dataFieldRequirements.name1))
        {
            sessionBean.SetMessageFirst("Invalid Administration Password. Alphanumeric only."); 
        }

        var errors1 = sessionBean.GetMessage();
        if(!errors1.equals(""))
        {
            var message = errors1;
            model.addAttribute("adminparm", adminParm);
            sessionBean.SetMessage(message);
            return "layout";
        }

          // valid credentials

         //*= = = = = = = call server to check valid credentials - - - -  
        String prefix = urlPrefix; 
        String url = prefix + "/adminsignin"; 
 
        ResponseEntity<String> response = null; 
        try
        {  
            // due to this being a get let's put parm in the header 
            // other parms in this app do updates! a get is done here.
             
            var headers = new LinkedMultiValueMap<String,String>();  
            headers.set("E20Id",adminId);  
            headers.set("E20Pass",adminPass); 
            // - - - - - - -  - - - - - - - - - - - - - - - - - - - - -
             
            try 
            {
                HttpEntity<String> request = new HttpEntity<String>("hello", headers);  
                response = restTemplate.exchange(url,HttpMethod.GET, request ,String.class);
                
                // not found means did not match or missing....
                // ok means matched 
                if(response.getStatusCode() != HttpStatus.OK)
                {
                    // not found goes to eception below this is bad status.
                    var message = "Bad status returned " + response.getStatusCode().toString();
                    model.addAttribute("adminparm", adminParm);
                    sessionBean.SetMessage(message);
                    return "layout";
                } 
            }
            catch (Exception e)
            {
                // expiration 
                var message = "Invalid Administrator Credentials.";
                model.addAttribute("adminparm", adminParm);
                sessionBean.SetMessage(message);
                return "layout";
            }
        }
        catch(HttpClientErrorException e)
        {
            var message = "Invalid Administrator Credentials";
            model.addAttribute("adminparm", adminParm);
            sessionBean.SetMessage(message);
            return "layout";
        }
        catch(Exception e)
        {

            if(response == null)
            {
                 // can not connect to server.
                var message = "Server is unavailable - contact Administrator. condition:null";
                model.addAttribute("adminparm", adminParm);
                sessionBean.SetMessage(message);
                return "layout";
            }

            // can not connect to server.
            var message = "Server is down. Please contact administrator. Thank you.";
            model.addAttribute("adminparm", adminParm);
            sessionBean.SetMessage(message);
            return "layout";

        }       

        //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =

        

         // no errors - set signed in flag and redirect to /adminaction link 

         // 'register' and 'signin' store token. 
         var headers2 = response.getHeaders();
         List<String> item = headers2.get("E20Token");
         String token = "";
         try {

             token =  item.get(0);

         }
         catch(Exception e)
         {
             // todo : change to error screen
            return "redirect:/error";
         }
         if(token != null && !token.equals(""))
         { 
             sessionBean.setToken(token);
         }
  
       // set adm signed in flag.
       sessionBean.setAdminSignedIn(true);  

       // redirect to external link that will
       // call Adminaction Controller
       return "redirect:/adminaction";
    } 

}
