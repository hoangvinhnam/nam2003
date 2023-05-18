/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Assignment;


import java.awt.Image;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;


/**
 *
 * @author MSI
 */
public class QuanLySinhVien extends javax.swing.JFrame {
    List<SinhVien> dsSV = new ArrayList<>();
    DefaultTableModel tbModel;
    String[] header = {"MaSV","Họ tên","Email", "Số ĐT", "Giới tính", "Địa chỉ", "Hình"};
    private String strHinhAnh = "";
    private String url = "jdbc:sqlserver://localhost:1433;databaseName=FPL_ĐàoTạo;username=sa;password=123;encrypt=true;trustServerCertificate=true";
    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    private PreparedStatement pst = null;
    int current = 0;
     
    /**
     * Creates new form QuanLySinhVien
     */
    public QuanLySinhVien() {
        initComponents();
        this.setTitle("Quản lý sinh viên");
        this.setLocationRelativeTo(null);
        tbModel = new DefaultTableModel(header, 5);
        tbl_thongTinSV.setModel(tbModel);
        loadDataArray();
        Display(current);
    }
    
    // loadData
     void loadDataArray(){
            Vector dataRow = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
                con = DriverManager.getConnection(url);
                String sql = "Select * from STUDENTS";
                st = con.createStatement();
                rs = st.executeQuery(sql);
                
                tbModel.setRowCount(0);
                while(rs.next()){
                    dataRow = new Vector();
                    dataRow.add(rs.getString(1));
                    dataRow.add(rs.getString(2));
                    dataRow.add(rs.getString(3));
                    dataRow.add(rs.getString(4));
                    dataRow.add(rs.getBoolean(5));
                    dataRow.add(rs.getString(6));
                    dataRow.add(rs.getString(7));
                   
                    tbModel.addRow(dataRow);
                }
                
            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
            }finally{
                try {
                    if(con != null){
                    con.close();
                    }
                    
                    if(st != null){
                    st.close();
                    }
                    
                    if(rs != null){
                    rs.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error: "+e.getMessage());
                }
                
            }
                
        }
     
     void Display(int index){
         txt_maSV.setText(tbModel.getValueAt(index, 0).toString());
         txt_hoTen.setText(tbModel.getValueAt(index, 1).toString());
         txt_email.setText(tbModel.getValueAt(index, 2).toString());
         txt_soDT.setText(tbModel.getValueAt(index, 3).toString());
         
         if(tbModel.getValueAt(index, 4).toString().equals("true")){
             rdo_Nam.setSelected(true);
         }else{
             rdo_Nu.setSelected(true);
         }
         txt_diaChi.setText(tbModel.getValueAt(index, 5).toString());
         //loadHinh
         strHinhAnh = tbModel.getValueAt(index, 6).toString();
         if(strHinhAnh.equals("No Avatar")){
             label_picture.setText("Hình ảnh");
             label_picture.setIcon(null);
         }else{
             label_picture.setText("");
             ImageIcon imageIcon = new ImageIcon(strHinhAnh);
             Image image = imageIcon.getImage();
             
             Icon icon= new ImageIcon(image.getScaledInstance(label_picture.getWidth(), label_picture.getHeight(), 0));
             label_picture.setIcon(icon);
         }
     }
    
    //MyCode
    private void New(){
        txt_maSV.setText("");
        txt_hoTen.setText("");
        txt_email.setText("");
        txt_soDT.setText("");
        rdo_Nam.setSelected(false);
        rdo_Nu.setSelected(false);
        txt_diaChi.setText("");
        label_picture.setIcon(null);
        label_picture.setText("Hình ảnh");
        strHinhAnh = "";
    }
    SinhVien readForm(){
        SinhVien sv = new SinhVien();
        sv.setMaSV(txt_maSV.getText());
        sv.setHoTen(txt_hoTen.getText());
        sv.setEmail(txt_email.getText());
        sv.setSoDT(txt_soDT.getText());
        sv.setGioiTinh(rdo_Nam.isSelected()?true:false);
        sv.setDiaChi(txt_diaChi.getText());
        if(strHinhAnh.equals("")){
            sv.setHinh("No Avatar");
        }else{
            sv.setHinh(strHinhAnh);
            
        }
        
        return sv;
    }
    
    void addSV(){
        dsSV.add(readForm());
        JOptionPane.showMessageDialog(this, "Lưu thành công");
        fillToTable();
    }
    
