package p20.e10insurance.e10insurance.Controllers;
 
import java.util.Arrays;
import java.util.List;

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
import p20.e10insurance.e10insurance.Models.Signin;
import p20.e10insurance.e10insurance.Beans.CustomerBean;
import p20.e10insurance.e10insurance.Beans.EditBean;


@Controller
public class SigninController { 
    
    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;
    
    @Autowired
    private SessionBean sessionBean;

    @Autowired
    private EditBean editBean; 
    
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerBean customerBean;

    @Autowired
    Signin signin;
 
    
    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signin(Model m, @ModelAttribute Signin signin)
    {  
        m.addAttribute("signin", signin); 
        sessionBean.SetMessage("Enter Customer Id and Password to signin..");
        sessionBean.SetRedirect("/signin");
        return "layout";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)  
    public String SigninProcess(Model model, @ModelAttribute Signin signin) 
    {
        String custId = signin.getCustid().trim();
        String custPass = signin.getCustpass().trim();  
        String empty = "";

       if(custId.equals(empty))  
       {
           sessionBean.SetMessage("Please enter your Customer Identification Number");
           return "layout";
       }  
       
       if(custPass.equals(empty))  
       {
           sessionBean.SetMessage("Please enter your password.");
           return "layout";
       }   

       if(!editBean.ValidData(custId, EditBean.dataFieldRequirements.name1))
       {
           sessionBean.SetMessage("Invalid Customer Id. Alphanumeric only.");
           return "layout";
       }

       if(!editBean.ValidData(custPass, EditBean.dataFieldRequirements.name1))
       {
           sessionBean.SetMessage("Invalid Customer Password. Alphanumeric only.");
           return "layout";
       }

         // Passed edits - now find the customer.

        //*= = = = = = = call server to add custome and check results - - - -  
        String prefix = urlPrefix; 
        String url = prefix + "/signin/" + custId; 
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> entity = new HttpEntity<String>(headers1);
        ResponseEntity<Customer> response = null;


        try
        { 

            response = restTemplate.exchange(url,HttpMethod.GET,entity,Customer.class);
            var error = response.getStatusCode() != HttpStatus.FOUND;
            if (error)
            {
                var message = "Unable to process signin code is: " + response.getStatusCode();
                model.addAttribute("Signin", signin);
                sessionBean.SetMessage(message);
                return "layout";
            } 
        }
        catch(HttpClientErrorException e)
        {
            var message = "Customer not found.";
            model.addAttribute("Signin", signin);
            sessionBean.SetMessage(message);
            return "layout";
        }
        catch(Exception e)
        {

            if(response == null)
            {
                 // can not connect to server.
                var message = "Server is unavailable - contact Administrator. condition:null";
                model.addAttribute("Signin", signin);
                sessionBean.SetMessage(message);
                return "layout";
            }

            // can not connect to server.
            var message = "Server is down. Please contact administrator. Thank you.";
            model.addAttribute("Signin", signin);
            sessionBean.SetMessage(message);
            return "layout";

        }       

        //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =

        // - success show first and last name on links and show menu.
        Customer customer  = response.getBody(); 
        if(customer.getCustId() == null)
        {
            var message = "Null response or server down. PLease contact administrators. condition null 2";
            model.addAttribute("Signin", signin);
            sessionBean.SetMessage(message);
            return "layout";
        } 

        //* copy for update screen - internal copy 
        //* avoids database read on the update screen
       
        customerBean.setCustid(customer.getCustId().trim());  
        customerBean.setCustpassword(customer.getCustPassword().trim());
        customerBean.setCustfirst(customer.getCustFirst().trim());
        customerBean.setCustlast(customer.getCustLast().trim());

        customerBean.setCustphone(customer.getCustPhone().trim());
        customerBean.setCustemail(customer.getCustEmail().trim());

        customerBean.setCustmiddle(customer.getCustMiddle().trim());
        customerBean.setCustbirthdate(customer.getCustBirthDate().trim());
        customerBean.setCustgender(customer.getCustGender().trim());

        customerBean.setCustaddr1(customer.getCustAddr1().trim());
        customerBean.setCustaddr2(customer.getCustAddr2().trim());

        customerBean.setCustcity(customer.getCustCity().trim());
        customerBean.setCuststate(customer.getCustState().trim());
        customerBean.setCustzip(customer.getCustZip().trim());

        customerBean.setCustPlan(customer.getCustPlan());

        // claim count
        customerBean.setClaimCount(customer.getClaimCount());

        // unused
        customerBean.setCustconfirm("");
        customerBean.setCustpromo("");

        // 'register' and 'signin' store token.  
        var headers2 = response.getHeaders();
        List<String> item = headers2.get("E20Token");
        String token =  item.get(0);
        if(token != null && !token.equals(""))
        { 
            sessionBean.setToken(token);
        }

        // redundant added cust bean later but keep.
        sessionBean.SetCustomerNames(customer.getCustFirst(), customer.getCustLast());
        custId = custId.trim();
        sessionBean.setCustId(custId);
        sessionBean.SetMessage("Customer successfully signed in.");
        sessionBean.SetSignedIn(true); 
        sessionBean.SetRedirect("/menu");   
        return "layout";
    }

}
