
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue

class CreadorPDF {
    fun generarPdf(fileName: String, title: String, headers: List<String>, data: List<List<String>>) {
        PdfWriter(fileName).use { writer ->
            PdfDocument(writer).use { pdf ->
                Document(pdf).use { document ->
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
    }
}