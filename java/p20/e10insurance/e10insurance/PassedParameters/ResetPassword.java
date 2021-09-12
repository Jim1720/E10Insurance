package p20.e10insurance.e10insurance.PassedParameters;

import org.springframework.stereotype.Component;

@Component
public class ResetPassword {
    
    private String custId; 
    private String newPassword; 
    private String message;
    private boolean editerror;

    ResetPassword() 
    {

    } 
    

    public ResetPassword(boolean editError,String message)
    { 
        this.message = message;
        this.editerror = editError;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getCustId()
    {
        return this.custId;
    }
 

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }

    public String getNewPassword()
    {
        return this.newPassword;
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
