package p20.e10insurance.e10insurance.PassedParameters;

import org.springframework.stereotype.Component;

@Component
public class FormattedDates {

    private String databaseFormat;
    private String slashedFormat;
    private String condensedFormat;
    private String enteredDate;

    public void Init()
    {
        databaseFormat = "";
        slashedFormat = "";
        condensedFormat = "";
    }

    public void setDatabaseFormat(String d)
    {
        this.databaseFormat = d;
    }
    public String getDatabaseFormat() {
        return databaseFormat;
    }

    public void setSlashedFormat(String d)
    {
        this.slashedFormat = d;
    }
    public String getSlashedFormat() {
        return slashedFormat;
    }

    public void setCondensedFormat(String d)
    {
        this.condensedFormat = d;
    }
    public String getCondensedFormat() {
        return condensedFormat;
    }

    public void setEnteredDate(String enteredDate)
    {
        this.enteredDate = enteredDate;
    }
    public String getEnteredDate()
    {
        return this.enteredDate;
    }
    
}
