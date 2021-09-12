package p20.e10insurance.e10insurance.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import p20.e10insurance.e10insurance.Beans.PlanBean;
import p20.e10insurance.e10insurance.Beans.ServiceBean;
import p20.e10insurance.e10insurance.PassedParameters.CostParm;
 

@Service
public class CostCalculator {

    @Autowired
    PlanBean planBean;

    @Autowired
    ServiceBean serviceBean;

    // calculate covered amount based on plan selected.

    public void CalculateCoveredCost(CostParm costParm)
    {
         // determine owed amount 

         // skip for debugging temp 
         //costParm.setCovered(0);
         //costParm.setOwed(0);
         //return;

        if(!planBean.isLoaded())
        {
            planBean.LoadPlans();
        }

        // Get Percentage covered in plan 
        double coveredPercent = planBean.getPercent(costParm.getPlan());

        // Get Service Cost  
        String claimType = costParm.getClaimType();
        double serviceCost = serviceBean.getCost(claimType, costParm.getService());

        // determine covered amount 
        double coveredAmount = (serviceCost * coveredPercent) / 100; 
        double amountOwed = serviceCost - coveredAmount; 
 
        costParm.setServiceCost(serviceCost);
        costParm.setCovered(coveredAmount);
        costParm.setOwed(amountOwed); 
    }
    
}
