package p20.e10insurance.e10insurance.Beans;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import p20.e10insurance.e10insurance.Models.Service;
 
@Component
@SessionScope
public class ServiceList {

    private List<Service> services;

    public void Load()
    {
        var a = new Service();
        services.add(a);
    }


}
