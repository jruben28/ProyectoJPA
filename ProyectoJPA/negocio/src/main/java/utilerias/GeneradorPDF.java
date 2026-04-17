package utilerias;

import com.dtos.ReporteClienteFrecuenteDTO;
import com.dtos.ReporteComandaDTO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Utileria para la generacion de reportes en formato PDF usando PDFBox.
 *
 * @author Devora
 */
public class GeneradorPDF {

    private static final float MARGEN = 50;
    private static final float ANCHO_PAGINA = PDRectangle.LETTER.getWidth();
    private static final float ALTO_PAGINA = PDRectangle.LETTER.getHeight();
    private static final float ALTO_FILA = 20;
    private static final float FONT_SIZE_TITULO = 16;
    private static final float FONT_SIZE_SUBTITULO = 10;
    private static final float FONT_SIZE_TABLA = 9;
    private static final SimpleDateFormat SDF_FECHA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final SimpleDateFormat SDF_FECHA = new SimpleDateFormat("dd/MM/yyyy");

    private GeneradorPDF() {
    }

    // ==================== Reporte de Comandas ====================

    public static void generarReporteComandasPDF(List<ReporteComandaDTO> comandas, Double totalVentas,
                                                  Date fechaInicio, Date fechaFin, String rutaArchivo) throws IOException {
        int numComandas = comandas != null ? comandas.size() : 0;
        int canceladas = 0;
        if (comandas != null) {
            for (ReporteComandaDTO c : comandas) {
                if (c.getEstado() != null && "CANCELADA".equalsIgnoreCase(c.getEstado())) canceladas++;
            }
        }
        double ticketPromedio = numComandas == 0 ? 0.0 : (totalVentas != null ? totalVentas : 0.0) / numComandas;
        generarReporteComandasPDF(comandas, totalVentas, numComandas, canceladas, ticketPromedio,
                fechaInicio, fechaFin, rutaArchivo);
    }

    public static void generarReporteComandasPDF(List<ReporteComandaDTO> comandas, Double totalVentas,
                                                  int numComandas, int canceladas, double ticketPromedio,
                                                  Date fechaInicio, Date fechaFin, String rutaArchivo) throws IOException {
        try (PDDocument documento = new PDDocument()) {
            String[] encabezados = {"Fecha", "Folio", "Mesa", "Mesero", "Items", "Total", "Estado"};
            float[] anchos = {70, 95, 55, 80, 45, 75, 80};

            int filasPorPagina = calcularFilasPorPagina();
            int totalPaginas = Math.max(1, (int) Math.ceil((double) comandas.size() / filasPorPagina));
            int indice = 0;

            for (int pag = 1; pag <= totalPaginas; pag++) {
                PDPage pagina = new PDPage(PDRectangle.LETTER);
                documento.addPage(pagina);

                try (PDPageContentStream cs = new PDPageContentStream(documento, pagina)) {
                    float y = ALTO_PAGINA - MARGEN;

                    // Encabezado del reporte
                    y = dibujarEncabezadoReporte(cs, "Reporte de Ventas",
                            "Periodo: " + SDF_FECHA.format(fechaInicio) + " - " + SDF_FECHA.format(fechaFin), y);

                    // Resumen KPI solo en la primera pagina
                    if (pag == 1) {
                        y = dibujarResumenKPI(cs, totalVentas, numComandas, ticketPromedio, canceladas, y);
                    }

                    // Encabezados de tabla
                    y = dibujarFilaEncabezado(cs, encabezados, anchos, y);

                    // Filas de datos
                    int filasEnPagina = 0;
                    while (indice < comandas.size() && filasEnPagina < filasPorPagina) {
                        ReporteComandaDTO c = comandas.get(indice);
                        String[] fila = {
                            c.getFechaHora() != null ? SDF_FECHA.format(c.getFechaHora()) : "-",
                            c.getFolio() != null ? c.getFolio() : "-",
                            c.getNumMesa() != null ? "Mesa " + c.getNumMesa() : "-",
                            "-",
                            "-",
                            String.format("$%,.2f", c.getTotal()),
                            c.getEstado()
                        };
                        y = dibujarFila(cs, fila, anchos, y, indice % 2 == 0);
                        indice++;
                        filasEnPagina++;
                    }

                    // Total de ventas en la ultima pagina
                    if (pag == totalPaginas && totalVentas != null) {
                        y -= 10;
                        cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
                        cs.beginText();
                        cs.newLineAtOffset(MARGEN, y);
                        cs.showText("Total de ventas del periodo: " + String.format("$%,.2f", totalVentas));
                        cs.endText();
                    }

                    // Pie de pagina
                    dibujarPiePagina(cs, pag, totalPaginas);
                }
            }

            documento.save(rutaArchivo);
        }
    }

