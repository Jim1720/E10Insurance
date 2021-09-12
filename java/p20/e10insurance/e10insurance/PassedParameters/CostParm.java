package p20.e10insurance.e10insurance.PassedParameters;

import org.springframework.stereotype.Component;

@Component
public class CostParm {
    
    String Plan;
    String Service;
    String ClaimType;

    Double ServiceCost;
    Double Covered;
    Double Owed;

    public String getPlan()
    {
        return Plan;
    }

    public String getService()
    {
        return Service;
    }

    public void setPlan(String plan)
    {
        Plan = plan;
    }

    public void setService(String service)
    {

        Service = service;
    }

    public String getCovered()
    {
        // todo apply dollar format
        return Covered.toString();
    }

    public void setCovered(double covered)
    {
        Covered = covered;
    }

    public String getOwed()
    {
          // todo apply dollar format
         return Owed.toString(); 
    }

    public void setOwed(double owed)
    {
        this.Owed = owed;
    }

    public void setClaimType(String claimType)
    {
        ClaimType = claimType;
    }

    public String getClaimType()
    {
        return ClaimType;
    }

    public void setServiceCost(Double amount)
    {
        this.ServiceCost = amount;
    }

    public String getServiceCost()
    {
        return this.ServiceCost.toString();
    }
}
