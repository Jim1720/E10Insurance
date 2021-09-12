package p20.e10insurance.e10insurance.Beans; 
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import p20.e10insurance.e10insurance.Enums.DateRange.DateRangeValues;
import p20.e10insurance.e10insurance.PassedParameters.FormattedDates;
import p20.e10insurance.e10insurance.PassedParameters.GeneralParm;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
@Component
@RequestMapping

public class EditBean { 

    EditBean()
    {
        
    }
    
    public enum dataFieldRequirements { name1, name2, addr2, phone, email, date1, mid1,
         zip1, passupdate1, allnumeric, passadminactions, name3, addr1 };
 
    public String CheckData(String Value, String Name, dataFieldRequirements dfr)
    {
        // compbines empty and valid data checks into one routine.
        // usage: CheckData(name,vlaue, dataFieldRequiements)
        String empty = "";
        boolean allowSpaces = false;
        
        switch(dfr)
        {
            case addr2: allowSpaces = true; break;
            case mid1: allowSpaces = true; break;
            default: break;
        }
         

        if(Value == null || Value.equals(empty))
        {
            if(allowSpaces) return "";
            return Name + ", ";
        }
        if(!ValidData(Value, dfr))
        {
            return Name + ", ";
        }
        return "";
    }

    
    public Boolean ValidData(String value, dataFieldRequirements dfr)
    {
        String pattern = "";  

       

 
        var name1 = "^[a-zA-Z0-9]+$";   // cust id / password  1.more
		var name2 = "^[a-zA-Z0-9.#\\s]+$"; // city names  
        var name3 = "^[a-zA-Z0-9.#\\s-]+$"; // general names first, last, doc etc.
		var phone = "^[0-9]{10}|([0-9]{3})[0-9]{3}-[0-9]{4}$";
		var email =  "^[0-9a-zA-Z]+@[0-9a-zA-Z]+.{1}[0-9a-zA-Z]+$";
		var date1 = "^[0-9/]+$"; // dob     
        var zip1 = "^[a-zA-Z0-9/s]+$"; 
        var passadmactions = "^[a-zA-Z0-9.@!$*+-]*$";   // for adm actions
        var allnumeric1 = "^[0-9]+$"; 
 
        var addr1 = "^[a-zA-Z0-9.#\\s.@!$*+-]+$"; // use + addr1 has to be there
		var addr2 = "^[a-zA-Z0-9.#\\s.@!$*+-]*$"; // addr 2 is not req allow space. * o,more , clinic
		var mid1 = "^[a-zA-z\\s-@%+&]*$";  // * optional 


        switch(dfr)
        {
            case name1: pattern = name1; break;
            case name2: pattern = name2; break;
            case addr1: pattern = addr1; break;
            case addr2: pattern = addr2; break;
            case phone: pattern = phone; break;
            case email: pattern = email; break;
            case date1: pattern = date1; break;
            case mid1:  pattern = mid1; break; 
            case zip1:  pattern = zip1; break; 
            case name3: pattern = name3; break;
            case allnumeric: pattern = allnumeric1; break;
            case passadminactions: pattern = passadmactions; break;
            default:   pattern = name1; break; 
        }

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        return m.matches(); 
    }

 
    public void EditDate(String Value, DateRangeValues dateRange, GeneralParm g, FormattedDates fd) 
    {
        /* return code 0 is ok */

        /* checks placement of slashes and digits
           veriies valid m d y ranges
           supplies century if not keyed
           smallRange = plus minus 1 year; otherwise birth dates of 120 years */

        /* formats:  mmddyy   mmddyyyy  */
        /* formats: mm/dd/yy  mm/dd/yyyy */

        /* prerequesite: other date edit verified only slashes and digits */

        /* General parm will contain true/false and an mesage if error found */
 
            fd.Init();

        g.setMessage("OK");
        g.setStatus(true);
        fd.setEnteredDate(Value);

        var slashesused = Value.indexOf("/") > -1;
        String mm, dd, yy;
        mm = "";
        dd = "";
        yy = "";

        var digits1 = "^[0-9]+$"; // dob   
 

        if(!slashesused)
        {
            // mmddyy or mmddyyyy 
            var length = Value.trim().length();  
            if(length != 6 && length != 8)
            { 
                g.setMessage("Date: invalid length");
                return;
            }     
            mm = Value.substring(0,2);
            dd = Value.substring(2,4);
            yy = Value.substring(4);
        }
        else
        {
            var firstSlash = Value.indexOf("/");
            // begins ends with slash - invalid
            if(firstSlash == 0 || firstSlash == Value.length() - 01)
            {
                g.setMessage("Invalid Date - can not start with slash");
                return;
            }
            mm = Value.substring(0, firstSlash);
            var secondSlash = Value.indexOf("/",firstSlash + 01); // check nothing after
            // nothing between - invalid
            if(secondSlash == firstSlash + 01)
            {
                g.setMessage("Invalid Date - consecutive slashes");
                return;
            }
            dd = Value.substring(firstSlash + 01, secondSlash);
            // we know it does not end with slash so get year
            yy = Value.substring(secondSlash + 01); 
            // note: we have variable length values and
            // each length must be checked
            var mml = mm.length();
            var ddl = dd.length();
            var yyl = yy.length();
            // verify proper lengths
            var mmlok = mml == 1 || mml == 2;
            var ddlok = ddl == 1 || ddl == 2;
            var yylok = yyl == 2 || yyl == 4; 
            if(!mmlok || !ddlok || !yylok)
            {
                g.setMessage("Invalid Date - too many digits on m,d, or y.");
                return;
            }
        }
        // we know format is valid check for numerics.
        // safety recheck incase coding issue in calls.

        Pattern p = Pattern.compile(digits1);
        Matcher mma = p.matcher(mm);
        Matcher dda = p.matcher(dd);
        Matcher yya = p.matcher(dd);
        var notNumeric = !mma.matches() || !dda.matches() || !yya.matches();
        if(notNumeric)
        {
            g.setMessage("Invalid Date - not numeric.");
            return;
        }
        // range checks
        Integer m = Integer.valueOf(mm);
        Integer d = Integer.valueOf(dd);
        Integer y = Integer.valueOf(yy);
        //
        var validMonth = (m >= 1 && m <= 12);
        if(!validMonth)
        {
            g.setMessage("Invalid Date - invalid month out of range.");
            return;
        }
        
        boolean shortMonth = (m == 9 || m == 6 || m == 4 || m == 11);
        boolean leapYear = (y % 4) == 0;
        boolean feb = (m == 2);
        var normalMonth = 31;
        
        Integer dayMax = normalMonth;
        if(shortMonth)
        {
            dayMax = 30;
        }
        else if(feb)
        {
            dayMax = leapYear ? 29 : 28;
        } 

        var dayInRange =  d >= 1 && d <= dayMax;
        if(!dayInRange)
        {
            g.setMessage("Invalid Date - invalid day out of range.");
            return;
        }
        // year range : short range ??

        // current next month
        Date da = new Date(); 
        SimpleDateFormat yearof  = new SimpleDateFormat("yyyy"); 
        SimpleDateFormat monthof = new SimpleDateFormat("MM"); 
        String  curMonthString = monthof.format(da);
        Integer curMonth = Integer.valueOf(curMonthString); 
        var     nextMonth = (curMonth == 12) ? 1 : curMonth + 1; 
        String  curYString = yearof.format(da);   
        Integer curYear = Integer.valueOf(curYString);
        Integer curYearYY = Integer.valueOf(curYString.substring(2));
        Integer nextYearYY = curYearYY + 1;
        String  curCString = curYString.substring(0,2);
        Integer curCentury = Integer.valueOf(curCString);
        Integer nextYear = curYear + 1; 
        

        // if short date keyed supply century 20. for 1900 dates the 19 must be keyed.

        int needToAddCentury = y < 1500 ? 1 : 0;
        int whichCentury = y > nextYearYY  ?  1900 : 2000;
        y = y + needToAddCentury * whichCentury; 
        var year = y; // readability.
        var month = m; // readability.

        // pad back to a 4 digits
        var yy4 = y.toString();
        var mOut = m.toString(); // remove zeros use integer value;
        var dOut = d.toString(); // remove zeros use integer value;

        // birth dates can be 120 years or so, other dates such
        // as date of service should be with one year back or one year forward.


        // SmallRange =  
        // 1 - Data of Birth 1900's or 1 month in future for medicare ?
        // 2 - Date of Service - 1 years old or one year in future
        // 0 - unlimited - not used 
        // get the input century in integer format to check date range below.
        String  stringY = y.toString();
        String  centY = stringY.substring(0,2); 
        Integer century = Integer.valueOf(centY);  
        // when short form of year used - check if greater than 1 month
        // in future : true: use last century
        // ex. 010150 goes to 1950.  010120 goes to 2010. 
        if(year > nextYear && dateRange == DateRangeValues.BIRTHDATE )
        {
            century--; // use last century
        } 
        boolean rangeOK = true;
        // year, month refere to input values.
        switch(dateRange)
        {
            case UNLIMITED: break; // skip

            case BIRTHDATE : // date of birth

                     var inLastCentury = century == curCentury - 1;
                     var inCurrentCenturyBeforeThisYear = century.equals(curCentury)
                            && year < curYear;
                     var inThisYear =  year.equals(curYear);
                     var acceptableMonth = month <= nextMonth;

                     rangeOK = (inLastCentury || inCurrentCenturyBeforeThisYear ||
                               (inThisYear && acceptableMonth));   
                    break;

            case DATESERVICE : // service , confine, and release dates. 
                     var lastThisOrNextYear = year >= curYear - 1 && year <= curYear + 1;
                     rangeOK = lastThisOrNextYear;
                     break;
        } 
        if(!rangeOK)
        {
            switch(dateRange)
            {
                case UNLIMITED : break; // skip

                case BIRTHDATE : // date of birth
                         g.setMessage("Invalid Birth Date - year must be last century or up to 1 month in future."); 
                         break;
                         
                case DATESERVICE : // service , confine, and release datess. 
                         g.setMessage("Invalid Date - year should be last, current, or next.");
                         break;
            }
        }

        // format outputs...
        var slash = '/';
        var dash = '-';
        // mmddyy
        var condensed = mOut + dOut + yy;
        //  mm/dd/yy 
        var slashed   = mOut + slash + dOut + slash + yy;
        // yyyy-mm-dd
        var mmba = (mm.length() == 1) ? "0" + mm : mm;
        var ddba = (dd.length() == 1) ? "0" + dd : dd;
        //
        var database = yy4 + dash + mmba + dash + ddba;
        //
        // store in FormattedDates for use by caller. 
        fd.setCondensedFormat(condensed);
        fd.setSlashedFormat(slashed);
        fd.setDatabaseFormat(database); 
        //
        return;
    }
}
