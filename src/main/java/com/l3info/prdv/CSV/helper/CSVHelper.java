package com.l3info.prdv.CSV.helper;
import java.io.*;
import java.util.*;

import com.l3info.prdv.account.Account;
import com.l3info.prdv.account.AccountService;
import com.l3info.prdv.account.AccountType;
import com.l3info.prdv.account.dto.CreateAccountDto;
import com.l3info.prdv.account.exception.AccountNotFoundException;
import com.l3info.prdv.group.Group;
import com.l3info.prdv.group.GroupService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
public class CSVHelper {

    @Autowired
    public GroupService groupService;
    @Autowired
    public AccountService accountService;

    public static String TYPE = "csv";
    public boolean hasCSVFormat(MultipartFile file) {
        String name = file.getOriginalFilename();
        assert name != null;
        return TYPE.equals(name.substring(name.lastIndexOf(".") + 1));
    }


    public void csvToAccount(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withTrim())) {
            List<Account> accounts = new ArrayList<>();
            List<CSVRecord> csvRecords = csvParser.getRecords();
            System.out.println(csvRecords.size());
            String[] firstLine = csvRecords.get(0).get(0).split(";");
            System.out.println(Arrays.toString(firstLine));
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < firstLine.length; i++) {
                System.out.println(firstLine.length);
                map.put(firstLine[i].toLowerCase(Locale.ROOT), i);
            }
            System.out.println(csvRecords.size());
            for (int i = 1; i < csvRecords.size(); i++) {
                String[] line = csvRecords.get(i).get(0).split(";");
                System.out.println(Arrays.toString(line));
                CreateAccountDto dto = new CreateAccountDto();
                dto.setUsername(line[map.get("login")]);
                dto.setPassword(line[map.get("password")]);
                dto.setFirstName(line[map.get("firstname")]);
                dto.setLastName(line[map.get("lastname")].toUpperCase(Locale.ROOT));
                dto.setEmail(line[map.get("email")]);
                dto.setType(AccountType.accountTypeFromString(line[map.get("role")]));
                Account account = accountService.create(dto.toAccount());
                System.out.println(account.getGroups());
                String[] groupsName = line[map.get("group")].split(",");
                System.out.println(Arrays.toString(groupsName));
                System.out.println(account.getId());
                List<Group> groups = new ArrayList<>();
                for (String s: groupsName) {
                    groups.add(groupService.findGroupByName(s));
                }
                for (Group g : groups) {
                    System.out.println(account.getGroups());
                    accountService.addToGroup(account.getId(), g.getId());
                }
                accounts.add(account);
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to parse com.l3info.prdv.CSV file: " + e.getMessage());
        }
    }

    public String accountToCsv() {
         List<Account> accounts = accountService.findAll();
         StringBuilder contents = new StringBuilder("login;password;firstname;lastname;email;role;group\n");
         for (Account account : accounts) {
             contents.append(account.exportString());
             contents.append("\n");
         }
         return contents.toString();
    }
}
