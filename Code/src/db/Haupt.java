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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author abi611
 */
public class Haupt extends javax.swing.JFrame {

    String Username;
    String Passwort;
    String pre = "";
// static final String USER = Username; 
//private static final String PW = Passwort; 
    Connection c;
    Statement sLat, sPerson, sSpecies, sSubSpecies, sOrt, sRegion, sLand, sArea, sInsertBeobachtung, sBeobachtungen, sCLAusgabe, sCLDeu, sTickLifer;
    ResultSet rLat, rPersonen, rVoegelSpecies, rOrt, rBeob, rVoegelSubSpecies, rRegion, rLand, rArea, rBeobachtungen, rCLAusgabe, rCLDeu, rTickLifer;
    Date dt = new Date();
    SimpleDateFormat datum = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat uhrzeit = new SimpleDateFormat("HH:mm");

    public class ComboBoxObject {

        private String id;
        private String value;

        public ComboBoxObject(String id, String val) {
            this.id = id;
            this.value = val;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String toValue() {
            return this.id;
        }
    }

    public Haupt() {
        initComponents();
    }
    
    public String getLat(String deu){
        String erg = null;
        try {
            
            rLat = sLat.executeQuery("select * from " + pre + "MA_vogel where name_deu like '"+deu+"' OR name_lat like '"+deu+"'");
            
                rLat.first();
            if (rLat.getString("name_lat") == null) {
                erg = "";
            } else {
                erg = rLat.getString("name_lat").trim();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Haupt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return erg;
    }

    public Haupt(String Username, String Passwort, String DBOwner) throws SQLException {
        initComponents();
        this.Username = Username;
        this.Passwort = Passwort;


        System.out.println("Connecting...");

        try {


            if (Username.toUpperCase() != DBOwner.toUpperCase()) {
                pre = DBOwner + ".";
            }


            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@oracle.informatik.haw-hamburg.de:1521:inf09";
            c = DriverManager.getConnection(url, Username, Passwort);
            sPerson = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sSpecies = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sSubSpecies = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sRegion = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sArea = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sLand = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sOrt = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sInsertBeobachtung = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sBeobachtungen = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sCLAusgabe = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sCLDeu = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sTickLifer = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sLat = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // normalerweise kann ein ResultSet nur sequentiell von vorn nach
            // hinten ausgewertet
            // werden (TYPE_FORWARD_ONLY) - hier: Scrollable Cursor


            System.out.println("Lade Daten...");

            rPersonen = sPerson.executeQuery("select * from " + pre + "MA_PERSONEN where PERSONEN_NAME like '" + Username.toUpperCase() + "%'");
            rVoegelSpecies = sSpecies.executeQuery("Select coalesce(name_deu, name_lat) as NAME, name_deu,name_lat from " + pre + "ma_vogel where CATEGORY like 'species%'  ORDER BY name_deu asc,name_lat desc");
            rRegion = sRegion.executeQuery("Select DISTINCT REGION from " + pre + "MA_ORT");
            rOrt = sOrt.executeQuery("select * from " + pre + "MA_ORT order by Region, LAND");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "KEIN ZUGRIFF ZU BIRDWATCH", JOptionPane.ERROR_MESSAGE);
            System.out.println("KEIN ZUGRIFF ZU BIRDWATCH");
            this.dispose();
            e.printStackTrace();

        }

        System.out.println("Daten geladen!");


        System.out.println("Fülle Comboboxen...");

        while (rVoegelSpecies.next()) {
            jComboBoxSpecies.addItem(rVoegelSpecies.getString("name").trim());
        }


        while (rRegion.next()) {
            jComboBoxOrt1.addItem(rRegion.getString("REGION").trim());
        }



        jTextFieldBeobachtetVon.setText(Username);



        jTextFieldDV.setText(datum.format(dt));
        jTextFieldDB.setText(datum.format(dt));
        jTextFieldUV.setText(uhrzeit.format(dt));
        jTextFieldUB.setText(uhrzeit.format(dt));


        System.out.println("...geladen");

        //  c.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jButtonSpeichern = new javax.swing.JButton();
        jTextFieldDV = new javax.swing.JTextField();
        jComboBoxSpecies = new javax.swing.JComboBox();
        jTextFieldDB = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldUV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldUB = new javax.swing.JTextField();
        jComboBoxOrt1 = new javax.swing.JComboBox();
        jComboBoxSubSpecies = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaBemerkung = new javax.swing.JTextArea();
        jTextFieldBeobachtetVon = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBoxOrt2 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jComboBoxOrt3 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelFehler = new javax.swing.JLabel();
        jLabelSucsess = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemUebernahme = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItemAusgabeCL = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        jMenuItem4.setText("jMenuItem4");

        jMenuItem5.setText("jMenuItem5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("frameMain"); // NOI18N
        setResizable(false);

        jButtonSpeichern.setText("Speichern");
        jButtonSpeichern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSpeichernActionPerformed(evt);
            }
        });

