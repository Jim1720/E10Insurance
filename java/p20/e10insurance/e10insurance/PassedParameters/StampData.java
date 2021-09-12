package p20.e10insurance.e10insurance.PassedParameters;

public class StampData {

    private String adjustedClaimIdNumber;
    private String adjustingClaimIdNumber;
    private String adjustmentDate;
    private String custId;

    public String getAdjustedClaimIdNumber()
   {
        return adjustedClaimIdNumber;
    }
    public void setAdjustedClaimIdNumber(String claimId) 
    {
        this.adjustedClaimIdNumber = claimId;
    }

    public String getAdjustingClaimIdNumber()
    {
        return adjustingClaimIdNumber;
    }

    public void setAdjustingClaimIdNumber(String claimId)
    {
        this.adjustingClaimIdNumber = claimId;
    }

    public String getAdjustmentDate()
    {
        return adjustmentDate;
    }

    public void setAdjustmentDate(String adjustmentDate)
    {
        this.adjustmentDate = adjustmentDate;
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
