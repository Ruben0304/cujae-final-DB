package database
import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val url = "jdbc:postgresql://proyecto-final-db-cujae.ct2gscugqitp.us-east-2.rds.amazonaws.com:5432/"
    private const val user = "postgres"
    private const val password = "Zixelowe1"

    fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
}