        jTextFieldDV.setName("d_von"); // NOI18N

        jComboBoxSpecies.setName("species"); // NOI18N
        jComboBoxSpecies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSpeciesActionPerformed(evt);
            }
        });

        jTextFieldDB.setName("d_bis"); // NOI18N
        jTextFieldDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDBActionPerformed(evt);
            }
        });

        jLabel1.setText("Datum von:");

        jLabel2.setText("bis:");

        jTextFieldUV.setName("u_von"); // NOI18N

        jLabel3.setText("Uhrzeit von:");

        jLabel4.setText("bis:");

        jTextFieldUB.setName("u_bis"); // NOI18N
        jTextFieldUB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUBActionPerformed(evt);
            }
        });

        jComboBoxOrt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrt1ActionPerformed(evt);
            }
        });

        jComboBoxSubSpecies.setName("subspecies"); // NOI18N

        jTextAreaBemerkung.setColumns(20);
        jTextAreaBemerkung.setRows(5);
        jTextAreaBemerkung.setName("desc"); // NOI18N
        jScrollPane1.setViewportView(jTextAreaBemerkung);

        jTextFieldBeobachtetVon.setEnabled(false);
        jTextFieldBeobachtetVon.setName("user"); // NOI18N

        jLabel5.setText("Species:");

        jLabel6.setText("Sub Species:");

        jLabel7.setText("Bemerkung:");

        jLabel8.setText("Geoökologische Region:");

        jLabel9.setText("Land:");

        jComboBoxOrt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrt2ActionPerformed(evt);
            }
        });

        jLabel10.setText("Geografisches Gebiet:");

        jLabel11.setText("Beobachtet von:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Neue Beobachung Eintragen (UC02)");

        jLabelFehler.setForeground(new java.awt.Color(255, 0, 0));
        jLabelFehler.setText(".");

        jLabelSucsess.setForeground(new java.awt.Color(51, 153, 0));
        jLabelSucsess.setText(".");

        jMenu2.setText("Stamdaten");

        jMenuItemUebernahme.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemUebernahme.setText("Übernahme von Stammdaten (UC03)");
        jMenuItemUebernahme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUebernahmeActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemUebernahme);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Checkliste");

        jMenuItemAusgabeCL.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAusgabeCL.setText("Ausgabe einer Checkliste (UC05)");
        jMenuItemAusgabeCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAusgabeCLActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemAusgabeCL);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldBeobachtetVon, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11)
                                .addComponent(jButtonSpeichern))
                            .addGap(29, 29, 29)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabelFehler, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelSucsess, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jComboBoxOrt1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jComboBoxOrt2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jComboBoxOrt3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jComboBoxSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6)
                                    .addComponent(jComboBoxSubSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldDV, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jTextFieldDB, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldUV, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTextFieldUB, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldUV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldUB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxSubSpecies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBoxOrt1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBoxOrt3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxOrt2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldBeobachtetVon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelFehler, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSpeichern)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelSucsess, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jTextFieldDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDBActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextFieldDBActionPerformed

