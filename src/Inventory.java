import java.time.*;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.awt.color.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;


public class Inventory extends JFrame implements ActionListener{
    JButton search, category,option, add, delete,change, many,little ;
    private JTextField SearchText,categorySearch ;
    Vector<String> columnName;
    Vector<Vector<String>> rowData;
    JTable table=null;
    DefaultTableModel model=null;
    JScrollPane tableSP;

    protected Connection getConnection() throws SQLException,ClassNotFoundException{
        //connection객체를 만들어  사용하여 코드의 반복을 줄였
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.err.println("JDBC 드라이버가 정상적으로 연결되었습니다.");

        String url = System.getenv("INVENTORY_DB_URL");
        String user = System.getenv("INVENTORY_DB_USER");
        String password = System.getenv("INVENTORY_DB_PASSWORD");
        if (url == null || user == null || password == null) {
            throw new SQLException("DB 환경변수 INVENTORY_DB_URL, INVENTORY_DB_USER, INVENTORY_DB_PASSWORD를 설정하세요.");
        }
        return DriverManager.getConnection(url, user, password);
    }


    public Inventory(){



        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"); //출력 형식 지정
        LocalDate day = LocalDate.now(); //현재 날짜 출력을 위한 객체 생성




        Container a = getContentPane(); //왼쪽파트
        a.setLayout(null);
        JLabel page = new JLabel("재고관리");
        page.setBounds(0,0,120,30);
        a.add(page);

        SearchText = new JTextField("");
        SearchText.setBounds(0,30,120,30);
        JButton search = new JButton("메뉴 검색");
        search.setBounds(120,30,120,30);
        search.addActionListener(this);
        a.add(SearchText); a.add(search);

        categorySearch = new JTextField("");
        categorySearch.setBounds(0,60,120,30);
        JButton category = new JButton("카테고리 검색");
        category.setBounds(120,60,120,30);
        category.addActionListener(this);
        a.add(categorySearch); a.add(category);

        JButton cate = new JButton("카테고리 관리");
        cate.setBounds(0,90,240,90);
        cate.addActionListener(this);

        JButton many = new JButton("재고 많은 순");
        many.addActionListener(this);
        many.setBounds(0,180,240,90);

        JButton little = new JButton("재고 적은 순");
        little.addActionListener(this);
        little.setBounds(0,270,240,90);
        a.add(cate);a.add(many);a.add(little);

        Container b = getContentPane();   //오른쪽 파트
        JLabel date = new JLabel("날짜 :"+dateFormat.format(day));
        date.setBounds(1080,0,200,30);
        date.setHorizontalAlignment(date.RIGHT);
        date.setForeground(Color.green);
        b.add(date);


        columnName = new Vector<String>();
        columnName.add("재고 코드");columnName.add("재고명");columnName.add("보유재고량");  columnName.add("조정사유"); columnName.add("조정개수"); columnName.add("조정 후 개수");
        rowData = new Vector<Vector<String>>();
        model = new DefaultTableModel(rowData, columnName);
        table = new JTable(model);
        tableSP = new JScrollPane(table);
        tableSP.setBounds(240,90,1080,450);
        b.add(tableSP);
        stockdata();

        Container c = getContentPane(); //하단 파트
        JButton option =new JButton("옵션 관리");
        option.addActionListener(this);
        option.setBounds(801,538,160,160);
        c.add(option);

        JButton add = new JButton("메뉴 추가 및 변경");
        add.addActionListener(this);
        add.setBounds(961,538,160,160);
        c.add(add);

        JButton delete = new JButton("삭제");
        delete.setBounds(1121,538,160,160);
        delete.addActionListener(this);
        c.add(delete);





    }

