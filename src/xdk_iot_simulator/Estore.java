/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

/**
 *
 * @author User
 */
public class Estore implements Acao{
    
    @Override
    public void on(){
        System.out.println("Estore subido ...");
    }
    
    @Override   
    public void off(){
        System.out.println("Estore baixado ...");
    } 
}
