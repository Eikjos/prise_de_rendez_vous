package com.l3info.prdv.CSV.controller;

import java.io.IOException;
import com.l3info.prdv.CSV.helper.CSVHelper;
import com.l3info.prdv.CSV.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    CSVHelper csvHelper;

    @Autowired
    CSVService csvService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file) {
        if (csvHelper.hasCSVFormat(file)) {
            try {
                csvService.save(file);
                return "redirect:/account/list";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/account/list?adderror";
            }
        }
        return "redirect:/account/list?formaterror";
    }

    @PostMapping(value = "/export", produces = {"text/csv"})
    @ResponseBody
    public String exportFile() {
        return csvService.export();
    }
}
