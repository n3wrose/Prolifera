package com.prolifera.api;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.prolifera.api.model.DTO.AmostraDTO;
import com.prolifera.api.model.DTO.AmostraQualificadorDTO;
import com.prolifera.api.model.DTO.AmostraQuantificadorDTO;
import com.prolifera.api.model.DTO.EtapaDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.helpers.XSSFXmlColumnPr;

import java.awt.image.ImagingOpException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportXlsFile {

    public static void exportEtapaData(List<AmostraDTO> amostras) throws IOException, InvalidFormatException {

        EtapaDTO etapa = amostras.get(0).getEtapa();
        int rowIndex = 2, columnIndex = 3;
        Workbook workbook = new XSSFWorkbook();
        CreationHelper helper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet(etapa.getCodigo()+" - "+etapa.getNome());

        Map<Long, Integer> rowColumnId = new HashMap<>();
        List<Row> rows = new ArrayList<>();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCell = workbook.createCellStyle();
        headerCell.setFont(headerFont);

        Row headerRow = sheet.createRow(1);
        Cell sampleName = headerRow.createCell(1);
        sampleName.setCellStyle(headerCell);
        sampleName.setCellValue("Nome");
        Cell sampleID = headerRow.createCell(2);
        sampleID.setCellStyle(headerCell);
        sampleID.setCellValue("ID");
        for (AmostraDTO amostra : amostras) {
            List<Long> ids = new ArrayList<>();
            Row sampleRow = sheet.createRow(rowIndex);
            rowIndex++;
            Cell nameCell = sampleRow.createCell(1);
            nameCell.setCellValue(amostra.getNome());
            Cell idCell = sampleRow.createCell(2);
            idCell.setCellValue(amostra.getIdAmostra());
            for (AmostraQuantificadorDTO aqdto : amostra.getMedidas()) {
                if (!ids.contains((Long) aqdto.getQuantificador().getIdQuantificador())) {
                    if (!rowColumnId.containsKey(aqdto.getQuantificador().getIdQuantificador())) {
                        Cell newColumn = headerRow.createCell(columnIndex);
                        newColumn.setCellStyle(headerCell);
                        newColumn.setCellValue(aqdto.getQuantificador().getNome()+" ("+aqdto.getQuantificador().getUnidade()+")");
                        rowColumnId.put(aqdto.getQuantificador().getIdQuantificador(),columnIndex);
                        columnIndex++;
                    }

                    Cell value = sampleRow.createCell(rowColumnId.get(aqdto.getQuantificador().getIdQuantificador()));
                    value.setCellValue(aqdto.getValor());
                    ids.add(aqdto.getQuantificador().getIdQuantificador());
                }
            }
            ids = new ArrayList<>();
            for (AmostraQualificadorDTO aqdto : amostra.getClassificacoes()) {
                if (!ids.contains((Long) aqdto.getQualificadorDTO().getIdQualificador())) {
                    if(!rowColumnId.containsKey(aqdto.getQualificadorDTO().getIdQualificador())){
                        Cell newColumn = headerRow.createCell(columnIndex);
                        newColumn.setCellStyle(headerCell);
                        newColumn.setCellValue(aqdto.getQualificadorDTO().getNome());
                        rowColumnId.put(aqdto.getQualificadorDTO().getIdQualificador(), columnIndex);
                        columnIndex++;
                    }

                    Cell value = sampleRow.createCell(rowColumnId.get(aqdto.getQualificadorDTO().getIdQualificador()));
                    value.setCellValue(aqdto.getValor());
                    ids.add(aqdto.getQualificadorDTO().getIdQualificador());
                }
            }
        }

        for (int i=0; i<columnIndex; i++)
            sheet.autoSizeColumn(i);

        FileOutputStream fileOut = new FileOutputStream(etapa.getProcesso().getLote()+"-"+etapa.getCodigo()+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();


    }

    public static void exportEtapaData(List<AmostraDTO> amostras, Workbook workbook) throws IOException, InvalidFormatException {

        if (amostras.isEmpty()) return ;
        EtapaDTO etapa = amostras.get(0).getEtapa();
        int rowIndex = 2, columnIndex = 3;
        CreationHelper helper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet(etapa.getCodigo()+" - "+etapa.getNome());

        Map<Long, Integer> rowColumnId = new HashMap<>();
        List<Row> rows = new ArrayList<>();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCell = workbook.createCellStyle();
        headerCell.setFont(headerFont);

        Row headerRow = sheet.createRow(1);
        Cell sampleName = headerRow.createCell(1);
        sampleName.setCellStyle(headerCell);
        sampleName.setCellValue("Nome");
        Cell sampleID = headerRow.createCell(2);
        sampleID.setCellStyle(headerCell);
        sampleID.setCellValue("ID");
        for (AmostraDTO amostra : amostras) {
            List<Long> ids = new ArrayList<>();
            Row sampleRow = sheet.createRow(rowIndex);
            rowIndex++;
            Cell nameCell = sampleRow.createCell(1);
            nameCell.setCellValue(amostra.getNome());
            Cell idCell = sampleRow.createCell(2);
            idCell.setCellValue(amostra.getIdAmostra());
            for (AmostraQuantificadorDTO aqdto : amostra.getMedidas()) {
                if (!ids.contains((Long) aqdto.getQuantificador().getIdQuantificador())) {
                    if (!rowColumnId.containsKey(aqdto.getQuantificador().getIdQuantificador())) {
                        Cell newColumn = headerRow.createCell(columnIndex);
                        newColumn.setCellStyle(headerCell);
                        newColumn.setCellValue(aqdto.getQuantificador().getNome()+" ("+aqdto.getQuantificador().getUnidade()+")");
                        rowColumnId.put(aqdto.getQuantificador().getIdQuantificador(),columnIndex);
                        columnIndex++;
                    }

                    Cell value = sampleRow.createCell(rowColumnId.get(aqdto.getQuantificador().getIdQuantificador()));
                    value.setCellValue(aqdto.getValor());
                    ids.add(aqdto.getQuantificador().getIdQuantificador());
                }
            }
            ids = new ArrayList<>();
            for (AmostraQualificadorDTO aqdto : amostra.getClassificacoes()) {
                if (!ids.contains((Long) aqdto.getQualificadorDTO().getIdQualificador())) {
                    if(!rowColumnId.containsKey(aqdto.getQualificadorDTO().getIdQualificador())){
                        Cell newColumn = headerRow.createCell(columnIndex);
                        newColumn.setCellStyle(headerCell);
                        newColumn.setCellValue(aqdto.getQualificadorDTO().getNome());
                        rowColumnId.put(aqdto.getQualificadorDTO().getIdQualificador(), columnIndex);
                        columnIndex++;
                    }

                    Cell value = sampleRow.createCell(rowColumnId.get(aqdto.getQualificadorDTO().getIdQualificador()));
                    value.setCellValue(aqdto.getValor());
                    ids.add(aqdto.getQualificadorDTO().getIdQualificador());
                }
            }
        }

        for (int i=0; i<columnIndex; i++)
            sheet.autoSizeColumn(i);

    }

    public static void saveAndClose(Workbook workbook, String name) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(name);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

}
