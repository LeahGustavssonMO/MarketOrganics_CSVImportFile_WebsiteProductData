import java.sql.*;

class connectingToDB {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //Loading the required JDBC Driver class
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        //Creating a connection to the database
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://20.5.61.14:1433;DatabaseName=MarketOrganics_LIVE;encrypt=true;trustServerCertificate=true;","sa","Exonet001");

        //Executing SQL query and fetching the result
        Statement st = conn.createStatement();
        String sqlStr = "DECLARE @theDate As date\n" +
                "SET @theDate = getdate()\n" +
                "\n" +
                "SELECT CONVERT(VarChar, STOCK_ITEMS.BARCODE1) As 'Barcode',\n" +
                "       STOCK_ITEMS.STOCKCODE As 'StockCode', \n" +
                "       STOCK_ITEMS.DESCRIPTION As 'Name', \n" +
                "       STOCK_ITEMS.SELLPRICE1 As 'Price', \n" +
                "       CASE WHEN DR_PRICE_POLICY.START_DATE <= @theDate AND DR_PRICE_POLICY.END_DATE >= @theDate THEN DR_PRICES.PRICE ELSE '' END As 'Special', \n" +
                "       STOCK_ITEMS.EBS_POS_ISWEIGHED As 'Scale',\n" +
                "       CASE WHEN STOCK_ITEMS.SALESTAXRATE = '10' THEN STOCK_ITEMS.SALESTAXRATE ELSE '0' END As 'Tax',  \n" +
                "       STOCK_ITEMS.STOCKGROUP As 'D 1 Code', \n" +
                "       STOCK_GROUPS.GROUPNAME As 'D 1 Name',\n" +
                "       STOCK_ITEMS.STOCKGROUP2 As 'D 2 Code',\n" +
                "       STOCK_GROUP2S.GROUPNAME As 'D 2 Name', \n" +
                "       CASE WHEN STOCK_ITEMS.X_BRAND LIKE '%FRESH PRODUCE%' THEN '' ELSE STOCK_ITEMS.X_BRAND END As 'Brand',\n" +
                "       STOCK_ITEMS.X_SUPPLIERCODE As 'Order Code', \n" +
                "       (SELECT TOP 1 CAST(TRANSDATE As Date) FROM DR_INVLINES WHERE DR_INVLINES.STOCKCODE = STOCK_ITEMS.STOCKCODE ORDER BY TRANSDATE DESC) As 'Last Sold', \n" +
                "       --CAST(STOCK_WEB.SALES_HTML As VarChar(1000)) As 'Description',\n" +
                "       STOCK_ITEMS.PACK As 'Size Unit', \n" +
                "       CASE WHEN STOCK_ITEMS.PACK = 'CAP' THEN '80' ELSE CONVERT(INT, STOCK_ITEMS.WEIGHT) END AS 'Retail Size',\n" +
                "       CASE WHEN STOCK_ITEMS.PACK = 'CAP' THEN 'G' ELSE STOCK_ITEMS.PACK END AS 'Retail Uom',\n" +
                "       STOCK_ITEMS.ISACTIVE As 'Available', \n" +
                "       CASE WHEN STOCK_ITEMS.SALESTAXRATE = '10' THEN 'I' ELSE 'E' END As 'Tax Type',\n" +
                "       '' As 'Price2', \n" +
                "       '' As 'Price3', \n" +
                "       '' As 'Price4', \n" +
                "       '' As 'Price5',\n" +
                "       CASE WHEN STOCK_ITEMS.WEIGHT <= 11 THEN '' ELSE STOCK_ITEMS.WEIGHT / 1000 END AS 'Net Weight',\n" +
                "       COALESCE(CASE WHEN STOCK_ITEMS.STOCKGROUP = 2 AND (SELECT TOP 1 CAST(TRANSDATE As Date) FROM DR_INVLINES WHERE DR_INVLINES.STOCKCODE = STOCK_ITEMS.STOCKCODE ORDER BY TRANSDATE DESC) > '2022-01-01' THEN 2 END,\n" +
                "\t   CASE WHEN STOCK_ITEMS.TOTALSTOCK <= 2 THEN 0 ELSE STOCK_ITEMS.TOTALSTOCK END) As 'Stock On Hand' \n" +
                "FROM STOCK_ITEMS STOCK_ITEMS\n" +
                "      LEFT JOIN STOCK_WEB STOCK_WEB ON \n" +
                "     (STOCK_WEB.STOCKCODE = STOCK_ITEMS.STOCKCODE)\n" +
                "      INNER JOIN STOCK_GROUPS STOCK_GROUPS ON \n" +
                "     (STOCK_GROUPS.GROUPNO = STOCK_ITEMS.STOCKGROUP)\n" +
                "      INNER JOIN STOCK_GROUP2S STOCK_GROUP2S ON \n" +
                "     (STOCK_GROUP2S.GROUPNO = STOCK_ITEMS.STOCKGROUP2)\n" +
                "     LEFT JOIN DR_PRICES DR_PRICES ON\n" +
                "     (DR_PRICES.STOCKCODE = STOCK_ITEMS.STOCKCODE)\n" +
                "     LEFT JOIN DR_PRICE_POLICY DR_PRICE_POLICY ON\n" +
                "     (DR_PRICE_POLICY.POLICY_HDR = DR_PRICES.POLICY_HDR)\n" +
                "WHERE STOCK_ITEMS.ISACTIVE = 'Y' AND STOCK_ITEMS.BARCODE1 <> '' AND STOCK_ITEMS.STOCKGROUP <> 4";
        ResultSet rs = st.executeQuery(sqlStr);
        while (rs.next()) {
            System.out.println(rs.getString("Barcode") +
                   "," + rs.getString("StockCode"));
        }
    }
}