    void fillToTable(){
        if(dsSV.size()>0){
            tbModel.setRowCount(0);
            for (SinhVien sv : dsSV) {
                Object[] dataRow = {sv.getMaSV(), sv.getHoTen(), sv.getEmail(), sv.getSoDT(), sv.isGioiTinh(), sv.getDiaChi(), sv.getHinh()};
                tbModel.addRow(dataRow);
            }
        }
    }
    
    void showDetail(){
        int index = tbl_thongTinSV.getSelectedRow();
        txt_maSV.setText(dsSV.get(index).getMaSV());
        txt_hoTen.setText(dsSV.get(index).getHoTen());
        txt_email.setText(dsSV.get(index).getEmail());
        txt_soDT.setText(dsSV.get(index).getSoDT());
        boolean gt;
        if(dsSV.get(index).isGioiTinh()){
           rdo_Nam.setSelected(true);
        }else{
            rdo_Nu.setSelected(true);
        }
        //load hinh ảnh
        if(dsSV.get(index).getHinh().equals("No Avatar")){
            label_picture.setText("No Avartar");
            label_picture.setIcon(null);
        }else{
            label_picture.setText("");
            ImageIcon imageIcon = new ImageIcon(dsSV.get(index).getHinh());
            Image image = imageIcon.getImage();
            Icon icon = new ImageIcon(image.getScaledInstance(label_picture.getWidth(), label_picture.getHeight(), 0));
            label_picture.setIcon(icon);
        }
        
        txt_diaChi.setText(dsSV.get(index).getDiaChi());
    }
    
    int getStudentByID(){
       for(int i = 0; i < dsSV.size(); i++){
           if(txt_maSV.getText().equals(dsSV.get(i).getMaSV())){
               return i;
           }
       }
       return -1;
    }
    
    void deleteSV(){
        int index = getStudentByID();
        if(index != -1){
            int res = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa sinh viên có mã là: "+txt_maSV.getText());
            if(res == JOptionPane.OK_OPTION){
                dsSV.remove(index);
                
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                fillToTable();
            }
        }else{
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn sinh viên để xóa");
        }
        
        
    }
    
