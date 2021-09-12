package p20.e10insurance.e10insurance.PassedParameters;

import java.io.Serializable; 
import org.springframework.stereotype.Component;  

@Component  
public class PlanData implements Serializable  {  
    
	private static final long serialVersionUID = 1L;
 
    private String custId;  
    private String custPlan;   
 
    transient private String token;
  

    public String getcustId() {
        return custId;
    }

    public void setcustId(String custId) {
        this.custId = custId;
    } 

    public String getCustPlan()
    {
        return this.custPlan;
    }
    public void setCustPlan(String custPlan)
    {
        this.custPlan = custPlan;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

}