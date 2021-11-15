package p20.e10insurance.e10insurance.Models; 
import org.springframework.stereotype.Component;

@Component
public class Settings {
    
    private String name;
    private String state;

    Settings() {

    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        name = value;
    } 

    public String getState()
    {
        return state;
    }

    public void setState(String value)
    {
        state = value;
    }



}
