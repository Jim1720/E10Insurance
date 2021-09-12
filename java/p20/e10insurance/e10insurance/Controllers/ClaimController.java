package p20.e10insurance.e10insurance.Controllers; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
 
import p20.e10insurance.e10insurance.Beans.CustomerBean;
import p20.e10insurance.e10insurance.Beans.EditBean;
import p20.e10insurance.e10insurance.Beans.ServiceBean;
import p20.e10insurance.e10insurance.Beans.SessionBean;
import p20.e10insurance.e10insurance.Models.Claim;
import p20.e10insurance.e10insurance.Models.Service;
import p20.e10insurance.e10insurance.PassedParameters.CostParm;
import p20.e10insurance.e10insurance.PassedParameters.FormattedDates;
import p20.e10insurance.e10insurance.PassedParameters.GeneralParm;
import p20.e10insurance.e10insurance.PassedParameters.StampData;
import p20.e10insurance.e10insurance.Services.CostCalculator;
 
import p20.e10insurance.e10insurance.Enums.DateRange.DateRangeValues;

@Controller
public class ClaimController {

     //* Url Prefix 

     @Value("${E10UrlPrefix:notSet}")
     String urlPrefix;

     @Autowired
     private SessionBean sessionBean;

     @Autowired
     private CustomerBean customerBean;


     @Autowired
     private Claim claim;  

     @Autowired
     RestTemplate restTemplate;

     @Autowired
     private EditBean editBean;

     @Autowired
     private CostParm costParm;

     @Autowired
      CostCalculator costCalculator;

      @Autowired
      ServiceBean serviceBean;

      @Autowired
      GeneralParm generalParm; 

      @Autowired 
      FormattedDates formattedDates;

     // hold services for this claim type
     List<Service> typeServices = new ArrayList<Service>();

     String serviceDefault = "";
     

     @RequestMapping(value = "/claim", method = RequestMethod.GET)
     public String ShowClaimInputScreen(Model model, @ModelAttribute Claim claim)
     {  

         if(!sessionBean.GetSignedIn())
         {
            // shows must be signed in screen with
            // a button to return to start 
            // valid signin check  
            sessionBean.SetRedirect("/unauth");  
            return "layout";
         }  

         // turn off adjustement flags
         var none = "";
         sessionBean.setAdjustedId(none);
         sessionBean.setAdjustingId(none);
  
         claim.clear(); // set fields to spaces.
         claim.setClaimType("m");

         ServiceInterface(claim);  
         
         var message = "Enter New Claim Information.";
         sessionBean.SetMessage(message);

         model.addAttribute("Claim", claim);
         sessionBean.SetRedirect("/claim");
         return "layout";
     }

