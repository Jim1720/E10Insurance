package p20.e10insurance.e10insurance.Controllers;
  
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import p20.e10insurance.e10insurance.Beans.HistoryOperationBean;
import p20.e10insurance.e10insurance.Beans.SessionBean;  
import p20.e10insurance.e10insurance.Models.Claim; 
import p20.e10insurance.e10insurance.Models.History;
import p20.e10insurance.e10insurance.PassedParameters.PayData;

@Controller
public class HistoryController {

     //* Url Prefix  

     @Value("${E10UrlPrefix:notSet}")
     String urlPrefix;

     @Autowired
     private SessionBean sessionBean;    

     @Autowired
     RestTemplate restTemplate;

     @Autowired
     Claim claim;

     @Autowired
     History history;

     @Autowired
     PayData payData;

     @Autowired
     HistoryOperationBean historyOperationBean;
 
     
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String signin(Model model, @ModelAttribute History history)
    {   

        if(!sessionBean.GetSignedIn())
        {
            // shows must be signed in screen with
            // a button to return to start 
            // valid signin check  
            sessionBean.SetRedirect("/unauth");  
            return "layout";
        }


         //*= = = = = = = call server to add custome and check results - - - -  
         String prefix = urlPrefix; 
         String custId = sessionBean.getCustId();

         String url = prefix + "/history/" + custId; 
 
         HttpHeaders headers = new HttpHeaders();
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         HttpEntity <String> entity = new HttpEntity<String>(headers);
         ResponseEntity<Claim[]> response = null;
  

         try
         { 
 
             response = restTemplate.exchange(url,HttpMethod.GET,entity,Claim[].class);
             var error = response.getStatusCode() != HttpStatus.FOUND;
             if (error)
             {
                 var message = "Unable to read history: " + response.getStatusCode();
                 //model.addAttribute("Signin", signin);
                 sessionBean.SetMessage(message);
                 return "layout";
             } 
         }
         catch(HttpClientErrorException e)
         {
                //* - - - no claims - - - - - 
                sessionBean.SetMessage("No Claims Found");
                sessionBean.SetRedirect("/history");
                return "layout";

         }
         catch(Exception e)
         {
 
             if(response == null)
             {
                  // can not connect to server. 
                 var message = "unable to read history - 3.";
                 //model.addAttribute("Signin", signin);
                 sessionBean.SetMessage(message);
                 return "layout";
             }
 
             // can not connect to server.
             var message = "unable to read history - 4.";
             //model.addAttribute("Signin", signin);
             sessionBean.SetMessage(message);
             return "layout";
 
        }

        if(response.getBody() == null)
        {
             // can not connect to server.
             var message = "unable to read history - 5.";
             //model.addAttribute("Signin", signin);
             sessionBean.SetMessage(message);
             return "layout";
        }

        var focusedClaimId = historyOperationBean.getFocusClaim(); // from adjust or pay process
        var environmentFocusVariableOn = historyOperationBean.isFocusUsed(); 
        var screenFocusButtonOn = historyOperationBean.isFocusOn(); 

        Claim[] claims = response.getBody(); 
 

        for(Claim c : claims)
        {

            // trim claim id - for screen html compares 
            c.setClaimIdNumber(c.getClaimIdNumber().trim());

            var date = c.getDateService();
            String d = formatDate(date);
            c.setDateService(d);

            if(c.getClaimType().toUpperCase().equals("M"))
            {
                 date = c.getDateConfine();
                 d = formatDate(date);
                 c.setDateConfine(d);
                  

                 date = c.getDateRelease();
                 d = formatDate(date);
                 c.setDateRelease(d);
            }

           
            if(c.getClaimStatus().trim().equals("Adjusted"))
            {
                date = c.getAdjustedDate();
                d = formatDate(date);
                c.setAdjustedDate(d);
            }

            if(c.getClaimStatus().trim().equals("Paid"))
            {
                date = c.getPaymentDate();
                d = formatDate(date);
                c.setPaymentDate(d);
            }
            /* workaround since we added service to history but
               past claims have null field */
            if(c.getService() == null)
            {
                c.setService("was null");
            }

            // mark focused claim if:
            //  o focus environment variable is set Y
            //  o screen focus button set to 'focus on'
            //  o this claim matches focus claim id set on adjustment or pay routines

          

            var claimId =  c.getClaimIdNumber().toString().trim(); 

            Boolean normalFocusUsed = screenFocusButtonOn && environmentFocusVariableOn;
            Boolean payResetFocusUsed = historyOperationBean.isPayResetFocusedUsed(); 

            if(focusedClaimId.equals(claimId) 
            &&
            (normalFocusUsed || payResetFocusUsed) )
            { 
               
                c.setFocused(true); 
                if(payResetFocusUsed)
                {
                    historyOperationBean.setPayResetFocusUsed(false);
                }  
            } 
        } 
 
        // reset focused claim id off
        historyOperationBean.setFocusClaim("");

        // load screen display object - history 
        history.setHistoryClaims(claims);

        // history = claims + indexSelected; 
        model.addAttribute("history", history);

        String count = String.valueOf(claims.length);
        var message = "";
        switch(claims.length)
        {
            case 0: message = "No Claims Found."; break;
            case 1: message = "1 claim found."; break;
            default: message = count + " claims found."; break;
           
                 
        }
         
        sessionBean.SetMessage(message);
        sessionBean.SetRedirect("/history");
        return "layout";
    }

