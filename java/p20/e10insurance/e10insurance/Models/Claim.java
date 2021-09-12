package p20.e10insurance.e10insurance.Models;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class Claim
{     
 
    private int id;  
    private String ClaimIdNumber; 
    private String ClaimDescription;
    private String CustomerId;
    private String PlanId;
    private String PatientFirst;
    private String PatientLast;
    private String Diagnosis1;
    private String Diagnosis2;
    private String Procedure1;
    private String Procedure2;
    private String Procedure3;
    private String Physican;
    private String Clinic;
    private String DateService;   
    private String Service;
    private String Location;
    private String TotalCharge;
    private String CoveredAmount;
    private String BalanceOwed;
    private String PaymentAmount;
    private String PaymentDate;
    private String DateAdded;
    private String AdjustedClaimId;
    private String AdjustingClaimId;
    private String AdjustedDate;
    private String AppAdjusting;
    private String ClaimStatus;
    private String Referral;
    private String PaymentAction;
    private String ClaimType;
    private String DateConfine;
    private String DateRelease;
    private String ToothNumber;
    private String DrugName;
    private String Eyeware;  

    public List<String> medservices;
    public List<String> denservices;
    public List<String> visservices;
    public List<String> drgservices;

    private String medselected;
    private String denselected;
    private String visselected;
    private String drgselected; 

    Claim()
    {

         medservices = new ArrayList<String>();
         denservices = new ArrayList<String>();
         visservices = new ArrayList<String>();
         drgservices = new ArrayList<String>(); 

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
 
  
    public String getClaimIdNumber()
    {
        return ClaimIdNumber;
    }

    public void setClaimIdNumber(String ClaimIdNumber)
    {
        this.ClaimIdNumber = ClaimIdNumber;
    }

    public String getClaimDescription()
    {
        return ClaimDescription;
    }

    public void setClaimDescription(String ClaimDescription)
    {
        this.ClaimDescription = ClaimDescription;
    }
    public String getCustomerId()
    {
        return CustomerId;
    }

    public void setCustomerId(String CustomerId)
    {
        this.CustomerId = CustomerId;
    }
 
    public String getPlanId()
    {
        return PlanId;
    }

    public void setPlanId(String PlanId)
    {
        this.PlanId = PlanId;
    }

    public String getPatientFirst()
    {
        return PatientFirst;
    }

    public void setPatientFirst(String PatientFirst)
    {
        this.PatientFirst = PatientFirst;
    }

    public String getPatientLast()
    {
        return PatientLast;
    }

    public void setPatientLast(String PatientLast)
    {
        this.PatientLast = PatientLast;
    }

    public String getDiagnosis1()
    {
        return Diagnosis1;
    }

    public void setDiagnosis1(String Diagnosis1)
    {
        this.Diagnosis1 = Diagnosis1;
    }

    public String getDiagnosis2()
    {
        return Diagnosis2;
    }

    public void setDiagnosis2(String Diagnosis2)
    {
        this.Diagnosis2 = Diagnosis2;
    }

    public String getProcedure1()
    {
        return Procedure1;
    }

    public void setProcedure1(String Procedure1)
    {
        this.Procedure1 = Procedure1;
    }

    public String getProcedure2()
    {
        return Procedure2;
    }

    public void setProcedure2(String Procedure2)
    {
        this.Procedure2 = Procedure2;
    }

    public String getProcedure3()
    {
        return Procedure3;
    }

    public void setProcedure3(String Procedure3)
    {
        this.Procedure3 = Procedure3;
    }

    public String getPhysician()
    {
        return this.Physican;
    }

    public void setPhysician(String Physician)
    {
        this.Physican = Physician;
    }

    public String getClinic()
    {
        return this.Clinic;
    }

    public void setClinic(String Clinic)
    {
        this.Clinic = Clinic;
    }

    public String getDateService()
    {
        return this.DateService;
    }

    public void setDateService(String DateService)
    {
        this.DateService = DateService;
    }

    
    public String getService()
    {
        return this.Service;
    }

    public void setService(String Service)
    {
        this.Service = Service;
    }

    public String getLocation()
    {
        return this.Location;
    }

    public void setLocation(String Location)
    {
        this.Location = Location;
    }

    public String getTotalCharge()
    {
        return this.TotalCharge;
    }

    public void setTotalCharge(String TotalCharge)
    {
        this.TotalCharge = TotalCharge;
    }

    public String getCoveredAmount()
    {
        return this.CoveredAmount;
    }

    public void setCoveredAmount(String CoveredAmount)
    {
        this.CoveredAmount = CoveredAmount;
    }

    public String getBalanceOwed()
    {
        return this.BalanceOwed;
    }

    public void setBalanceOwed(String BalanceOwed)
    {
        this.BalanceOwed = BalanceOwed;
    }

    public String getPaymentAmount()
    {
        return this.PaymentAmount;
    }

    public void setPaymentAmount(String PaymentAmount)
    {
        this.PaymentAmount = PaymentAmount;
    }

    public String getPaymentDate()
    {
        return this.PaymentDate;
    }

    public void setPaymentDate(String PaymentDate)
    {
        this.PaymentDate = PaymentDate;
    }

    public String getDateAdded()
    {
        return this.DateAdded;
    }

    public void setDateAdded(String DateAdded)
    {
        this.DateAdded = DateAdded;
    }

    public String getAdjustedClaimId()
    {
        return this.AdjustedClaimId;
    }

    public void setAdjustedClaimId(String AdjustedClaimId)
    {
        this.AdjustedClaimId = AdjustedClaimId;
    }

    public String getAdjustingClaimId()
    {
        return this.AdjustingClaimId;
    }

    public void setAdjustingClaimId(String AdjustingClaimId)
    {
        this.AdjustingClaimId = AdjustingClaimId;
    }

    public String getAdjustedDate()
    {
        return this.AdjustedDate;
    }

    public void setAdjustedDate(String AdjustedDate)
    {
        this.AdjustedDate = AdjustedDate;
    }

    public String getAppAjusting()
    {
        return this.AppAdjusting;
    }

    public void setAppAdjusting(String AppAdjusting)
    {
        this.AppAdjusting = AppAdjusting;
    }

    public String getClaimStatus()
    {
        return this.ClaimStatus;
    }

    public void setClaimStatus(String ClaimStatus)
    {
        this.ClaimStatus = ClaimStatus;
    }

    public String getReferral()
    {
        return this.Referral;
    }

    public void setReferral(String Referral)
    {
        this.Referral= Referral;
    }

    public String getPaymentActon()
    {
        return this.PaymentAction;
    }

    public void setPaymentAction(String PaymentAction)
    { 
        this.PaymentAction = PaymentAction;
    }

    public String getClaimType()
    {
       var value = this.ClaimType;
       if(value == null)
       {
           value = "?";
       }
       return value;
    }

    public void setClaimType(String ClaimType)
    { 
        this.ClaimType = ClaimType;
    }

    public String getDateConfine()
    {
        return this.DateConfine;
    }

    public void setDateConfine(String DateConfine)
    { 
        this.DateConfine = DateConfine;
    }

    public String getDateRelease()
    {
        return this.DateRelease;
    }

    public void setDateRelease(String DateRelease)
    { 
        this.DateRelease = DateRelease;
    }

    public String getToothNumber()
    {
        return this.ToothNumber;
    }

    public void setToothNumber(String ToothNumber)
    { 
        this.ToothNumber = ToothNumber;
    }

    public String getDrugName()
    {
        return this.DrugName;
    }

    public void setDrugName(String DrugName)
    { 
        this.DrugName = DrugName;
    }

    public String getEyeware()
    {
        return this.Eyeware;
    }

    public void setEyeware(String Eyeware)
    { 
        this.Eyeware = Eyeware;
    }

    // utility
    public String getNames()
    {
        return this.PatientFirst + " " + this.PatientLast;
    }

    public String getIdStatusFields()
    {
        return this.ClaimIdNumber + ":" + this.ClaimStatus;
    }

    public boolean isMedicalClaim()
    {
        return ClaimType.equals("m");
    }

    public boolean isDentalClaim()
    {
        return ClaimType.equals("d");
    }

    public boolean isVisionClaim()
    {
        return ClaimType.equals("v");
    }

    public boolean isDrugClaim()
    {
        return ClaimType.equals("x");
    }

    // we only add 1 entry at a time top or next.
    // top is for adjustments with pre-selected services.

    public void addMed(String service, Integer position)
    {
         this.medservices.add(position, service);

    }
    public List<String> getMedServices()
    {
        return medservices;

    }

    public void setMedSelected(String selected)
    {
        medselected = selected;

    }

    public String getMedSelected()
    {

        return medselected;
    }


    public void addDen(String service, Integer position)
    {
         this.denservices.add(position, service);

    }
 
 

    public List<String> getDenServices()
    {
        return denservices;

    }

    public void setDenSelected(String selected)
    {
        denselected = selected;

    }

    public String getDenSelected()
    {

        return denselected;
    }

    public void addVis(String service, Integer position)
    {
         this.visservices.add(position, service);

    }
 

    public List<String> getVisServices()
    {
        return visservices;

    }

    public void setVisSelected(String selected)
    {
        visselected = selected;

    }

    public String getVisSelected()
    {

        return visselected;
    }

    public void addDrg(String service, Integer position)
    {
         this.drgservices.add(position, service);

    }
 

    public List<String> getDrgServices()
    {
        return drgservices;

    }

    public void setDrgSelected(String selected)
    {
        drgselected = selected;

    }

    public String getDrgSelected()
    {

        return drgselected;
    }


    public void clear()
    {
        this.ClaimIdNumber = "";
        this.ClaimDescription = "";
        this.CustomerId = "";
        this.PlanId = "";
        this.PatientFirst = "";
        this.PatientLast = "";
        this.Diagnosis1 = "";
        this.Diagnosis2 = "";
        this.Procedure1 = "";
        this.Procedure2 = "";
        this.Procedure3 = "";
        this.Physican = "";
        this.Clinic = "";
        this.DateService = "";  
        this.Service = "";
        this.Location = "";
        this.TotalCharge = "";
        this.CoveredAmount = "";
        this.BalanceOwed = "";
        this.PaymentAmount = "";
        this.PaymentDate = "";
        this.DateAdded = "";
        this.AdjustedClaimId = "";
        this.AdjustingClaimId = "";
        this.AdjustedDate = "";
        this.AppAdjusting = "";
        this.ClaimStatus = "";
        this.Referral = "";
        this.PaymentAction = "";
        this.ClaimType = "";
        this.DateConfine = "";
        this.DateRelease = "";
        this.ToothNumber = "";
        this.DrugName = "";
        this.Eyeware =  "";
    }
 
}