/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.dr;

import fr.gsb.rv.dr.entites.Praticien;
import fr.gsb.rv.dr.entites.RapportVisite;
import fr.gsb.rv.dr.entites.Visiteur;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.dr.technique.ConnexionException;
import fr.gsb.rv.dr.technique.Mois;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;




/**
 *
 * @author etudiant
 */
public class PanneauRapports extends Pane{
    
    private ComboBox<Visiteur> cbVisiteurs = new ComboBox();
    private ComboBox<Mois> cbMois = new ComboBox();
    private ComboBox<Integer> cbAnnee= new ComboBox();
    TableView<RapportVisite> tabRapportVisites = new TableView();
    
    
       public PanneauRapports() throws ConnexionException { 
           
        super() ;
        setStyle("-fx-background-color:white");
        
        //creation d'une liste des mois en chiffres
         /*List<Integer> mois =  new ArrayList<Integer>();
        
        for(Mois unMois : Mois.values() ) {  
            System.out.println(unMois.ordinal()+1) ;  
                 mois.add(unMois.ordinal());
        }*/
       
       VBox vbox = new VBox();
        
        cbVisiteurs.setPromptText("Visiteur");
        cbMois.setPromptText("Mois");
        cbAnnee.setPromptText("Année");
        
        
        cbMois.getItems().addAll(Mois.values());
        
       
       cbVisiteurs.getItems().addAll(ModeleGsbRv.getVisiteurs());
       
       
       LocalDate aujourdhui = LocalDate.now() ;
       int anneeCourante = aujourdhui.getYear();
       int anneeAnterieure = aujourdhui.getYear()-5;
       //list des annees 5 ans avant l'annnee courante
        List<Integer> annees =  new ArrayList<Integer>();
       
       int i = anneeAnterieure;
       
       while (i<=anneeCourante) {
           annees.add(i);
           i = i+1;
       }
       
       cbAnnee.getItems().addAll(annees);
       
       Button validerBtn = new Button("Valider");
       
      // List<String> listech =  new ArrayList<String>();
       
       validerBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                if (rafraichir()==false) {
                     ObservableList<RapportVisite> rapportlist = null;
                    try {
                        /*String string = cbVisiteurs.getValue().toString(); //decouper le matricule de string
                        String[] parts = string.split("(",2);
                        String part1 = parts[0]; // NOM et prenom du visiteur
                        String part2 = parts[1]; // matricule)
                         if (part2 != null) {
                            part2 = part2.substring(0, part2.length() - 1);
                            System.out.println(part2);
                         }*/
                        
                        rapportlist = FXCollections.observableList(ModeleGsbRv.getRapportsVisite(cbVisiteurs.getValue().getMatricule(), cbMois.getValue().ordinal()+1, cbAnnee.getValue()));
                        
                      /*  for (int i=0; i < rapportlist.size(); i++) {
                            System.out.println(rapportlist.get(i).getNumero());
                        }*/
                        
                       //System.out.println(rapportlist.get(0).getNumero());
                       // System.out.println(cbVisiteurs.getValue().getMatricule());
                       // System.out.println(cbMois.getValue().ordinal()+1);
                      //  System.out.println(cbAnnee.getValue());
                    } catch (ConnexionException ex) {
                        Logger.getLogger(PanneauRapports.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tabRapportVisites.setItems(rapportlist);
                } else {
                     Alert alert = new Alert(AlertType.INFORMATION);
                     alert.setTitle("Alert");
                     alert.setHeaderText(null);
                     alert.setContentText("La sélection est incomplète !");
                     alert.showAndWait();
                }
            }
        });
        
        GridPane gp = new GridPane();
        
        HBox hbox = new HBox(cbVisiteurs, cbMois, cbAnnee);
        
        TableColumn<RapportVisite,Integer> colNumero = new TableColumn<RapportVisite,Integer>("Numéro");
        TableColumn<RapportVisite,String> colNom = new TableColumn<RapportVisite,String>("Praticien");
        TableColumn<RapportVisite,LocalDate> colVisite = new TableColumn<>("Date visite");  
        TableColumn<RapportVisite,LocalDate> colRedaction = new TableColumn<RapportVisite,LocalDate>("Date rédaction");
       
