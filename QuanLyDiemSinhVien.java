/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import java.sql.*;
import javax.swing.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MSI
 */
public class QuanLyDiemSinhVien extends javax.swing.JFrame {
private Connection con = null;
private Statement st = null;
private ResultSet rs = null;
private PreparedStatement pst = null;

private ResultSet rst;
private PreparedStatement preDetails;

private String url = "jdbc:sqlserver://localhost:1433;databaseName = FPL_ĐàoTạo;username=sa;password=123;encrypt=true; trustServerCertificate=true";
    ArrayList<DiemSinhVien> dsDiem = new ArrayList<>();
    DefaultTableModel tbModel;
    int current = 0;
    /**
     * Creates new form QuanLyDiemSinhVien
     */
    public QuanLyDiemSinhVien() {
        initComponents();
        this.setLocationRelativeTo(null);
        txt_hoTenSV.disable();
        tbModel = (DefaultTableModel) tbl_ThongTinDiemSV.getModel();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Select Hoten , st.MASV, Tienganh, Tinhoc, GDTC, round(((Tienganh + Tinhoc + GDTC)/3),1) as 'DiemTB' from GRADE gr"
                   + " inner join STUDENTS st "
                   + " on gr.MASV = st.MASV";
                 
            preDetails = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rst = preDetails.executeQuery();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadData();
    }
    
    // load data
    
