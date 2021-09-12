package p20.e10insurance.e10insurance.Models;

import org.springframework.stereotype.Component;

@Component
public class Service {

    private String ServiceName;
    private String ClaimType; 
    private String ClaimTypeLiteral;
    private String Cost;

    public void setServiceName(String ServiceName)
    {
        this.ServiceName = ServiceName;
    }

    public String getServiceName()
    {
        return this.ServiceName.trim();
    }

    public String getClaimType()
    {
        return this.ClaimType.trim();
    }

    public void setClaimType(String claimType)
    {
        this.ClaimType = claimType;
    }

    public String getClaimTypeLiteral()
    {
        return this.ClaimTypeLiteral.trim();
    }

    public void setClaimTypeLiteral(String claimTypeLiteral)
    {
        this.ClaimTypeLiteral = claimTypeLiteral;
    }

    public String getCost()
    {
        return this.Cost;
    }

    public void setCost(String cost)
    {
        this.Cost = cost;
    }
    
}
