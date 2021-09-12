package p20.e10insurance.e10insurance.Controllers;

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
import org.springframework.web.client.RestTemplate;

import p20.e10insurance.e10insurance.Beans.EditBean;
import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Models.AdminAction; 

import p20.e10insurance.e10insurance.PassedParameters.ResetPassword;
import p20.e10insurance.e10insurance.PassedParameters.ResetCustomer;

@Controller
public class AdminActionController 
{   
    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SessionBean sessionBean;

    @Autowired
    EditBean editBean;

    @Autowired
    AdminAction adminaction; 

    @Autowired
    ResetPassword resetPassword;

    @Autowired
    ResetCustomer resetCustomer;
 

    @RequestMapping(value = "/adminaction", method = RequestMethod.GET )
    public String ShowAdminActions(Model model, @ModelAttribute AdminAction adminaction)
    {
        model.addAttribute("adminaction", adminaction); 
        sessionBean.SetRedirect("/adminaction"); 
        return "layout";
    }

    @RequestMapping(value = "/adminaction", method = RequestMethod.POST)  
    public String ActionProcess(Model model, @ModelAttribute AdminAction adminaction) 
    { 
 
        if(!sessionBean.isAdminSignedIn())
        {
           // shows must be signed in screen with
           // a button to return to start 
           // valid signin check  
           sessionBean.SetRedirect("/unauth");  
           return "layout";
        }  

         

        String Action = adminaction.getButtonAction();
        String message = "";

        if(Action.equals("customerlist"))
        {
            sessionBean.SetMessage(""); 
            return "redirect:/listcustomers";  
        }

        switch(Action)
        { 
            case "resetpassword":
                 message =  ResetPassword(adminaction);
                 break;
            case "resetcustomer": 
                 message =  ResetCustomer(adminaction);
                 break; 
        }
            
          
         sessionBean.SetMessage(message); 
         var succeeded = message.contains("Success");
         if(!succeeded)
         {
               model.addAttribute("adminaction", adminaction);  // keep values on screen
               return "layout";
         }
         else
         {

             // clear screen 
             return "redirect:/adminaction"; 

         }
       
    } 

   
    private String ResetPassword(AdminAction adminaction)
    {

        // custId newPassword NewPassword2

        var custId = adminaction.getCurrentCustomer();
        var pass = adminaction.getNewPassword();
        var pass2 = adminaction.getNewConfirmPassword();
        var empty = "";

        if(custId.equals(empty))  
        { 
            return "Enter customer id.";
        }  
        
        if(pass.equals(empty))  
        { 
            return "missing password";
        }   

        if(pass2.equals(empty))  
        { 

            return "missing confirm password";
        }   
 
        // - - - - security only allow valid symbols
 
        if(!editBean.ValidData(custId, EditBean.dataFieldRequirements.name1))
        {
             return "customer id must be alpha numeric";
        }
 
        if(!editBean.ValidData(pass, EditBean.dataFieldRequirements.passadminactions))
        {
            return "invalid passsword"; 
        }

        if(!editBean.ValidData(pass2, EditBean.dataFieldRequirements.passadminactions))
        { 
            return "invalid confirm password"; 
        }
         
        if(!pass.equals(pass2))
        { 
            return "confirmation password does not match password";
        }

        var message = ResetPasswordOperation(adminaction);

        return  message;
    }

