package com.github.edm.EDMExcel.repository;

import com.github.edm.EDMExcel.repository.entity.DocumentExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DocumentExcelRepository {
    private final static String pathToExcelList = "./ExampleList.xlsx";
    private final static String nameOfList = "Документы";
    public List<DocumentExcel> documentExcelList;

    public void saveDocument(DocumentExcel documentExcel) {
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(pathToExcelList));
            Sheet sheet = workbook.getSheet(nameOfList);
            DataFormat format = workbook.createDataFormat();
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            Row newRow = findEmptyRow(sheet);
            assert newRow != null;
            addDocumentInExcel(newRow, documentExcel, dataStyle);
            workbook.write(new FileOutputStream(pathToExcelList));
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateDocument(DocumentExcel updatedDocumentExcel) {
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(pathToExcelList));
            Sheet sheet = workbook.getSheet(nameOfList);
            DataFormat format = workbook.createDataFormat();
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            Row row = sheet.getRow(updatedDocumentExcel.getId());
            addDocumentInExcel(row, updatedDocumentExcel, dataStyle);
            workbook.write(new FileOutputStream(pathToExcelList));
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteDocument(DocumentExcel documentExcel) {
        int rowToDelete = documentExcel.getId();
        Sheet sheet = getConnectionToExcelTable();
        if (rowToDelete >= 0 && rowToDelete <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(rowToDelete);
            if (row != null) {
                sheet.removeRow(row);
            }
        }
        for (int rowNum = rowToDelete + 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            Row rowAbove = sheet.getRow(rowNum - 1);
            if (row != null && rowAbove != null) {
                for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                    Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell cellAbove = rowAbove.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cellAbove.setCellValue(cell.getStringCellValue());
                }
            }
        }
        if (sheet.getLastRowNum() >= 0) {
            sheet.removeRow(sheet.getRow(sheet.getLastRowNum()));
        }
    }
    public Sheet getConnectionToExcelTable() {
        try {
            return new XSSFWorkbook(new FileInputStream(pathToExcelList)).getSheet(nameOfList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DocumentExcel> getExcelData() {
        List<DocumentExcel> allData = new ArrayList<>();
        Sheet sheet = getConnectionToExcelTable();
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            DocumentExcel document = new DocumentExcel();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                setDocumentDataFromCell(document, cell);
            }
            allData.add(document);
        }
        documentExcelList = allData;
        return allData;
    }
    private void addDocumentInExcel(Row row, DocumentExcel document, CellStyle dataStyle) {
        Cell cellCode = row.createCell(0);
        cellCode.setCellValue(document.getId());
        Cell cellNumberOfDocument = row.createCell(1);
        cellNumberOfDocument.setCellValue(document.getNumberOfDocument());
        Cell cellKindOfDocument = row.createCell(2);
        cellKindOfDocument.setCellValue(document.getKindOfDocument());
        Cell cellStartDate = row.createCell(3);
        cellStartDate.setCellStyle(dataStyle);
        cellStartDate.setCellValue(document.getStartData());
        Cell cellEndDate = row.createCell(4);
        cellEndDate.setCellStyle(dataStyle);
        cellEndDate.setCellValue(document.getEndData());
        Cell cellDaysUntilDue = row.createCell(5);
        cellDaysUntilDue.setCellValue(document.getDaysUntilOverdue());
    }

    private void setDocumentDataFromCell(DocumentExcel document, Cell cell) {
        int columnIndex = cell.getColumnIndex();
        switch (columnIndex) {
            case 0 -> document.setId(getCellValueAsInt(cell));
            case 1 -> document.setNumberOfDocument(getCellValueAsInt(cell));
            case 2 -> document.setKindOfDocument(getCellValueAsString(cell));
            case 3 -> document.setStartData(getCellValueAsString(cell));
            case 4 -> document.setEndData(getCellValueAsString(cell));
            case 5 -> document.setDaysUntilOverdue(getCellValueAsInt(cell));
        }
        System.out.println(cell);
    }

    private int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0;
        }

        if (Objects.requireNonNull(cell.getCellType()) == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }
        return 0;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (Objects.requireNonNull(cell.getCellType()) == CellType.STRING) {
            return cell.getStringCellValue();
        }
        else {
            return cell.getDateCellValue().toString();
        }
    }

    private Row findEmptyRow(Sheet sheet) {
        for (Row row : sheet) {
            boolean isEmpty = true;
            for (Cell cell : row) {
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                return row;
            }
        }
        return null;
    }
}