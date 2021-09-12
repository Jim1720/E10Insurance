package p20.e10insurance.e10insurance.Models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class History {

    // place holder
 
    private List<Claim> claims;

    /* button action */
    private String buttonAction;
    
    /* populated by javascript when adjust selected */
    private String adjustedClaimId;

    
    /* populated by javascript when adjust selected */
    private String payClaimId;
    private String paymentAmount;

       
    /* populated by javascript when adjust selected */
    private String copyClaimId;

    public History()
    {
        claims = new ArrayList<Claim>();
    }
    
    

    public void setHistoryClaims(Claim[] claim)
    {
        for(Claim c : claim )
        {
            this.claims.add(c);
        }
    }

    public List<Claim> getHistoryClaims()
    {
        return this.claims;
    }

    public String getAdjustedClaimId()
    {
        return this.adjustedClaimId;
    }

    public void setAdjustedClaimId(String adjustedClaimId)
    {
        this.adjustedClaimId = adjustedClaimId;
    }

    public String getPayClaimId()
    {
        return this.payClaimId;
    }

    public void setPayClaimId(String claimId)
    {
        payClaimId = claimId;
    }

    public String getPaymentAmount()
    {
        return this.paymentAmount;
    }

    public void setPaymentAmount(String amount)
    {
        this.paymentAmount = amount;
    }

    public void setButtonAction(String action)
    {
        this.buttonAction = action;
    }

    public String getButtonAction()
    {
        return this.buttonAction;
    }

    public void setCopyClaimId(String claimId)
    {
        this.copyClaimId = claimId;
    }

    public String getCopyClaimId()
    {
        return this.copyClaimId;
    }
}
