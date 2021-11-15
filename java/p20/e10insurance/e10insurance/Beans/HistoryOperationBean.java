package p20.e10insurance.e10insurance.Beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import p20.e10insurance.e10insurance.Models.ActionObject;

/* documentation:

   This bean assists in history screen operations. They are Stay, Focus,
   Nav and Actions. These basically determine how the app operates:
   stay - return to history after adjustment or pay action;
   focus - if stay is used; return to claim on screen adjusted or paid;
   action 1/2 - click to position screen to last paid or adjusted claims
   Support: this bean caputurs the environment settings to
   activate the above operations.
   Note: since the history screen is not submitted when the Stay or
   Focus buttons are toggled; javascript will ajax into the 
   (proposed name) HistoryStatus controller to update the stay on/off
   or focus on/off settings located here. 

*/ 

@Component 
@SessionScope 
public class HistoryOperationBean {

 //* Use Stay Environment Value 
 @Value("${E10UseStay:notSet}")
 String usingStay; 
     
 //* Use Focus Environment Value 
 @Value("${E10UseFocus:notSet}")
 String usingFocus;

 //* Use Nav Environment Value 
 @Value("${E10UseNav:notSet}")
 String usingNav; 
 
 //* Use Actions Environment Value 
 @Value("${E10UseActions:notSet}")
 String usingActions;

 private boolean stayOn = false;
 private boolean focusOn = false;

 private String focusClaimId = "";

 private List<ActionObject> actionList;
 private String yes = "Y";  

 HistoryOperationBean() 
 {

     actionList = new ArrayList<ActionObject>(); 

 }

 public boolean isStayUsed()
 {
     return usingStay.equals(yes);
 } 
 
 public boolean isFocusUsed()
 {
     return usingFocus.equals(yes);
 } 

 public boolean isNavUsed()
 {
     return usingNav.equals(yes);
 } 
 
 public boolean isActUsed()
 {
     return usingActions.equals(yes);
 }

 public void setStay(boolean value)
 {
     this.stayOn = value;
 }

 public boolean isStayOn()
 {
     return this.stayOn;
 }
 
 public String isStayOnString()
 {
     return this.stayOn ? "on" : "off";
 }

 public String getStayLiteral()
 {
     return (this.stayOn) ? "stay on" : "stay off";
     
 }

 public void toggleStay()
 {
     this.stayOn = !this.stayOn;
 }
 

 
 public void setFocus(boolean value)
 {
     this.focusOn = value;
 }

 public boolean isFocusOn()
 {
     return this.focusOn;
 } 

 
 public String isFocusOnString()
 {
     return this.focusOn ? "on" : "off";
 } 

 public void toggleFocus()
 {
     this.focusOn = !this.focusOn;
 }

  

 public void setFocusClaim(String claimId)
 {
     this.focusClaimId = claimId;
 }

 public String getFocusClaim()
 {
     return this.focusClaimId;
 }
 
 
 public String getFocusLiteral()
 {
     return (this.focusOn) ? "focus on" : "focus off";
 }

 public String getActionClaimId(Integer selection) 
 {
    // does not know when used how many elements are in
    // due to parser apparently.
    // input: 1 or 2 for action 1 or 2.

    var count = this.actionList.size();
    if(selection > count) 
    {
        return "";
    } 
 
    // getAction uses 1 or 2 for action 1 or 2...
    ActionObject act = this.getAction(selection);
    var claimId = act.getClaimId();
    return claimId;

 }

 public String getAction1Literal()
 { 
      // test for action >= 1 needs to be done in the caller i.e. html test.
       ActionObject act = this.getAction(1);
       var action = act.getAction();
       var claimId = act.getClaimId().trim();
       var act1lit = formatActionButton(action, claimId); 
       return act1lit;
 }

 public String getAction2Literal()
 { 
      // test for action >= 1 needs to be done in the caller i.e. html test.
      ActionObject act = this.getAction(2);
      var action = act.getAction();
      var claimId = act.getClaimId().trim();
      var act2lit = formatActionButton(action, claimId);
      return act2lit;
 }

 private String formatActionButton(String action, String claimId)
 {
     var act = action.substring(0,3);
     var len = claimId.length();
     var claimSuffix = claimId.substring(len-2);
     var result = act + '-' + claimSuffix;
     return result;

 }

 public Integer getActionCount()
 {
     return actionList.size(); 
 }



 public void setAction(String Action, String claimId) 
 {
    // stores adjustment or pay actions with associated claim numbers.

    // when full remove oldest ...
    ActionObject actionObject = new ActionObject(Action, claimId);

    Integer count = actionList.size();
    switch(count)
    {
         case 0 : actionList.add(actionObject); break;
         case 1 : actionList.add(1, actionObject); break;
         case 2 : actionList.remove(0); actionList.add(1,actionObject); break;
         default: break; 
    }

 }

 public ActionObject getAction(Integer number)
 {
    // number is 1 or 2 = retrieves last actions.

    Integer count = actionList.size();

    if(number > count)
    {
        ActionObject empty = new ActionObject("","");
        return empty;
    }

    Integer index = number - 1; // zero based

    ActionObject foundEntry = actionList.get(index);

    return foundEntry;

 }
    
}
