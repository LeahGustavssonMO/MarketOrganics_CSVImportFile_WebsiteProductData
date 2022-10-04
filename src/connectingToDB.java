import java.sql.*;

class connectingToDB {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //Loading the required JDBC Driver class
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        //Creating a connection to the database
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://20.5.61.14:1433;DatabaseName=MarketOrganics_LIVE;encrypt=true;trustServerCertificate=true;","sa","Exonet001");

        //Executing SQL query and fetching the result
        Statement st = conn.createStatement();
        String sqlStr = "select * from [MarketOrganics_LIVE].[dbo].[TASKS]";
        ResultSet rs = st.executeQuery(sqlStr);
        while (rs.next()) {
            System.out.println(rs.getString("SEQNO"));
        }
    }
}