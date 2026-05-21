package com.axity.dinosaurpark.persistence;

import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class DatabaseService {
    private Connection connection;

    public DatabaseService(String dbPath) {
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + dbPath, "sa", "");
            runLiquibase();
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    private void runLiquibase() throws Exception {
        Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        
        Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
            CommandScope updateCommand = new CommandScope("update");
            updateCommand.addArgumentValue("changelogFile", "db/changelog/db.changelog-master.xml");
            updateCommand.addArgumentValue("database", db);
            updateCommand.execute();
        });
        connection.setAutoCommit(true);
    }

    public void appendRevenue(RevenueRecord r) {
        String sql = "INSERT INTO revenues (type, amount, tourist_id, zone, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, r.type());
            pstmt.setDouble(2, r.amount());
            pstmt.setInt(3, r.touristId());
            pstmt.setString(4, r.zone());
            pstmt.setTimestamp(5, Timestamp.valueOf(r.timestamp()));
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error guardando ingreso: " + e.getMessage());
        }
    }

    public void appendExpense(ExpenseRecord e) {
        String sql = "INSERT INTO expenses (type, amount, description, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, e.type());
            pstmt.setDouble(2, e.amount());
            pstmt.setString(3, e.description());
            pstmt.setTimestamp(4, Timestamp.valueOf(e.timestamp()));
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.err.println("Error guardando gasto: " + ex.getMessage());
        }
    }

    public void appendEvent(EventRecord ev) {
        String sql = "INSERT INTO events (step, event_name, description, affected_entities, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, ev.step());
            pstmt.setString(2, ev.eventName());
            pstmt.setString(3, ev.description());
            pstmt.setString(4, ev.affectedEntities());
            pstmt.setTimestamp(5, Timestamp.valueOf(ev.timestamp()));
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.err.println("Error guardando evento: " + ex.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (Exception e) {
            System.err.println("Error cerrando base de datos: " + e.getMessage());
        }
    }
}