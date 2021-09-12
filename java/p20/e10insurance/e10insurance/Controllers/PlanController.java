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

import p20.e10insurance.e10insurance.Beans.CustomerBean;
import p20.e10insurance.e10insurance.Beans.PlanBean;
import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Models.PlanSelect;
import p20.e10insurance.e10insurance.PassedParameters.GeneralParm;
import p20.e10insurance.e10insurance.PassedParameters.PlanData;

@Controller
public class PlanController {

    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;
    
    @Autowired
    private SessionBean sessionBean; 
 

    @Autowired
    private PlanBean planBean; 
    
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GeneralParm generalParm;

    @Autowired
    PlanSelect planSelect;

    
    @Autowired
    PlanData planData;

    @Autowired
    CustomerBean customerBean;

    
    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public String setupPlanScreen(Model model, @ModelAttribute PlanSelect planSelect)
    {  
        // load and show plans.

        if(!sessionBean.GetSignedIn())
        {
            // shows must be signed in screen with
            // a button to return to start 
            // valid signin check  
            sessionBean.SetRedirect("/unauth");  
            return "layout";
        }

        var off = "";
        sessionBean.SetMessage(off);
        
        if(!planBean.isLoaded())
        {
             planBean.LoadPlans(); 
        }
       
        if(sessionBean.arePlansLoaded())
        { 
            sessionBean.SetMessage("Plans not loaded.");
            sessionBean.SetRedirect("/error");
        } 

        var listedPlans = planBean.getPlans();
        planSelect.setPlans(listedPlans); 
        
        model.addAttribute("planSelect", planSelect);

        // now show the plans 
        sessionBean.SetRedirect("/plan");
        return "layout";
    }

    @RequestMapping(value = "/plan", method = RequestMethod.POST)  
    public String ProcessPlanScreen(Model model, @ModelAttribute PlanSelect planSelect) 
    {
        
            // store selected plan in the customer table.

            // what customer?
            var custId = sessionBean.getCustId().trim();

            // which plan?
            var plan = planSelect.getSelectedPlan().trim();

            // set up PlanData.
            planData.setcustId(custId);
            planData.setCustPlan(plan);

            // now update customer table.

            //*= = = = = = = call server to update plan in customer table  - - - -  
            String prefix = urlPrefix;
            String url = prefix + "/setplan";
            
             // plan token - - - - - - - - - - - - - - - - - - - - - 
             var token = sessionBean.getToken();
             var headers = new LinkedMultiValueMap<String,String>();  
             headers.set("E20Token",token); 
             HttpEntity<PlanData> request = new HttpEntity<PlanData>(planData, headers);  
            // - - - - - - - - - - - - - - - - - - - - - - - -  
            try
            {
                ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.PUT,request,String.class);
                var error = response.getStatusCode() != HttpStatus.OK;
                if (error)
                {
                    var message = "could not update plan status: " + response.getStatusCode();
                    model.addAttribute("PlanSelect", planSelect);
                    sessionBean.SetMessage(message);
                    return "layout";
                } 
            }
            catch(Exception e)
            {
                var message = "Bad Request - Possible Timeout";
                model.addAttribute("PlanSelect", planSelect);
                sessionBean.SetMessage(message);
                return "layout";

            }
            //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =
            
            // update customerBean in working in core copy.
            customerBean.setCustPlan(plan);

            // message customer and return to menu.
            sessionBean.SetMessage("Plan successfully updated."); 
            sessionBean.SetRedirect("/menu"); 
            return "layout";
    }
    
}
