package p20.e10insurance.e10insurance.Controllers;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
import p20.e10insurance.e10insurance.Beans.HistoryOperationBean; 

@Controller
public class SettingsController { 
    
    /* receives ajax call from history screen to update style or focus settings
       in the HisteoryOperationBean. 
    */

    @Autowired
    HistoryOperationBean historyOperationBean; 

    @RequestMapping(value = "/settings",
                    method = RequestMethod.GET,
                    params = {"name", "state"})  

    public ResponseEntity<?> settingProcess(@RequestParam String name,
                                 @RequestParam String state) 
    { 
 
        var boolState = state.contains("on");  

        switch(name)
        {
            case "stay" : 
                  historyOperationBean.setStay(boolState);
                  break;
            case "focus" :
                  historyOperationBean.setFocus(boolState);
                  break;
            default: break; 
        } 

        return new ResponseEntity<String>(HttpStatus.OK);
 
    }


}
