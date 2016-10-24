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
public class LigarLampadaCommand implements Command{
    
    private Lampada lampada;

        public LigarLampadaCommand(Lampada l) {
            this.lampada = l;
        }

        @Override
        public void execute() {
            lampada.on();
        }
}
