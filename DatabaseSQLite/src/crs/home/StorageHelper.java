package crs.home;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Optional;

class StorageHelper {
  private static final String sqlAddStudent
      = "INSERT INTO Student(name, code, phone) VALUES(?, ?, ?)";
  private static final String sqlAddDomain
      = "INSERT INTO Domain(domain) VALUES(?)";
  private static final String sqlAddMark
      = "INSERT INTO Mark(id_student, id_domain, mark) VALUES(?, ?, ?)";

  private static final String sqlSelectAllStudents
      = "SELECT * FROM Student";
  private static final String sqlCountStudents
      = "SELECT count(*) FROM Student WHERE Student.name LIKE ?";
  private static final String sqlSelectAllDomains
      = "SELECT * FROM Domain";
  private static final String sqlListMarks
      = "SELECT Student.name AS student, Domain.domain, Mark.mark "
      + "FROM Mark "
      + "INNER JOIN Student ON Mark.id_student = Student.id "
      + "INNER JOIN Domain ON Mark.id_domain = Domain.id "
      + "WHERE Student.name LIKE ?";
  private static final String sqlCountMarks
      = "SELECT count(ALL Mark.mark) "
      + "FROM Mark "
      + "INNER JOIN Student ON Mark.id_student = Student.id "
      + "WHERE Student.name LIKE ?";
  private static final String sqlAverageMarks
      = "SELECT avg(ALL Mark.mark) "
      + "FROM Mark "
      + "INNER JOIN Student ON Mark.id_student = Student.id "
      + "WHERE Student.name LIKE ?";

  private static final String sqlSelectStudentIdByName
      = "SELECT id FROM Student WHERE name LIKE ?";
  private static final String sqlSelectDomainIdByName
      = "SELECT id FROM Domain WHERE domain LIKE ?";

  static boolean addStudent(Storage _storage, String _name, String _code, String _phone) {
    try {
      final Connection conn = _storage.getConnection();

      conn.setAutoCommit( false );

      final PreparedStatement statement = conn.prepareStatement(sqlAddStudent, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, _name);
      statement.setString( 2, _code );
      statement.setString( 3, _phone );

      final int affectedRowCount = statement.executeUpdate();

      if (affectedRowCount != 1) {
        conn.rollback();
        return false;
      } else {
        conn.commit();
        return true;
      }

    } catch ( SQLException e ) {
      System.err.println( "ERROR: " + e.toString() );
      return false;
    }
  }

  static boolean addDomain(Storage _storage, String _name) {
    try {
      final Connection conn = _storage.getConnection();

      conn.setAutoCommit( false );

      final PreparedStatement statement = conn.prepareStatement(sqlAddDomain, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, _name);

      final int affectedRowCount = statement.executeUpdate();

      if (affectedRowCount != 1) {
        conn.rollback();
        return false; // TODO: this value is not used
      } else {
        conn.commit();
        return true;
      }

    } catch ( SQLException e ) {
      System.err.println( "ERROR: " + e.toString() );
      return false;
    }
  }

  static boolean addMark(Storage _storage, Integer _studentId, Integer _domainId, Integer _mark) {
    try {
      final Connection conn = _storage.getConnection();

      conn.setAutoCommit( false );

      final PreparedStatement statement = conn.prepareStatement(sqlAddMark, Statement.RETURN_GENERATED_KEYS);

      statement.setInt( 1, _studentId);
      statement.setInt( 2, _domainId);
      statement.setInt( 3, _mark);

      final int affectedRowCount = statement.executeUpdate();

      if (affectedRowCount != 1) {
        conn.rollback();
        return false;
      } else {
        conn.commit();
        return true;
      }

    } catch ( SQLException e ) {
      System.err.println( "ERROR: " + e.toString() );
      return false;
    }
  }

  static void listStudents(Storage _storage) {
    try {
      int count = 0;

      final Connection conn = _storage.getConnection();
      final Statement stat = conn.createStatement();
      ResultSet resultSet = stat.executeQuery( sqlSelectAllStudents );

      System.out.println( "Students:" );
      while(resultSet.next()) {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        final String code = resultSet.getString( "code" );
        final String phone = resultSet.getString( "phone" );
        System.out.println( "    " + id + "\t" + name + "\t" + code + "\t" + phone );
        count++;
      }

      if ( count == 0 ) {
        System.out.println( "    <empty>" );
      }

      System.out.println();

    } catch (SQLException ex) {
      System.err.println( "ERROR: while listing students: " + ex.toString() );
    }
  }