    private String ResetCustomer(AdminAction adminaction)
    {

         // new currCustId custId newCustId2

         var custId = adminaction.getCurrentCustomer();
         var newCust = adminaction.getNewCustomer();
         var newCust2 = adminaction.getNewConfirmCustomer(); 
         var empty = "";
 
         if(custId.equals(empty))  
         { 
             return "Enter customer id.";
         }   
         
         if(newCust.equals(empty))  
         { 
             return "Enter new customer id.";
         }  

         if(newCust2.equals(empty))  
         { 
             return "Enter new customer id confirm.";
         }  

         if(!editBean.ValidData(custId, EditBean.dataFieldRequirements.name1))
         {
              return "current customer id must be alpha numeric";
         }

         if(!editBean.ValidData(newCust, EditBean.dataFieldRequirements.name1))
         {
              return "new customer id must be alpha numeric";
         }

         if(!editBean.ValidData(newCust2, EditBean.dataFieldRequirements.name1))
         {
              return "confirmation customer id must be alpha numeric";
         }

         if(!newCust.equals(newCust2))
         { 
             return "confirmation new customer id  does not match new customer id";
         }
          
         var message = ResetCustomerOperation(adminaction);
         
         return message;
    }


    public String ResetPasswordOperation(AdminAction action)
    {
         //*= = = = = = = call server to add custome and check results - - - -  
         String prefix = urlPrefix;
         String url = prefix + "/resetpassword";

         var custId = action.getCurrentCustomer(); 
         var newPassword = action.getNewPassword(); 

         resetPassword.setCustId(custId); 
         resetPassword.setNewPassword(newPassword);
         
         try {

        // reset password  token - - - - - - - - - - - - - - - - - - - - - 
         var token = sessionBean.getToken();
         var headers = new LinkedMultiValueMap<String,String>();  
         headers.set("E20Token",token); 

         HttpEntity<ResetPassword> request = new HttpEntity<ResetPassword>(resetPassword, headers);  
         // - - - - - - - - - - - - - - - - - - - - - - - -  

         ResponseEntity<ResetPassword> response;

         try
         {  
            response = restTemplate.exchange(url,HttpMethod.PUT,request,ResetPassword.class);
         
            if(response.getStatusCode() != HttpStatus.OK)
            {
                return "bad status code: " + response.getStatusCode().toString();
            } 

        } catch(Exception e)
        {
             // tijeb tune iyt
             return "Bad Request - Possible Timeout.";
        }


         resetPassword = response.getBody();
         if(resetPassword.isEditError())
         {
             return resetPassword.getMessage();
         }
         else
         {
             return "Password Reset Successfully.";
         }


         } catch(Exception e)
         {
             return "Password Reset Failed: " + e.getMessage();
         }
         //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =
     
          
    }

    public String ResetCustomerOperation(AdminAction adminaction)
    {
         //*= = = = = = = call server to add custome and check results - - - -  
         String prefix = urlPrefix;
         String url = prefix + "/resetcustomer";

         // pass old and new customer id's the api may return a old id customer = not found edit.

         var newCustId = adminaction.getNewCustomer();
         var curCustId = adminaction.getCurrentCustomer();

         resetCustomer.setNewCustId(newCustId);
         resetCustomer.setCurCustId(curCustId);
         
         try 
         { 
                  // update token - - - - - - - - - - - - - - - - - - - - - 
            var token = sessionBean.getToken();
            var headers = new LinkedMultiValueMap<String,String>();  
            headers.set("E20Token",token); 
            HttpEntity<ResetCustomer> request = new HttpEntity<ResetCustomer>(resetCustomer, headers); 
            // - - - - - - - - - - - - - - - - - - - - - - - -   

            ResponseEntity<ResetCustomer> response;
            try
            { 
                response = restTemplate.exchange(url,HttpMethod.PUT,request,ResetCustomer.class);
                if(response.getStatusCode() != HttpStatus.OK)
                {
                    return "bad status code: " + response.getStatusCode().toString();
                } 
            }
            catch(Exception e)
            {
                return "Bad Request or Timeout";
            }

            resetCustomer = response.getBody();
            if(resetCustomer.isEditError())
            {
                return resetCustomer.getMessage();
            }
            else
            {
                return "Customer Reset Successfully.";
            } 
         }
         catch(Exception e)
         {
                var message = "Reset Failed exceptional message is: " + e.getMessage();
                return message;

         } 
         //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =
     
    }
    

}