    private String formatDate(String value)
    {
        // yyyy-mm-dd   to    mmddyyyy

        var y = value.substring(0,4);
        var m = value.substring(5,7);
        var d = value.substring(8,10); 
        var slash = "/";
        var result = m + slash +  d + slash + y;
        // check unused 
        if(y.equals("1900") || y.equals("1753"))
        {
            result = "";
        }
        return result;
    }
    
    
    @RequestMapping(value = "/history", method = RequestMethod.POST) 
    public String adjustment(Model model, @ModelAttribute History history)
    {   

        if(!sessionBean.GetSignedIn())
        {
            // shows must be signed in screen with
            // a button to return to start 
            // valid signin check  
            sessionBean.SetRedirect("/unauth");  
            return "layout";
        } 

        var buttonAction = history.getButtonAction(); 
        

        if(buttonAction.equals(""))
        {
            // when invalid value keyed...

            // use focus so we can reposition at the claim being paid...
            sessionBean.SetRedirect("/history");
            return "layout";
        }

        if(buttonAction.equals("Adjusting"))
        { 
            var adjustedClaimId = history.getAdjustedClaimId().trim(); 
            // remove adjust prefix 
            sessionBean.setAdjustedId(adjustedClaimId);  
    
            // here we call a controler to process the adjustment 
            return "redirect:/adjustment";
        } 

        if(buttonAction.equals("PayReset"))
        {
            /* called from java script pay button logic
               when payment invalid or payment is cancelled.
               Java script clicks hidden id="PayReset"
               button so form will be reloaded and positioned
               at at the claim being paid using the focus logic.

               fix for: form was blanking data out after these actions.
            */ 
          
            // focusing ...
            var payClaimId = history.getPayClaimId();
            
            // set focus claim id to claim customer trying to pay...
            historyOperationBean.setFocusClaim(payClaimId);

            // trigger focus for this case: pay cancel or pay bad amount
            historyOperationBean.setPayResetFocusUsed(true);

            // back to history ... with focus in mind.
            //sessionBean.SetRedirect("/history");   /* to use focus logic */
            //return "layout";
            return "redirect:/history"; 
        }
      

        if(buttonAction.equals("Paying"))
        {
              var payClaimId = history.getPayClaimId();
              var payAmount = history.getPaymentAmount();
              var result = PayClaim(payClaimId, payAmount);
              if(!result)
              {
                var message = "Payment action unsuccessful.";
                sessionBean.SetMessage(message);  
                return "redirect:/history"; 
              }

              var message = "Claim " + payClaimId + " Paid."; 
              sessionBean.SetMessage(message);  
              
               // set focus claim id
               historyOperationBean.setFocusClaim(payClaimId);

               // set Action for history screen
               historyOperationBean.setAction("payment", payClaimId);

               // check Stay value and return to histroy if 'stay on'
               var stayOnHistorySelected = historyOperationBean.isStayOn();
               if(stayOnHistorySelected)
               {
                  // go back to history if stay is on.
                  return "redirect:/history";
               }  
              return "redirect:/menu"; 
        } 
        
         /* --stay action
        if(buttonAction.equals("Stay"))
        { 
            // update settings and return to history
            historyOperationBean.toggleStay(); 
            return "redirect:/history";
        } */
        
        /* --focus action
        if(buttonAction.equals("Focus"))
        { 
              update settings and return to history
            historyOperationBean.toggleFocus();
            return "redirect:/history"; 
        } */

        // should not get here
        var message = "action of :" + buttonAction + " is incorrect.";
        sessionBean.SetMessage(message);
        return "redirect:/history";
    }

    private boolean PayClaim(String id,String amount)
    {
            //*= = = = = = = call server to add custome and check results - - - -  
            String prefix = urlPrefix;
            String url = prefix + "/payClaim";

            Date d = new Date(); // current 
            // database format current date
            SimpleDateFormat currentof = new SimpleDateFormat("yyyy-MM-dd");
            var currentDate = currentof.format(d);  
            
            payData.setClaimIdNumber(id);
            payData.setPaymentAmount(amount);
            payData.setPaymentDate(currentDate);
            var custId = sessionBean.getCustId();
            payData.setCustId(custId);

            // pay token - - - - - - - - - - - - - - - - - - - - - 
            var token = sessionBean.getToken();
            var headers = new LinkedMultiValueMap<String,String>();  
            headers.set("E20Token",token);  
            HttpEntity<PayData> request = new HttpEntity<PayData>(payData,headers);  
            // - - - - - - - - - - - - - - - - - - - - - - - -  
            try
            {
                ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.PUT,request,String.class);
                var error = response.getStatusCode() != HttpStatus.OK;
                if (error)
                {
                return false;
                } 
            }
            catch(Exception e)
            {
                // time out or bad request.
                return false;
            }
            //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =
            return true;
    }
 
   
    
}
