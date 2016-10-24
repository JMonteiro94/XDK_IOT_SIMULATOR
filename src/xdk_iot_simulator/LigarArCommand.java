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
public class LigarArCommand implements Command{
    
    private ArCondicionado ar;

        public LigarArCommand(ArCondicionado ac) {
            this.ar = ac;
        }

        @Override
        public void execute() {
            ar.on();
        }
}
