package com.nomoma;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomoma.budget.svc.ExpensesService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final ExpensesService expensesService;

    //test3
    @GetMapping("/test")
    public void download(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment;filename=budget.xlsx");
        response.setContentType(String.valueOf(MediaType.APPLICATION_OCTET_STREAM));

        ByteArrayInputStream inputStream = new ExcelTest(expensesService).createExcel();
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
