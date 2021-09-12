package p20.e10insurance.e10insurance.PassedParameters; 
import org.springframework.stereotype.Component;

@Component
public class AdminParm {
    
    private String adminid; 
    private String adminpassword;
    private boolean valid; 

    AdminParm()
    {
        
    }  

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String password) {
        this.adminpassword = password;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    public boolean isValid()
    {
        return valid;
    }
 

    
}
    