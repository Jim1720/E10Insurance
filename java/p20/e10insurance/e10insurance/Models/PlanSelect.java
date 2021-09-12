package p20.e10insurance.e10insurance.Models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PlanSelect {

    // this the model for the plan screen and it 
    // will hold list of plans from the PlanBean
    // and selected plan returned from form.

    private String selectedPlan;
    private List<Plan> plans; 

    // constructor
    PlanSelect()
    {
        plans = new ArrayList<Plan>(); 
    }  

    public void setSelectedPlan(String plan)
    {
        selectedPlan = plan;
    }
    public String getSelectedPlan()
    {
        return selectedPlan;
    }

    // controller loads plans from PlanBean when plan screen is first displayed.
    public void setPlans(List<Plan> inputPlans)
    {
        for (Plan p : inputPlans)
        {
            plans.add(p);
        }
    }

    // screen uses this to load data.
    public List<Plan> getPlans()
    {
        
        return this.plans;
    }
 
    
}
