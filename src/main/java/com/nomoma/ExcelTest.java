package com.nomoma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nomoma.budget.model.dto.ExpensesDto;
import com.nomoma.budget.svc.ExpensesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelTest {

    private final ExpensesService expensesService;

    @Transactional
    public ByteArrayInputStream createExcel() throws IOException {
        try(final Workbook workbook = new XSSFWorkbook()){
            final Sheet sheet = workbook.createSheet("budget");

            final AtomicInteger rowNumber = new AtomicInteger(1);
            final AtomicInteger cellNumber = new AtomicInteger(0);

            //header
            final Row row = sheet.createRow(0);
            ExpensesDto.getTitles().forEach(title -> {
                row.createCell(cellNumber.getAndIncrement()).setCellValue(title);
            });

            //body
            expensesService.getExpenses().forEach(v -> {
                cellNumber.set(0);
                final Row rowBody = sheet.createRow(rowNumber.getAndIncrement());
                v.getBody(rowNumber.get() - 1).forEach(expenses -> {
                    if(cellNumber.get() == 5) {
                        rowBody.createCell(cellNumber.getAndIncrement()).setCellValue((int)expenses);
                    } else {
                        rowBody.createCell(cellNumber.getAndIncrement()).setCellValue(String.valueOf(expenses));
                    }
                });
            });

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