  private static Optional<Double> getOneDoubleValueFromQueryWithStringArgument(
      Storage _storage, String _sql, String _stringParameter) {
    final Connection conn = _storage.getConnection();
    if (conn != null) {
      try (
          final PreparedStatement prepStat = conn.prepareStatement(_sql);
      ) {
        prepStat.setString( 1, "%" + _stringParameter + "%" );

        ResultSet resultSet = prepStat.executeQuery();
        if ( resultSet.next() ) {
          final double count = resultSet.getDouble( 1 );
          if (resultSet.next()) {
            System.err.println( "ERROR: more than one result is not expected" );
            return Optional.empty();
          }
          return Optional.of( count );
        } else {
          return Optional.of( 0.0 );
        }

      } catch ( SQLException ex ) {
        System.err.println( "ERROR: while executing query: " + ex.toString() );
      }
    } else {
      System.err.println( "ERROR: no connection" ); // TODO: throw
    }
    return Optional.empty();
  }

  static Optional<Integer> countStudents(Storage _storage, String _studentName) {
    final Optional<Double> count
        = getOneDoubleValueFromQueryWithStringArgument( _storage, sqlCountStudents, _studentName );
    if (count.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of( (int)(count.get() + 0.5) );
    }
  }

  static Optional<Integer> countMarks(Storage _storage, String _studentName) {
    final Optional<Double> count
        = getOneDoubleValueFromQueryWithStringArgument( _storage, sqlCountMarks, _studentName );
    if (count.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of( (int)(count.get() + 0.5) );
    }
  }

  static Optional<Double> averageMarks(Storage _storage, String _studentName) {
    return getOneDoubleValueFromQueryWithStringArgument( _storage, sqlAverageMarks, _studentName );
  }

  static void listDomains(Storage _storage) {
    try {
      int count = 0;

      final Connection conn = _storage.getConnection();
      final Statement stat = conn.createStatement();
      ResultSet resultSet = stat.executeQuery( sqlSelectAllDomains );

      System.out.println( "Domains:" );
      while(resultSet.next()) {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("domain");
        System.out.println( "    " + id + "\t" + name );
        count++;
      }

      if ( count == 0 ) {
        System.out.println( "    <empty>" );
      }

      System.out.println();

    } catch (SQLException ex) {
      System.err.println( "ERROR: while listing students: " + ex.toString() );
    }
  }

  static void listMarks(Storage _storage, String _studentName) {
    final Connection conn = _storage.getConnection();
    if (conn != null) {
      try (
          final PreparedStatement prepStat = conn.prepareStatement( sqlListMarks );
          ) {
        prepStat.setString( 1 , "%" + _studentName + "%" );

        ResultSet resultSet = prepStat.executeQuery();
        int count = 0;

        System.out.println( "Marks for student(s) whose name contain '" + _studentName + "':" );
        while ( resultSet.next() ) {
          final String stud = resultSet.getString( "student" );
          final String domain = resultSet.getString( "domain" );
          final int mark = resultSet.getInt( "mark" );
          System.out.println( "    " + stud + "\t" + domain + "\t" + mark );
          count++;
        }

        if ( count == 0 ) {
          System.out.println( "    <none>" );
        }

      } catch ( SQLException ex ) {
        System.err.println( "ERROR: while listing students: " + ex.toString() );
      }
    }
  }

  private static Optional<Integer> getEntityIdByStringColumn(
      Storage _storage, String _sql, String _searchColumnValue, String _resultColumnName) {
    final Connection conn = _storage.getConnection();
    if (conn != null) {
      try (
          final PreparedStatement prepStat = conn.prepareStatement( _sql );
          ) {
        prepStat.setString( 1 , "%" + _searchColumnValue + "%" );
        ResultSet resSet = prepStat.executeQuery();
        Optional<Integer> res = Optional.empty();
        while ( resSet.next() ) {
          if ( res.isEmpty()) {
            final int id = resSet.getInt( _resultColumnName );
            res = Optional.of(id);
          } else {
            // duplicate
            System.err.println( "ERROR: more than one result for search term '" + _searchColumnValue + "' was found." );
            res = Optional.empty();
            break;
          }
        }

        return res;

      } catch ( SQLException _e ) {
        System.err.println( "ERROR: " + _e.toString() );
      }
    }
    return Optional.empty();
  }

  static Optional<Integer> getDomainIdByName(Storage _storage, String _domainName) {
    return getEntityIdByStringColumn( _storage, sqlSelectDomainIdByName, _domainName, "id");
  }

  static Optional<Integer> getStudentIdByName(Storage _storage, String _studentName) {
    return getEntityIdByStringColumn( _storage, sqlSelectStudentIdByName, _studentName, "id");
  }
}
