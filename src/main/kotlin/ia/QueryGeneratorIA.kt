package ia


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.*
import io.ktor.http.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import kotlinx.serialization.Serializable
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*


//Llama api : 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


suspend fun generateLlmPrompt(dbSchema: String, naturalLanguageQuery: String): String {
    val query = """
    Eres un asistente especializado en generar consultas SQL precisas. Tu tarea es convertir consultas en lenguaje natural a código SQL válido para PostgreSQL. No debes proporcionar explicaciones ni comentarios adicionales, solo el código SQL requerido.

    Dado el siguiente esquema de base de datos:
    $dbSchema

    Genera una consulta SQL para:
    $naturalLanguageQuery

    Reglas importantes:
    1. Responde ÚNICAMENTE con el código SQL, sin ningún texto adicional.
    2. El código SQL debe estar encerrado entre los símbolos .SQL. y .END.
    3. Asegúrate de que el código SQL sea válido y esté listo para ser ejecutado sin modificaciones.
    4. No incluyas comentarios ni explicaciones en el código SQL.
    5.Asegúrate de que el código SQL sea sintácticamente correcto y no contenga caracteres que puedan dar error al compilar en SQL console.
    6. En ese esquema esta TODO , lo que no sale ahi no esta hecho, no asumas nada en caso de que algo no exista.
   
    Ejemplo de respuesta correcta: .SQL.
SELECT 
    h.nombre AS hospital,
    d.nombre AS departamento,
    COUNT(m.codigo) AS total_medicos
FROM 
    public.hospital h
JOIN 
    public.departamento d ON h.codigo = d.hospital_codigo
JOIN 
    public.unidad u ON d.codigo = u.departamento_codigo AND d.hospital_codigo = u.hospital_codigo
JOIN 
    public.medico m ON u.codigo = m.unidad_codigo AND u.departamento_codigo = m.departamento_codigo AND u.hospital_codigo = m.hospital_codigo
GROUP BY 
    h.nombre, d.nombre
ORDER BY 
    h.nombre, d.nombre;
.END.
    """

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) // Configura Json para ignorar claves desconocidas
        }
    }

    val payload = buildJsonObject {
        put("model", "llama3-70b-8192")
        put("messages", buildJsonArray {
            add(buildJsonObject {
                put("role", "user")
                put("content", query)
            })
        })
    }

    val rawResponse = client.post("https://api.groq.com/openai/v1/chat/completions") {
        headers {
            append("Authorization", "Bearer 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")
            append("Content-Type", "application/json")
        }
        setBody(payload.toString())
    }.body<String>()

    client.close()

    println("Raw Response: $rawResponse")

    return try {
        // Intenta deserializar la respuesta
        val responseJson = Json.parseToJsonElement(rawResponse).jsonObject
        val choices = responseJson["choices"]?.jsonArray?.firstOrNull()?.jsonObject
        var sqlCode = choices?.get("message")?.jsonObject?.get("content")?.toString() ?: ""

        // Limpiar caracteres especiales
        sqlCode = sqlCode
            .replace("\\n", "\n") // Reemplaza \n por saltos de línea reales
            .replace("\\\"", "\"") // Reemplaza las comillas escapadas
            .replace("\\\\", "\\") // Reemplaza las barras invertidas dobles
            .substringAfter(".SQL.")
            .substringBefore(".END.")
            .trim()

        sqlCode
    } catch (e: Exception) {
        println("Error parsing response: ${e.message}")
        // Intenta parsear el error
        try {
            val errorResponse = Json.decodeFromString<GroqError>(rawResponse)
            "Error from API: ${errorResponse.message}"
        } catch (errorParsingException: Exception) {
            "Error parsing error response: ${errorParsingException.message}"
        }
    }
}


// Define las clases necesarias para manejar la respuesta
@Serializable
data class GroqError(val message: String, val type: String, val code: String)






