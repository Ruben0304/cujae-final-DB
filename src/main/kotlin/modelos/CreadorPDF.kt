import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.element.Cell
import dao.HospitalDAO
import java.io.File
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class CreadorPDF {

    // Método para generar un PDF
    fun generarPdf(outputPath: String, fileName: String, title: String, headers: List<String>, data: List<List<String>>) {
        // Asegurarse de que el directorio de salida exista
        File(outputPath).mkdirs()

        // Construir la ruta completa del archivo
        val fullPath = File(outputPath, fileName).absolutePath

        PdfWriter(fullPath).use { writer ->
            PdfDocument(writer).use { pdf ->
                Document(pdf, PageSize(3000f,500f)).use { document ->
                    // Configurar fuente
                    val font = PdfFontFactory.createFont()
                    document.setFont(font)

                    // Agregar título
                    val titleParagraph = Paragraph(title)
                        .setFontSize(18f)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setMarginBottom(20f)
                    document.add(titleParagraph)

                    // Crear tabla
                    val table = Table(UnitValue.createPercentArray(headers.size)).useAllAvailableWidth()

                    // Agregar encabezados
                    headers.forEach { header ->
                        table.addHeaderCell(
                            Cell().add(Paragraph(header))
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setFontSize(12f)
                                .setBold()
                        )
                    }

                    // Agregar datos
                    data.forEach { row ->
                        row.forEach { cellData ->
                            table.addCell(
                                Cell().add(Paragraph(cellData))
                                    .setFontSize(10f)
                            )
                        }
                    }

                    document.add(table)
                }
            }
        }

        println("PDF creado en: $fullPath")
    }

    // Método que recibe una lista de cualquier data class
    fun <T : Any> generarPdfDesdeDataClass(outputPath: String, fileName: String, title: String, lista: List<T>) {
        if (lista.isEmpty()) {
            println("La lista está vacía. No se generará el PDF.")
            return
        }

        // Obtener los nombres de los atributos (propiedades) del data class como encabezados
        val headers = lista.first()::class.memberProperties.map { it.name }

        // Obtener los valores de los atributos de cada objeto en la lista
        val data = lista.map { item ->
            item::class.memberProperties.map { prop ->
                (prop as KProperty1<Any, *>).get(item)?.toString() ?: ""
            }
        }

        // Llamar al método que genera el PDF usando los encabezados y los datos
        generarPdf(outputPath, fileName, title, headers, data)
    }
}

// Ejemplo de uso con un data class
data class Persona(val nombre: String, val edad: Int, val ciudad: String)

suspend fun main() {
    val personas = listOf(
        Persona("Juan", 28, "Madrid"),
        Persona("Ana", 24, "Barcelona"),
        Persona("Pedro", 35, "Valencia")
    )

    val resultado = HospitalDAO.resumenProceso("H001")
    val creadorPDF = CreadorPDF()
    creadorPDF.generarPdfDesdeDataClass(
        outputPath = "./",
        fileName = "personas.pdf",
        title = "Listado de Personas",
        lista = if (resultado!=null) listOf(resultado) else emptyList()
    )
}
