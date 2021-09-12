package p20.e10insurance.e10insurance.PassedParameters;

import org.springframework.stereotype.Component;

@Component
public class GeneralParm {


    // allows message and true/false status to be passed and returned.

    private boolean status;
    private String message;

    public  void setStatus(boolean status) {
        this.status = status;
    }

    public  boolean getStatus()
    {
        return this.status;
    }

    public void setMessage(String message)
    {
        this.message = message;
        this.status = false; // convenience. 
    }

    public String getMessage()
    {
        return this.message;
    }
    
}
