package p20.e10insurance.e10insurance.Models;

import org.springframework.stereotype.Component;

@Component
public class AppAlert {
    
    public String message;
    public String detailMessage;

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }
}
