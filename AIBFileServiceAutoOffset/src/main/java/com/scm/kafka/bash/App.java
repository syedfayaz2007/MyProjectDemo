package com.scm.kafka.bash;

import com.scm.kafka.main.AIBFileUploadAutoOffsetConsumer;
//import CumsumerClasses.ConsumerOffset;
//import CumsumerClasses.Consumer_ccw_orders_sava;
//import CumsumerClasses.SimpleConsumer2;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Calling Consumer Main" );        
        //ConsumerOffset.main(args);
        AIBFileUploadAutoOffsetConsumer.main(args);
    }
}
