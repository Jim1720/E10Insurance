package p20.e10insurance.e10insurance.Models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CustomerList {

        
    private List<Customer> customers; 

    CustomerList()
    {
        customers = new ArrayList<Customer>();
    }



    public void setCustomerList(Customer[] customers)
    {
        for(Customer c : customers )
        {
            this.customers.add(c);
        }
    }

    public List<Customer> getCustomersInList()
    {

        return this.customers;
    }

    
}