    public void loadData(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Select TOP 3 Hoten, gr.MASV, Tienganh, Tinhoc, GDTC, round(((Tienganh + Tinhoc + GDTC)/3),1) as 'DiemTB'  from GRADE gr "
                    + "inner join STUDENTS st"
                    + " on gr.MASV = st.MASV"
                    + " order by DiemTB desc";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            dsDiem.clear();
            while(rs.next()){
                DiemSinhVien grade = new DiemSinhVien();
                grade.setMaSV(rs.getString("MASV"));
                grade.setHoTen(rs.getString("Hoten"));         
                grade.setDiemTiengAnh(rs.getDouble("Tienganh"));
                grade.setDiemTinHoc(rs.getDouble("Tinhoc"));
                grade.setDiemGDTC(rs.getDouble("GDTC"));
                grade.setDiemTB(rs.getDouble("DiemTB"));
                dsDiem.add(grade);
            }
            
            if(dsDiem.size() > 0){
                tbModel.setRowCount(0);
                for (DiemSinhVien gr : dsDiem) {
                    Object[] dataRow = {gr.maSV, gr.hoTen, gr.diemTiengAnh, gr.diemTinHoc, gr.diemGDTC, gr.diemTB};
                    tbModel.addRow(dataRow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // My Code
    
    void clearForm(){
        txt_findMaSV.setText("");
        txt_hoTenSV.setText("");
        txt_maSV.setText("");
        txt_diemTA.setText("");
        txt_diemTinHoc.setText("");
        txt_diemGDTC.setText("");
        label_diemTB.setText("0.0");
    }
    
    void updateDiemTB(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Select round(((Tienganh + Tinhoc + GDTC)/3),1) as 'DiemTB' from GRADE where MASV = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, txt_maSV.getText());
            rs = pst.executeQuery();
            while(rs.next()){
                label_diemTB.setText(rs.getDouble("DiemTB")+"");
            }
            con.close();
            pst.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    boolean checkValidate(){
        if(txt_maSV.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sinh viên!");
            txt_maSV.requestFocus();
            return false;
        }
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Select MASV from STUDENTS";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int flag = 0;
            while(rs.next()){
                if(rs.getString("MASV").equals(txt_maSV.getText())){
                    flag = 1;
                }    
            }
            if(flag == 0){
                JOptionPane.showMessageDialog(this, "Mã sinh viên không tồn tại !");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        if(txt_diemTA.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm tiếng anh!");
            txt_diemTA.requestFocus();
            return false;
        }
        
        try {
            double diemTA = Double.parseDouble(txt_diemTA.getText());
            if(diemTA < 0){
                JOptionPane.showMessageDialog(this, "Điểm tiếng anh phải là số dương!");
                txt_diemTA.requestFocus();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm tiếng anh không hợp lệ!");
            txt_diemTA.requestFocus();
            return false;
        }
        
        if(txt_diemTinHoc.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm tin học!");
            txt_diemTinHoc.requestFocus();
            return false;
        }
        
        try {
            double diemTH = Double.parseDouble(txt_diemTinHoc.getText());
            if(diemTH < 0){
                JOptionPane.showMessageDialog(this, "Điểm tin học phải là số dương!");
                txt_diemTinHoc.requestFocus();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm tin học không hợp lệ!");
            txt_diemTinHoc.requestFocus();
            return false;
        }
        
        
        if(txt_diemGDTC.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập điểm GDTC!");
            txt_diemGDTC.requestFocus();
            return false;
        }
        
        try {
            double diemGDTC = Double.parseDouble(txt_diemGDTC.getText());
            if(diemGDTC < 0){
                JOptionPane.showMessageDialog(this, "Điểm GDTC phải là số dương!");
                txt_diemGDTC.requestFocus();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm GDTC không hợp lệ!");
            txt_diemGDTC.requestFocus();
            return false;
        }
        
        return true;
    }
    
    void saveDiemSV(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Insert into GRADE(MASV, Tienganh, Tinhoc, GDTC) values (?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, txt_maSV.getText());
            pst.setDouble(2, Double.parseDouble(txt_diemTA.getText()));
            pst.setDouble(3, Double.parseDouble(txt_diemTinHoc.getText()));
            pst.setDouble(4, Double.parseDouble(txt_diemGDTC.getText()));
            int kq = pst.executeUpdate();
            if(kq == 1){
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                updateDiemTB();
                loadData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
    
    void search(){
        try {
           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
           con = DriverManager.getConnection(url);
           String sql = "Select Hoten , st.MASV, Tienganh, Tinhoc, GDTC, round(((Tienganh + Tinhoc + GDTC)/3),1) as 'DiemTB' from GRADE gr"
                   + " inner join STUDENTS st "
                   + " on gr.MASV = st.MASV"
                   + " where st.MASV = ?";
           pst = con.prepareStatement(sql);
           if(!txt_findMaSV.getText().equals("")){
               pst.setString(1, txt_findMaSV.getText());
                rs = pst.executeQuery();
                if(rs.isBeforeFirst() == false){
                    JOptionPane.showMessageDialog(this, "Không tồn tại điểm sinh viên có mã: "+ txt_findMaSV.getText());
                    clearForm();
                }else{
                    JOptionPane.showMessageDialog(this, "Tìm thấy thành công!");
                    while(rs.next()){
                txt_hoTenSV.setText(rs.getString("Hoten"));
                txt_maSV.setText(rs.getString("MASV"));
                txt_diemTA.setText(rs.getString("Tienganh"));
                txt_diemTinHoc.setText(rs.getString("Tinhoc"));
                txt_diemGDTC.setText(rs.getString("GDTC"));
                label_diemTB.setText(rs.getString("DiemTB"));
                    }
                      con.close();
                      pst.close();
                      rs.close();
                }
                
           }else{
               JOptionPane.showMessageDialog(this, "Bạn chưa nhập mã sinh viên để tìm kiếm");
           }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void showDetail(){
        int index = tbl_ThongTinDiemSV.getSelectedRow();
        txt_maSV.setText(tbModel.getValueAt(index, 0).toString());
        txt_hoTenSV.setText(tbModel.getValueAt(index, 1).toString());
        txt_diemTA.setText(tbModel.getValueAt(index, 2).toString());
        txt_diemTinHoc.setText(tbModel.getValueAt(index, 3).toString());
        txt_diemGDTC.setText(tbModel.getValueAt(index, 4).toString());
        label_diemTB.setText(tbModel.getValueAt(index, 5).toString());
        
    }
    
    void delete(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Delete from GRADE where MASV = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, txt_maSV.getText());
            int kq = pst.executeUpdate();
            if( kq == 1){
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadData();
            }else{
                JOptionPane.showMessageDialog(this, "Điểm sinh viên không tồn tại để xóa!");
            }
            
            con.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void update(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
            con = DriverManager.getConnection(url);
            String sql = "Update GRADE SET Tienganh = ?, Tinhoc = ?, GDTC = ? where MASV = ?";
            pst = con.prepareStatement(sql);
            pst.setDouble(1, Double.parseDouble(txt_diemTA.getText()));
            pst.setDouble(2,Double.parseDouble(txt_diemTinHoc.getText()));
            pst.setDouble(3,Double.parseDouble(txt_diemGDTC.getText()));
            pst.setString(4, txt_maSV.getText());
            int kq = pst.executeUpdate();
            if(kq == 1){
                JOptionPane.showMessageDialog(this, "Cập nhật thành công !");
                updateDiemTB();
                loadData();
            }else{
                JOptionPane.showMessageDialog(this, "Điểm sinh viên không tồn tại để cập nhật!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // End Code
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_findMaSV = new javax.swing.JTextField();
        btn_searchDiem = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_maSV = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_diemTA = new javax.swing.JTextField();
        txt_diemTinHoc = new javax.swing.JTextField();
        txt_diemGDTC = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        label_diemTB = new javax.swing.JLabel();
        txt_hoTenSV = new javax.swing.JTextField();
        btn_newDiem = new javax.swing.JButton();
        btn_saveDiem = new javax.swing.JButton();
        btn_deleteDiem = new javax.swing.JButton();
        btn_updateDiem = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btn_First = new javax.swing.JButton();
        btn_Next = new javax.swing.JButton();
        btn_Previous = new javax.swing.JButton();
        btn_Last = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_ThongTinDiemSV = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 255));
        jLabel1.setText("Quản Lý Điểm Sinh Viên");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 153), new java.awt.Color(153, 153, 153)), "Tìm kiếm", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel2.setText("Mã SV:");

        btn_searchDiem.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\search.png")); // NOI18N
        btn_searchDiem.setText("Search");
        btn_searchDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchDiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_findMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btn_searchDiem)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_findMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_searchDiem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Họ tên SV:");

        jLabel4.setText("Mã SV:");

        jLabel5.setText("Tiếng anh:");

        jLabel6.setText("Tin học:");

        jLabel7.setText("Giáo dục TC:");

        jLabel8.setText("Điểm TB:");

        label_diemTB.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        label_diemTB.setForeground(new java.awt.Color(0, 51, 255));
        label_diemTB.setText("0.0");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_diemGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_diemTA, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_diemTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(label_diemTB)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txt_hoTenSV, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_maSV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_hoTenSV, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_maSV, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_diemTA, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_diemTinHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(label_diemTB)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_diemGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_newDiem.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\add.png")); // NOI18N
        btn_newDiem.setText("New");
        btn_newDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newDiemActionPerformed(evt);
            }
        });

        btn_saveDiem.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\icon\\save1.png")); // NOI18N
        btn_saveDiem.setText("Save");
        btn_saveDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveDiemActionPerformed(evt);
            }
        });

        btn_deleteDiem.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\delete.png")); // NOI18N
        btn_deleteDiem.setText("Delete");
        btn_deleteDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteDiemActionPerformed(evt);
            }
        });

        btn_updateDiem.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\icon\\edit.png")); // NOI18N
        btn_updateDiem.setText("Update");
        btn_updateDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateDiemActionPerformed(evt);
            }
        });

        btn_First.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\skip_backward.png")); // NOI18N
        btn_First.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FirstActionPerformed(evt);
            }
        });

        btn_Next.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\fast_forward.png")); // NOI18N
        btn_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NextActionPerformed(evt);
            }
        });

        btn_Previous.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\rewind.png")); // NOI18N
        btn_Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PreviousActionPerformed(evt);
            }
        });

        btn_Last.setIcon(new javax.swing.ImageIcon("F:\\Java 3\\Icons\\Icons\\skip_forward.png")); // NOI18N
        btn_Last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_First)
                .addGap(18, 18, 18)
                .addComponent(btn_Next)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Previous)
                .addGap(18, 18, 18)
                .addComponent(btn_Last)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_First)
                    .addComponent(btn_Next)
                    .addComponent(btn_Previous)
                    .addComponent(btn_Last))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabel10.setForeground(new java.awt.Color(51, 0, 255));
        jLabel10.setText("3 Sinh viên có điểm cao nhất");

        tbl_ThongTinDiemSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "MSSV", "Họ tên", "Tiếng anh", "Tin học", "GDTC", "Điểm TB"
            }
        ));
        tbl_ThongTinDiemSV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ThongTinDiemSVMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_ThongTinDiemSV);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btn_updateDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btn_deleteDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btn_saveDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btn_newDiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)))
                        .addGap(0, 59, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_newDiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_saveDiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_deleteDiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_updateDiem))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_searchDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchDiemActionPerformed
        // TODO add your handling code here:
        search();
    }//GEN-LAST:event_btn_searchDiemActionPerformed

    private void btn_newDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newDiemActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btn_newDiemActionPerformed

    private void btn_saveDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveDiemActionPerformed
        // TODO add your handling code here:
        if(checkValidate()){
            saveDiemSV();
        }
        
    }//GEN-LAST:event_btn_saveDiemActionPerformed

    private void btn_deleteDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteDiemActionPerformed
        // TODO add your handling code here:
        delete();
        clearForm();
    }//GEN-LAST:event_btn_deleteDiemActionPerformed

    private void btn_updateDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateDiemActionPerformed
        // TODO add your handling code here:
        if(checkValidate()){
            update();
        }
        
    }//GEN-LAST:event_btn_updateDiemActionPerformed

    private void tbl_ThongTinDiemSVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ThongTinDiemSVMouseClicked
        // TODO add your handling code here:
        showDetail();
    }//GEN-LAST:event_tbl_ThongTinDiemSVMouseClicked

    private void btn_FirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FirstActionPerformed
        // TODO add your handling code here:
        try {
             rst.first();
             txt_hoTenSV.setText(rst.getString("HoTen"));
             txt_maSV.setText(rst.getString("MASV"));
             txt_diemTA.setText(String.valueOf(rst.getDouble("Tienganh")));
             txt_diemTinHoc.setText(String.valueOf(rst.getDouble("Tinhoc")));
             txt_diemGDTC.setText(String.valueOf(rst.getDouble("GDTC")));
             label_diemTB.setText(String.valueOf(rst.getDouble("DiemTB")));
             btn_Next.setEnabled(true);
             btn_Previous.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        
    }//GEN-LAST:event_btn_FirstActionPerformed

    private void btn_LastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LastActionPerformed
        // TODO add your handling code here:
        try {
             rst.last();
             txt_hoTenSV.setText(rst.getString("HoTen"));
             txt_maSV.setText(rst.getString("MASV"));
             txt_diemTA.setText(String.valueOf(rst.getDouble("Tienganh")));
             txt_diemTinHoc.setText(String.valueOf(rst.getDouble("Tinhoc")));
             txt_diemGDTC.setText(String.valueOf(rst.getDouble("GDTC")));
             label_diemTB.setText(String.valueOf(rst.getDouble("DiemTB")));
             btn_Next.setEnabled(true);
             btn_Previous.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_btn_LastActionPerformed

    private void btn_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NextActionPerformed
        // TODO add your handling code here:
        try {
            rst.next();
            if(rst.isAfterLast()){
                JOptionPane.showMessageDialog(this, "Đang ở cuối danh sách !");
                btn_Next.setEnabled(false);
                btn_Previous.setEnabled(true);
            }else{
                 btn_Next.setEnabled(true);
                 btn_Previous.setEnabled(true);
                txt_hoTenSV.setText(rst.getString("Hoten"));
                txt_maSV.setText(rst.getString("MASV"));
                txt_diemTA.setText(String.valueOf(rst.getString("Tienganh")));
                txt_diemTinHoc.setText(rst.getDouble("Tinhoc")+"");
                txt_diemGDTC.setText(rst.getDouble("GDTC")+"");
                label_diemTB.setText(rst.getDouble("DiemTB")+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_NextActionPerformed

    private void btn_PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PreviousActionPerformed
        // TODO add your handling code here:
        try {
            rst.previous();
            if(rst.isBeforeFirst()){
                JOptionPane.showMessageDialog(this, "Đang ở đầu danh sách !");
                
                btn_Previous.setEnabled(false);
                btn_Next.setEnabled(true);
            }else{
                 btn_Next.setEnabled(true);
                btn_Previous.setEnabled(true);
                txt_hoTenSV.setText(rst.getString("Hoten"));
                txt_maSV.setText(rst.getString("MASV"));
                txt_diemTA.setText(String.valueOf(rst.getString("Tienganh")));
                txt_diemTinHoc.setText(rst.getDouble("Tinhoc")+"");
                txt_diemGDTC.setText(rst.getDouble("GDTC")+"");
                label_diemTB.setText(rst.getDouble("DiemTB")+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_PreviousActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyDiemSinhVien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_First;
    private javax.swing.JButton btn_Last;
    private javax.swing.JButton btn_Next;
    private javax.swing.JButton btn_Previous;
    private javax.swing.JButton btn_deleteDiem;
    private javax.swing.JButton btn_newDiem;
    private javax.swing.JButton btn_saveDiem;
    private javax.swing.JButton btn_searchDiem;
    private javax.swing.JButton btn_updateDiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_diemTB;
    private javax.swing.JTable tbl_ThongTinDiemSV;
    private javax.swing.JTextField txt_diemGDTC;
    private javax.swing.JTextField txt_diemTA;
    private javax.swing.JTextField txt_diemTinHoc;
    private javax.swing.JTextField txt_findMaSV;
    private javax.swing.JTextField txt_hoTenSV;
    private javax.swing.JTextField txt_maSV;
    // End of variables declaration//GEN-END:variables
}
