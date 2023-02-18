package p20.e10insurance.e10insurance.Controllers;
import  p20.e10insurance.e10insurance.Models.Customer;
import p20.e10insurance.e10insurance.PassedParameters.FormattedDates;
import p20.e10insurance.e10insurance.PassedParameters.GeneralParm;
import  p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Beans.CustomerBean;
import  p20.e10insurance.e10insurance.Beans.EditBean;
 
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import p20.e10insurance.e10insurance.Enums.DateRange.DateRangeValues;
 

@Controller
public class RegisterController { 
 
    /* topmost menu driver for app */ 

    //https://exceptionshub.com/why-is-my-spring-autowired-field-null.html

    //* Url Prefix 

    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;

    //* used for authentication since we do not use OAUTH.

    @Value("${E10Promotion:notSet}")
    String ValidPromotionCode;

    // was E10EmailList now E10EmailPattern
    @Value("${E10EmailPattern:notSet}")
    String emailPattern;

    //*-------------------------------------------------

    @Autowired
    private SessionBean sessionBean;

    @Autowired
    private EditBean editBean;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CustomerBean customerBean;

    @Autowired
    GeneralParm generalParm;

    @Autowired 
    FormattedDates formattedDates;

      
    public enum dataFieldRequirements { name1, name2, addr2, phone, email, date1, mid1, zip1, name3 };


@RequestMapping(value = "/register", method = RequestMethod.GET)
public String Register(Model m, @ModelAttribute Customer customer) 
{  
    sessionBean.SetMessage("Enter Customer Information to Register.");
    sessionBean.setPass1("");
    m.addAttribute("Customer", customer);
    sessionBean.SetRedirect("/register");
    return "layout";
}

@RequestMapping(value = "/register", method = RequestMethod.POST)  
public String RegisterProcess(Model model, @ModelAttribute Customer customer) 
{
    String message = "";
    String custId = customer.getCustId();
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

    // cust Promo - will need to get value from server
    var promo = customer.getCustPromo();
    if(promo == null || !promo.equals(ValidPromotionCode))
    {
        message += " promotion code,";
    }
   
    // the field name is added to the message 
    // so the result will be :   please correct: xx, yy, zz.

       // cust Promo - will need to get value from server 

    // pull last keyed password, confirm password so customer
    // does not have to rekey them.

    if(custPass == null || custPass.equals(""))
    {
        custPass = sessionBean.getPass1();
    }

    if(custConfirm == null || custConfirm.equals(""))
    {
        custConfirm = sessionBean.getPass1();
    }

    message += editBean.CheckData(custId,"Customer Identification",EditBean.dataFieldRequirements.name1);
    message += editBean.CheckData(custPass,"Customer Password",EditBean.dataFieldRequirements.name1);
    message += editBean.CheckData(custConfirm,"Confirm Password",EditBean.dataFieldRequirements.name1);

    message += editBean.CheckData(custFirst,"First Name",EditBean.dataFieldRequirements.name3);
    message += editBean.CheckData(custLast,"Last Name",EditBean.dataFieldRequirements.name3);
    message += editBean.CheckData(custMiddle,"MiddleName",EditBean.dataFieldRequirements.mid1);
    message += editBean.CheckData(custPhone,"Phone",EditBean.dataFieldRequirements.phone);
    
    message += editBean.CheckData(custAddr1,"Address1",EditBean.dataFieldRequirements.addr1); 
    message += editBean.CheckData(custAddr2,"Address2",EditBean.dataFieldRequirements.addr2); 
    
    //var notFound = -1;
    // if(custEmail == null || (ValidEmails.indexOf(custEmail) == notFound))
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

    var forDatabaseBirthDate = "";
    var forScreenBirthDate = "";  
    editBean.EditDate(custBirthDate, DateRangeValues.BIRTHDATE , generalParm, formattedDates);
    if(!generalParm.getStatus())
    {
        message += "BirthDate: " + generalParm.getMessage() + ",";
        forScreenBirthDate = formattedDates.getEnteredDate(); 
    } 
    else
    {  
            forDatabaseBirthDate = formattedDates.getDatabaseFormat(); 
            forScreenBirthDate = formattedDates.getEnteredDate(); 
    }  

    if(gen == null || (!gen.toUpperCase().equals("M") && !gen.toUpperCase().equals("F")))  
    {
        message += "Gender";
    }

    if(state == null || (!state.toUpperCase().equals("WA") && !state.toUpperCase().equals("CA")))  
    {
        message += "State";
    }

    message += editBean.CheckData(custCity,"City",EditBean.dataFieldRequirements.name2);
    message += editBean.CheckData(custZip,"Zip",EditBean.dataFieldRequirements.zip1);   

    if(custPass != null && custConfirm != null)
    {
        if(!custPass.equals(custConfirm))
        {
            // put last in message
            message += " confirmation password does not match,";
        }
    }
   

    Boolean haveErrors = (message.length() > 0);

    message = "Invalid or missing " + message;
    message = message.substring(0, message.length() - 01);

    if(haveErrors)
    {
        // remember cust password and confirm password if keyed.
        if(custPass != null && !custPass.equals("") && custConfirm.equals(custPass))
        { 
           sessionBean.setPass1(custPass); 
        }
        //  edit will pull last keyed values if blank in the edits above,
        //  so customer does not have to rekey them.
        customer.setCustPassword(custPass);
        customer.setCustConfirm(custConfirm);
        customer.setCustBirthDate(forScreenBirthDate);
        model.addAttribute("Customer", customer);
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



    // Passed edits - now create the customer.

    // duplicate customer check 

    Integer dupResult = DuplicateCheck(custId);
    var duplicate = 1;
    var serverDown = 2;
    if(dupResult == serverDown)
    {
        model.addAttribute("Customer", customer);
        sessionBean.SetMessage("Server is down. Contact Administrator.");
        return "layout"; 
    }

    if(dupResult == duplicate)
    {
        model.addAttribute("Customer", customer);
         // if user jumps to menu with this message - turn it off !!
         String MEMU_SUPPRESS_FLAG = "*";
         message = "Customer aleady exists.";
         message = MEMU_SUPPRESS_FLAG + message;
         sessionBean.SetMessage(message);
         return "layout";
    }
 
    // set birthdate in database format
    customer.setCustBirthDate(forDatabaseBirthDate);
     

    //* set application id to E10.
    customer.setAppId("E10");

    // set claim count zero
    customer.setClaimCount("0");

    var unselected = "";
    // set plan to blank - plan screen will be used to select customer id
    // and is required before claim filings.
    customer.setCustPlan(unselected);
    

    ResponseEntity<Customer> response = null;

    try
    {
        //*= = = = = = = call server to add custome and check results - - - -  
        String prefix = urlPrefix;
        String url = prefix + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("E20Register","Yes");
        HttpEntity<Customer> request = new HttpEntity<Customer>(customer, headers); 
        try
        {
            response = restTemplate.exchange(url,HttpMethod.POST,request,Customer.class);
            var error = response.getStatusCode() != HttpStatus.OK;
            if (error)
            {
                //message = response.getBody();
                //var code  = "Error in create customer status: " + response.getStatusCode() + 
                //    "message: " + message;
                model.addAttribute("Customer", customer);
                sessionBean.SetMessage(message);
                return "layout";
            } 
        }
        catch (Exception e)
        {
            // expiration
            message = "Bad Request.";
            model.addAttribute("Customer", customerBean);
            sessionBean.SetMessage(message);
            return "layout";
        }
    }
    catch(Exception e)
    {
        
        if(response == null)
        {
             // can not connect to server.
            message = "Server is unavailable - contact Administrator.";
            model.addAttribute("Customer", customer);
            sessionBean.SetMessage(message);
            return "layout";
        }

        // can not connect to server.
        message = "Server is down. Please contact administrator. Thank you.";
        model.addAttribute("Customer", customer);
        sessionBean.SetMessage(message);
        return "layout";

    }

 
    //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =

    /*

       set customer first last name
       set signed in
       redirect to menu using the session bean redirect field
       the respone will contain the jwt token, store it in session bean.

    */ 

   // 'register' and 'signin' store token. 
   var headers = response.getHeaders();
   List<String> item = headers.get("E20Token");
   String token =  item.get(0);
   if(token != null && !token.equals(""))
   { 
      sessionBean.setToken(token);
   }

   setCustomerBean(customer, customerBean);
   sessionBean.SetCustomerNames(custFirst,custLast);
   sessionBean.setCustId(custId);
   sessionBean.SetSignedIn(true);  
   message = "Successfully registered.";
   sessionBean.SetMessage(message);
   //sessionBean.SetRedirect("/menu");
   return "redirect:/menu";
   //return "layout";
}

private Integer DuplicateCheck(String custId)
{
    /* 
        0 - good : no duplicate
        1 - bad : duplicate customer
        2 - server down
    */

    String prefix = urlPrefix; 
    String url = prefix + "/customer/" + custId; 

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity <String> entity = new HttpEntity<String>(headers);
    ResponseEntity<Customer> response = null; 

    try
    { 

        response = restTemplate.exchange(url,HttpMethod.GET,entity,Customer.class);
        var found = response.getStatusCode() == HttpStatus.FOUND;
        if (found)
        {
            var duplicateFound = 1;
            return duplicateFound;
        } 
        else
        {
            var noDuplicate = 0;
            return noDuplicate;
        }
    }
    catch(HttpClientErrorException e)
    {
         // customer not found 
         var noDuplicateCustomer = 0;
         return noDuplicateCustomer;
    }
    catch(Exception e)
    {

        if(response == null)
        {
             // can not connect to server.
             var serverDown = 2;
             return serverDown;
           
        }
 
        // can not connect to server.
        var serverDown = 2;
        return serverDown;

    } 
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
    String defaultPlanValue = "n//a";
    customerBean.setCustPlan(defaultPlanValue);
    // plan not used here.
}

}