    void updateSV(){
        int index = getStudentByID();
        if(index != -1){
            int res = JOptionPane.showConfirmDialog(this, "Bạn có muốn cập nhật sinh viên có mã là: "+txt_maSV.getText());
            if(res == JOptionPane.OK_OPTION){
                dsSV.set(index, readForm());
                fillToTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn sinh viên để cập nhật");
        }
        
    }
    
    boolean checkForm(){
        if(txt_maSV.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sinh viên!");
            txt_maSV.requestFocus();
            return false;
        }
        
       // int index = getStudentByID();
//        if(index != -1){
//            if(txt_maSV.getText().equals(dsSV.get(getStudentByID()).getMaSV())){
//            JOptionPane.showMessageDialog(this, "Trùng mã sinh viên. Xin vui lòng nhập lại!");
//            txt_maSV.requestFocus();
//            return false;
//            }
//        }

      
     
        
        String patternHoTen = "SV\\d+";
        if(!txt_maSV.getText().matches(patternHoTen)){
            JOptionPane.showMessageDialog(this, "Mã sinh viên không hợp lệ. Vui lòng nhập lại! VD:SV001");
            txt_maSV.requestFocus();
            return false;
        }
        
        if(txt_hoTen.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!");
            txt_hoTen.requestFocus();
            return false;
        }
        
        
        
        if(txt_email.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email!");
            txt_email.requestFocus();
            return false;
        }
        
         
        
        String pattern = "\\w+@(\\w+.\\w+){1,2}";
        if(!txt_email.getText().matches(pattern)){
            JOptionPane.showMessageDialog(this, "Sai định dạng email!");
            txt_email.requestFocus();
            return false;
        }
        
        if(txt_soDT.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            txt_soDT.requestFocus();
            return false;
        }
        
        if(!(rdo_Nam.isSelected() || rdo_Nu.isSelected())){
             JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính nam hay nữ!");
             return false;
        }
        
        if(txt_diaChi.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ!");
            txt_diaChi.requestFocus();
            return false;
        }
        return true;
    }
    
     
    // End Code
    
    // JDBC CODE
    
        void saveSV(){
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
                con = DriverManager.getConnection(url);
                String sql = "insert into STUDENTS values (?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, txt_maSV.getText());
                pst.setString(2, txt_hoTen.getText());
                
                pst.setString(3, txt_email.getText());
                pst.setString(4, txt_soDT.getText());
                boolean gt;
                if(rdo_Nam.isSelected()){
                    gt = true;
                }else{
                    gt = false;
                }
                pst.setBoolean(5, gt);
                pst.setString(6, txt_diaChi.getText());
                if(strHinhAnh.equals("")){
                    strHinhAnh = "No Avatar";
                }
                pst.setString(7, strHinhAnh);
                
                int num = pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                System.out.println(num+" row affected");
                loadDataArray();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        
       
        
        void showDetail2(){
            int r = tbl_thongTinSV.getSelectedRow();
            txt_maSV.setText(tbModel.getValueAt(r, 0).toString());
            txt_hoTen.setText(tbModel.getValueAt(r, 1).toString());
            txt_email.setText(tbModel.getValueAt(r, 2).toString());
            txt_soDT.setText(tbModel.getValueAt(r, 3).toString());
            boolean gt;
            if(tbl_thongTinSV.getValueAt(r, 4).equals(true)){
                rdo_Nam.setSelected(true);
            }else{
                rdo_Nu.setSelected(true);
            }
            
            txt_diaChi.setText(tbModel.getValueAt(r, 5).toString());
            //load hinh anh
            strHinhAnh = tbModel.getValueAt(r, 6).toString();
            if(strHinhAnh.equals("No Avatar")){
                label_picture.setText("Hình ảnh");
                label_picture.setIcon(null);
            }else{
                label_picture.setText("");
                ImageIcon imageIcon = new ImageIcon(strHinhAnh);
                Image image = imageIcon.getImage();
                
                Icon icon = new ImageIcon(image.getScaledInstance(label_picture.getWidth(), label_picture.getHeight(), 0));
                label_picture.setIcon(icon);
            }
        }
        
        SinhVien getStudentByID2(String maSV){
            
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
                con = DriverManager.getConnection(url);
                String sql = "Select * from STUDENTS where MASV = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, maSV);
                rs = pst.executeQuery();
                while(rs.next()){
                   SinhVien sv = new SinhVien();
                   sv.setMaSV(rs.getString(1));
                   sv.setHoTen(rs.getString(2));
                   sv.setEmail(rs.getString(3));
                   sv.setSoDT(rs.getString(4));
                   sv.setGioiTinh(rs.getBoolean(5));
                   sv.setDiaChi(rs.getString(6));
                   sv.setHinh(rs.getString(7));
                   return sv;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    con.close();
                    pst.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            return null;
            
        }
        
        void updateSV2(){
            try {
                 int row = tbl_thongTinSV.getSelectedRow();
                if(row != -1){
                    con = DriverManager.getConnection(url);
                    String maSV = tbModel.getValueAt(row, 0).toString();
                    String sql = "Update STUDENTS set hoTen = ?, Email = ?, soDT = ?, Gioitinh = ?, Diachi = ?, Hinh = ? where MASV = ?";
                    pst = con.prepareStatement(sql);
                    pst.setString(7, maSV);
                    pst.setString(1, txt_hoTen.getText());
                    pst.setString(2, txt_email.getText());
                    pst.setString(3, txt_soDT.getText());
                    pst.setBoolean(4, rdo_Nam.isSelected()?true:false);
                    pst.setString(5, txt_diaChi.getText());
                    pst.setString(6, strHinhAnh);
                    int i = pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadDataArray();
                    System.out.println(i + " row affected");
                }else{
                    JOptionPane.showMessageDialog(this, "Chưa chọn sinh viên để cập nhật");
                    return;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    if(con != null){
                        con.close();
                    }
                    if(pst != null){
                        pst.close();
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        }
        
        void deleteSV2(){
            try {
                int row = tbl_thongTinSV.getSelectedRow();
                if(row != -1){
                    String maSV = tbModel.getValueAt(row, 0).toString();
                    con = DriverManager.getConnection(url);
                    String sql = "Delete from STUDENTS where MASV = ?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, maSV);
                    int num = pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadDataArray();
                    System.out.println(num + " row affected");
                }else{
                    JOptionPane.showMessageDialog(this, "Bạn chưa chọn sinh viên để xóa!");
                    return;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
        }
    // END CODE

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup_gioiTinh = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_maSV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_hoTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_soDT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdo_Nam = new javax.swing.JRadioButton();
        rdo_Nu = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_diaChi = new javax.swing.JTextArea();
        label_picture = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_thongTinSV = new javax.swing.JTable();
        btn_new = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Quản Lý Sinh Viên");

        jLabel2.setText("MaSV:");

        jLabel3.setText("Họ tên:");

        jLabel4.setText("Email:");

        jLabel5.setText("Số ĐT:");

        jLabel6.setText("Giới tính:");

        btnGroup_gioiTinh.add(rdo_Nam);
        rdo_Nam.setText("Nam");

        btnGroup_gioiTinh.add(rdo_Nu);
        rdo_Nu.setText("Nữ");

        jLabel7.setText("Địa chỉ:");

        txt_diaChi.setColumns(20);
        txt_diaChi.setRows(5);
        jScrollPane1.setViewportView(txt_diaChi);

        label_picture.setText("Hình ảnh");
        label_picture.setToolTipText("");
        label_picture.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.gray, java.awt.Color.white, java.awt.Color.gray));
        label_picture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                label_pictureMouseClicked(evt);
            }
        });

        tbl_thongTinSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_thongTinSV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_thongTinSVMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_thongTinSV);

        btn_new.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\add.png")); // NOI18N
        btn_new.setText("New");
        btn_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newActionPerformed(evt);
            }
        });

        btn_save.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\save.png")); // NOI18N
        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_delete.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\delete.png")); // NOI18N
        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_update.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\icon\\edit.png")); // NOI18N
        btn_update.setText("Update");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(228, 228, 228))
            .addGroup(layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(17, 17, 17)
                        .addComponent(rdo_Nam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rdo_Nu))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txt_soDT, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel4))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txt_maSV)
                                        .addComponent(txt_hoTen)
                                        .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_picture, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_new, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(92, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel1)
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txt_maSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_hoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_soDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(label_picture, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdo_Nam)
                    .addComponent(rdo_Nu)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_new)
                            .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_delete)
                            .addComponent(btn_update))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newActionPerformed
        // TODO add your handling code here:
        New();
    }//GEN-LAST:event_btn_newActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        // TODO add your handling code here:
        if(checkForm()){
            if(getStudentByID2(txt_maSV.getText()) != null){
              JOptionPane.showMessageDialog(this, "Trùng mã sinh viên. Xin vui lòng nhập lại !");
              txt_maSV.requestFocus();
             return;
            }else{
                saveSV();
            }
            
        }
        
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // TODO add your handling code here:
       // deleteSV();
       deleteSV2();
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        // TODO add your handling code here:      
            //updateSV();
            if(checkForm()){
                updateSV2();
            }
            
         
    }//GEN-LAST:event_btn_updateActionPerformed

    private void label_pictureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label_pictureMouseClicked
        // TODO add your handling code here:
        try {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter imageFilter;
            imageFilter = new FileNameExtensionFilter("Image", "png", "jpg", "jpeg");
            fileChooser.setFileFilter(imageFilter);
            int res = fileChooser.showOpenDialog(this);
            if(res == JFileChooser.APPROVE_OPTION){
                
                label_picture.setText("");
                File file = fileChooser.getSelectedFile();
                System.out.println(file);
                strHinhAnh = file.getAbsolutePath();
               
                Image image = ImageIO.read(file);
               int width = label_picture.getWidth();
               int height = label_picture.getHeight();
                Icon icon = new ImageIcon(image.getScaledInstance(width, height, 0));
                label_picture.setIcon(icon);
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }//GEN-LAST:event_label_pictureMouseClicked

    private void tbl_thongTinSVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_thongTinSVMouseClicked
        // TODO add your handling code here:
        //showDetail();
        showDetail2();
    }//GEN-LAST:event_tbl_thongTinSVMouseClicked

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
            java.util.logging.Logger.getLogger(QuanLySinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLySinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLySinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLySinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLySinhVien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroup_gioiTinh;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_new;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_picture;
    private javax.swing.JRadioButton rdo_Nam;
    private javax.swing.JRadioButton rdo_Nu;
    private javax.swing.JTable tbl_thongTinSV;
    private javax.swing.JTextArea txt_diaChi;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_hoTen;
    private javax.swing.JTextField txt_maSV;
    private javax.swing.JTextField txt_soDT;
    // End of variables declaration//GEN-END:variables
}
