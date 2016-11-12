/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xdk_iot_simulator;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author User
 */
public class Controller extends Thread{
    
    private final ArrayList<Command> historico;
    private HashMap<String, ArrayList> programar = new HashMap<>();
    private final XDK xdk = new XDK();
    private final ArCondicionado ac;
    private final Estore es;
    private final Lampada la;
    private Timer timer;
    private final DesligarArCommand desligarAr;
    private final LigarArCommand ligarAr;
    private final DesligarLampadaCommand desligarLampada;
    private final LigarLampadaCommand ligarLampada;
    private final SubirEstoreCommand subirEstore;
    private final DescerEstoreCommand descerEstore;
    public static boolean blocked;
    
    public Controller(HashMap<String, ArrayList> programar, ArCondicionado ar, Lampada la, Estore es){
        this.historico = new ArrayList<>();
        this.programar=programar;
        this.ac=ar;
        this.es=es;
        this.la=la;
        this.desligarAr = new DesligarArCommand(ar);
        this.ligarAr = new LigarArCommand(ar);
        this.desligarLampada = new DesligarLampadaCommand(la);
        this.ligarLampada = new LigarLampadaCommand(la);
        this.descerEstore = new DescerEstoreCommand(es);
        this.subirEstore = new SubirEstoreCommand(es);
        this.blocked=false;
    }
    
    class Auto extends TimerTask {
        
        ArrayList<Command> comandos;
        int temp,luz;
        
        public Auto(ArrayList<Command> c,int temp,int luz){
            this.comandos=c;
            this.temp=temp;
            this.luz=luz;
        }
        @Override
        public void run() {
            try{
                for(int i=0; i<comandos.size(); i++) {
                    Command element = comandos.get(i);
                    if(element instanceof LigarArCommand){
                        if(this.temp < 30){
                            Thread.sleep(5*60*1000);
                        }
                        else{
                            element.execute();
                        }
                    }
                    if(element instanceof SubirEstoreCommand){
                        if(this.temp < 30){
                           element.execute(); 
                        }
                        else{
                            Thread.sleep(5*60*1000);
                        }
                    }
                    if(element instanceof LigarLampadaCommand){
                        if(this.luz < 700){
                            element.execute();
                        }
                        else{
                            Thread.sleep(5*60*1000);
                        }
                    }
                }
                System.out.println("WTF....");
                blocked=false;
            }catch(InterruptedException e){    
                System.out.println("got interrupted!");
            }
        }
    }
    
    @Override
    public void run() {
        int temp,luz,hora1;
        boolean done=false;
        ArrayList <Command> aux2=new ArrayList<>();
        aux2.add(new LigarArCommand(ac));
        ArrayList <Command> aux;
        Date date,date1;
        SimpleDateFormat hora = new SimpleDateFormat("kk");
        SimpleDateFormat minutos = new SimpleDateFormat("mm");
        String h,m;

        /*Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        data.set(Calendar.HOUR, 22);
        data.set(Calendar.MINUTE, 18);
        data.set(Calendar.SECOND, 0);
        data.set(Calendar.MILLISECOND, 0);*/
        //timer.schedule(new Auto (),data.getTime(),1000 * 60 * 60 * 24);
        /*date1.setHours(23);
        date1.setMinutes(30);
        date1.setSeconds(0);*/
        //Auto auto = new Auto(aux);
        try{
            while(true){
                luz=xdk.getLuz();
                temp=xdk.getTemperatura();   
                System.out.println("XDK: Temp "+temp+" Luz: "+luz);
                date = new Date();
                h=hora.format(date);
                m=minutos.format(date);
                System.out.println("Hora: "+h+" Minutos: "+m);
                if(m.equals("36")){
                    aux = programar.get(h);
                    Auto auto = new Auto(aux,temp,luz);
                    hora1=Integer.parseInt(h);
                    date1=new Date();
                    date1.setHours(01);
                    date1.setMinutes(36);
                    date1.setSeconds(1);
                    timer = new Timer();
                    if(!aux.isEmpty() && !done){
                        System.out.println("OLA::::::");
                        timer.schedule(auto,date1,1000 * 60 * 60 * 24);
                        done=true;
                        this.blocked=true;
                    }
                }
                if(!blocked){
                    if(temp >= 30 && ac.estado==0 && es.estado==0 ){             
                        historico.add(ligarAr);
                        ligarAr.execute();
                        ac.estado=1;
                    }
                    else{
                        if(temp >= 30 && ac.estado==0 && es.estado==1){
                            ligarAr.execute();
                            descerEstore.execute();
                            historico.add(ligarAr);
                            historico.add(descerEstore);
                            ac.estado=1;
                            es.estado=0;
                        }
                    }
                    if(temp <= 20 && ac.estado==1 && es.estado==0){
                        desligarAr.execute();
                        subirEstore.execute();
                        historico.add(desligarAr);
                        historico.add(subirEstore);
                        ac.estado=0;
                        es.estado=1;
                    }
                    if(temp > 20 && temp < 30 && ac.estado==0 && es.estado==1){
                        descerEstore.execute();         
                        historico.add(descerEstore);
                        es.estado=0;
                    }
                    if(luz < 500 && es.estado==1 && la.estado==0){
                        ligarLampada.execute();
                        historico.add(ligarLampada);
                        la.estado=1;
                    }else{
                        if(luz < 500 && es.estado==0 && la.estado==0){
                            subirEstore.execute();
                            historico.add(subirEstore);
                            es.estado=1;
                        }
                    }   
                    if(luz > 800 && la.estado==1){
                        desligarLampada.execute();
                        historico.add(desligarLampada);
                        la.estado=0;
                    }
                    Thread.sleep(1000);
                }
            }
        }catch(InterruptedException e){    
            System.out.println("got interrupted!");
        }
    }
}
