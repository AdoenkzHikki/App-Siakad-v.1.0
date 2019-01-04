/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Form;

import Tool.KoneksiDB;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Adoenkz_Hikki
 */
public class IfrMahasiswa extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    
    String sqlselect , sqlinsert, sqldelete;
    String vnim, vid_ta, vkd_prodi, vnama_mhs, vjk, vtmp_lahir, vtgl_lahir,
            vagama, vnama_ayah, vnama_ibu, valamat, vno_telepon, vfoto;
    File imageFile = null;
    
    private DefaultTableModel tbMahasiswa;
    SimpleDateFormat tglInput = new SimpleDateFormat("yyy-MM-dd");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyy");
    
    /**
     * Creates new form IfrMahasiswa
     */
    public IfrMahasiswa() {
        initComponents();
        
        listTA();
        listProdi();
        clearInput();
        setTabelMahasiswa();
        showDataMahasiswa();
        jdInputMahasiswa.setSize(600, 650);
        jdInputMahasiswa.setLocationRelativeTo(this);
    }

    private void clearInput (){
        txtNIM.setText ("");
        cmbTA.setSelectedIndex(0);
        cmbProdi.setSelectedIndex(0);
        cmbJnsKel.setSelectedIndex(0);
        cmbAgama.setSelectedIndex(0);
        txtNamaMhs.setText("");
        txtTempatLahir.setText("");
        dtTglLahir.setDate(new Date());
        txtNamaAyah.setText("");
        txtNamaIbu.setText("");
        txtAlamat.setText("");
        txtNoTelepon.setText("");
        lblFoto.setIcon (new javax.swing.ImageIcon (getClass().
                getResource("/image/no-picture.jpg")));
        imageFile = null;
        btnSimpan.setText("Simpan");
        vnim="";
        
    }
    
    private void setTabelMahasiswa(){
        String[] kolom1 = {"NIM", "Nama Mahasiswa", "L/P", "tempat", "Tgl.lahir",
        "program Studi", "Alamat", "No.Telepon"};
        tbMahasiswa = new DefaultTableModel (null, kolom1){
            Class [] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            public Class getColumnClass (int columnIndex){
                return types [columnIndex];
            }
            
            //agar tabel tidak bisa diedit
            public boolean isCellEditable (int row, int col){
                int cola = tbMahasiswa.getColumnCount();
                return (col < cola) ? false : true;
            }
        };
        tbDataMahasiswa.setModel (tbMahasiswa);
        tbDataMahasiswa.getColumnModel().getColumn (0).setPreferredWidth(75);
        tbDataMahasiswa.getColumnModel().getColumn (1).setPreferredWidth(250);
        tbDataMahasiswa.getColumnModel().getColumn (2).setPreferredWidth(25);
        tbDataMahasiswa.getColumnModel().getColumn (3).setPreferredWidth(75);
        tbDataMahasiswa.getColumnModel().getColumn (4).setPreferredWidth(75);
        tbDataMahasiswa.getColumnModel().getColumn (5).setPreferredWidth(150);
        tbDataMahasiswa.getColumnModel().getColumn (6).setPreferredWidth(300);
        tbDataMahasiswa.getColumnModel().getColumn (7).setPreferredWidth(150);
    }
    
    private void clearTabelMahasiswa(){
        int row = tbMahasiswa.getRowCount();
        for (int i = 0; i< row; i++){
            tbMahasiswa.removeRow(0);
        }
    }
    
    private void showDataMahasiswa(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabelMahasiswa();
            sqlselect = "select * from tbmahasiswa a, tbprodi b " 
                    + " where a.kd_prodi=b.kd_prodi order by a.kd_prodi, a.nama_mhs asc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while (res.next()){
                vnim = res.getString("nim");
                vnama_mhs = res.getString("nama_mhs");
                vjk = res.getString("jk");
                vtmp_lahir = res.getString("tmp_lahir");
                vtgl_lahir = tglview.format(res.getDate("tgl_lahir"));
                vkd_prodi = res.getString("prodi");
                valamat = res.getString("alamat");
                vno_telepon = res.getString("no_telepon");
                Object[]data = {vnim, vnama_mhs, vjk, vtmp_lahir,
                    vtgl_lahir, vkd_prodi, valamat, vno_telepon};
                tbMahasiswa.addRow(data);    
            }
            lblRecord.setText("Record : " +tbDataMahasiswa.getRowCount());
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method showDataMahasiswa () : " +ex);
        }
    }
    
    private void cariNamaMahasiswa(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            clearTabelMahasiswa();
            sqlselect = " select * from tbmahasiswa a, tbprodi b"
                    + " where a.kd_prodi=b.kd_prodi and a.nama_mhs like '%"+txtCari.getText()+"%'"
                    + " order by a.kd_prodi, a.nama_mhs asc ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            while (res.next()){
                vnim = res.getString("nim");
                vnama_mhs = res.getString("nama_mhs");
                vjk = res.getString("jk");
                vtmp_lahir = res.getString("tmp_lahir");
                vtgl_lahir = tglview.format(res.getDate("tgl_lahir"));
                vkd_prodi = res.getString("prodi");
                valamat = res.getString("alamat");
                vno_telepon = res.getString("no_telepon");
                Object[] data = {vnim, vnama_mhs, vjk, vtmp_lahir,
                    vtgl_lahir, vkd_prodi, valamat, vno_telepon};
                tbMahasiswa.addRow(data);
                
            }
            lblRecord.setText("Record :" +tbDataMahasiswa.getRowCount());
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method showDataMahasiswa () : " +ex);
        }
    }
    
    private void getDataMahasiswa (){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select * from tbmahasiswa a, tbprodi b, tbthangkatan c "
                    + " where a.kd_prodi=b.kd_prodi and a.id_ta=c.id_ta and a.nim='"+vnim+"' ";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                cmbTA.setSelectedItem(res.getString("tahun_angkatan"));
                cmbProdi.setSelectedItem(res.getString("prodi"));
                txtNamaMhs.setText(res.getString("nama_mhs"));
                vjk = res.getString ("jk");
                if(vjk.equals("L")){
                    cmbJnsKel.setSelectedItem("Laki-laki");
                }else{
                    cmbJnsKel.setSelectedItem("Perempuan");
                }
                cmbAgama.setSelectedItem(res.getString("agama"));
                txtTempatLahir.setText(res.getString("tmp_lahir"));
                dtTglLahir.setDate(res.getDate("tgl_lahir"));
                txtNamaAyah.setText(res.getString("nama_ayah"));
                txtNamaIbu.setText(res.getString("nama_ibu"));
                txtAlamat.setText(res.getString("alamat"));
                txtNoTelepon.setText(res.getString("no_telepon"));
                
                if (res.getBlob("foto").getBinaryStream() == null){
                    lblFoto.setIcon(new javax.swing.ImageIcon(getClass().
                            getResource("/Image/no-picture.jpg")));
                }else {
                    InputStream is = res.getBlob ("foto").getBinaryStream();
                    ImageIcon icon = new ImageIcon(resize (ImageIO.read(is),
                            lblFoto.getWidth(), lblFoto.getHeight()));
                    lblFoto.setIcon(icon);
                }
            }
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataMahasiswa() :" +ex,
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }catch (IOException ex){
            Logger.getLogger(IfrMahasiswa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    String [] KeyTA;
    private void listTA(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = " select * from tbthangkatan order by id_ta asc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            cmbTA.removeAllItems();
            cmbTA.repaint();
            cmbTA.addItem("-- pilih --");
            int i = 1;
            while (res.next ()){
                cmbTA.addItem(res.getString(2));
                i++;
            }
            res.first();
            KeyTA = new String [i+1];
            for (Integer x = 1; x<i; x++){
                KeyTA[x] = res.getString(1);
                res.next();
            }
        }catch (SQLException ex){
            JOptionPane.showMessageDialog (this, "Error method listTA() :"
                + ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    String[] KeyProdi;
    private void listProdi(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "select kd_prodi, prodi"
                    + " from tbprodi order by kd_jur asc";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            cmbProdi.removeAllItems();
            cmbProdi.repaint();
            cmbProdi.addItem("-- pilih --");
            int i = 1;
            while (res.next ()){
                cmbProdi.addItem(res.getString(2));
                i++;
            }
            res.first(); 
            KeyProdi = new String [i+1];
            for (Integer x = 1; x<i; x++){
                KeyProdi[x] = res.getString(1);
                res.next();
            }
        }catch (SQLException ex){
            JOptionPane.showMessageDialog (this, "Error method listProdi() : "
                + ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void aksiSimpan (){
        vnim = txtNIM.getText();
        vid_ta = KeyTA[cmbTA.getSelectedIndex()];
        vkd_prodi = KeyProdi [cmbProdi.getSelectedIndex()];
        vnama_mhs = txtNamaMhs.getText();
        vagama = cmbAgama.getSelectedItem().toString();
        vtmp_lahir = txtTempatLahir.getText();
        vtgl_lahir = tglInput.format(dtTglLahir.getDate());
        vnama_ayah = txtNamaAyah.getText();
        vnama_ibu = txtNamaIbu.getText();
        valamat = txtAlamat.getText();
        vno_telepon = txtNoTelepon.getText();
        if (btnSimpan.getText().equals("Simpan")){
            
            if (imageFile == null){
                try{
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "insert into tbmahasiswa values (?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement (sqlinsert);
                    stat.setString(1, vnim);
                    stat.setString(2, vid_ta);
                    stat.setString(3, vkd_prodi);
                    stat.setString(4, vnama_mhs);
                    stat.setString(5, vjk);
                    stat.setString(6, vtmp_lahir);
                    stat.setString(7, vtgl_lahir);
                    stat.setString(8, vagama);
                    stat.setString(9, vnama_ayah);
                    stat.setString(10, vnama_ibu);
                    stat.setString(11, valamat);
                    stat.setString(12, vno_telepon);
                    stat.executeUpdate();
                    clearInput(); showDataMahasiswa(); jdInputMahasiswa.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil Disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan() :"+ex,
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                try{
                    InputStream is = new FileInputStream(imageFile);
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "insert into tbmahasiswa values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnim);
                    stat.setString(2, vkd_prodi);
                    stat.setString(3, vid_ta);
                    stat.setString(4, vnama_mhs);
                    stat.setString(5, vjk);
                    stat.setString(6, vtmp_lahir);
                    stat.setString(7, vtgl_lahir);
                    stat.setString(8, vagama);
                    stat.setString(9, vnama_ayah);
                    stat.setString(10, vnama_ibu);
                    stat.setString(11, valamat);
                    stat.setString(12, vno_telepon);
                    stat.setBlob(13, is);
                    stat.executeUpdate();
                    clearInput(); showDataMahasiswa(); jdInputMahasiswa.setVisible(false);
                    JOptionPane.showMessageDialog (this, "Data berhasil Disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan() :" +ex,
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }catch (FileNotFoundException ex){
                    Logger.getLogger(IfrMahasiswa.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } 
        }else {
            
            if (imageFile == null) {
                try{
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbmahasiswa set kd_prodi=?, "
                    + " nama_mhs=?, jk=?, "
                    + " tmp_lahir=?, tgl_lahir=?,"
                    + " agama=?, nama_ayah=?, "
                    + " nama_ibu=?, alamat=?, "
                    + " no_telepon=? where nim='"+vnim+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkd_prodi);
                    stat.setString(2, vnama_mhs);
                    stat.setString(3, vjk);
                    stat.setString(4, vtmp_lahir);
                    stat.setString(5, vtgl_lahir);
                    stat.setString(6, vagama);
                    stat.setString(7, valamat);
                    stat.setString(8, vno_telepon);
                    stat.setString(9, vnama_ayah);
                    stat.setString(10, vnama_ibu);
                    stat.executeUpdate();
                    clearInput(); showDataMahasiswa(); jdInputMahasiswa.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah", 
                                "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex, 
                       "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }else{
                try{
                    InputStream is = new FileInputStream(imageFile);
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbmahasiswa set kd_prodi=?, "
                    + " nama_mhs=?, jk=?, "
                    + " tmp_lahir=?, tgl_lahir=?,"
                    + " agama=?, nama_ayah=?, "
                    + " nama_ibu=?, alamat=?, "
                    + " no_telepon=?, foto=? where nim='"+vnim+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkd_prodi);
                    stat.setString(2, vnama_mhs);
                    stat.setString(3, vjk);
                    stat.setString(4, vtmp_lahir);
                    stat.setString(5, vtgl_lahir);
                    stat.setString(6, vagama);
                    stat.setString(7, vnama_ayah);
                    stat.setString(8, vnama_ibu);
                    stat.setString(9, valamat);
                    stat.setString(10, vno_telepon);
                    stat.setBlob(11, is);
                    stat.executeUpdate();
                    clearInput(); showDataMahasiswa(); jdInputMahasiswa.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah", 
                                "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    
                }catch(SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan() : "+ex, 
                       "Informasi", JOptionPane.INFORMATION_MESSAGE);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(IfrMahasiswa.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
    }
    
    private void aksiHapus (){
        int jawab = JOptionPane.showConfirmDialog (this,
                "Apakah anda yakin akan menghapus data ini ? NIM :" +vnim,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION){
            try{
                _Cnn = null;
                _Cnn = getCnn.getConnection ();
                sqldelete = "delete from tbmahasiswa "
                        + "where nim='"+vnim+"' ";
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate (sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil Dihapus" ,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                clearInput (); showDataMahasiswa();
            }catch (SQLException ex){
                JOptionPane.showMessageDialog (this, "Error method aksiHapus() :" +ex,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    public static BufferedImage loadImage (String ref){
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File (ref));
        }catch (Exception e ){
        }
        return bimg;
    }
    
    
    public static BufferedImage resize (BufferedImage img, int newW, int newH){
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage (newW, newH,img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint ( RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null );
        g.dispose();
        return dimg;
    }
    
    private void ambilGambar (){
        javax.swing.JFileChooser jfc = new JFileChooser ();
        FileFilter jpgFilter , gifFilter, bothFilter;
        jpgFilter = new FileNameExtensionFilter ("Gambar JPEG ,","jpg");
        gifFilter = new FileNameExtensionFilter ("Gambar GIF ,","gif");
        bothFilter = new FileNameExtensionFilter ("Gambar JPEG dan GIF ,","jpg","gif");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(jpgFilter);
        jfc.addChoosableFileFilter(gifFilter);
        jfc.addChoosableFileFilter(bothFilter);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            vfoto = jfc.getSelectedFile().toString();
            imageFile = jfc.getSelectedFile();
            BufferedImage loadimg = loadImage (vfoto);
            ImageIcon imageIcon = new ImageIcon (resize (loadimg, lblFoto.getWidth(), lblFoto.getHeight()));
            lblFoto.setIcon(imageIcon);
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdInputMahasiswa = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblFoto = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        cmbTA = new javax.swing.JComboBox<>();
        cmbProdi = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        txtNIM = new javax.swing.JTextField();
        txtNamaMhs = new javax.swing.JTextField();
        cmbAgama = new javax.swing.JComboBox<>();
        cmbJnsKel = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        txtTempatLahir = new javax.swing.JTextField();
        dtTglLahir = new com.toedter.calendar.JDateChooser();
        txtNamaAyah = new javax.swing.JTextField();
        txtNamaIbu = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JEditorPane();
        txtNoTelepon = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDataMahasiswa = new javax.swing.JTable();
        lblRecord = new javax.swing.JLabel();

        jdInputMahasiswa.setTitle(".: Form Entry Mahasiswa");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/logo.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Form Entry Mahasiswa");

        jLabel5.setText("Form Ini Digunakan Untuk melakukan Input Data Mahasiswa");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Foto :"));

        lblFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/no-picture.jpg"))); // NOI18N
        lblFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFotoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFoto)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lblFoto)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Navigasi :"));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save-black.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/btn_delete.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(173, 173, 173))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        cmbTA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --" }));
        cmbTA.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tahun Angkatan :"));
        cmbTA.setOpaque(false);
        cmbTA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTAActionPerformed(evt);
            }
        });

        cmbProdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --" }));
        cmbProdi.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Program Studi :"));
        cmbProdi.setOpaque(false);
        cmbProdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProdiActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Input Data :"));
        jPanel3.setOpaque(false);

        txtNIM.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIM :"));
        txtNIM.setOpaque(false);

        txtNamaMhs.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Mahasiswa :"));
        txtNamaMhs.setOpaque(false);

        cmbAgama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Islam", "Katholik", "Protestan", "Hindu", "Budha" }));
        cmbAgama.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Agama :"));
        cmbAgama.setOpaque(false);
        cmbAgama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAgamaActionPerformed(evt);
            }
        });

        cmbJnsKel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Laki-laki", "Perempuan" }));
        cmbJnsKel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jenis Kelamin :"));
        cmbJnsKel.setOpaque(false);
        cmbJnsKel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbJnsKelItemStateChanged(evt);
            }
        });

        txtTempatLahir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tempat :"));
        txtTempatLahir.setOpaque(false);
        txtTempatLahir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTempatLahirActionPerformed(evt);
            }
        });

        dtTglLahir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Lahir :"));

        txtNamaAyah.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Ayah :"));
        txtNamaAyah.setOpaque(false);
        txtNamaAyah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaAyahActionPerformed(evt);
            }
        });

        txtNamaIbu.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Ibu :"));
        txtNamaIbu.setOpaque(false);
        txtNamaIbu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaIbuActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Alamat :"));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setOpaque(false);
        jScrollPane2.setViewportView(txtAlamat);

        txtNoTelepon.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "No Telepon / HP :"));
        txtNoTelepon.setOpaque(false);
        txtNoTelepon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoTeleponActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtTempatLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtTglLahir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtNamaAyah, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNamaIbu))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtNIM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNamaMhs, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cmbJnsKel, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbAgama, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtNoTelepon))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNIM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaMhs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJnsKel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAgama, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dtTglLahir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTempatLahir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaAyah)
                    .addComponent(txtNamaIbu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNoTelepon)
                .addContainerGap())
        );

        javax.swing.GroupLayout jdInputMahasiswaLayout = new javax.swing.GroupLayout(jdInputMahasiswa.getContentPane());
        jdInputMahasiswa.getContentPane().setLayout(jdInputMahasiswaLayout);
        jdInputMahasiswaLayout.setHorizontalGroup(
            jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputMahasiswaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jdInputMahasiswaLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jdInputMahasiswaLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdInputMahasiswaLayout.createSequentialGroup()
                                .addComponent(cmbTA, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbProdi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jdInputMahasiswaLayout.setVerticalGroup(
            jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputMahasiswaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdInputMahasiswaLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18))
                    .addComponent(jLabel3))
                .addGroup(jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jdInputMahasiswaLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jdInputMahasiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTA, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbProdi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle(".: Form Mahasiswa");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Navigasi"));

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-add.png"))); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-hapus.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Cari Nama");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(5, 5, 5))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Mehasiswa : Klik 2x untuk Mengubah / Menghapus Data"));

        tbDataMahasiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NIM", "Nama Mahasiswa", "L / P", "Tempat Lahir", "Tgl.Lahir", "Alamat", "No.Telepon"
            }
        ));
        tbDataMahasiswa.setRowHeight(25);
        tbDataMahasiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataMahasiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDataMahasiswa);

        lblRecord.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRecord.setText("Record : 0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblRecord)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (vnim.equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else{
            aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        cariNamaMahasiswa();
    }//GEN-LAST:event_txtCariActionPerformed

    private void cmbProdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProdiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProdiActionPerformed

    private void cmbAgamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAgamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAgamaActionPerformed

    private void txtTempatLahirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTempatLahirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTempatLahirActionPerformed

    private void txtNamaAyahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaAyahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaAyahActionPerformed

    private void txtNamaIbuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaIbuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaIbuActionPerformed

    private void txtNoTeleponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoTeleponActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoTeleponActionPerformed

    private void cmbTAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTAActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        clearInput();
        jdInputMahasiswa.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void tbDataMahasiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataMahasiswaMouseClicked
        if (evt.getClickCount() == 1){
            btnHapus.setEnabled (true);
            vnim = tbDataMahasiswa.getValueAt(tbDataMahasiswa.getSelectedRow(), 0).toString ();
            txtNIM.setText(vnim);
        }else if (evt.getClickCount()==2){
            btnSimpan.setText("Ubah");
            jdInputMahasiswa.setVisible(true); jdInputMahasiswa.setLocationRelativeTo(this);
            getDataMahasiswa();
        }
    }//GEN-LAST:event_tbDataMahasiswaMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtNIM.getText().equals("")){
            JOptionPane.showMessageDialog (this, " Data NIM harus diisi", "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
        }else if (txtNamaMhs.getText().equals("")){
            JOptionPane.showMessageDialog (this, " Data Nama harus diisi", "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
        }else{
            aksiSimpan();
            jdInputMahasiswa.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void lblFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFotoMouseClicked
        ambilGambar();
    }//GEN-LAST:event_lblFotoMouseClicked

    private void cmbJnsKelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbJnsKelItemStateChanged
        if(cmbJnsKel.getSelectedItem().equals("Laki-laki")){
            vjk ="L";
        }else {
            vjk = "P";
        }
    }//GEN-LAST:event_cmbJnsKelItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cmbAgama;
    private javax.swing.JComboBox<String> cmbJnsKel;
    private javax.swing.JComboBox<String> cmbProdi;
    private javax.swing.JComboBox<String> cmbTA;
    private com.toedter.calendar.JDateChooser dtTglLahir;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JDialog jdInputMahasiswa;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblRecord;
    private javax.swing.JTable tbDataMahasiswa;
    private javax.swing.JEditorPane txtAlamat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNIM;
    private javax.swing.JTextField txtNamaAyah;
    private javax.swing.JTextField txtNamaIbu;
    private javax.swing.JTextField txtNamaMhs;
    private javax.swing.JTextField txtNoTelepon;
    private javax.swing.JTextField txtTempatLahir;
    // End of variables declaration//GEN-END:variables
}
