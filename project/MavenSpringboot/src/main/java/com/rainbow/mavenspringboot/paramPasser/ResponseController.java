package com.rainbow.mavenspringboot.paramPasser;

import com.rainbow.mavenspringboot.pojo.Address;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResponseController {
    @RequestMapping("/response/strResponse")
    public String strResponse(){
        return "Hello world~";
    }
    @RequestMapping("/response/objResponse")
    public Address objResponse(){
        Address address = new Address();
        address.setProvince("广东");
        address.setCity("深圳");
        return address;
    }
    @RequestMapping("/response/listResponse")
    public List<Address> listResponse(){
        Address addr1 = new Address();
        addr1.setProvince("湖北");
        addr1.setCity("武汉");
        Address addr2 = new Address();
        addr2.setProvince("山东");
        addr2.setCity("菏泽");
        List<Address> addressList = new ArrayList<>();
        addressList.add(addr1);
        addressList.add(addr2);
        return addressList;
    }

}
