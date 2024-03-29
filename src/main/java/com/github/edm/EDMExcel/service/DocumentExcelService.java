package com.github.edm.EDMExcel.service;

import com.github.edm.EDMExcel.repository.entity.DocumentExcel;

import java.util.List;

public interface DocumentExcelService {
    void saveDocument(DocumentExcel documentExcel);

    void updateDocument(DocumentExcel updatedDocumentExcel);

    void deleteDocument(Integer id);

    List<DocumentExcel> excelData();
}