     @RequestMapping(value = "/adjustment", method = RequestMethod.GET)
     public String ShowCLaimToAdjust(Model model, @ModelAttribute Claim claim)
     {  

         if(!sessionBean.GetSignedIn())
         {
            // shows must be signed in screen with
            // a button to return to start 
            // valid signin check  
            sessionBean.SetRedirect("/unauth");  
            return "layout";
         } 

         

         //* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
         //  - produce new claim id - store it in sesson bean for return trip.
         //  - read and display selected claim fields.
         //    accomodate type logic.
         //* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

         // history screen stores selected id for adjustment when prompted.
         var adjustedId = sessionBean.getAdjustedId();

         // now produce and store adjusting id 
         Date d = new Date(); // current
         // generate a claim id for the adjustment.
         //
         // CL-MM-dd-yy-H:mm:ss
         SimpleDateFormat idfmt = new SimpleDateFormat("MM-dd-yy-hh:mm:ss");
         var adjustingId = idfmt.format(d);
         adjustingId = "CL-" + adjustingId;  
         sessionBean.setAdjustingId(adjustingId);

         // the claim will be read from database and stord in 'claim' object.
         Object ret[] = ReadClaim(adjustedId);
         Integer code = Integer.valueOf(ret[0].toString());
         if(code > 0)
         {
            var message = "Adjustment Process 0001: Unable to read claim to adjust (" + code + ").";
            // goto error screen since nothing to display yet.

            // do we return to history where this started or go somewhere else? 
            sessionBean.SetMessage(message);
            var message2 = "Attempt to read claim " + adjustedId;
            sessionBean.setMessageDetail(message2);
            sessionBean.SetRedirect("/appalert"); //general.htm shows general errors
            // use general error screen to convey message 
            return "layout";
         } 
         
         Claim a  = (Claim) ret[1]; 
         
         // trim all trailing spaces off fields before screen display.

         claim.setClaimType(a.getClaimType().trim());

         claim.setCustomerId(a.getCustomerId().trim());
         claim.setPatientFirst(a.getPatientFirst().trim());
         claim.setPatientLast(a.getPatientLast().trim());
         claim.setClaimDescription(a.getClaimDescription().trim());
         claim.setDiagnosis1(a.getDiagnosis1().trim());
         claim.setProcedure1(a.getProcedure1().trim());
         claim.setPhysician(a.getPhysician().trim());
         claim.setClinic(a.getClinic().trim());
         claim.setService(a.getService().trim());

         // set up type fields
         String type = a.getClaimType().trim();
         switch(type)
         {

            case "m":

                // format dates no matter what type 1900's to blanks on screen.
               var con = a.getDateConfine().trim();
               claim.setDateConfine(ScreenFormat(con));

               var rel = a.getDateRelease().trim();
               claim.setDateRelease(ScreenFormat(rel)); 

               break;

            case "d":

               claim.setToothNumber(a.getToothNumber().trim());
               break;

            case "v":

               claim.setEyeware(a.getEyeware().trim());
               break;

            case "x":

               claim.setDrugName(a.getDrugName().trim());
               break;

            default: break;

         }

         // format service date
         var dos = a.getDateService().trim();
         claim.setDateService(ScreenFormat(dos)); 

         // accomodate type logic when coded. 

         ServiceInterface(claim); // sets up service combos 

         var message = "Enter claim " + adjustingId + " to adjust claim " + adjustedId + ".";
         sessionBean.SetMessage(message);
        
         sessionBean.SetRedirect("/claim");
         return "layout";
     }

     private Object[] ReadClaim(String adjustedId)
     {
        // object 1 - is a integer code
        // object 2 - claim data returned or null
        Object[] ret = { 0, null};

        
        Integer code = 0;  // good call
        //*= = = = = = = call server to add custome and check results - - - -  
        String prefix = urlPrefix; 
        String url = prefix + "/claim/" + adjustedId; 

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Claim> response = null;

        try
        { 

            response = restTemplate.exchange(url,HttpMethod.GET,entity,Claim.class);
            var error = response.getStatusCode() != HttpStatus.FOUND;
            if (error)
            {
                code = 1; 
            } 
        }
        catch(HttpClientErrorException e)
        {
            code = 2; // not found
             
        }
        catch(Exception e)
        {

            if(response == null)
            {
                 code = 3;
            }
            else
            { 
               code = 4; 
            } 
        }

        //* check for an error.
  
        var errorFound = code > 0;

        if(errorFound)
        {
           ret[0] = code;
           return ret;
        }

        ret[0] = 0; // good result. emphasis. 
        // - success  - update passed paramter claim.
        ret[1]  = response.getBody();  
        return ret; 
     }

     private String ScreenFormat(String date)
     { 
         var year = date.substring(0,4);
         var SHOW_EMPTY_FOR_DEFAULT_YEAR_1900 = "";
         var DEFAULT_YEAR = "1900";
         var DEFAULT_YEAR2 = "1753";

         var  output = year.equals(DEFAULT_YEAR) || 
                       year.equals(DEFAULT_YEAR2) ? SHOW_EMPTY_FOR_DEFAULT_YEAR_1900 :
                       
                      date.substring(5,7) + date.substring(8,10)  +
                      date.substring(0,4); 
        
        return output;

     }

