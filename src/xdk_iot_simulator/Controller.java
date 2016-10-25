/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author User
 */
public class Controller extends Thread{
    
    private HashMap<String, Command> history = new HashMap<>();
    private XDK xdk = new XDK();
    private ArCondicionado ac;
    private Estore es;
    private Lampada la;
    
    public Controller(HashMap<String, Command> history, ArCondicionado ar, Lampada la, Estore es){
        this.history=history;
        this.ac=ar;
        this.es=es;
        this.la=la;
    }
    
    public void executeCommand(String command) {
        if (!history.containsKey(command)){
            System.out.println("ERROR 404 ...");
        }
        else{
            history.get(command).execute();
        }
    }
    
    @Override
    public void run() {
        int temp,luz;
        try{
            while(true){
                luz=xdk.getLuz();
                temp=xdk.getTemperatura();   
                System.out.println("XDK: "+temp+"Luz: "+luz);
                if(temp >= 30 && history.containsKey("OffAr") && history.containsKey("OffEstore")){
                    history.remove("OffAr");
                    history.remove("OffEstore");
                    history.put("OnAr", new LigarArCommand(ac));
                    history.put("OnEstore", new SubirEstoreCommand(es));
                    executeCommand("OnAr");
                    executeCommand("OnEstore");
                }
                else{
                    if(temp >= 30 && history.containsKey("OffAr") && history.containsKey("OnEstore")){
                        history.remove("OffAr");
                        history.put("OnAr", new LigarArCommand(ac));
                        executeCommand("OnAr");
                    }
                    else{
                        if(temp >= 30 && history.containsKey("OnAr") && history.containsKey("OffEstore")){
                            history.remove("OffEstore");
                            history.put("OnEstore", new SubirEstoreCommand(es));
                            executeCommand("OnEstore");
                        }
                    }
                }
                if(temp <= 20 && history.containsKey("OnAr") && history.containsKey("OnEstore")){
                    history.remove("OnAr");
                    history.remove("OnEstore");
                    history.put("OffAr", new DesligarArCommand(ac));
                    history.put("OffEstore", new DescerEstoreCommand(es));
                    executeCommand("OffAr");
                    executeCommand("OffEstore");
                }
                if(temp > 20 && temp < 30 && history.containsKey("OffAr") && history.containsKey("OffEstore")){
                    history.remove("OffEstore");
                    history.put("OnEstore", new SubirEstoreCommand(es));
                    executeCommand("OnEstore");
                }
                if(luz < 500 && history.containsKey("OffLampada")){
                    history.remove("OffLampada");
                    history.put("OnLampada", new LigarLampadaCommand(la));
                    executeCommand("OnLampada");
                }
                if(luz > 700 && history.containsKey("OnLampada")){
                    history.remove("OnLampada");
                    history.put("OffLampada", new DesligarLampadaCommand(la));
                    executeCommand("OffLampada");
                }
                Thread.sleep(1000);
            }  
        }catch(InterruptedException e){    
            System.out.println("got interrupted!");
        }
    }
}
