/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import fr.gsb.rv.dr.entites.RapportVisite;
import javafx.scene.control.Dialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author etudiant
 */
public class VueRapport extends Dialog {

    public VueRapport(RapportVisite rapvis) {
        
        JFrame frame = new JFrame("Rapport visite");
           
         JOptionPane.showMessageDialog(frame,rapvis,"Rapport visite",JOptionPane.INFORMATION_MESSAGE);
        
    }
    
    
}
