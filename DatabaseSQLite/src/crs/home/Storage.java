package crs.home;

import java.io.File;
import java.sql.*;

public class Storage implements AutoCloseable {
  private String directory;
  private Connection connection;

  Storage(String _directory) {
    directory = _directory;

    getConnection();

    createSchema();
  }

  static boolean testDriver() {
    try {
      Class.forName( "org.sqlite.JDBC" );
      return true;
    } catch ( ClassNotFoundException e ) {
      System.err.println( "ERROR: Driver not found: " + e.toString() );
      return false;
    }
  }

  private Statement createStatement() {
    try {
      return connection.createStatement();
    } catch (SQLException ex) {
      System.err.println( "ERROR creating statement: " + ex.toString() );
      return null;
    }
  }

  private void executeStatement(String _sql) {
    try {
      Statement stStudent = createStatement();
      stStudent.execute( _sql );
    } catch (SQLException ex) {
      System.err.println( "ERROR: when executing statement: '" + _sql + "': " + ex.toString() );
    }
  }

  Connection getConnection() {
    if ( connection == null ) {
      final String DATABASE_FILE_NAME = "tmp.db";
      final String SEPARATOR = System.getProperty( "file.separator" );
      File databaseFileFullPathAndName = new File( directory + SEPARATOR + DATABASE_FILE_NAME );
      try {
        connection = DriverManager.getConnection(
            "jdbc:sqlite:" + databaseFileFullPathAndName );
        //if ( connection != null ) {
        //  System.out.println( "Catalog: " + connection.getCatalog() );
        //  System.out.println( "Schema: " + connection.getSchema() );
        //  System.out.println( "Metadata: " + connection.getMetaData().toString() );
        //  System.out.println( "DriverName: " + connection.getMetaData().getDriverName() );
        //}
      } catch ( SQLException sqlEx ) {
        System.err.println( "ERROR: cannot getConnection: " + sqlEx.toString() );
        return null;
        //} finally {
        //  try {
        //    if (connection != null) {
        //      connection.close();
        //    }
        //  } catch (SQLException sqlEx) {
        //    System.out.println( sqlEx.toString() );
        //  }
      }
    }
    return connection;
  }

  private void createSchema() {
    executeStatement(
        "CREATE TABLE IF NOT EXISTS Student ("
        + " id INTEGER PRIMARY KEY,"
        + " name text NOT NULL,"
        + " code text,"
        + " phone text, "
        + " CONSTRAINT student_name_unique UNIQUE (name)"
        + ")" );

    executeStatement(
        "CREATE TABLE IF NOT EXISTS Domain("
        + " id INTEGER PRIMARY KEY,"
        + " domain text NOT NULL, "
        + " CONSTRAINT domain_domain_unique UNIQUE (domain)"
        + ")" );

    executeStatement(
        "CREATE TABLE IF NOT EXISTS Mark("
        + " id INTEGER PRIMARY KEY,"
        + " id_student INTEGER NOT NULL,"
        + " id_domain INTEGER NOT NULL,"
        + " mark INTEGER NOT NULL,"
        + " FOREIGN KEY (id_student) REFERENCES Student(id)," // ON UPDATE CASCADE ON DELETE RESTRICT
        + " FOREIGN KEY (id_domain) REFERENCES Domain(id),"
        + " CONSTRAINT mark_id_student_id_domain_unique UNIQUE (id_student, id_domain)"
        + ")" );
  }

  @Override
  public void close() throws Exception {
    if(connection != null) {
      connection.close();
      connection = null;
    }
  }
}
