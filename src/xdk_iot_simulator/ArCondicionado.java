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
public class ArCondicionado implements Acao{
    
    private String name;
    public int estado;
    
    public ArCondicionado(String n){
        this.name=n;
        this.estado=0;
    }
    
    @Override
    public void on() {
        System.out.println("Ar Condicionado "+this.name+" ligado ...");
    }
    
    @Override
    public void off() {
         System.out.println("Ar Condicionado "+this.name+" desligado ...");
    }
    
    
}
