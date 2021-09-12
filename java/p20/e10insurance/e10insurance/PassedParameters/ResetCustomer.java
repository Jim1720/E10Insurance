package p20.e10insurance.e10insurance.PassedParameters;

import org.springframework.stereotype.Component;
 

@Component
public class ResetCustomer {

    private String curCustId;
    private String newCustId;
    private String message;
    private boolean editerror;

    ResetCustomer()
    {

    } 
    
    public ResetCustomer(boolean editError,String message)
    { 
        this.message = message;
        this.editerror = editError;
    }

    public void setCurCustId(String curCustId) {
        this.curCustId = curCustId;
    }

    public String getCurCustId()
    {
        return this.curCustId;
    }

    public void setNewCustId(String newCustId) {
        this.newCustId = newCustId;
    }

    public String getNewCustId()
    {
        return this.newCustId;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public boolean isEditError()
    {
        return this.editerror;
    }

    public void setEditError(boolean flag)
    {
        this.editerror = flag;
    }

  
    
}
