package p20.e10insurance.e10insurance.Models;

import org.springframework.stereotype.Component;

@Component
public class Plan {

    private String PlanName;
    private String PlanLiteral;
    private String Percent;

    public void setPlanName(String planname)
    {
        PlanName = planname;
    }

    public String getPlanName()
    {
        return this.PlanName;
    } 

    public String getPlanLiteral()
    {
        return this.PlanLiteral;
    }

    public void setPlanLiteral(String literal)
    {
        this.PlanLiteral = literal;
    }

    public double getPercent()
    {
        double d = Double.parseDouble(Percent);
        return d; 
    }

    public void setPercent(String percent)
    { 
        this.Percent =  percent;
    }
    
}