    // ==================== Reporte de Clientes Frecuentes ====================

    public static void generarReporteClientesPDF(List<ReporteClienteFrecuenteDTO> clientes,
                                                  String rutaArchivo) throws IOException {
        try (PDDocument documento = new PDDocument()) {
            String[] encabezados = {"Nombre", "Visitas", "Total Gastado", "Ultima Comanda"};
            float[] anchos = {170, 70, 120, 120};

            int filasPorPagina = calcularFilasPorPagina();
            int totalPaginas = Math.max(1, (int) Math.ceil((double) clientes.size() / filasPorPagina));
            int indice = 0;

            for (int pag = 1; pag <= totalPaginas; pag++) {
                PDPage pagina = new PDPage(PDRectangle.LETTER);
                documento.addPage(pagina);

                try (PDPageContentStream cs = new PDPageContentStream(documento, pagina)) {
                    float y = ALTO_PAGINA - MARGEN;

                    // Encabezado del reporte
                    y = dibujarEncabezadoReporte(cs, "Reporte de Clientes Frecuentes",
                            "Fecha de generacion: " + SDF_FECHA.format(new Date()), y);

                    // Encabezados de tabla
                    y = dibujarFilaEncabezado(cs, encabezados, anchos, y);

                    // Filas de datos
                    int filasEnPagina = 0;
                    while (indice < clientes.size() && filasEnPagina < filasPorPagina) {
                        ReporteClienteFrecuenteDTO c = clientes.get(indice);
                        String[] fila = {
                            c.getNombre(),
                            String.valueOf(c.getNumVisitas()),
                            String.format("$%,.2f", c.getTotalGastado()),
                            c.getFechaUltimaComanda() != null ? SDF_FECHA.format(c.getFechaUltimaComanda()) : "Sin comandas"
                        };
                        y = dibujarFila(cs, fila, anchos, y, indice % 2 == 0);
                        indice++;
                        filasEnPagina++;
                    }

                    // Pie de pagina
                    dibujarPiePagina(cs, pag, totalPaginas);
                }
            }

            documento.save(rutaArchivo);
        }
    }

    // ==================== Metodos auxiliares de dibujo ====================

    private static float dibujarResumenKPI(PDPageContentStream cs, Double totalVentas,
                                            int numComandas, double ticketPromedio, int canceladas,
                                            float y) throws IOException {
        String[] etiquetas = {"TOTAL VENTAS", "COMANDAS", "TICKET PROMEDIO", "CANCELADAS"};
        String[] valores = {
            String.format("$%,.2f", totalVentas != null ? totalVentas : 0.0),
            String.valueOf(numComandas),
            String.format("$%,.2f", ticketPromedio),
            String.valueOf(canceladas)
        };
        float anchoTotal = ANCHO_PAGINA - 2 * MARGEN;
        float anchoTarjeta = (anchoTotal - 3 * 10) / 4;
        float altoTarjeta = 50;
        float xInicio = MARGEN;
        float yTop = y;

        for (int i = 0; i < 4; i++) {
            float x = xInicio + i * (anchoTarjeta + 10);
            cs.setNonStrokingColor(0.97f, 0.97f, 0.97f);
            cs.addRect(x, yTop - altoTarjeta, anchoTarjeta, altoTarjeta);
            cs.fill();
            cs.setNonStrokingColor(0, 0, 0);

            cs.setFont(PDType1Font.HELVETICA_BOLD, 8);
            cs.beginText();
            cs.newLineAtOffset(x + 8, yTop - 16);
            cs.showText(etiquetas[i]);
            cs.endText();

            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
            cs.beginText();
            cs.newLineAtOffset(x + 8, yTop - 36);
            cs.showText(valores[i]);
            cs.endText();
        }

        return yTop - altoTarjeta - 15;
    }

