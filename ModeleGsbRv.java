package fr.gsb.rv.dr.modeles;

import fr.gsb.rv.dr.entites.Visiteur;
import fr.gsb.rv.dr.entites.Praticien;
import fr.gsb.rv.dr.entites.RapportVisite;
import fr.gsb.rv.dr.technique.ConnexionBD;
import fr.gsb.rv.dr.technique.ConnexionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModeleGsbRv {
    
    public static Visiteur seConnecter( String matricule , String mdp ) throws ConnexionException{
        
        Connection connexion = ConnexionBD.getConnexion() ;
        
        String requete = "select v.vis_nom, v.vis_prenom "
                + "from Travailler t1 "
                + "inner join Visiteur v "
                + "on v.vis_matricule=t1.vis_matricule "
                + "where tra_role = 'Délégué' "
                + "and v.vis_matricule = ? "
                + "and v.vis_mdp = ? "
                + "and t1.jjmmaa = ("
                + "select max(t2.jjmmaa) "
                + "from Travailler t2 "
                + "where t2.vis_matricule=t1.vis_matricule"
                + ")" ;
        
        try {
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement( requete ) ;
            requetePreparee.setString( 1 , matricule );
            requetePreparee.setString( 2 , mdp );
            ResultSet resultat = requetePreparee.executeQuery() ;
            if( resultat.next() ){
                Visiteur visiteur = new Visiteur() ;
                visiteur.setMatricule( matricule );
                visiteur.setNom( resultat.getString( "vis_nom" ) ) ;
                visiteur.setPrenom( resultat.getString( "vis_prenom" ) ) ;
                requetePreparee.close() ;
                return visiteur ;
            }
            else {
                return null ;
            }
        }
        catch( Exception e ){
            return null ;
        } 
    }
    
    
     public static List<Praticien> getPraticiensHesitants() throws ConnexionException{
         
          Connection connexion = ConnexionBD.getConnexion() ;
          
          String requete = "select r.rap_num, p.pra_nom, p.pra_ville, \n" +
"                p.pra_coefnotoriete, r.rap_date_visite, r.rap_coef_confiance\n" +
"                from Praticien p \n" +
"                inner join RapportVisite r\n" +
"                on r.pra_num=p.pra_num\n" +
"                where r.rap_coef_confiance < 5\n" +
"                and r.rap_date_visite =\n" +
"                (select max(r1.rap_date_visite)\n" +
"                from RapportVisite r1\n" +
"                where r.pra_num = r1.pra_num\n" +
"                and r.vis_matricule=r1.vis_matricule)";
          
         
          
          try {
            
            List<Praticien> praticiens = new ArrayList<Praticien>();  
            
            Statement requetePreparee = (Statement) connexion.createStatement() ;
           
            ResultSet resultat = requetePreparee.executeQuery(requete) ;
            
            while( resultat.next() ){
                
                Praticien praticien = new Praticien() ;
               
                praticien.setNumero( resultat.getInt( "rap_num" ) ) ;
               
                praticien.setNom( resultat.getString( "pra_nom" ) ) ;
                
                praticien.setVille( resultat.getString( "pra_ville" ) ) ;
               
                praticien.setCoefNotoriete( resultat.getDouble( "pra_coefnotoriete" ) ) ;
                
                //praticien.setDateDerniereVisite((LocalDate) resultat.getObject( "pra_rap_date_visite" )) ;
                praticien.setDateDerniereVisite(resultat.getDate( "rap_date_visite" ).toLocalDate()) ;
                
                praticien.setDernierCoefConfiance( resultat.getInt( "rap_coef_confiance" ) ) ;
               
                praticiens.add(praticien);
               // requetePreparee.close() ;
                

            }
             return praticiens;
              
        }
        catch( Exception e ){
            return null ;
        }
          
          
         
     }
     
     public static List<Visiteur> getVisiteurs() throws ConnexionException{
         
          Connection connexion = ConnexionBD.getConnexion() ;
          
          String requete = "select vis_matricule, "
                  + "vis_nom, vis_prenom "
                  + "from Visiteur;";
          
          
          try {
              List<Visiteur> visiteurs = new ArrayList<Visiteur>(); 
               
             Statement requetePreparee = (Statement) connexion.createStatement() ;
           
            ResultSet resultat = requetePreparee.executeQuery(requete) ;
            
            while( resultat.next() ){
                
                Visiteur visiteur = new Visiteur() ;
               
               visiteur.setMatricule( resultat.getString( "vis_matricule" ) ) ;
               
               visiteur.setNom( resultat.getString( "vis_nom" ) ) ;
                
               visiteur.setPrenom( resultat.getString( "vis_prenom" ) ) ;
               
               
                visiteurs.add(visiteur);
               // requetePreparee.close() ;
                

            }
             return visiteurs;
              
        }
          catch( Exception e ){
             
            return null ;
        }
     }
     
     public static List<RapportVisite> getRapportsVisite(String matricule, int mois, int annee) throws ConnexionException{
         
          Connection connexion = ConnexionBD.getConnexion() ;
         
         String requete = "select * from RapportVisite r, Praticien p, Visiteur v "
                 + "where r.pra_num=p.pra_num "
                 + "and r.vis_matricule=v.vis_matricule "
                 + "and r.vis_matricule=? "
                 + "and month(rap_date_visite)=? "
                 + "and year(rap_date_visite)=?;";
          
             try {
             
            List<RapportVisite> rapportsvisite = new ArrayList<RapportVisite>();         
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement( requete ) ;   
            requetePreparee.setString( 1 , matricule );
            requetePreparee.setInt( 2 , mois );
            requetePreparee.setInt( 3 , annee );

            ResultSet resultat = requetePreparee.executeQuery() ;
             
            while( resultat.next() ){
                 
             Visiteur visiteur = new Visiteur();
             
             visiteur.setMatricule(resultat.getString("vis_matricule"));
             visiteur.setNom(resultat.getString("vis_nom"));
             visiteur.setPrenom(resultat.getString("vis_prenom"));          
               
              Praticien praticien = new Praticien();
              
              praticien.setNumero(resultat.getInt( "pra_num" ) ) ;
              praticien.setNom(resultat.getString( "pra_nom" ) ) ;
              praticien.setPrenom(resultat.getString( "pra_prenom" ) ) ;
              praticien.setAdresse(resultat.getString( "pra_adresse" ) ) ;
              praticien.setCodePostal(resultat.getString( "pra_cp" ) ) ;
              praticien.setVille(resultat.getString( "pra_ville" ) ) ;
              praticien.setCoefNotoriete(resultat.getInt( "pra_coefnotoriete" ) ) ;
              praticien.setDateDerniereVisite(resultat.getDate( "rap_date_visite" ).toLocalDate()) ;
              praticien.setDernierCoefConfiance( resultat.getInt( "rap_coef_confiance" ) ) ;
              
                                
              RapportVisite rapportvisite = new RapportVisite() ;     
              
              rapportvisite.setNumero(resultat.getInt( "rap_num" ) ) ;
              rapportvisite.setDateVisite(resultat.getDate( "rap_date_visite" ).toLocalDate() ) ;
              rapportvisite.setDateRedaction(resultat.getDate( "rap_date_redaction" ).toLocalDate() ) ;
              rapportvisite.setBilan(resultat.getString("rap_bilan"));
              rapportvisite.setMotif(resultat.getString("rap_motif"));
              rapportvisite.setCoefConfiance(resultat.getInt("rap_coef_confiance"));
              rapportvisite.setLu(resultat.getBoolean("rap_lu"));               
              rapportvisite.setLePraticien(praticien);
              rapportvisite.setLeVisiteur(visiteur);
             
                            
               rapportsvisite.add(rapportvisite);
               // requetePreparee.close() ;

            }
             return rapportsvisite;
              
        }
          catch( Exception e ){
            return null ;
        }
        
    
    }
     
     public static void setRapportVisiteLu(String matricule, int numRapport) throws ConnexionException{
         
         Connection connexion = ConnexionBD.getConnexion() ;
        
         String requete = "update RapportVisite "
                 + "set rap_lu = true "
                 + "where vis_matricule=? "
                 + "and rap_num=?";
        
         try {
           
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement( requete ) ;
             
            requetePreparee.setString( 1 , matricule );
            
            requetePreparee.setInt( 2 , numRapport );
             
            requetePreparee.executeUpdate();
              
         }
         
       catch( Exception e ){                     
         }
           
     }
        
    
}
