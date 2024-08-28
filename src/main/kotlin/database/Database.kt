package database
import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val url = "jdbc:postgresql://localhost:5432/gestion_hospitalaria"
    private const val user = "postgres"
    private const val password = "Zixelowe1"

    fun getConnection(): Connection {

        return DriverManager.getConnection(url, user, password)
    }
}
