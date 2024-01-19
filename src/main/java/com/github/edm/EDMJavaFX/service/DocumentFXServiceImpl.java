package com.github.edm.EDMJavaFX.service;

import com.github.edm.EDMJavaFX.repository.DocumentFXRepository;
import com.github.edm.EDMJavaFX.repository.entity.DocumentFX;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class DocumentFXServiceImpl implements DocumentFXService {

    private final DocumentFXRepository documentFXRepository = new DocumentFXRepository();

    @Override
    public void addDocument(String code, String documentNumber, String documentType, LocalDate signingDate,
                            LocalDate endDate, String daysUntilDue) {
        documentFXRepository.addDocumentFromDialog(
                code,
                documentNumber,
                documentType,
                signingDate,
                endDate, daysUntilDue
        );
    }

    @Override
    public void editDocument(DocumentFX documentFX, String code, String documentNumber, String documentType,
                             LocalDate signingDate, LocalDate endDate, String daysUntilDue,
                             TableView<DocumentFX> tableView) {
        documentFXRepository.editDocumentFromDialog(
                documentFX,
                code,
                documentNumber,
                documentType,
                signingDate,
                endDate,
                daysUntilDue,
                tableView
        );
    }
    @Override
    public ObservableList<DocumentFX> getExcelTableData() {
        return documentFXRepository.getExcelTableData();
    }

    @Override
    public ObservableList<DocumentFX> getData() {
        return documentFXRepository.getData();
    }

    @Override
    public void setData(ObservableList<DocumentFX> data) {
        documentFXRepository.setData(data);
    }
}