     // new claim and adjustements use Service Interface to load combos of services.
     private void ServiceInterface(Claim claim)
     { 
         // 1. make sure services are loaded
         if(!serviceBean.isLoaded())
         {
            serviceBean.LoadServices();
         }
         // 2. pass service list and claim to load combos.
         LoadServiceListBoxes(claim, serviceBean.getAllServices());
     }

     @RequestMapping(value = "/claim", method = RequestMethod.POST)  
     public String ClaimAddController(Model model, @ModelAttribute Claim claim) 
     {  
        
           var message = ""; 

        // on adjustements NCHAR has training blanks so we 
        var patientFirst = claim.getPatientFirst();
        var patientLast = claim.getPatientLast();
        var description = claim.getClaimDescription();
        var procedure1 = claim.getProcedure1();
        var diagnosis1 = claim.getDiagnosis1();

        var physician = claim.getPhysician();
        var clinic = claim.getClinic(); 
        
        // get customer plan and put in the claim ; if not there ; show edit message.  
        var customerPlan = customerBean.getCustPlan();
        if(customerPlan == null || customerPlan.equals("n//a"))
        {
            message = "Use plan screen to select a customer plan before submitting claim.";
            model.addAttribute("Claim", claim);
            sessionBean.SetMessage(message);
            return "layout";
        } 
        
        message += editBean.CheckData(patientFirst,"Patient First",EditBean.dataFieldRequirements.name3); 
        message += editBean.CheckData(patientLast,"Patient Last",EditBean.dataFieldRequirements.name3);
        message += editBean.CheckData(description,"Description",EditBean.dataFieldRequirements.name3);

        // adjust these: too tight:
        message += editBean.CheckData(physician,"Physician",EditBean.dataFieldRequirements.name3); 
        message += editBean.CheckData(clinic,"Clinic",EditBean.dataFieldRequirements.addr2);

        // all numeric 
        message += editBean.CheckData(procedure1,"Procedure",EditBean.dataFieldRequirements.allnumeric); 
        message += editBean.CheckData(diagnosis1,"Diagnosis",EditBean.dataFieldRequirements.allnumeric);
 
        var dateService = claim.getDateService(); 
        String forDatabaseServiceDate = "";
        String forScreenServiceDate = "";
        editBean.EditDate(dateService, DateRangeValues.DATESERVICE , generalParm, formattedDates);
        if(!generalParm.getStatus())
        {
            message += " Service Date: " +  generalParm.getMessage() + ",";
            forScreenServiceDate = formattedDates.getEnteredDate(); 
        } 
        else
        {  
             forDatabaseServiceDate = formattedDates.getDatabaseFormat(); 
             forScreenServiceDate = formattedDates.getEnteredDate(); 
        }  

          
        if(claim.getClaimType().equals("?"))
        {
            claim.setClaimType("m");
            model.addAttribute("Claim", claim);
            sessionBean.SetMessage("unassigned claim type - null.");
            return "layout";
        }
 

        var dbConfineDate = "";
        var dbReleaseDate = "";
        var screenConfineDate = "";
        var screenReleaseDate = "";

        if(claim.isMedicalClaim())
        {
            var empty = "";
            // note: date edit routine must be used to completely vet dates.
            // note: blank dates default... they are optional. 
            String dateConfine = claim.getDateConfine().trim();
          
            if(!dateConfine.equals(empty))
            { 

               editBean.EditDate(dateConfine, DateRangeValues.DATESERVICE, generalParm, formattedDates);
               if(!generalParm.getStatus())
               {
                     message += " Date Confined: " +  generalParm.getMessage() + ","; 
                     screenConfineDate = formattedDates.getEnteredDate(); 
               } 
               else
               {  
                     dbConfineDate = formattedDates.getDatabaseFormat(); 
                     screenConfineDate = formattedDates.getEnteredDate(); 
               }   
            
            }
            String dateRelease = claim.getDateRelease().trim();
            if(!dateRelease.equals(empty))
            {
               
               editBean.EditDate(dateRelease, DateRangeValues.DATESERVICE, generalParm, formattedDates);
               if(!generalParm.getStatus())
               {
                     message += " Date Released: " +  generalParm.getMessage() + ",";
                     screenReleaseDate = formattedDates.getEnteredDate(); 
               } 
               else
               {  
                     dbReleaseDate = formattedDates.getDatabaseFormat(); 
                     screenReleaseDate = formattedDates.getEnteredDate(); 
               }   

            }
        }

        if(claim.isDentalClaim())
        {
           var tooth = claim.getToothNumber();
           message += editBean.CheckData(tooth,"Tooth Number",EditBean.dataFieldRequirements.allnumeric);
 
        }

        if(claim.isVisionClaim())
        {
           var eyeWare = claim.getEyeware();
           message += editBean.CheckData(eyeWare,"Eyeware",EditBean.dataFieldRequirements.addr1);
 
        }

        if(claim.isDrugClaim())
        {
           var drug = claim.getDrugName();
           message += editBean.CheckData(drug,"Drug Name",EditBean.dataFieldRequirements.addr1);
 
        }
 
        // set formatted service date.
        claim.setDateService(forDatabaseServiceDate);

        // get customer id and put it on the claim 
        var custId = customerBean.getCustid();
        claim.setCustomerId(custId); 



        Boolean haveErrors = (message.length() > 0);

        message = "Invalid or missing " + message; 
    
        if(haveErrors)
        {
            claim.setDateService(forScreenServiceDate);
            if(claim.isMedicalClaim()) 
            { 
               claim.setDateConfine(screenConfineDate);
               claim.setDateRelease(screenReleaseDate);
            }
            // refill service list boxes;
            ServiceInterface(claim);  
            //
            model.addAttribute("Claim", claim);
            String MEMU_SUPPRESS_FLAG = "*";
            message = MEMU_SUPPRESS_FLAG + message;
            if(message.endsWith(",")) // remove trailing comma
            {
               message =  message.substring(0, message.length() - 01);
            }
            if(message.endsWith(", ")) // remove trailing comma
            {
               message =  message.substring(0, message.length() - 02);
            }
            sessionBean.SetMessage(message);
            return "layout";
        };

        if(claim.isMedicalClaim()) 
        { 
           claim.setDateConfine(dbConfineDate);
           claim.setDateRelease(dbReleaseDate);
        }
 

        //  set date added
        Date d = new Date(); // current
        SimpleDateFormat currentof = new SimpleDateFormat("yyyy-MM-dd");
        var dateAdded = currentof.format(d);
        claim.setDateAdded(dateAdded);

        //* check adjustement status 
        var adjustingId = sessionBean.getAdjustingId(); 
        var adjustedId = sessionBean.getAdjustedId();  

        // if adjusted claim id is filled in this claim is an adjustment.
        String empty = "";
        var ClaimIsAdjustement = false;
        if(adjustedId != null && !adjustedId.equals(empty))
        {
             ClaimIsAdjustement = true;
        } 


        // waring: make sure all dates and mumeric fields are set so no sql errors.

        var tempTotalCharge = "0.0";
        claim.setTotalCharge(tempTotalCharge);

        var defaultDate = "1900-01-01"; 

        claim.setAdjustedDate(defaultDate);
        claim.setAdjustedClaimId("");
        claim.setAdjustingClaimId("");

        claim.setClaimStatus("Entered");

        var zero = "0.0";
         
        claim.setCoveredAmount(zero);
        claim.setBalanceOwed(zero);  
        claim.setPaymentAmount(zero);   

        // note: claim.Service populated by java script.
        // see: claimpage.js for details. 

        // set Plan
        claim.setPlanId(customerPlan); // for debug 

        /* Calculate coverd and owed amounts */
        costParm.setClaimType(claim.getClaimType());
        costParm.setPlan(claim.getPlanId());
        costParm.setService(claim.getService());

        costCalculator.CalculateCoveredCost(costParm);
        claim.setTotalCharge(costParm.getServiceCost());
        claim.setCoveredAmount(costParm.getCovered());
        claim.setBalanceOwed(costParm.getOwed());   

        claim.setLocation("");
        claim.setAdjustedClaimId("");
        claim.setAdjustingClaimId("");
        claim.setReferral("");
        claim.setPaymentAmount("0.0"); 
        claim.setPaymentDate(defaultDate);


        //* clear ununsed fields
        if(!claim.isMedicalClaim())
        {
            claim.setDateConfine(defaultDate);
            claim.setDateRelease(defaultDate);
        }
        if(!claim.isDentalClaim())
        {
            claim.setToothNumber("0");
        }
        if(!claim.isVisionClaim())
        {
           claim.setEyeware("");
        }
        if(!claim.isDrugClaim())
        {
           claim.setDrugName("");
        }
        //* end unused field clearings.

        //* set adjustment related data 
        if(ClaimIsAdjustement) 
        {
           // set claim status to adjustment and plug in adjusteId showing claim adjusted
           claim.setClaimStatus("Adjustment");
           claim.setAdjustedClaimId(adjustedId);
        } 
        
        // id="hiddenService" mapped to claim.service.
        var serve = claim.getService();
        if(serve != null)
        {
          claim.setService(serve);
        }
        /* debug */
        if(claim.getService() == null)
        {
           claim.setService("not set");
        }


        var claimId = "notSet";
        if(ClaimIsAdjustement)
        {
            var adjustmentId = sessionBean.getAdjustingId();
            claim.setClaimIdNumber(adjustmentId);
            claimId = adjustmentId;
        }
        else
        { 
            // generate a claim id 
            //
            // CL-MM-dd-yy-H:mm:ss
            SimpleDateFormat idfmt = new SimpleDateFormat("MM-dd-yy-hh:mm:ss");
            claimId = idfmt.format(d);
            claimId = "CL-" + claimId;
            claim.setClaimIdNumber(claimId);    
        }
      
       //*= = = = = = = call server to add custome and check results - - - -  
       String prefix = urlPrefix;
       String url = prefix + "/claim"; 
      // claim add token - - - - - - - - - - - - - - - - - - - - - 
      var token = sessionBean.getToken();
      var headers = new LinkedMultiValueMap<String,String>();  
      headers.set("E20Token",token); 
      // - - - - - - - - - - - - - - - - - - - - - - - - - - - -
       HttpEntity<Claim> request = new HttpEntity<Claim>(claim, headers);  
      // - - - - - - - - - - - - - - - - - - - - - - - -  
      try
      {
         ResponseEntity<Claim> response = restTemplate.exchange(url,HttpMethod.POST,request,Claim.class);
         var error = response.getStatusCode() != HttpStatus.OK;
         if (error)
         {
            message = "Error in create customer status: " + response.getStatusCode();
            model.addAttribute("Claim", claim);
            sessionBean.SetMessage(message);
            return "layout";
         } 
      }
      catch(Exception e)
      {
         message = "Bad Request - Possible Expiration.";
         model.addAttribute("Claim", claim);
         sessionBean.SetMessage(message);
         return "layout";
      }
       //* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
       //* if adjustment stamp the claim being adjusted with adjusted date and 
       //* this, the adjusting claim id number.

       if(ClaimIsAdjustement)
       { 
              Boolean success = StampAdjustedClaim(adjustingId, adjustedId, dateAdded);
              if(success)
              { 

                  sessionBean.SetMessage("Claim " + adjustingId + " adjusted " + adjustedId + ".");
                  //sessionBean.SetRedirect("/menu");  
                  //return "layout"; 
                   return "redirect:/menu";
  

              }
              else
              {
                  // show eror situation.
                  sessionBean.SetMessage("Error: Could not stamp claim: " + adjustedId);
                  sessionBean.SetRedirect("/claim");   
                  return "layout";
              }
       } 

      //* == = = = = = = = = = = = = = = = = = = == = = = = == = = = = = = =

      /*

       set claim added message 
       redirect to menu using the session bean redirect field
       used for new claim addition not adjustments

      */  
        
        sessionBean.SetMessage("Claim " + claimId + " added."); 
        // menu controller will show and clear message from sessionBean.
        return "redirect:/menu";
        //sessionBean.SetRedirect("/menu");   
        //return "layout";

     }
 

