/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;


import fr.gsb.rv.dr.entites.Praticien;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.dr.technique.ConnexionException;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefConfiance;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefNotoriete;
import fr.gsb.rv.dr.utilitaires.ComparateurDateVisite;
import java.util.Collections;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


/**
 *
 * @author etudiant
 */
public class PanneauPraticiens extends Pane {
    
     public static final int CRITERE_COEF_CONFIANCE = 1;
     public static final int CRITERE_COEF_NOTORIETE = 2;
     public static final int CRITERE_DATE_VISITE = 3; 
     
     private int critereTri = CRITERE_COEF_CONFIANCE;
     private RadioButton rbCoefConfiance = new RadioButton("Confiance");
     private RadioButton rbCoefNotoriete = new RadioButton("Notoriété");
     private RadioButton rbDateVisite = new RadioButton("Date Visite");;
     
     //private TableView<Praticien> tabPraticiens = new TableView();
    
     public PanneauPraticiens() throws ConnexionException { 
         
       super() ;
       
       setStyle("-fx-background-color:white");
         
       VBox vbox = new VBox();  
       
       Label label = new Label("Sélectionner un critère de tri : ");

       ToggleGroup group = new ToggleGroup();
       
       rbCoefConfiance.setToggleGroup(group);
       rbCoefConfiance.setSelected(true);
       rbCoefNotoriete.setToggleGroup(group);
       rbDateVisite.setToggleGroup(group);
      //Label label = new Label("Praticiens");
      
     //   label.relocate(230, 30);
        
       // this.getChildren().add(group);
       GridPane gp = new GridPane();
       HBox hbox = new HBox(rbCoefConfiance,rbCoefNotoriete,rbDateVisite);
       //gp.setAlignment(Pos.CENTER);
       
       gp.add(hbox, 1, 3);
       
       TableView<Praticien> tabPraticiens = new TableView();
       
       TableColumn<Praticien,Integer> colNumero = new TableColumn<Praticien,Integer>("Numéro");
       TableColumn<Praticien,String> colNom = new TableColumn<Praticien,String>("Nom");
       TableColumn<Praticien,String> colVille = new TableColumn<Praticien,String>("Ville");
       
       colNumero.setCellValueFactory(new PropertyValueFactory<Praticien,Integer>("numero"));
       colNom.setCellValueFactory(new PropertyValueFactory<Praticien,String>("nom"));
       colVille.setCellValueFactory(new PropertyValueFactory<Praticien,String>("ville"));
       
       tabPraticiens.getColumns().add(colNumero);
       tabPraticiens.getColumns().add(colNom);
       tabPraticiens.getColumns().add(colVille);
       
       tabPraticiens.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       
       ObservableList<Praticien> obslist = FXCollections.observableList(ModeleGsbRv.getPraticiensHesitants());
       
       
       if (rbCoefConfiance.isSelected()==true){
           Collections.sort(obslist,new ComparateurCoefConfiance());
           tabPraticiens.setItems(obslist);
       } 
       
       if (rbCoefNotoriete.isSelected()==true) {
           Collections.sort(obslist,new ComparateurCoefNotoriete().reversed());
           tabPraticiens.setItems(obslist);
          
       }
       
        if (rbDateVisite.isSelected()==true) {
           Collections.sort(obslist,new ComparateurDateVisite().reversed());
           tabPraticiens.setItems(obslist);
          
       }
      
      
       
       rbCoefConfiance.setOnAction(
       new EventHandler<ActionEvent>(){
           
           @Override
           public void handle(ActionEvent event) {
               critereTri = CRITERE_COEF_CONFIANCE;
               tabPraticiens.refresh();
               Collections.sort(obslist,new ComparateurCoefConfiance());
               tabPraticiens.setItems(obslist);
            }
          }
       );
       
       rbCoefNotoriete.setOnAction(
       new EventHandler<ActionEvent>(){
           
           @Override
           public void handle(ActionEvent event) {
               critereTri = CRITERE_COEF_NOTORIETE;
               tabPraticiens.refresh();
               Collections.sort(obslist,new ComparateurCoefNotoriete().reversed());
                tabPraticiens.setItems(obslist);
            }
          }
       );
       
       rbDateVisite.setOnAction(
       new EventHandler<ActionEvent>(){
           
           @Override
           public void handle(ActionEvent event) {
                critereTri = CRITERE_DATE_VISITE;
                tabPraticiens.refresh();
                Collections.sort(obslist,new ComparateurDateVisite().reversed());
                tabPraticiens.setItems(obslist);
            }
          }
       );
       
       
       
       
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 20, 10, 20)); 
        vbox.getChildren().addAll(label, gp, tabPraticiens);
       
        this.getChildren().add(vbox);
        
        
    }  
    
    public List<Praticien> rafraichir() throws ConnexionException {
         
      List<Praticien> praticiens = ModeleGsbRv.getPraticiensHesitants();
          
         return null;
         
     }
     
     public void setCritereTri(int tri) { 
         this.critereTri = tri;
         //return critereTri;
     }
     
    
}
