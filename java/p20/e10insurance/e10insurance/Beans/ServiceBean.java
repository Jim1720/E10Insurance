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
  
import p20.e10insurance.e10insurance.Models.Service;

@Component
@SessionScope
public class ServiceBean {

    @Value("${E10UrlPrefix:notSet}")
    String urlPrefix;
    
    @Autowired
    private SessionBean sessionBean;

    @Autowired
    RestTemplate restTemplate;
    
    private boolean loaded;
    private List<Service> services;

    ServiceBean() {

        services = new ArrayList<Service>();
    }


    public boolean isLoaded()
    {
        return this.loaded;
    }

    
    public boolean LoadServices()
    {

         // returns true if one or more plans are found.
         var found = true;

         //*= = = = = = = call server to add custome and check results - - - -  
         String prefix = urlPrefix;  

         String url = prefix + "/readServices"; 
 
         HttpHeaders headers = new HttpHeaders();
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         HttpEntity <String> entity = new HttpEntity<String>(headers);
         ResponseEntity<Service[]> response = null; 

         try
         { 
 
             response = restTemplate.exchange(url,HttpMethod.GET,entity,Service[].class);
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
         }

        // read one or more plans.

        Service[] _services = response.getBody(); 
        
        for(Service p : _services)
        {
            services.add(p);
        }

        if(!services.isEmpty())
        {
            sessionBean.setSevicesLoaded(true);
        }
 
        this.loaded = true;
        return found;

    }

    public List<Service> getAllServices()
    { 

        return this.services; 
    }

   

    public double getCost(String ClaimType, String Service)
    {
        // find cost for service within the claim type.

        double cost = 0.0;

        for(Service s: services)
        {
            if(s.getClaimType().equals(ClaimType) && s.getServiceName().trim().equals(Service))
            {
                cost =  Double.parseDouble(s.getCost());
            }

        }

        return cost;
    }
    
}
