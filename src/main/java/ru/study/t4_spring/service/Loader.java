package ru.study.t4_spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.study.t4_spring.config.LoaderConfig;
import ru.study.t4_spring.record.LoginRec;
import ru.study.t4_spring.utils.Utils;

import java.io.*;
import java.util.List;

@Component
public class Loader implements Serviceable {
    private final LoaderConfig loaderConfig;
    private final Serviceable nextService;

    public Loader(LoaderConfig loaderConfig, @Qualifier("fioService") Serviceable nextService) {
        this.loaderConfig = loaderConfig;
        this.nextService = nextService;
    }

    @Override
    public List<LoginRec> make(List<LoginRec> list) {
        File folder = new File(loaderConfig.getIn());
        File[] files = folder.listFiles();

        if (files == null) return list;

        for (File file : files) {
            System.out.println(file.getName());
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    LoginRec rec = mapStringToLoginRec(line, file.getName());
                    if (rec != null) list.add(rec);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Перенос обработанного файла в архивный каталог
            File archiveFile = new File(loaderConfig.getArchive(), file.getName());
            archiveFile.delete();
            if (file.renameTo(archiveFile)) {
                file.delete();
            }
        }
        return list;
    }

    @Override
    public Serviceable getNextService() {
        return nextService;
    }

    private LoginRec mapStringToLoginRec(String line, String filename) {
        try {
            String[] phrases = line.split(",");
            if (phrases.length == 4) {
                return new LoginRec(
                        phrases[0],
                        phrases[1],
                        Utils.mapStringToDate(phrases[2]),
                        phrases[3],
                        filename);
            } else {
                System.out.println("Ошибка разбора строки " + line + " : некорректная строка");
            }
        } catch (Exception e) {
            System.out.println("Ошибка разбора строки " + line + " : " + e);
        }
        return null;
    }
}

