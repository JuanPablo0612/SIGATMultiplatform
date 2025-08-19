package com.juanpablo0612.sigat.data.reports.local

import com.juanpablo0612.sigat.data.actions.model.ActionModel
import com.juanpablo0612.sigat.data.users.model.UserModel
import com.juanpablo0612.sigat.data.contracts.model.ContractModel
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.write
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

actual class ReportsLocalDataSourceImpl actual constructor() :
    ReportsLocalDataSource {
    @OptIn(ExperimentalTime::class, FormatStringsInDatetimeFormats::class)
    private fun mapInfoToReportData(
        user: UserModel,
        contract: ContractModel,
        startTimestamp: Long,
        endTimestamp: Long,
    ): Map<String, String> {
        val now = Clock.System.now()
        val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val monthFormatPattern = "dd/MM/yyyy"
        val monthFormat = LocalDate.Format {
            byUnicodePattern(monthFormatPattern)
        }
        val decimalFormat = DecimalFormat.getInstance(Locale.US)

        val startDate = formatDate(startTimestamp)
        val endDate = formatDate(endTimestamp)

        return mapOf(
            "dia" to localDate.day.toString(),
            "mes" to localDate.format(monthFormat),
            "Mes" to localDate.format(monthFormat).replaceFirstChar { it.uppercase() },
            "año" to localDate.year.toString(),
            "NombreCompleto" to "${user.firstName} ${user.lastName}",
            "cc" to decimalFormat.format(user.idNumber.toDouble()),
            "MES" to localDate.format(monthFormat).uppercase(),
            "numeroContrato" to contract.number,
            "fechaInicio" to startDate,
            "fechaFin" to endDate,
        )
    }

    actual override suspend fun generateReport(
        template: PlatformFile,
        output: PlatformFile,
        user: UserModel,
        contract: ContractModel,
        startTimestamp: Long,
        endTimestamp: Long,
        actions: List<ActionModel>
    ) {
        val data = mapInfoToReportData(user, contract, startTimestamp, endTimestamp)
        val document = XWPFDocument(template.readBytes().inputStream())
        val outputByteArray = ByteArrayOutputStream()

        document.paragraphs.forEach { paragraph ->
            data.forEach { (key, value) ->
                if (paragraph.text.contains(key)) {
                    val newText = paragraph.text.replace("[$key]", value)
                    paragraph.runs.forEach { it.setText("", 0) }
                    paragraph.createRun().setText(newText, 0)
                }
            }

            if (paragraph.text.contains("tablaAcciones")) {
                paragraph.runs.forEach { it.setText("", 0) }
                createActionTable(document, paragraph, actions)
            }
        }

        document.write(outputByteArray)
        document.close()
        output.write(outputByteArray.toByteArray())
    }

    private fun createActionTable(
        document: XWPFDocument,
        paragraph: XWPFParagraph,
        actions: List<ActionModel>
    ) {
        val cursor = paragraph.ctp.newCursor()
        val table = document.insertNewTbl(cursor)

        val headers = listOf("No", "Obligaciones", "Acciones realizadas", "Evidencias")
        val headerRow = table.getRow(0)
        headers.forEachIndexed { index, text ->
            if (index == 0) {
                headerRow.getCell(0).setText(text)
            } else {
                headerRow.addNewTableCell().setText(text)
            }
        }

        actions.forEachIndexed { index, action ->
            val row = table.createRow()
            row.getCell(0).setText((index + 1).toString()) // Column No
            row.getCell(1).setText(action.obligationName) // Column Obligaciones
            row.getCell(2)
                .setText("Actividad ${action.obligationNumber}.1 (${formatDate(action.timestamp)})") // Acciones realizadas
            row.getCell(3)
                .setText("Evidencia ${action.obligationNumber}.1 (${formatDate(action.timestamp)})") // Evidencias
        }
    }

    // Function to format a timestamp into a date string
    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
    }
}