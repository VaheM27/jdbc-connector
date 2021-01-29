package org.bdg;

import java.util.Scanner;
import java.sql.*;


public class Application {

    static final String postgresDriver = "org.postgresql.Driver";
    static final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    static final String usernamepostgres = "postgres";
    static final String passwordpostgres = "1234";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        createTables();
        while (true){
            System.out.println("Input 1 to create accounut");
            System.out.println("Input 2 to create role");
            System.out.println("Input 3 to connect accounut and role");
            System.out.println("Input 0 to exit");
            int Number = scanner.nextInt();
            switch (Number){
                case 0:
                    System.exit(1);
                    break;
                case 1:
                    System.out.println("Insert username");
                    String username = scanner.next();
                    System.out.println("Insert password");
                    String password = scanner.next();
                    insertAccount(username,password);
                    break;
                case 2:
                    System.out.println("Insert role");
                    String rolename = scanner.next();
                     insertRole(rolename);
                    break;

                case 3:
                    System.out.println("Insert username: ");
                    String userName = scanner.next();
                    System.out.println("Insert roleName: ");
                    String roleName = scanner.next();
                    connectAccountToRole(roleName, userName);
                    break;
            }

        }

    }


    private static void createTables() throws ClassNotFoundException, SQLException {
        Class.forName(postgresDriver);
        Connection conn = DriverManager.getConnection(dbUrl, usernamepostgres, passwordpostgres);
        Statement stmt = conn.createStatement();
        String createTableAccounts = " CREATE TABLE IF NOT EXISTS accounts(account_id SERIAL NOT NULL PRIMARY KEY," +
                "username varchar(265) NOT NULL UNIQUE ," +
                "password varchar(265) NOT NULL )";
        String createTableRoles = "CREATE TABLE IF NOT EXISTS role(role_id SERIAL NOT NULL PRIMARY KEY," +
                "role_name varchar (265) UNIQUE NOT NULL)";
        String createTableAccountRoles = "CREATE TABLE IF NOT EXISTS account_roles (" +
                "  account_id INT NOT NULL," +
                "  role_id INT NOT NULL," +
                "  PRIMARY KEY (account_id, role_id)," +
                "  FOREIGN KEY (role_id)" +
                "      REFERENCES role (role_id)," +
                "  FOREIGN KEY (account_id)" +
                "      REFERENCES accounts (account_id)" +
                ")";
        stmt.execute(createTableAccounts);
        stmt.execute(createTableRoles);
        stmt.execute(createTableAccountRoles);

    }

    public static void insertAccount(String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(postgresDriver);
        Connection connection = DriverManager.getConnection(dbUrl, usernamepostgres, passwordpostgres);
        PreparedStatement statement = connection.prepareStatement("insert into accounts (username , password)values (? , ?)");
        statement.setString(1, username);
        statement.setString(2, password);
        statement.executeUpdate();

    }



    public static int getAccountid(String username) throws ClassNotFoundException, SQLException {
        Class.forName(postgresDriver);
        Connection connection = DriverManager.getConnection(dbUrl, usernamepostgres, passwordpostgres);
        Statement statement = connection.createStatement();
        String getId = "Select account_id from accounts where username = " + "'" + username + "'";
        ResultSet resultSet = statement.executeQuery(getId);
        if (resultSet.next()) {
            return resultSet.getInt("account_id");
        } else return 0;
    }

    public static void insertRole(String roleName) throws ClassNotFoundException, SQLException {

        Class.forName(postgresDriver);
        Connection connection = DriverManager.getConnection(dbUrl, usernamepostgres, passwordpostgres);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into role (role_name)values (?)");
        preparedStatement.setString(1, roleName);
        preparedStatement.executeUpdate();


    }
    public static int getRoleId(String roleName) throws SQLException, ClassNotFoundException {
        Class.forName(postgresDriver);
        Connection conn = DriverManager.getConnection(dbUrl, usernamepostgres, passwordpostgres);
        Statement statement = conn.createStatement();
        String getId = "SELECT role_id FROM role WHERE role_name =  " + "'" + roleName + "'";
        ResultSet resultSet = statement.executeQuery(getId);
        if (resultSet.next()){
            return resultSet.getInt("role_id");
        }else return 0;
    }

    public static void connectAccountToRole(String roleName, String username) throws SQLException, ClassNotFoundException {
        Class.forName(postgresDriver);
        Connection connection = DriverManager.getConnection(dbUrl, usernamepostgres, passwordpostgres);
        int accountId = getAccountid(username);
        int roleId = getRoleId(roleName);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into account_roles (accound_id , role_id)values (? , ?)");
        preparedStatement.setInt(1, accountId);
        preparedStatement.setInt(2, roleId);
        preparedStatement.executeUpdate();
        System.out.println(" Account: " + accountId + " Role " + roleId + "is connect");
    }
}
