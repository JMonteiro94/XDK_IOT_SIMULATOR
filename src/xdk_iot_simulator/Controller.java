/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author User
 */
public class Controller extends Thread{
    
    private HashMap<String, Command> history = new HashMap<>();
    private ArrayList<Command> historico = new ArrayList<>();
    private HashMap<String, ArrayList> programar = new HashMap<>();
    private XDK xdk = new XDK();
    private ArCondicionado ac;
    private Estore es;
    private Lampada la;
    
    public Controller(HashMap<String, Command> history,HashMap<String, ArrayList> programar, ArCondicionado ar, Lampada la, Estore es){
        this.history=history;
        this.programar=programar;
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
    
    class Auto extends TimerTask {
        
        public Auto(){
            
        }
        @Override
        public void run() {
            System.out.println("WTF....");
        }
    }
    
    @Override
    public void run() {
        int temp,luz;
        Calendar now =  Calendar.getInstance();
        int i=now.get(Calendar.HOUR_OF_DAY);
        ArrayList <Command> aux;
        aux = this.programar.get("4:00");
        Date date = new Date();
        SimpleDateFormat s = new SimpleDateFormat("kk:mm");
        String a;
        Timer timer = new Timer();
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        data.set(Calendar.HOUR, 16);
        data.set(Calendar.MINUTE, 9);
        data.set(Calendar.SECOND, 0);
        data.set(Calendar.MILLISECOND, 0);
        //timer.schedule(new Auto (),data.getTime(),1000 * 60 * 60 * 24);
        try{
            while(true){
                luz=xdk.getLuz();
                temp=xdk.getTemperatura();   
                System.out.println("XDK: "+temp+"Luz: "+luz);
                date = new Date();
                a=s.format(date);
                System.out.println("Hora "+a);
                timer.schedule(new Auto (),data.getTime(),1000 * 60 * 60 * 24);
                if(temp >= 30 && history.containsKey("OffAr") && history.containsKey("OffEstore")){
                    history.remove("OffAr");
                    history.put("OnAr", new LigarArCommand(ac));
                    historico.add(new LigarArCommand(ac));
                    executeCommand("OnAr");
                }
                else{
                    if(temp >= 30 && history.containsKey("OffAr") && history.containsKey("OnEstore")){
                        history.remove("OffAr");
                        history.remove("OnAr");
                        history.put("OnAr", new LigarArCommand(ac));
                        history.put("OffAr", new DescerEstoreCommand(es));
                        executeCommand("OnAr");
                        executeCommand("OffEstore");
                        historico.add(new LigarArCommand(ac));
                        historico.add(new DescerEstoreCommand(es));
                    }
                    else{
                        if(temp >= 30 && history.containsKey("OnAr") && history.containsKey("OffEstore")){
                            /*history.remove("OffEstore");
                            history.put("OnEstore", new SubirEstoreCommand(es));
                            executeCommand("OnEstore");*/
                        }
                    }
                }
                if(temp <= 20 && history.containsKey("OnAr") && history.containsKey("OffEstore")){
                    history.remove("OnAr");
                    history.remove("OffEstore");
                    history.put("OffAr", new DesligarArCommand(ac));
                    history.put("OnEstore", new SubirEstoreCommand(es));
                    executeCommand("OffAr");
                    executeCommand("OnEstore");
                    historico.add(new DesligarArCommand(ac));
                    historico.add(new SubirEstoreCommand(es));
                }
                if(temp > 20 && temp < 30 && history.containsKey("OffAr") && history.containsKey("OnEstore")){
                    history.remove("OnEstore");
                    history.put("OffEstore", new DescerEstoreCommand(es));
                    executeCommand("OffEstore");
                    historico.add(new DescerEstoreCommand(es));
                }
                if(luz < 500 && !history.containsKey("OffEstore") && !history.containsKey("OnLampada")){
                    if(luz < 500 && history.containsKey("OffLampada")){
                        history.remove("OffLampada");
                        history.put("OnLampada", new LigarLampadaCommand(la));
                        executeCommand("OnLampada");
                        historico.add(new LigarLampadaCommand(la));
                    }
                }else{
                    history.remove("OffEstore");
                    history.put("OnEstore", new SubirEstoreCommand(es));
                    executeCommand("OnEstore");
                    historico.add(new SubirEstoreCommand(es));
                }
                    
                if(luz > 700 && history.containsKey("OnLampada")){
                    history.remove("OnLampada");
                    history.put("OffLampada", new DesligarLampadaCommand(la));
                    executeCommand("OffLampada");
                    historico.add(new DesligarLampadaCommand(la));
                }
                Thread.sleep(1000);
            }  
        }catch(InterruptedException e){    
            System.out.println("got interrupted!");
        }
    }
}
