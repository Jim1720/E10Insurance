package p20.e10insurance.e10insurance.Controllers;
 

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import p20.e10insurance.e10insurance.Beans.CustomerBean;
import p20.e10insurance.e10insurance.Beans.EditBean;
import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Models.Customer;
import p20.e10insurance.e10insurance.PassedParameters.FormattedDates;
import p20.e10insurance.e10insurance.PassedParameters.GeneralParm; 
 
import p20.e10insurance.e10insurance.Enums.DateRange.DateRangeValues;


@Controller
public class UpdateController 
{ 

    
    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;

   
    // was E10EmailList now E10EmailPattern
    @Value("${E10EmailPattern:notSet}")
    String emailPattern;

    @Autowired
    private SessionBean sessionBean;  
    
    @Autowired
    private EditBean editBean; 
    
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerBean customerBean;

    @Autowired
    Customer customer;

    @Autowired
    GeneralParm generalParm;

    @Autowired 
    FormattedDates formattedDates;
 

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String showUpdateScreen(Model model, @ModelAttribute Customer customer)
    { 
        if(!sessionBean.GetSignedIn())
        {
            // shows must be signed in screen with
            // a button to return to start 
            // valid signin check  
            sessionBean.SetRedirect("/unauth");  
            return "layout";
        } 
 

        // copy from session bean 'customerBean'
        // to scren model 'customer'.
        
        customer.setCustId(customerBean.getCustid());
        customer.setCustPassword(customerBean.getCustpassword());
        customer.setCustFirst(customerBean.getCustfirst());
        customer.setCustMiddle(customerBean.getCustmiddle());
        customer.setCustLast(customerBean.getCustlast()); 
        customer.setCustBirthDate(customerBean.getCustbirthdate());
        customer.setCustGender(customerBean.getCustgender()); 
        customer.setCustEmail(customerBean.getCustemail());
        customer.setCustPhone(customerBean.getCustphone()); 
        customer.setCustAddr1(customerBean.getCustaddr1());
        customer.setCustAddr2(customerBean.getCustaddr2()); 
        customer.setCustCity(customerBean.getCustcity());
        customer.setCustState(customerBean.getCuststate());
        customer.setCustZip(customerBean.getCustzip()); 
        customer.setCustPlan(customerBean.getCustPlan());
        

        // if birthdate from dateabase , format it.

        var fromDatabase = customer.getCustBirthDate().indexOf("-") == 4;
        if(fromDatabase)
        {
            var date = customer.getCustBirthDate();
            var formatted = date.substring(5,7) + date.substring(8,10)  +
                            date.substring(0,4);
            customer.setCustBirthDate(formatted);
        }
        final String  NOT_SELECTED = "n//a";
        if(customer.getCustPlan().equals(NOT_SELECTED))
        {
            sessionBean.SetMessage("Warning: Plan must be assgined before claim can be added.");
        }
 
        model.addAttribute("Customer", customer);
        sessionBean.SetRedirect("/update");
        return "layout";
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)  
    public String UpdateProcess(Model model, @ModelAttribute Customer customer) 
    { 

        String message = "";
        // cust id not used on update screen 
        String custPass = customer.getCustPassword().trim(); 
        String custConfirm = customer.getCustConfirm().trim(); 
        String custFirst = customer.getCustFirst();
        String custMiddle = customer.getCustMiddle();
        String custLast = customer.getCustLast();
        String gen = customer.getCustGender();
        String custBirthDate = customer.getCustBirthDate();
        String custEmail = customer.getCustEmail();
        String custPhone = customer.getCustPhone();
        String custAddr1 = customer.getCustAddr1();
        String custAddr2 = customer.getCustAddr2();
        String custCity = customer.getCustCity();
        String state = customer.getCustState();
        String custZip = customer.getCustZip();
      

        String saveCustomerId = sessionBean.getCustId();
    
        var userKeyedPass = custPass != null && !custPass.equals(""); 
        var userKeyedConfirm = custConfirm != null && !custConfirm.equals("");
        var anyPassFieldKeyed = userKeyedPass || userKeyedConfirm;
        var noPassesKeyed = !anyPassFieldKeyed;
        // one or both keyed use edits else use existing customer 
        if(anyPassFieldKeyed)
        {
             
            var pwvalid =  editBean.ValidData(custPass,EditBean.dataFieldRequirements.name1);
            if(!pwvalid)
            {
                message += "password, ";  
            }

            var confvalid =  editBean.ValidData(custConfirm,EditBean.dataFieldRequirements.name1);
            if(!confvalid)
            {
                message = "Confirm Password, ";
            }

            if(!custPass.equals("") && !custConfirm.equals(""))
            {
                if(!custPass.equals(custConfirm))
                {
                    // put last in message
                    message += " confirmation password does not match,"; 
                }
            }

        } 
 
    
        message += editBean.CheckData(custFirst,"First Name",EditBean.dataFieldRequirements.name3);
        message += editBean.CheckData(custLast,"Last Name",EditBean.dataFieldRequirements.name3);
        message += editBean.CheckData(custMiddle,"MiddleName",EditBean.dataFieldRequirements.mid1);
        message += editBean.CheckData(custPhone,"Phone",EditBean.dataFieldRequirements.phone); 
 
      
        if (custEmail == null || custEmail == "")
        {
            message += " Email, ";
        }
        else 
        {  
            
            // matching against emailPattern from
            // environment variable for
            // flexability.
    
            Pattern p = Pattern.compile(emailPattern);
            Matcher m = p.matcher(custEmail);
            if (!m.matches())
            {
                message += " Email, ";
            }
        }
           
        message += editBean.CheckData(custAddr1,"Address1",EditBean.dataFieldRequirements.name2); 
        message += editBean.CheckData(custAddr2,"Address2",EditBean.dataFieldRequirements.addr2); 

        if(gen == null || (!gen.toUpperCase().equals("M") && !gen.toUpperCase().equals("F")))  
        {
            message += "Gender, ";
        }
    
        if(state == null || (!state.toUpperCase().equals("WA") && !state.toUpperCase().equals("CA")))  
        {
            message += "State, ";
        }
    
        message += editBean.CheckData(custCity,"City",EditBean.dataFieldRequirements.name2);
        message += editBean.CheckData(custZip,"Zip",EditBean.dataFieldRequirements.zip1);   
    
      

        // var onScreen = "";
        // var useNewRoutine = custBirthDate.indexOf('*') == 0; // first position
        String forScreenBirthDate = "";
        String forDatabaseBirthDate = "";
         
        
        editBean.EditDate(custBirthDate, DateRangeValues.BIRTHDATE, generalParm, formattedDates);
        if(!generalParm.getStatus())
        {
            message += " Birthdate: " + generalParm.getMessage() + ",";
            forScreenBirthDate = formattedDates.getEnteredDate(); 
        } 
        else
        {  
                forDatabaseBirthDate = formattedDates.getDatabaseFormat(); 
                forScreenBirthDate = formattedDates.getEnteredDate(); 
        }  
     
    
        Boolean haveErrors = (message.length() > 0);
    
        message = "Invalid or missing " + message; 
    
        if(haveErrors)
        {
            customer.setCustBirthDate(forScreenBirthDate); // as entered format.
            model.addAttribute("customer", customer);
            // if user jumps to menu with this message - turn it off !!
            String MEMU_SUPPRESS_FLAG = "*";
            message = MEMU_SUPPRESS_FLAG + message;
            if(message.endsWith(",")) // remove trailing comma
            {
               message =  message.substring(0, message.length() - 01);
            }
            if(message.endsWith(", ")) // remove trailing comma
            {
               message =  message.substring(0, message.length() - 02);
            }
            sessionBean.SetMessage(message);
            return "layout";
        }; 
    
        // Passed edits - now update the customer 
        // copy to customer bean - internal copy.
        var holdPassword = "";
        var notEntered = custPass.equals("");
        if(notEntered)
        {
             holdPassword = customerBean.getCustpassword();
        } 
     
        if(notEntered)
        {
            // retain origional password on update screen if not entered. -- 
            // so we add it as a screen field to be copied to the bean next,....
            customer.setCustPassword(holdPassword);
            // put this in 'customer' so the next routine will copy it back. 

        }

        // maintainclaim count - it is not on screen..
        customer.setClaimCount("0"); 
    
        setCustomerBean(customer, customerBean);

        //* add customer Id from seesion bean
        //* it is not on the update screen
        customer.setCustId(saveCustomerId);

        // preserver claim count - not updated - only incrimented on claim add
        customer.setClaimCount(customerBean.getClaimCount());

        //* hard code cust birth date to 2020-01-01 until date edit completed.
        //var hard = "2020-01-01";
        //customer.setCustBirthDate(hard); 
        //* end temp code

         // set database format for date.
         customer.setCustBirthDate(forDatabaseBirthDate);

         // cust plan is read only get it from the customerBean  
         customer.setCustPlan(customerBean.getCustPlan()); 

         // keep appid e10
         // register sets this but not strictly copied; it's a constant.
         customer.setAppId("E10");

         // no password date keyed - copy origional password to customer from the customerBean.
         if(noPassesKeyed)
         {
            // copy password from existing data.
            customer.setCustPassword(customerBean.getCustpassword());            
         }

        //*= = = = = = = call server to add custome and check results - - - -  
        String prefix = urlPrefix;
        String url = prefix + "/customer";
        
         // update token - - - - - - - - - - - - - - - - - - - - - 
         var token = sessionBean.getToken();
         var headers = new LinkedMultiValueMap<String,String>();  
         headers.set("E20Token",token); 
         // - - - - - - -  - - - - - - - - - - - - - - - - - - - - -
        HttpEntity<Customer> request = new HttpEntity<Customer>(customer, headers);  
        // - - - - - - - - - - - - - - - - - - - - - - - -  
        try
        { 
            ResponseEntity<Customer> response = restTemplate.exchange(url,HttpMethod.PUT,request,Customer.class);
            var error = response.getStatusCode() != HttpStatus.OK;
            if (error)
            {
                message = "Error in update customer status: " + response.getStatusCode();
                model.addAttribute("Customer", customerBean);
                sessionBean.SetMessage(message);
                return "layout";
            } 
        }
        catch (Exception e)
        {
            // expiration
            message = "Bad Request - Possible Expiration.";
            model.addAttribute("Customer", customerBean);
            sessionBean.SetMessage(message);
            return "layout";
        }
        //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =
    
        /*
    
           set customer first last name 
           redirect to menu using the session bean redirect field
    
        */ 
    
       sessionBean.SetCustomerNames(custFirst,custLast); 
       sessionBean.SetMessage("Customer successfully updated.");
       // use redirect because controller will blank message.
       //sessionBean.SetRedirect("/menu"); 
       return "redirect:/menu";
        //return "layout";
    }

    
private void setCustomerBean(Customer customer, CustomerBean customerBean)
{
    // hold session customer data values matching dateabase set in rgister and update controllers.
    customerBean.setCustid(customer.getCustId());
    customerBean.setCustpassword(customer.getCustPassword());
    customerBean.setCustfirst(customer.getCustFirst());
    customerBean.setCustlast(customer.getCustLast());
    customerBean.setCustgender(customer.getCustGender());
    customerBean.setCustmiddle(customer.getCustMiddle());
    customerBean.setCustphone(customer.getCustPhone());
    customerBean.setCustemail(customer.getCustEmail());
    customerBean.setCustbirthdate(customer.getCustBirthDate());
    customerBean.setCustaddr1(customer.getCustAddr1());
    customerBean.setCustaddr2(customer.getCustAddr2());
    customerBean.setCustcity(customer.getCustCity());
    customerBean.setCuststate(customer.getCustState());
    customerBean.setCustzip(customer.getCustZip());   

    // do not copy plan it is read only.

    // claim count not changed only when claim added for customer.
}
}