     private void LoadServiceListBoxes(Claim claim, List<Service> serviceList)
     {                         
     
           var hasPriorService = !claim.getService().equals("");
           var priorService = claim.getService(); // extra var for readability.
           var firstInList = true;

           for (Service a : serviceList)
           {

                 var matchesName = a.getServiceName().trim().equals(priorService);
                 var serviceType = a.getClaimType().trim();
                 var matchesType = serviceType.equals(claim.getClaimType());
                 var serviceName = a.getServiceName().trim(); 
                 boolean atTop =  hasPriorService && matchesType && matchesName;
                 AddToListBox(serviceName, atTop, serviceType, claim);  
                 // default to first. will stay there service box not clicked. 
                 if(matchesType && !hasPriorService && firstInList)
                 {
                    // set claim service to first in service list for that claim type.
                    claim.setService(a.getServiceName().trim());
                    firstInList = false;
                 }
           } 
     }

     private void AddToListBox(String value, boolean addAtTop, String type, Claim claim)
     {
            // either add at top of list or at end of list.
            Integer position = 0; 
            switch(type)  
            {
               case "m" : position = (addAtTop) ? 0 : claim.medservices.size(); // number of elements
                          claim.addMed(value, position); 
                          break;
               case "d" : position = (addAtTop) ? 0 : claim.denservices.size(); // number of elements
                           claim.addDen(value, position); 
                           break;
               case "v" : position = (addAtTop) ? 0 : claim.visservices.size(); // number of elements
                           claim.addVis(value, position); 
                           break;
               case "x" : position = (addAtTop) ? 0 : claim.drgservices.size(); // number of elements
                           claim.addDrg(value, position); 
                           break; 
            }  

     }
               

