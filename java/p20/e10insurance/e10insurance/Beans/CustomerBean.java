package p20.e10insurance.e10insurance.Beans;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component 
@SessionScope 
public class CustomerBean {

    /* transport customer data to update screen */ 
    
    private String custid; 
    private String custpassword;
    private String custfirst;
    private String custlast; 
    private String custconfirm;
    private String custpromo;
    private String custmiddle;
    private String custemail;
    private String custphone;
    private String custbirthdate;
    private String custgender;
    private String custaddr1;
    private String custaddr2;
    private String custcity;
    private String custState;
    private String custzip;
    private String custplan;

    private String custConfirm; 
    private String claimCount;

    public CustomerBean()
    {
        custid = "";
        custpassword = "";
        
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getCustpassword() {
        return custpassword;
    }

    public void setCustpassword(String custpassword) {
        this.custpassword = custpassword;
    }

    public String getCustfirst()
    {
        return custfirst;
    }

    public void setCustfirst(String custFirst)
    {
        this.custfirst = custFirst;
    }

    public String getCustlast()
    {
        return custlast;
    }

    public void setCustlast(String custlast)
    {
        this.custlast = custlast;
    }

    public String getCustconfirm()
    {
        return custconfirm;
    }

    public void setCustconfirm(String custConfirm)
    {
        this.custconfirm = custConfirm;
    }

    public String getCustpromo()
    {
        return custpromo;
    }

    public void setCustpromo(String custpromo)
    {
        this.custpromo = custpromo;
    }
  

    public String getCustmiddle()
    {
        return custmiddle;
    }

    public void setCustmiddle(String custMiddle)
    {
        this.custmiddle = custMiddle;
    }

    public String getCustemail()
    {
        return custemail;
    }
    public void setCustemail(String custemail)
    {
        this.custemail = custemail;
    }
    public String getCustphone()
    {
        return custphone;
    }
    public void setCustphone(String custphone)
    {
        this.custphone = custphone;
    }
    public String getCustbirthdate()
    {
        return custbirthdate;
    }
    public void setCustbirthdate(String birthdate)
    {
        this.custbirthdate = birthdate;
    }
    public String getCustgender()
    {
        return custgender;
    }
    public void setCustgender(String gender)
    {
        this.custgender = gender;
    }
    public String getCustaddr1()
    {
        return custaddr1;
    }
    public void setCustaddr1(String addr1)
    {
        this.custaddr1 = addr1;
    }
    public String getCustaddr2()
    {
        return custaddr2;
    }
    public void setCustaddr2(String addr2)
    {
        this.custaddr2 = addr2;
    }
    public String getCustcity()
    {
        return custcity;
    }
    public void setCustcity(String city)
    {
        this.custcity = city;
    }
    public String getCuststate()
    {
        return custState;
    }
    public void setCuststate(String state)
    {
        this.custState = state;
    }
    public String getCustzip()
    {
        return custzip;
    }
    public void setCustzip(String zip)
    {
        this.custzip = zip;
    }
    // not present on customer entity but for screen present here.
    public void setConfirm(String custConfirm)
    {
        this.custcity = custconfirm;
    }
    public String getCustConfirm()
    {
        return custConfirm;
    }

    
    public void setClaimCount(String ClaimCount)
    {
        this.claimCount = ClaimCount;
    }

    public String getClaimCount()
    {
        return claimCount;
    }

    public void setCustPlan(String plan)
    {
        this.custplan = plan;
    }

    public String getCustPlan()
    {
        return this.custplan;
    }
 
} 
