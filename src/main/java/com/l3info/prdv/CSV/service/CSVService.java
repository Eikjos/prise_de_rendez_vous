package com.l3info.prdv.CSV.service;

import com.l3info.prdv.CSV.helper.CSVHelper;
import com.l3info.prdv.account.Account;
import com.l3info.prdv.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    CSVHelper csvHelper;

    @Autowired
    AccountRepository repository;

    public void save(MultipartFile file) {
        try {
            csvHelper.csvToAccount(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Fail to save accounts" + e.getMessage());
        }
    }

    public String export() {
        return csvHelper.accountToCsv();
    }
}