private void jTextFieldUBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUBActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextFieldUBActionPerformed

    private void jComboBoxSpeciesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSpeciesActionPerformed
        try {
            rVoegelSubSpecies = sSubSpecies.executeQuery("Select coalesce(name_deu, name_lat) as NAME, id_v, name_deu, name_lat from " + pre + "ma_vogel where NAME_LAT like '" + jComboBoxSpecies.getSelectedItem().toString() + "%' OR NAME_deu like '" + jComboBoxSpecies.getSelectedItem().toString() + "%'");
            jComboBoxSubSpecies.removeAll();
            jComboBoxSubSpecies.removeAllItems();
            while (rVoegelSubSpecies.next()) {
                jComboBoxSubSpecies.addItem(new ComboBoxObject(rVoegelSubSpecies.getString("ID_V").trim(), rVoegelSubSpecies.getString("NAME").trim()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Haupt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jComboBoxSpeciesActionPerformed

    private void jComboBoxOrt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOrt1ActionPerformed
        // TODO add your handling code here:
        jComboBoxOrt2.removeAllItems();
        jComboBoxOrt3.removeAllItems();
        try {
            rLand = sLand.executeQuery("Select DISTINCT LAND from " + pre + "MA_ORT where REGION like '" + jComboBoxOrt1.getSelectedItem().toString() + "%'");
            jComboBoxOrt2.removeAll();
            jComboBoxOrt2.removeAllItems();


            while (rLand.next()) {
                jComboBoxOrt2.addItem(rLand.getString("LAND").trim());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Haupt.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jComboBoxOrt1ActionPerformed

    private void jComboBoxOrt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOrt2ActionPerformed
        // TODO add your handling code here:
        jComboBoxOrt3.removeAllItems();

        if (jComboBoxOrt2.getSelectedItem() != "") {

            try {
                rArea = sArea.executeQuery("Select * from " + pre + "MA_ORT where LAND like '" + jComboBoxOrt2.getSelectedItem().toString() + "%'");



                while (rArea.next()) {
                    jComboBoxOrt3.addItem(new ComboBoxObject(rArea.getString("ID_O").trim(), rArea.getString("AREA").trim()));
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }
        }
    }//GEN-LAST:event_jComboBoxOrt2ActionPerformed

    private void jButtonSpeichernActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpeichernActionPerformed
        jLabelFehler.setText("");
        jLabelSucsess.setText("");

        String vogelID = ((ComboBoxObject) jComboBoxSubSpecies.getSelectedItem()).toValue();

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
        String datumUhrzeitVon;
        try {
            String Uhrzeit;
            if (jTextFieldUV.getText() == "") {
                Uhrzeit = "00:00";
            } else {
                Uhrzeit = jTextFieldUV.getText();
            }

            datumUhrzeitVon = df.format(df.parse(jTextFieldDV.getText() + " " + Uhrzeit));
            System.out.print("==> datumUhrzeitVon: " + datumUhrzeitVon);

        } catch (ParseException ex) {

            jLabelFehler.setText("datumUhrzeitVon: " + ex.getLocalizedMessage());
            return;
        }

        String datumUhrzeitBis;
        try {
            String Uhrzeit;
            if (jTextFieldUB.getText() == "") {
                Uhrzeit = "00:00";
            } else {
                Uhrzeit = jTextFieldUB.getText();
            }
            datumUhrzeitBis = df.format(df.parse(jTextFieldDB.getText() + " " + Uhrzeit));
            System.out.print("\t datumUhrzeitBis: " + datumUhrzeitBis);
        } catch (ParseException ex) {
            jLabelFehler.setText("datumUhrzeitBis: " + ex.getLocalizedMessage());
            return;
        }

        String personenID = "";
        try {
            rPersonen.first();
            personenID = rPersonen.getString("ID_P").trim();
            System.out.print("\t PersonenID: " + personenID);
        } catch (SQLException ex) {
            jLabelFehler.setText("personenID; " + ex.getMessage());
            return;
        }

        String ortID;
        try {
            ortID = ((ComboBoxObject) jComboBoxOrt3.getSelectedItem()).toValue();
            System.out.print("\t OrtID: " + ortID);

        } catch (Exception ex) {
            jLabelFehler.setText("ortID: " + ex.getMessage());
            return;
        }


        String bemerkung;
        try {
            bemerkung = jTextAreaBemerkung.getText();
        } catch (Exception ex) {
            jLabelFehler.setText("bemerkung: " + ex.getMessage());
            return;
        }


        String beobachtungsID = "";
        try {
            rBeobachtungen = sBeobachtungen.executeQuery("select MAX(ID_B)+1 as ID_B from " + pre + "MA_BEOBACHTUNGEN");
            rBeobachtungen.first();
            if (rBeobachtungen.getString("ID_B") == null) {
                beobachtungsID = "1";
            } else {
                beobachtungsID = rBeobachtungen.getString("ID_B").trim();
            }

            System.out.println("\nNeue BeobachtungsID: " + beobachtungsID);

        } catch (SQLException ex) {
            jLabelFehler.setText("beobachtungsID: " + ex.getMessage());
            return;
        }

        String TickLifer;
        try {
            rTickLifer = sTickLifer.executeQuery("SELECT count(*) anzahl FROM " + pre + "ma_beobachtungen  where id_v='" + vogelID + "' and id_p = '" + personenID + "' ");
            rTickLifer.first();
            int anzahl = Integer.parseInt(rTickLifer.getString("Anzahl").trim());
            System.out.println("Anzahl bisher:" + anzahl);

            if (anzahl == 0) {
                TickLifer = "LIFER";
            } else {
                TickLifer = "TICK";
            }

        } catch (SQLException ex) {
            jLabelFehler.setText("TickLifer: " + ex.getMessage());
            return;
        }



        try {

            // TODO add your handling code here:
            sInsertBeobachtung.executeUpdate("INSERT INTO " + pre + "MA_BEOBACHTUNGEN (ID_B, UZ_VON, VERIFIZIERT, ID_V, ID_P, ID_O, UZ_BIS, BEMERKUNG) VALUES ('" + beobachtungsID + "', '" + datumUhrzeitVon + "', '0', '" + vogelID + "', '" + personenID + "', '" + ortID + "', '" + datumUhrzeitBis + "', '" + bemerkung + "')");
            // sInsertBeobachtung.close();

        } catch (SQLException ex) {
            jLabelFehler.setText(ex.toString());
            return;
        }
        JOptionPane.showMessageDialog(this, "Eintrag erfolgreich als " + TickLifer + " gespeichert!");
        jLabelSucsess.setText("Eintrag erfolgreich " + TickLifer + " gespeichert!");

    }//GEN-LAST:event_jButtonSpeichernActionPerformed

    private void jMenuItemUebernahmeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUebernahmeActionPerformed

        new Stammdaten(Username, Passwort,pre).setVisible(true);

    }//GEN-LAST:event_jMenuItemUebernahmeActionPerformed

    private void jMenuItemAusgabeCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAusgabeCLActionPerformed
        try {
            //rCLAusgabe = sCLAusgabe.executeQuery("select NAME_LAT, UZ_VON, UZ_BIS, AREA, LAND from " + pre + "MA_BEOBACHTUNGEN, MA_VOGEL, ma_ort WHERE id_p= (SELECT id_p FROM MA_PERSONEN WHERE personen_name = '" + Username.toUpperCase() + "') and MA_BEOBACHTUNGEN.id_v = MA_VOGEL.id_v and ma_beobachtungen.id_o = ma_ort.id_o");
            //rCLDeu = sCLDeu.executeQuery("SELECT * FROM MERLIN.birds_de");
            // rCLDeu = sCLDeu.executeQuery("SELECT Name_lat As Vogel_Name,  uz_von As Gesehen_Am , Area As Gesehen_im_Bereich FROM MERLIN.birds_de deu LEFT JOIN " + pre + "ma_vogel alle ON deu.de_latein = alle.name_lat LEFT JOIN " + pre + "MA_BEOBACHTUNGEN beo ON alle.id_v = beo.id_v left JOIN " + pre + "MA_Ort ort on beo.id_o = ort.id_o and ort.land = 'Deutschland' LEFT JOIN " + pre + "ma_personen per on beo.id_p = per.id_p and per.personen_name = '" + Username.toUpperCase() + "%' ORDER BY deu.DE_LATEIN");

            rCLDeu = sCLDeu.executeQuery(" SELECT coalesce(de_deutsch, de_latein) As Vogel_Name, Gesehen_im_Bereich, Gesehen_Am FROM MERLIN.birds_de deu LEFT JOIN (SELECT  name_lat, uz_von As Gesehen_Am , Area As Gesehen_im_Bereich ,per.personen_name            FROM " + pre + "MA_BEOBACHTUNGEN beo       LEFT JOIN " + pre + "ma_vogel vog          ON beo.id_v = vog.id_v      LEFT JOIN " + pre + "MA_Ort ort            on beo.id_o = ort.id_o    LEFT JOIN " + pre + "ma_personen per       on beo.id_p = per.id_p  where ort.land = 'Deutschland' and per.personen_name = '" + Username.toUpperCase() + "') sub ON deu.de_latein = sub.name_lat ORDER BY de_deutsch asc");



            JTable table = new JTable(buildTableModel(rCLDeu));
            JOptionPane.showMessageDialog(null, new JScrollPane(table), "Checkliste für: DEUTSCHLAND", 1);


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }



    }//GEN-LAST:event_jMenuItemAusgabeCLActionPerformed

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
            java.util.logging.Logger.getLogger(Haupt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Haupt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Haupt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Haupt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Haupt().setVisible(true);
            }
        });
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column).trim());

        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSpeichern;
    private javax.swing.JComboBox jComboBoxOrt1;
    private javax.swing.JComboBox jComboBoxOrt2;
    private javax.swing.JComboBox jComboBoxOrt3;
    private javax.swing.JComboBox jComboBoxSpecies;
    private javax.swing.JComboBox jComboBoxSubSpecies;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFehler;
    private javax.swing.JLabel jLabelSucsess;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItemAusgabeCL;
    private javax.swing.JMenuItem jMenuItemUebernahme;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaBemerkung;
    private javax.swing.JTextField jTextFieldBeobachtetVon;
    private javax.swing.JTextField jTextFieldDB;
    private javax.swing.JTextField jTextFieldDV;
    private javax.swing.JTextField jTextFieldUB;
    private javax.swing.JTextField jTextFieldUV;
    // End of variables declaration//GEN-END:variables
}
