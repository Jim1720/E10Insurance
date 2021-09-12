package p20.e10insurance.e10insurance.Models;

import org.springframework.stereotype.Component;

@Component
public class AdminAction {
    
    private String buttonAction;
    private String CurrentCustomer;

    private String NewPassword;
    private String NewCustomer;

    private String NewConfirmPassword;
    private String NewConfirmCustomer;

     AdminAction()
    {
        
    }

    public void setButtonAction(String buttonAction)
    {
        this.buttonAction = buttonAction;
    }

    public String getButtonAction()
    {
        return this.buttonAction;
    }

    public void setCurrentCustomer(String CurrentCustomer)
    {
        this.CurrentCustomer = CurrentCustomer;
    }

    public String getCurrentCustomer()
    {

        return CurrentCustomer;
    } 
    
    public String getNewPassword()
    {
        return this.NewPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.NewPassword = newPassword;
    }

    public String getNewCustomer()
    {
        return this.NewCustomer;
    }

    public void setNewCustomer(String newCustomer)
    {
        this.NewCustomer = newCustomer;
    }

    public String getNewConfirmPassword()
    {
        return this.NewConfirmPassword;
    }

    public void setNewConfirmPassword(String newConfirmPassword)
    {
        this.NewConfirmPassword = newConfirmPassword;
    }

    
    public String getNewConfirmCustomer()
    {
        return this.NewConfirmCustomer;
    }

    public void setNewConfirmCustomer(String newConfirmCustomer)
    {
        this.NewConfirmCustomer = newConfirmCustomer;
    }



}