    void clearTable() {
        for(int i=0;i<rowData.size();)
            rowData.remove(i);
    }
    void stockdata(){//재고테이블 사용
        try {

            Connection con=getConnection();
            System.out.println("DB 연결 완료.");
            Statement dbST = con.createStatement();

            String strSql="SELECT *FROM stock;";
            ResultSet result = dbST.executeQuery(strSql);

            clearTable(); table.updateUI();
            while(result.next()) {
                Vector<String> txt = new Vector<String>();

                txt.add(result.getString("재고코드"));
                txt.add(result.getString("재고명"));
                txt.add(result.getString("보유재고량"));
                txt.add(result.getString("조정사유"));
                txt.add(result.getString("조정개수"));
                txt.add(result.getString("조정후개수"));
                rowData.add(txt); table.updateUI();


            }
            dbST.close();
            con.close();
        }catch(ClassNotFoundException e) {
            System.err.println("드라이버 로드에 실패했습니다.");
        }catch(SQLException e) {
            System.out.println("SQLException:"+e.getMessage());
        }
    }
    public void actionPerformed(ActionEvent ae) {//재고 관리 화면 클릭시 이벤트
        String s = ae.getActionCommand();
        if(s.equals("카테고리 관리")) {
            Category cg = new Category("카테고리 관리");
            cg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            cg.setSize(350,200);
            cg.setLocationRelativeTo(null);
            cg.setVisible(true);
        }else if(s.equals("메뉴 추가 및 변경")) {
            Menu mn = new Menu("메뉴 추가 및 변경");
            mn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mn.setSize(300,350);
            mn.setLocationRelativeTo(null);
            mn.setVisible(true);
        }else if(s.contentEquals("삭제")) {
            Del dt = new Del("메뉴 삭제");
        }else if(s.equals("옵션 관리")) {
            Option opt = new Option("옵션 관리");
            opt.setSize(350,200);
            opt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            opt.setLocationRelativeTo(null);
            opt.setVisible(true);
        }else if(s.equals("메뉴 검색")) {//다른창으로 이동해서  상품에 해당하는 테이블을 검색
            String st = SearchText.getText();
            MenuSearch ms = new MenuSearch("메뉴 검색",st,"메뉴명");
            ms.setSize(800,600);
            ms.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ms.setLocationRelativeTo(null);
            ms.setVisible(true);
        }else if(s.equals("카테고리 검색")) {//카테고리에 해당하는 메뉴 창으로 이동해서 테이블로 보여주고 테이블을 검색
            String ct = categorySearch.getText();
            MenuSearch ms = new MenuSearch("카테고리 검색",ct,"카테고리명");
            ms.setSize(800,600);
            ms.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ms.setLocationRelativeTo(null);
            ms.setVisible(true);
        }else if(s.equals("재고 많은 순")) {//재고 수량이 많은 순으로 테이블을 정렬
            sortstock("DESC");
        }else if(s.equals("재고 적은 순")) {//재고 수량이 적은 순으로 테이블을 정렬
            sortstock("ASC");
        }


    }

    void sortstock(String sort) {//재고 정렬 메소드(재고 테이블 사용)
        try {

            Connection con=getConnection();
            System.out.println("DB 연결 완료.");
            Statement dbST = con.createStatement();

            String strSql="SELECT *FROM stock ORDER BY 조정후개수 "+sort;
            ResultSet result = dbST.executeQuery(strSql);

            clearTable();
            rowData.clear();
            table.updateUI();
            while(result.next()) {
                Vector<String> txt = new Vector<String>();

                txt.add(result.getString("재고코드"));
                txt.add(result.getString("재고명"));
                txt.add(result.getString("보유재고량"));
                txt.add(result.getString("조정사유"));
                txt.add(result.getString("조정개수"));
                txt.add(result.getString("조정후개수"));
                rowData.add(txt); table.updateUI();


            }
            dbST.close();
            con.close();
        }catch(ClassNotFoundException e) {
            System.err.println("드라이버 로드에 실패했습니다.");
        }catch(SQLException e) {
            System.out.println("SQLException:"+e.getMessage());
        }
    }

    class Menu extends JFrame implements ActionListener{ //메뉴 추가,변경 창
        JTextField menu, price,stock;
        JComboBox<String> cb; JButton check, cancel;