        colNumero.setCellValueFactory(new PropertyValueFactory<RapportVisite,Integer>( "numero" ));
        colVisite.setCellValueFactory(new PropertyValueFactory<RapportVisite,LocalDate>( "dateVisite" ));
        colRedaction.setCellValueFactory(new PropertyValueFactory<RapportVisite,LocalDate>( "dateRedaction" ));
        
        colNom.setCellValueFactory(
        new Callback<CellDataFeatures<RapportVisite,String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<RapportVisite,String> param) {
                String nom = param.getValue().getLePraticien().getNom() ;
                String ville = param.getValue().getLePraticien().getVille() ;
                return new SimpleStringProperty(nom+" "+ville);
            }
        });
        
        colVisite.setCellFactory(
        colonne-> {
            return new TableCell<RapportVisite,LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item,empty);
                    
                    if(empty) {
                        setText("");
                    }
                    else {
                        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        setText(item.format(formateur)) ;
                    }
                }
            };
        });
        
        colRedaction.setCellFactory(
        colonne-> {
            return new TableCell<RapportVisite,LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item,empty);
                    
                    if(empty) {
                        setText("");
                    }
                    else {
                        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                        setText(item.format(formateur)) ;
                    }
                }
            };
        });
        
        
        tabRapportVisites.setRowFactory(
        ligne-> {
            return new TableRow<RapportVisite>(){
               @Override
               protected void updateItem(RapportVisite item, boolean empty){
                   super.updateItem(item, empty);
                   if (item != null){
                       if (item.isLu()){
                           setStyle("-fx-background-color: gold");
                       }
                       else {
                           setStyle("-fx-background-color: cyan");
                       }
                   }
               }
            };
        });
        
        tabRapportVisites.setOnMouseClicked(
        (MouseEvent event)-> {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                int indiceRapport =  tabRapportVisites.getSelectionModel().getSelectedIndex();
                RapportVisite viewRapport = tabRapportVisites.getSelectionModel().getSelectedItem();
                //Rapport de visite marqué comme lu
              tabRapportVisites.getSelectionModel().getSelectedItem().isLu();
              
                try {
                    //MAJ de la BD
          ModeleGsbRv.setRapportVisiteLu(tabRapportVisites.getSelectionModel().getSelectedItem().getLeVisiteur().getMatricule(),tabRapportVisites.getSelectionModel().getSelectedItem().getNumero());
                    //Rafraichissement de la table
      ObservableList<RapportVisite> rapportlist = rapportlist = FXCollections.observableList(ModeleGsbRv.getRapportsVisite(cbVisiteurs.getValue().getMatricule(), cbMois.getValue().ordinal()+1, cbAnnee.getValue()));
                      tabRapportVisites.setItems(rapportlist);
                    
                    //Affichage du rapport (realisé dans la partie suivante)
                   
                    VueRapport rapport = new VueRapport(viewRapport);
                    
                } catch (ConnexionException ex) {
                    Logger.getLogger(PanneauRapports.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
                
       
       
       tabRapportVisites.getColumns().add(colNumero);
       tabRapportVisites.getColumns().add(colNom);
       tabRapportVisites.getColumns().add(colVisite);
       tabRapportVisites.getColumns().add(colRedaction);
       
       tabRapportVisites.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
       tabRapportVisites.setPrefSize( 700, 420 );
       

        gp.add(hbox, 1, 3);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 20, 10, 20)); 
        vbox.getChildren().addAll(gp, validerBtn, tabRapportVisites);
       
        this.getChildren().add(vbox);

        
        //cbMois.getItems().addAll(Mois.values());

       /*Label label = new Label("Rapports");
      
        label.relocate(230, 30);
        this.getChildren().add(label);*/
          
       
 }  
    
       public boolean rafraichir() {
           if (cbVisiteurs.getValue() == null || cbMois.getValue() == null || cbAnnee.getValue() == null) {
               return true;
           }       
        return false;         
       }
   
   
    
}
