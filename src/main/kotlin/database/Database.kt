package database
import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val url = "jdbc:postgresql://0.tcp.us-cal-1.ngrok.io:13901/postgres"
    private const val user = "postgres"
    private const val password = "FERXXO100"

    fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
}