        Menu(String title){
            setTitle(title);

            Container ct = getContentPane();
            ct.setLayout(new BorderLayout(0,20));

            JPanel top = new JPanel();
            top.setLayout(new GridLayout(5,1));

            JPanel p1 = new JPanel();
            p1.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel l1 = new JLabel("카테고리 ");
            cb = new JComboBox<>();
            categorydb();
            p1.add(l1); p1.add(cb);

            JPanel p2 = new JPanel();
            p2.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel l2 = new JLabel("메뉴 이름");
            menu =new JTextField(15);
            p2.add(l2); p2.add(menu);

            JPanel p3 = new JPanel();
            p3.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel l3 = new JLabel("가격         ");
            price = new JTextField(15);
            p3.add(l3); p3.add(price);

            JPanel p4 = new JPanel();
            p4.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel l4 = new JLabel("재고유무     ");
            JButton o = new JButton("O");
            o.addActionListener(this);
            JButton x = new JButton("X");
            x.addActionListener(this);
            p4.add(l4); p4.add(o); p4.add(x);

            JPanel p5 = new JPanel();
            p5.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel l5 = new JLabel("재고 수량 ");
            stock = new JTextField(15);
            stock.setEnabled(false);
            p5.add(l5); p5.add(stock);

            top.add(p1);top.add(p2);top.add(p3); top.add(p4); top.add(p5);
            ct.add(top, BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            check = new JButton("추가");
            change = new JButton("변경");
            cancel = new JButton("취소");
            check.addActionListener(this);
            change.addActionListener(this);
            cancel.addActionListener(this);
            bottom.add(check); bottom.add(change); bottom.add(cancel);
            ct.add(bottom,BorderLayout.SOUTH);

        }
        public void actionPerformed(ActionEvent ae) {
            String s =ae.getActionCommand();
            if(s.equals("취소")){
                menu.setText(""); price.setText(""); stock.setText(""); stock.setEnabled(true);
                cb.setSelectedIndex(0);
            }else if(s.equals("O")) {
                stock.setEnabled(true);
            }else if(s.equals("X")) {
                stock.setEnabled(false);
            }
            else if(s.equals("추가")){//추가 클릭시 데이터베이스에 추가
                menuadd();
            }
            else if(s.equals("변경")) {//변경 클릭시 테이터베이스를 변경
                menuchange();
            }
        }

        void menuadd() {//메뉴 테이블 및 카테고리 테이블 사용
            String select = (String) cb.getSelectedItem(); // 콤보박스에서 선택된 카테고리명
            String menuname = menu.getText().trim();
            int menuprice;
            Integer menustock = null; //null이 될 수 있음
            String categoryCode = null;
            int a =0;
            try {
                menuprice = Integer.parseInt(price.getText().trim());


                if (!stock.getText().trim().isEmpty()) {// 재고 입력 확인
                    menustock = Integer.parseInt(stock.getText().trim());
                    if (menustock < 0)
                        throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
                }

                Connection con =getConnection();
                Statement dbst = con.createStatement();

                String mn ="SELECT 메뉴명 FROM menu where 메뉴명= '"+menuname+"';";
                ResultSet re = dbst.executeQuery(mn);
                if(re.next()) {
                    a=1;
                }
                re.close();

                String cate = "SELECT 카테고리코드 FROM category WHERE 카테고리명 = '" + select + "';";
                ResultSet result = dbst.executeQuery(cate);
                if (result.next()) {
                    categoryCode = result.getString("카테고리코드");
                }
                result.close();

                if (categoryCode == null) {
                    throw new SQLException("선택한 카테고리가 존재하지 않습니다.");
                }


                String strSql;
                if (menustock == null && a==0) {
                    strSql = "INSERT INTO menu (카테고리코드, 메뉴명, 카테고리명, 가격) " +"VALUES (" + categoryCode + ", '" + menuname + "', '" + select + "', " + menuprice + ");";
                    dbst.executeUpdate(strSql);
                    dbst.close();
                    JOptionPane.showMessageDialog(this, "메뉴가 추가되었습니다.");
                } else if(menustock!=null&&a==0 ) {
                    strSql = "INSERT INTO menu (카테고리코드, 메뉴명, 카테고리명, 재고량, 가격) " +"VALUES (" + categoryCode + ", '" + menuname + "', '" + select + "', " + menustock + ", " + menuprice + ");";
                    dbst.executeUpdate(strSql);
                    dbst.close();
                    JOptionPane.showMessageDialog(this, "메뉴가 추가되었습니다.");
                }else {
                    JOptionPane.showMessageDialog(this, "이미 존재하는 메뉴입니다.","중복 메뉴 추가",JOptionPane.ERROR_MESSAGE);
                    menu.setText(""); price.setText(""); stock.setText(""); stock.setEnabled(true);
                    cb.setSelectedIndex(0);dbst.close();
                }

                con.close();


            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }catch(IllegalArgumentException e) {
                System.err.println("IllegalArgumentException:"+e.getMessage());
            }catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }

        void menuchange() {//메뉴테이블 및 카테고리 테이블 사용
            String select = (String) cb.getSelectedItem();
            String menuname = menu.getText().trim();
            int menuprice;
            Integer menustock = null;
            String categoryCode=null;

            try {

                if (menuname.isEmpty()) {//메뉴명이 비어 있을 때
                    throw new IllegalArgumentException("메뉴 이름을 입력해야 합니다.");
                }
                menuprice = Integer.parseInt(price.getText().trim());

                if (!stock.getText().trim().isEmpty()) {//재고가 입력되지 않을때
                    menustock = Integer.parseInt(stock.getText().trim());
                    if (menustock < 0)
                        throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
                }



                Connection con = getConnection();
                Statement dbst = con.createStatement();


                String cate = "SELECT 카테고리코드 FROM category WHERE 카테고리명 = '" + select + "';";
                ResultSet result = dbst.executeQuery(cate);
                if (result.next()) {
                    categoryCode = result.getString("카테고리코드");
                }
                result.close();

                if (categoryCode == null) {
                    throw new SQLException("선택한 카테고리가 존재하지 않습니다.");
                }


                String check = "SELECT * FROM menu WHERE 메뉴명 = '" + menuname + "' AND 카테고리코드 = " + categoryCode + ";";
                result = dbst.executeQuery(check);
                if (!result.next()) {
                    throw new SQLException("해당 메뉴가 존재하지 않습니다.");
                }
                result.close();


                String change;
                if (menustock == null) {
                    change = "UPDATE menu SET 가격 = " + menuprice +" WHERE 메뉴명 = '" + menuname + "' AND 카테고리코드 = " + categoryCode + ";";
                } else {
                    change = "UPDATE menu SET 가격 = " + menuprice + ", 재고량 = " + menustock +" WHERE 메뉴명 = '" + menuname + "' AND 카테고리코드 = " + categoryCode + ";";
                }
                dbst.executeUpdate(change);

                dbst.close();
                con.close();

                JOptionPane.showMessageDialog(this, "메뉴 정보가 변경되었습니다.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException:"+e.getMessage());
            } catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "SQLException:"+e.getMessage(),"오류",JOptionPane.ERROR_MESSAGE);
            }
        }
        void categorydb() {//카테고리db 연결
            cb.removeAllItems(); //cb초기화

            try {

                Connection con = getConnection();
                Statement dbst = con.createStatement();
                ResultSet result = dbst.executeQuery("SELECT 카테고리명 FROM category;");
                while (result.next()) {
                    cb.addItem(result.getString("카테고리명"));

                }
                result.close();
                dbst.close();
            } catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }



    }

    class MenuSearch extends JFrame implements ActionListener{//메뉴 검색 및 카테고리명 검색 창(메뉴 테이블 사용)
        Vector<String> columnName;
        Vector<Vector<String>> rowData;
        JTable table=null;
        DefaultTableModel model=null;
        JScrollPane tableSP;
        String st; String type;


        MenuSearch(String title, String st, String type ){
            setTitle(title);
            this.st = st;
            this.type = type;

            Container ct = getContentPane();
            ct.setLayout(new BorderLayout());
            JPanel top=new JPanel();
            JPanel center = new JPanel();
            JPanel bottom =new JPanel();

            ct.add(top,BorderLayout.NORTH);
            ct.add(center,BorderLayout.CENTER);
            ct.add(bottom,BorderLayout.SOUTH);


            columnName = new Vector<String>();
            columnName.add("메뉴 코드");columnName.add("카테고리코드"); columnName.add("메뉴명"); columnName.add("카테고리 명");
            columnName.add("재고 수량"); columnName.add("가격");

            rowData = new Vector<Vector<String>>();
            model = new DefaultTableModel(rowData,columnName);
            table = new JTable(model);
            tableSP = new JScrollPane(table);

            JButton quiryB = new JButton("조회");
            JButton cancelB = new JButton("취소");
            quiryB.addActionListener(this);
            cancelB.addActionListener(this);

            top.setLayout(new FlowLayout());
            top.add(new JLabel("<<<<메뉴>>>>"));

            center.setLayout(new FlowLayout());
            center.add(tableSP);

            bottom.setLayout(new FlowLayout());
            bottom.add(quiryB); bottom.add(cancelB);

            menudata();
        }
        public void actionPerformed(ActionEvent ae) {
            String s = ae.getActionCommand();

            if(s.equals("취소")) {
                clearTable(); table.updateUI();
            }else {
                menudata();

            }


        }
        void clearTable() {
            for(int i=0;i<rowData.size();)
                rowData.remove(i);
        }
        void menudata(){
            try {


                Connection con=getConnection();
                System.out.println("DB 연결 완료.");
                Statement dbST = con.createStatement();

                String strSql="SELECT * FROM menu;";

                if(st !=null && !st.trim().isEmpty()) {
                    if("메뉴명".equals(type)) {
                        strSql = "SELECT *From menu WHERE 메뉴명 LIKE '%"+st.trim()+"%';";
                    }else if("카테고리명".equals(type)) {
                        strSql="SELECT *FROM menu WHERE 카테고리명 LIKE '%"+st.trim()+"%';";
                    }
                }
                ResultSet result = dbST.executeQuery(strSql);

                clearTable(); table.updateUI();
                while(result.next()) {
                    Vector<String> txt = new Vector<String>();

                    txt.add(result.getString("메뉴코드"));
                    txt.add(result.getString("카테고리코드"));
                    txt.add(result.getString("메뉴명"));
                    txt.add(result.getString("카테고리명"));
                    txt.add(result.getString("재고량"));
                    txt.add(result.getString("가격"));
                    rowData.add(txt); table.updateUI();


                }
                dbST.close();
                con.close();
            }catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }

    }
    class Category extends JFrame implements ActionListener{//카테고리 관리 창(카테고리 테이블 사용)
        JComboBox<String> cate; Vector <String> code= new Vector <>();
        Category(String title){
            setTitle(title);

            categorydb();

            Container ct = getContentPane();
            ct.setLayout(new BorderLayout(0,20));

            JPanel t = new JPanel();
            t.setLayout(new GridLayout(1,1));

            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel c = new JLabel("카테고리 명:");
            cate = new JComboBox<>(code);
            JButton create = new JButton("추가");
            create.addActionListener(this);
            JButton delete = new JButton("삭제");
            delete.addActionListener(this);


            p.add(c); p.add(cate); p.add(create);p.add(delete);
            t.add(p);
            ct.add(t);


        }
        public void actionPerformed(ActionEvent ae) {
            String s = ae.getActionCommand();
            if(s.equals("추가")) {
                String category = JOptionPane.showInputDialog("추가할 카테고리 명 입력");
                if(category!=null && !category.trim().isEmpty()) {


                    if(category.length()<=10) {
                        int plus = JOptionPane.showConfirmDialog(this,"입력한 "+category+ " 카테고리를 추가 하겠습니까?","카테고리 추가",JOptionPane.YES_NO_OPTION);
                        if(plus == JOptionPane.YES_OPTION) {//데이터 베이스에서 새로운 카테고리 생성
                            addcate(category);
                        }
                    }else {
                        JOptionPane.showMessageDialog(this,"카테고리명은 10자까지 가능합니다.","입력 실패",JOptionPane.ERROR_MESSAGE);
                    }
                }


            }else if(s.equals("삭제")) {
                String select = (String)cate.getSelectedItem();

                if(select != null) {
                    int result = JOptionPane.showConfirmDialog(this,"선택한 "+ select+ " 카테고리와 카테고리에 해당하는 모든 메뉴를 삭제 하시겠습니까? ","카테고리 삭제",JOptionPane.YES_NO_OPTION);

                    if(result == JOptionPane.YES_OPTION) {//데이터 베이스에서 카테고리와 메뉴에 대한 테이블을 삭제
                        deletecate(select);
                    }


                }
            }}
        void categorydb() {//카테고리db 연결
            code.clear();
            try {
                Connection con = getConnection();
                Statement dbst = con.createStatement();
                ResultSet result = dbst.executeQuery("SELECT 카테고리명 FROM category ORDER BY 카테고리코드");
                while (result.next()) {
                    code.add(result.getString("카테고리명"));
                }
                result.close();
                dbst.close();
            } catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }
        void addcate(String category) {//db에 카테고리 추가
            try {
                Connection con = getConnection();
                Statement dbst = con.createStatement();
                dbst.executeUpdate("INSERT INTO category(카테고리명) VALUES ('"+category+"');");
                dbst.close();
                JOptionPane.showMessageDialog(this, category +" 카테고리가 추가되었습니다.","카테고리 추가창",JOptionPane.INFORMATION_MESSAGE);
                categorydb();
            }catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                JOptionPane.showMessageDialog(this,"SQLException:"+e.getMessage(),"오류",JOptionPane.ERROR_MESSAGE);
            }
        }
        void deletecate(String category) {//db에서 카테고리삭제
            try {
                Connection con = getConnection();
                Statement dbst = con.createStatement();

                String menudel = "DELETE FROM menu WHERE 카테고리코드 ="+"(SELECT 카테고리코드 FROM category WHERE 카테고리명= '"+category+"');";
                dbst.executeUpdate(menudel);

                String catedel = "DELETE FROM category WHERE 카테고리명 = '"+category+"';";
                dbst.executeUpdate(catedel);


                JOptionPane.showMessageDialog(this, category+ " 카테고리와 메뉴가 삭제되었습니다. ", "카테고리 삭제",JOptionPane.INFORMATION_MESSAGE);
                dbst.close();
                con.close();
                categorydb();
            }catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }
    }
    class Del extends JDialog { //메뉴 삭제 창(메뉴테이블사용)
        Del(String title){
            String name = JOptionPane.showInputDialog("삭제하고 싶은 메뉴명을 입력하세요");//입력하지 않거나 취소를 누르면 null값이 저장됨.
            if(name != null && !name.trim().isEmpty()) {

                try{
                    Connection con = getConnection();
                    Statement stmt = con.createStatement();

                    String checkStock = "SELECT 재고량 FROM menu WHERE 메뉴명 = '"+name+"';"; //재고량을 확인
                    ResultSet result = stmt.executeQuery(checkStock);



                    if (result.next()) {
                        int stock = result.getInt("재고량");

                        if (stock > 0) { //재고가 있는 경우
                            int result1 = JOptionPane.showConfirmDialog(this,name+"에 메뉴가 남아있습니다. 모두 삭제 하시겠습니까? ","재고 삭제",JOptionPane.YES_NO_OPTION);

                            if(result1 == JOptionPane.YES_OPTION) {//데이터 베이스에서 재고명에 대한 테이블을 삭제
                                String delDB = "DELETE FROM menu WHERE 메뉴명= '"+name+"';";
                                int del = stmt.executeUpdate(delDB);

                                if (del > 0) {
                                    JOptionPane.showMessageDialog(this,name + " 메뉴가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(this,"삭제 중 문제가 발생했습니다.","삭제 실패", JOptionPane.ERROR_MESSAGE);
                                }

                            }

                        }else { // 재고량이 0인 경우
                            int result1 = JOptionPane.showConfirmDialog(this, name + " 메뉴를 삭제하시겠습니까?", "메뉴 삭제", JOptionPane.YES_NO_OPTION);

                            if (result1 == JOptionPane.YES_OPTION) {
                                String delDB = "DELETE FROM menu WHERE 메뉴명 = '"+name+"';";
                                int del = stmt.executeUpdate(delDB);

                                if (del > 0) {
                                    JOptionPane.showMessageDialog(this, name + " 메뉴가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
                                }

                            } else {
                                JOptionPane.showMessageDialog(this,"메뉴 삭제가 취소되었습니다.", "삭제 취소 안내", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } else { // 메뉴가 존재하지 않을때
                        JOptionPane.showMessageDialog(this, "입력한 메뉴명이 존재하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    }

                    result.close();
                    stmt.close();
                    con.close();
                }  catch(ClassNotFoundException e) {
                    System.err.println("드라이버 로드에 실패했습니다.");
                }catch(SQLException e) {
                    System.out.println("SQLException:"+e.getMessage());
                }
            }
        }


    }
    class Option extends JFrame implements ActionListener{//옵션관리창(옵션테이블)
        Vector<String> code =new Vector<>();JComboBox<String> option;
        Option(String title){
            setTitle(title);
            optionsdb();

            Container ct = getContentPane();
            ct.setLayout(new BorderLayout(0,20));

            JPanel t = new JPanel();
            t.setLayout(new GridLayout(1,1));

            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            JLabel c = new JLabel("옵션 명:");
            option = new JComboBox<>(code);
            JButton create = new JButton("추가");
            create.addActionListener(this);
            JButton delete = new JButton("삭제");
            delete.addActionListener(this);


            p.add(c); p.add(option); p.add(create);p.add(delete);
            t.add(p);
            ct.add(t);
        }
        public void actionPerformed(ActionEvent ae) {
            String s = ae.getActionCommand();
            if(s.equals("추가")) {
                String opt = JOptionPane.showInputDialog("추가할 옵션명을 입력해주세요.");
                if(opt != null && !opt.trim().isEmpty()) {
                    int plus = JOptionPane.showConfirmDialog(this, "입력한 " + opt + " 옵션을 추가 하겠습니까?", "옵션 추가", JOptionPane.YES_NO_OPTION);
                    if(plus == JOptionPane.YES_OPTION) {
                        // 옵션 가격 입력받기
                        String priceStr = JOptionPane.showInputDialog("옵션 가격을 입력해주세요.");
                        if(priceStr != null && !priceStr.trim().isEmpty()) {
                            try {
                                int price = Integer.parseInt(priceStr.trim());
                                // 옵션 추가 메서드에 가격도 파라미터로 넘기도록 수정
                                addoptions(opt, price);
                            } catch(NumberFormatException e) {
                                JOptionPane.showMessageDialog(this, "가격은 숫자로 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }else if(s.equals("삭제")) {

                String select = (String)option.getSelectedItem();

                if(select != null) {
                    int result = JOptionPane.showConfirmDialog(this,"선택한 옵션을 삭제 하시겠습니까? ","옵션 삭제",JOptionPane.YES_NO_OPTION);
                    if(result == JOptionPane.YES_OPTION) {
                        deleteoptions(select);

                    }else {
                        JOptionPane.showMessageDialog(this, "삭제할 옵션을 선택해주세요.", "옵션 삭제 실패", JOptionPane.WARNING_MESSAGE);
                    }


                }
            }

        }
        void optionsdb() {//옵션db 연결
            code.clear();
            try {
                Connection con = getConnection();
                Statement dbst = con.createStatement();
                ResultSet result = dbst.executeQuery("SELECT 옵션명 FROM options ORDER BY 옵션코드");
                while (result.next()) {
                    code.add(result.getString("옵션명"));
                }
                result.close();
                dbst.close();
            } catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }
        void addoptions(String opt, int price) {
            try {
                Connection con = getConnection();
                Statement dbst = con.createStatement();
                // options 테이블에 옵션명과 옵션가격을 저장하는 칼럼이 있다고 가정 (옵션가격 컬럼명: 옵션가격)
                String sql = "INSERT INTO options(옵션명, 옵션가격) VALUES ('" + opt + "', " + price + ");";
                dbst.executeUpdate(sql);
                dbst.close();
                JOptionPane.showMessageDialog(this, opt +" 옵션(" + price + "원)이 추가되었습니다.","옵션 추가창",JOptionPane.INFORMATION_MESSAGE);
                optionsdb(); // 옵션 목록 갱신 메서드 호출
            } catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            } catch(SQLException e) {
                System.out.println("SQLException:" + e.getMessage());
            }
        }
        void deleteoptions(String options) {//db에서 옵션삭제
            try {
                Connection con = getConnection();
                Statement dbst = con.createStatement();
                dbst.executeUpdate("DELETE FROM options WHERE 옵션명 = '"+ options+ "';");
                dbst.close();
                JOptionPane.showMessageDialog(this, options+ " 옵션이 삭제되었습니다. ", "옵션 삭제",JOptionPane.INFORMATION_MESSAGE);
                optionsdb();
            }catch(ClassNotFoundException e) {
                System.err.println("드라이버 로드에 실패했습니다.");
            }catch(SQLException e) {
                System.out.println("SQLException:"+e.getMessage());
            }
        }
    }












    public static void main(String args[]) { //main
        Inventory win = new Inventory();
        win.setSize(1280,720);
        win.setResizable(false);
        win.setVisible(true);
    }
}
