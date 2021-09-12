package p20.e10insurance.e10insurance.Beans;

import java.util.ArrayList;
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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
 
import p20.e10insurance.e10insurance.Models.Plan;

@Component
@SessionScope
public class PlanBean {
    
    
    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;
    
    @Autowired
    private SessionBean sessionBean;

    @Autowired
    RestTemplate restTemplate;
    
    private boolean loaded;
    private List<Plan> plans; 

    PlanBean() {

        plans = new ArrayList<Plan>();
    }


    public boolean isLoaded()
    {
        return this.loaded;
    }

    public boolean LoadPlans()
    {

         // returns true if one or more plans are found.
         var found = true;

         //*= = = = = = = call server to add custome and check results - - - -  
         String prefix = urlPrefix;  

         String url = prefix + "/readPlans"; 
 
         HttpHeaders headers = new HttpHeaders();
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         HttpEntity <String> entity = new HttpEntity<String>(headers);
         ResponseEntity<Plan[]> response = null;
  

         try
         { 
 
             response = restTemplate.exchange(url,HttpMethod.GET,entity,Plan[].class);
             var error = response.getStatusCode() != HttpStatus.FOUND;
             if (error)
             {
                 
             } 
         }
         catch(HttpClientErrorException e)
         {
            

         }
         catch(Exception e)
         {
 
                if(response == null)
                {
                     
                }
    
                // can not connect to server.
              

            }

            Plan[] _plans = response.getBody();   

            for(Plan p : _plans)
            {
                plans.add(p);
            } 

            if(plans.isEmpty())
            {
                sessionBean.setPlansLoaded(false);
            }

            this.loaded = true;
            return found;

    }

    public List<Plan> getPlans()
    {
        return this.plans; 
    }

    public double getPercent(String plan)
    {
        double percent = 0;
        var inputPlan = plan.trim();
        for(Plan p: plans)
        {
            var checkPlanName = p.getPlanName().trim();
            if(inputPlan.equals(checkPlanName))
            {
                percent = p.getPercent();
            }
        }
        return percent;
    }
    
}