     private boolean  StampAdjustedClaim(String adjustingId, String adjustedId, String dateAdded)
     { 

         //*= = = = = = = call server to add custome and check results - - - -  
         String prefix = urlPrefix;
         String url = prefix + "/stampClaim/" + adjustedId;   

         StampData stampData = new StampData(); // not autowired
         stampData.setAdjustingClaimIdNumber(adjustingId);
         stampData.setAdjustedClaimIdNumber(adjustedId);
         stampData.setAdjustmentDate(dateAdded);
         var custId = sessionBean.getCustId();
         stampData.setCustId(custId);
          // stamp token - - - - - - - - - - - - - - - - - - - - - 
          var token = sessionBean.getToken();
          var headers = new LinkedMultiValueMap<String,String>();  
          headers.set("E20Token",token); 
         // - - - - - - - - - - - - - - - - - - - - - - - - - - - 
         HttpEntity<StampData> request = new HttpEntity<StampData>(stampData, headers);  
         // - - - - - - - - - - - - - - - - - - - - - - - -  
         try
         {
            ResponseEntity<StampData> response = restTemplate.exchange(url,HttpMethod.PUT,request,StampData.class);
            var error = response.getStatusCode() != HttpStatus.OK;
            if (error)
            {
               return false;
            } 
         }
         catch(Exception e)
         {
            // for timeout or bad request.
            return false;
         }
         return true;
     }

     public String  parking()
     {
        claim.getClaimDescription();
        sessionBean.SetRedirect("/menu"); // for compile.
        return "layout";
     }
     
    
}
