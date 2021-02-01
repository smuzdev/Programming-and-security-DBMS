package by.bstu.mark.lab_17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    private static final String TAG = "LAB_17_DB";
    private Button btn_connectMS_Sql_Server;
    private Connection connection;
    private DataTable dataTable;

    private static String ip = "80.94.168.145";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "LAB_17_SQLlite";
    private static String username = "student";
    private static String password = "Pa$$w0rd";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_connectMS_Sql_Server = findViewById(R.id.btn_connect);
        dataTable = findViewById(R.id.dataTable);

        btn_connectMS_Sql_Server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    Class.forName(Classes);
                    connection = DriverManager.getConnection(url, username, password);
                    Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Drawer
        // <---- ----->
        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.select_operation:
                        String query = "SELECT * FROM products";
                        DataTableHeader header = new DataTableHeader.Builder()
                                .item("ID_PRODUCT\n(PK)" , 1)
                                .item("NameProduct", 1)
                                .item("Price", 1)
                                .build();

                        ArrayList<DataTableRow> rows = new ArrayList<>();


                        if(connection != null) {
                            Statement statement = null;
                            try {
                                statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery(query);
                                while (resultSet.next()) {
                                    DataTableRow row = new DataTableRow.Builder()
                                            .value(resultSet.getString(1))
                                            .value(resultSet.getString(2))
                                            .value(resultSet.getString(3))
                                            .build();
                                    rows.add(row);
                                }

                                resultSet.close();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                                Log.d(TAG, throwables.getMessage());
                            }
                        }

                        dataTable.setHeader(header);
                        dataTable.setHeaderTextSize(10);
                        dataTable.setRows(rows);
                        dataTable.inflate(getApplicationContext());
                        break;
                    case R.id.batch_insert:
                        try {
                            PreparedStatement preparedStatement = connection
                                    .prepareStatement("INSERT into products (ProductName, Price) values (?, ?)");
                            preparedStatement.setString(1, "Lenovo IdeaPad 5i");
                            preparedStatement.setInt(2, 2150);
                            preparedStatement.addBatch();

                            preparedStatement.setString(1, "Apple MacBook Air 2020");
                            preparedStatement.setInt(2, 2800);
                            preparedStatement.addBatch();

                            preparedStatement.setString(1, "ASUS ZenBook 14");
                            preparedStatement.setInt(2, 2300);
                            preparedStatement.addBatch();

                            preparedStatement.setString(1, "Samsung Galaxy Book S");
                            preparedStatement.setInt(2, 2400);
                            preparedStatement.addBatch();

                            preparedStatement.executeBatch();

                            Toast.makeText(MainActivity.this, "Batch insert is successful", Toast.LENGTH_SHORT).show();


                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            Log.d(TAG, throwables.getMessage());
                        }
                        break;
                    case R.id.call_procedure:
                        try {
                            header = new DataTableHeader.Builder()
                                    .item("ID_PRODUCT\n(PK)" , 1)
                                    .item("NameProduct", 1)
                                    .build();
                            rows = new ArrayList<>();

                            //CREATE PROCEDURE dbo.ProductName (@pr_id int)
                            //AS
                            //SELECT Id, ProductName from dbo.products where id = @pr_id;

                            CallableStatement callableStatement = connection.prepareCall("dbo.ProductName ?");
                            callableStatement.setInt(1, 1);
                            ResultSet resultSet = callableStatement.executeQuery();

                            while (resultSet.next()) {
                                DataTableRow row = new DataTableRow.Builder()
                                        .value(resultSet.getString(1))
                                        .value(resultSet.getString(2))
                                        .build();
                                rows.add(row);
                            }

                            dataTable.setHeader(header);
                            dataTable.setHeaderTextSize(10);
                            dataTable.setRows(rows);
                            dataTable.inflate(getApplicationContext());

                            resultSet.close();
                        }

                        catch (SQLException throwables) {
                            throwables.printStackTrace();
                            Log.d(TAG, throwables.getMessage());
                        }
                        break;
                    case R.id.call_function:
                        if(connection !=null) {
                            try {

                                //CREATE FUNCTION [dbo].NumProducts() RETURNS int
                                //AS
                                //BEGIN
                                //DECLARE @num int = -1;
                                //SELECT @num=COUNT(*) FROM dbo.products;
                                //RETURN @num;
                                //END;

                                Statement statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery("SELECT dbo.NumProducts() AS num;");
                                Integer num = null;
                                while (resultSet.next()) {
                                    num = Integer.parseInt(resultSet.getString(1));
                                }

                                Toast.makeText(MainActivity.this, "Rows in table: " + (++num).toString(), Toast.LENGTH_SHORT).show();

                            } catch (SQLException throwable) {
                                throwable.printStackTrace();
                                Log.d(TAG, throwable.getMessage());
                            }
                        }

                        break;
                    case R.id.truncate:
                        Statement statement = null;
                        try {
                            statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery("TRUNCATE TABLE products;");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        break;
                }
                return true;
            }
        });
        // <---- ----->
    }
}