package database
import modelos.Departamento
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private const val url = "jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres"
    private const val user = "postgres.jhxtxukwfkcuzeqxgrtf"
    private const val password = "5OzfWl8c9CnFfVjI"

    fun getConnection(): Connection {

        return DriverManager.getConnection(url, user, password)
    }
}



fun main() {


    try {
        val connection: Connection = DriverManager.getConnection(
            "jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:6543/postgres","postgres.wgfdmgsbsqflcgtecglw","@flowFerxxo2024"
        )
        println("ok")
    }catch (e: SQLException) {
        println(e.message)
    }


}
