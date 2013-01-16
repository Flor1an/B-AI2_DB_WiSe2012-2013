/*
 * Birdwatching Praktikum 
 * WISE 12-13
 * Autoren:
 * DB Praktikum Aufgabe 04
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Florian
 */
public class Stammdaten extends javax.swing.JFrame {

    String Username;
    String Passwort;
    String pre = "";
    Connection c;
    Statement s;


    /**
     * Creates new form Stammdaten
     */
    public Stammdaten() {
        initComponents();
    }

    Stammdaten(String Username, String Passwort, String DBOwner) {
        initComponents();
        this.Username = Username;
        this.Passwort = Passwort;


        System.out.println("laden");

        try {


            if (Username.toUpperCase() != DBOwner.toUpperCase()) {
                pre = DBOwner+".";
            }


            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@oracle.informatik.haw-hamburg.de:1521:inf09";
            c = DriverManager.getConnection(url, Username, Passwort);
            s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           

        } catch (Exception e) {
            jLabelFehler.setText(e.getMessage());
            return;
        }
        jLabelSucsess.setText("Bereit");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel12 = new javax.swing.JLabel();
        jButtonUebernehmen = new javax.swing.JButton();
        jLabelFehler = new javax.swing.JLabel();
        jLabelSucsess = new javax.swing.JLabel();

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Übernahme von Stammdaten (UC03)");

        jButtonUebernehmen.setText("Stammdatenübernehmen");
        jButtonUebernehmen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUebernehmenActionPerformed(evt);
            }
        });

        jLabelFehler.setForeground(new java.awt.Color(255, 0, 0));
        jLabelFehler.setText(".");

        jLabelSucsess.setForeground(new java.awt.Color(51, 153, 0));
        jLabelSucsess.setText(".");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jButtonUebernehmen)))
                        .addGap(0, 46, Short.MAX_VALUE))
                    .addComponent(jLabelFehler, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelSucsess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(54, 54, 54)
                .addComponent(jButtonUebernehmen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFehler, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelSucsess, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonUebernehmenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUebernehmenActionPerformed
          jLabelFehler.setText("");
        jLabelSucsess.setText("");
        try {
            s.executeUpdate("INSERT INTO " + pre + "MA_VOGEL(ID_V, Name_Eng, Name_Lat, category, name_deu) SELECT g.B_ID, g.B_ENGLISH_NAME, g.B_SCIENTIFIC_NAME, g.b_category, (SELECT de_deutsch from MERLIN.birds_de deu where deu.de_latein = g.b_scientific_name) FROM MERLIN.birds g where g.B_ID NOT IN (Select ID_V FROM " + pre + "ma_vogel) ");
        } catch (SQLException ex) {
           jLabelFehler.setText("KEINE RECHTE UM DIE STAMMDATEN ZU AKTUALISIEREN !!!\n\n"+ ex.getMessage());
           return;
        }
        jLabelSucsess.setText("Erfolgreich übernommen");

    }//GEN-LAST:event_jButtonUebernehmenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Stammdaten.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Stammdaten.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Stammdaten.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Stammdaten.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Stammdaten().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonUebernehmen;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabelFehler;
    private javax.swing.JLabel jLabelSucsess;
    // End of variables declaration//GEN-END:variables
}
