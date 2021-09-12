package p20.e10insurance.e10insurance.PassedParameters;

import org.springframework.stereotype.Component;

@Component
public class PayData {

    private String PaymentAmount;
    private String ClaimIdNumber;
    private String PaymentDate; 
    private String custId;

    public String getPaymentAmount()
    {
        return PaymentAmount;
    }
    public void setPaymentAmount(String amount)
    {
        this.PaymentAmount = amount;
    }

    public void setClaimIdNumber(String id)
    {
        this.ClaimIdNumber = id;
    }

    public String getClaimIdNumber()
    {
        return ClaimIdNumber;
    }

    public void setPaymentDate(String date)
    {
        this.PaymentDate = date;
    }

    public String getPaymentDate()
    {
        return this.PaymentDate;
    }

    
    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }
    
    
}
