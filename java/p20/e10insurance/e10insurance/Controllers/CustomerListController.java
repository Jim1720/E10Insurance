package p20.e10insurance.e10insurance.Controllers;
 

import java.util.Arrays;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Models.Customer;
import p20.e10insurance.e10insurance.Models.CustomerList; 

@Controller
public class CustomerListController 
{ 

        @Value("${E10UrlPrefix:notSet}")
        String urlPrefix;  
        @Autowired
        RestTemplate restTemplate;
            
        @Autowired
        SessionBean sessionBean;   

        @Autowired
        CustomerList customerList;

        
                
        @RequestMapping(value = "/listcustomers", method = RequestMethod.GET)
        public String ActionDisplay(Model model, @ModelAttribute CustomerList customerList)
        {   
            if(!sessionBean.isAdminSignedIn())
            {
               // shows must be signed in screen with
               // a button to return to start 
               // valid signin check  
               model.addAttribute("CustomerList",customerList);
               sessionBean.SetRedirect("/unauth");  
               return "layout";
            }  
   
            //*= = = = = = = call server to add custome and check results - - - -  
            String prefix = urlPrefix;  

            String url = prefix + "/listcustomers"; 

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity <String> entity = new HttpEntity<String>(headers);
            ResponseEntity<Customer[]> response = null;

            // ref: https://www.baeldung.com/spring-rest-template-list
   
            try
            { 
    
                response = restTemplate.exchange(url,HttpMethod.GET,entity,Customer[].class);
                var error = response.getStatusCode() != HttpStatus.FOUND;
                if (error)
                {
                    var message = "Unable to read customers: " + response.getStatusCode();
                    //model.addAttribute("Signin", signin);
                    sessionBean.SetMessage(message);
                    return "layout";
                } 
            }
            catch(HttpClientErrorException e)
            {
                   //* - - - no customers - - - - - 
                   sessionBean.SetMessage("No Customers Found");
                   sessionBean.SetRedirect("/adminaction");
                   return "layout";
   
            }
            catch(Exception e)
            {
    
                if(response == null)
                {
                     // can not connect to server. 
                    var message = "unable to read customers - 3.";
                    //model.addAttribute("Signin", signin);
                    sessionBean.SetMessage(message);
                    return "layout";
                }
    
                // can not connect to server.
                var message = "unable to read customers - 4.";
                //model.addAttribute("Signin", signin);
                sessionBean.SetMessage(message);
                return "layout";
    
           }
   
           if(response.getBody() == null)
           {
                // can not connect to server.
                var message = "unable to read customers - 5.";
                //model.addAttribute("Signin", signin);
                sessionBean.SetMessage(message);
                return "layout";
           }
   
           Customer[] customers = response.getBody(); 
    
   
           /*for(Customer c : customers)
           {
               // augment any date here for customer display screen.....
           }*/

            // load screen display object - history 
            customerList.setCustomerList(customers);

            // history = claims + indexSelected; 
            model.addAttribute("customerlist", customerList); 
            
            sessionBean.SetMessage("");
            sessionBean.SetRedirect("/listcustomers");
            return "layout"; 
        }

}
