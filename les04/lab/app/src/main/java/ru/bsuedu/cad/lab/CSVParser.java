package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String text) {
        List<Product> productsList = new ArrayList<>();

        String[] lines = text.split("\\R");

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] params = line.split(",", -1);

            productsList.add(new Product(
                    stringToInteger(params[0]),
                    params[1],
                    params[2],
                    stringToInteger(params[3]),
                    stringToDecimal(params[4]),
                    stringToInteger(params[5]),
                    params[6],
                    stringToDate(params[7]),
                    stringToDate(params[8])
            ));
        }

        return productsList;
    }

    public Date stringToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return sdf.parse(dateString.trim());
        } catch (ParseException e) {
            throw new RuntimeException("Ошибка парсинга даты: " + dateString, e);
        }
    }

    public BigDecimal stringToDecimal(String priceString) {
        return new BigDecimal(priceString.trim());
    }

    public Integer stringToInteger(String numberString) {
        return Integer.parseInt(numberString.trim());
    }
}