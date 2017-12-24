/**
 * OrderFactory.java. Created Tuesday, August 5 2014
 * @author Michael Kazarian
 * Tags:
 */
package com.blogspot.mkazarian;

import java.util.List;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;


public class OrderFactory{
    /**
     * This class incorporate master and detail records.
     * For master records used OrderBean class; OrderDetailBean for detail record.
     */
    public static List<OrderBean> create(){
        /**
         * Factory-method for get datasource.
         */
        //Add two master records
        OrderBean order1 = new OrderBean(); 
        order1.setNDoc("1");
        OrderBean order2 = new OrderBean();
        order2.setNDoc("2");
        //Set Date format
        SimpleDateFormat textDate = new SimpleDateFormat("dd.MM.yyyy");
        try{
            order1.setDate(textDate.parse("05.08.2014"));
            order2.setDate(textDate.parse("10.08.2014"));
        } catch (ParseException pe){
            order1.setDate(new Date());
            order2.setDate(new Date());
        }
        //Detail records for master record 1
        OrderDetailBean detail1 = new OrderDetailBean();
        detail1.setName("Position one for document1");
        detail1.setQuantity(10);
        detail1.setPrice(123.45);
        OrderDetailBean detail2 = new OrderDetailBean();
        detail2.setName("Position two for document1");
        detail2.setQuantity(7);
        detail2.setPrice(23.52);
        //Detail records for master record 2
        OrderDetailBean detail3 = new OrderDetailBean();
        detail3.setName("Position one for document2");
        detail3.setQuantity(76);
        detail3.setPrice(0.34);
        OrderDetailBean detail4 = new OrderDetailBean();
        detail4.setName("Position two for document2");
        detail4.setQuantity(22);
        detail4.setPrice(1.12);
        //Add detail records
        order1.setOrderDetails(Arrays.asList(detail1, detail2));
        order2.setOrderDetails(Arrays.asList(detail3, detail4));
        return Arrays.asList(order1, order2);
    }
}
//File: OrderFactory.java ends here.
