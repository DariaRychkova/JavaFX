/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import static fr.gsb.rv.dr.PanneauPraticiens.CRITERE_COEF_CONFIANCE;
import fr.gsb.rv.dr.entites.Visiteur;
import fr.gsb.rv.dr.entites.Praticien;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.dr.technique.ConnexionBD;
import fr.gsb.rv.dr.technique.ConnexionException;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefConfiance;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefNotoriete;
import fr.gsb.rv.dr.utilitaires.ComparateurDateVisite;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.swing.JOptionPane;

/**
 *
 * @author etudiant
 */
public class Appli extends Application {
    
    
    private PanneauAccueil vueAccueil ;
    private PanneauRapports vueRapports ;
    private PanneauPraticiens vuePraticiens ;
    
    @Override
    public void start(Stage primaryStage) throws ConnexionException {
        MenuBar barreMenus = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        MenuItem itemSeConnecter = new MenuItem("Se connecter");
        MenuItem itemSeDeconnecter = new MenuItem("Se déconnecter");
        itemSeDeconnecter.setDisable(true);
        MenuItem itemQuitter = new MenuItem("Quitter");
        itemQuitter.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        menuFichier.getItems().addAll(itemSeConnecter, itemSeDeconnecter, new SeparatorMenuItem(), itemQuitter);
       
        barreMenus.getMenus().add(menuFichier);
        
        Menu menuRapports = new Menu("Rapports");
        MenuItem consulter = new MenuItem("Consulter");
        menuRapports.setDisable(true);
        menuRapports.getItems().add(consulter);
        barreMenus.getMenus().add(menuRapports);
        
        Menu menuPraticiens = new Menu("Praticiens");
        MenuItem hesitant = new MenuItem("Hésitants");
        menuPraticiens.setDisable(true);
        menuPraticiens.getItems().add(hesitant);
        barreMenus.getMenus().add(menuPraticiens);
        
        itemSeConnecter.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                
               VueConnexion dialog = new VueConnexion() ;
               Optional<Pair<String, String>> result = dialog.showAndWait();
               if(result.isPresent()){
                   System.out.println("matricule=" + result.get().getKey()+ ", mdp=" + result.get().getValue());
                   
                   
                   String matricule = result.get().getKey() ;
                   String mdp = result.get().getValue() ;
                   
                   
                   Visiteur dr = new Visiteur();
                   try {
                       dr = ModeleGsbRv.seConnecter(matricule, mdp) ;
                       if (dr ==null){
                          JOptionPane.showMessageDialog(null, "Erreur de connexion");
                       } else {
                           
                           menuPraticiens.setDisable(false);
                           menuRapports.setDisable(false);
                           itemSeConnecter.setDisable(true);
                           itemSeDeconnecter.setDisable(false);
                           
                           List<Praticien> praticiens = ModeleGsbRv.getPraticiensHesitants();
    
                           /*for(Praticien unPraticien : praticiens) {
                               System.out.println(unPraticien);
                           }*/
                           
                          /* Collections.sort(praticiens, new ComparateurCoefConfiance());
                           
                           for (Praticien unPraticien : praticiens ) {
                               System.out.println(unPraticien);
                           }*/
                           
                           Collections.sort(praticiens, new ComparateurCoefNotoriete());
                           
                           for (Praticien unPraticien : praticiens ) {
                               System.out.println(unPraticien);
                           }
                            Collections.reverse(praticiens);
                          
                   /*       Collections.sort(praticiens, new ComparateurDateVisite());
                          
                          for (Praticien unPraticien : praticiens ) {
                               System.out.println(unPraticien);
                           }*/
                  int critereTri = CRITERE_COEF_CONFIANCE;
                           
                           
                       }

                   } catch (ConnexionException ex) {
                       Logger.getLogger(Appli.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   
                   
                   
               }
            }
        });
        
        
        itemSeDeconnecter.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                itemSeConnecter.setDisable(false);
                menuPraticiens.setDisable(true);
                menuRapports.setDisable(true);
                itemSeDeconnecter.setDisable(true);
            }
        });
        
        itemQuitter.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
                alertQuitter.setTitle("Quitter");
                alertQuitter.setHeaderText("Demande de confirmation");
                alertQuitter.setContentText("Voulez-vous quitter l'application?");
                ButtonType btnOui = new ButtonType("Oui");
                ButtonType btnNon = new ButtonType("Non");
                
                alertQuitter.getButtonTypes().setAll(btnOui, btnNon);
                Optional<ButtonType> result = alertQuitter.showAndWait();
                if (result.get() == btnOui){
                    int critereTri = CRITERE_COEF_CONFIANCE;
                    Platform.exit();
                // ... user chose OK
                } else {
                // ... user chose CANCEL or closed the dialog
                 
                }
                
            }
        });
        
        consulter.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                menuRapports.setDisable(false);
                vueRapports.toFront();
            }
        });
        
        hesitant.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                menuPraticiens.setDisable(false);
                try {
                    List<Praticien> praticiens = ModeleGsbRv.getPraticiensHesitants();
                } catch (ConnexionException ex) {
                    Logger.getLogger(Appli.class.getName()).log(Level.SEVERE, null, ex);
                }
                 vuePraticiens.toFront();
            }
        });
        
        vuePraticiens = new PanneauPraticiens();
        vueAccueil = new PanneauAccueil();
        vueRapports = new PanneauRapports();
      
        
        StackPane stackpane = new StackPane();
        
       // stackpane.getChildren().add(vueAccueil);
        stackpane.getChildren().addAll(vueAccueil, vueRapports, vuePraticiens);
        vueAccueil.toFront();
 
        BorderPane root = new BorderPane();
        root.setTop(barreMenus);
        root.setCenter(stackpane);
        
        Scene scene = new Scene(root, 800, 550);
        
        primaryStage.setTitle("GSB-RV-DR");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