    private static float dibujarEncabezadoReporte(PDPageContentStream cs, String titulo,
                                                   String subtitulo, float y) throws IOException {
        cs.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_TITULO);
        cs.beginText();
        cs.newLineAtOffset(MARGEN, y);
        cs.showText(titulo);
        cs.endText();
        y -= 18;

        cs.setFont(PDType1Font.HELVETICA, FONT_SIZE_SUBTITULO);
        cs.beginText();
        cs.newLineAtOffset(MARGEN, y);
        cs.showText(subtitulo);
        cs.endText();
        y -= 25;

        // Linea separadora
        cs.setLineWidth(1);
        cs.moveTo(MARGEN, y);
        cs.lineTo(ANCHO_PAGINA - MARGEN, y);
        cs.stroke();
        y -= 15;

        return y;
    }

    private static float dibujarFilaEncabezado(PDPageContentStream cs, String[] encabezados,
                                                float[] anchos, float y) throws IOException {
        // Fondo del encabezado
        float anchoTotal = 0;
        for (float a : anchos) anchoTotal += a;
        cs.setNonStrokingColor(0.9f, 0.9f, 0.95f);
        cs.addRect(MARGEN, y - ALTO_FILA + 4, anchoTotal, ALTO_FILA);
        cs.fill();
        cs.setNonStrokingColor(0, 0, 0);

        // Texto de encabezados
        cs.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_TABLA);
        float x = MARGEN + 4;
        for (int i = 0; i < encabezados.length; i++) {
            cs.beginText();
            cs.newLineAtOffset(x, y - ALTO_FILA + 10);
            cs.showText(encabezados[i]);
            cs.endText();
            x += anchos[i];
        }

        return y - ALTO_FILA;
    }

    private static float dibujarFila(PDPageContentStream cs, String[] valores,
                                      float[] anchos, float y, boolean fondoAlterno) throws IOException {
        if (fondoAlterno) {
            float anchoTotal = 0;
            for (float a : anchos) anchoTotal += a;
            cs.setNonStrokingColor(0.97f, 0.97f, 0.97f);
            cs.addRect(MARGEN, y - ALTO_FILA + 4, anchoTotal, ALTO_FILA);
            cs.fill();
            cs.setNonStrokingColor(0, 0, 0);
        }

        cs.setFont(PDType1Font.HELVETICA, FONT_SIZE_TABLA);
        float x = MARGEN + 4;
        for (int i = 0; i < valores.length; i++) {
            String texto = valores[i];
            // Truncar texto largo para que no se salga de la columna
            if (texto != null && texto.length() > (int)(anchos[i] / 5)) {
                texto = texto.substring(0, (int)(anchos[i] / 5) - 2) + "..";
            }
            cs.beginText();
            cs.newLineAtOffset(x, y - ALTO_FILA + 10);
            cs.showText(texto != null ? texto : "-");
            cs.endText();
            x += anchos[i];
        }

        return y - ALTO_FILA;
    }

    private static void dibujarPiePagina(PDPageContentStream cs, int paginaActual, int totalPaginas) throws IOException {
        cs.setFont(PDType1Font.HELVETICA, 8);
        String textoPagina = "Pagina " + paginaActual + " de " + totalPaginas;
        cs.beginText();
        cs.newLineAtOffset(ANCHO_PAGINA / 2 - 30, 30);
        cs.showText(textoPagina);
        cs.endText();

        String fechaGen = "Generado: " + SDF_FECHA_HORA.format(new Date());
        cs.beginText();
        cs.newLineAtOffset(MARGEN, 30);
        cs.showText(fechaGen);
        cs.endText();
    }

    private static int calcularFilasPorPagina() {
        float espacioDisponible = ALTO_PAGINA - MARGEN - 90 - 40;
        return (int) (espacioDisponible / ALTO_FILA);
    }
}
