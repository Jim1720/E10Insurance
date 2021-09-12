package p20.e10insurance.e10insurance.Models;
 
import org.springframework.stereotype.Component; 
  
@Component
public class Customer {
    
    private String custId; 
    private String custPassword;
    private String custFirst;
    private String custLast; 
    private String custConfirm;
    private String custPromo;
    private String custMiddle;
    private String custEmail;
    private String custPhone;
    private String custBirthDate;
    private String custGender;
    private String custAddr1;
    private String custAddr2;
    private String custCity;
    private String custState;
    private String custZip; 
    private String custPlan;
    private String appId;
    private String claimCount; 

    public Customer()
    {

    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustPassword() {
        return custPassword;
    }

    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }

    public String getCustFirst()
    {
        return custFirst;
    }

    public void setCustFirst(String custFirst)
    {
        this.custFirst = custFirst;
    }

    public String getCustLast()
    {
        return custLast;
    }

    public void setCustLast(String custLast)
    {
        this.custLast = custLast;
    }

    public String getCustConfirm()
    {
        return custConfirm;
    }

    public void setCustConfirm(String custConfirm)
    {
        this.custConfirm = custConfirm;
    }

    public String getCustPromo()
    {
        return custPromo;
    }

    public void setCustPromo(String custPromo)
    {
        this.custPromo = custPromo;
    }
  

    public String getCustMiddle()
    {
        return custMiddle;
    }

    public void setCustMiddle(String custMiddle)
    {
        this.custMiddle = custMiddle;
    } 

    public String getCustEmail()
    {
        return custEmail;
    }
    public void setCustEmail(String custEmail)
    {
        this.custEmail = custEmail;
    }
    public String getCustPhone()
    {
        return custPhone;
    }
    public void setCustPhone(String custPhone)
    {
        this.custPhone = custPhone;
    }
    public String getCustBirthDate()
    {
        return custBirthDate;
    }
    public void setCustBirthDate(String BirthDate)
    {
        this.custBirthDate = BirthDate;
    }
    public String getCustGender()
    {
        return custGender;
    }
    public void setCustGender(String gender)
    {
        this.custGender = gender;
    }
    public String getCustAddr1()
    {
        return custAddr1;
    }
    public void setCustAddr1(String addr1)
    {
        this.custAddr1 = addr1;
    }
    public String getCustAddr2()
    {
        return custAddr2;
    }
    public void setCustAddr2(String addr2)
    {
        this.custAddr2 = addr2;
    }
    public String getCustCity()
    {
        return custCity;
    }
    public void setCustCity(String city)
    {
        this.custCity = city;
    }
    public String getCustState()
    {
        return custState;
    }
    public void setCustState(String state)
    {
        this.custState = state;
    }
    public String getCustZip()
    {
        return custZip;
    }
    public void setCustZip(String zip)
    {
        this.custZip = zip;
    }

    public void setCustPlan(String plan)
    {
        this.custPlan = plan;
    }

    public String getCustPlan()
    {
        return custPlan;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppId()
    {
        return this.appId;
    }

    public void setClaimCount(String ClaimCount)
    {
        this.claimCount = ClaimCount;
    }

    public String getClaimCount()
    {
        return claimCount;
    }
}
    