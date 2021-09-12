package p20.e10insurance.e10insurance.Models;

import org.springframework.stereotype.Component;

@Component
public class Signin {
    
    private String custid; 
    private String custpass;

    public Signin()
    {
        
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getCustpass() {
        return custpass;
    }

    public void setCustpass(String custpass) {
        this.custpass = custpass;
    }

    
}
    