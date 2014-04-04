package ru.antonlavr.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.antonlavr.model.Birthday;
import ru.antonlavr.repository.BirthdayRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;

@EnableScheduling
@Service
public class BirthdayShedule {
    public static  final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Autowired
    BirthdayRepository birthdayRepository;

    public BirthdayShedule() {}

    @Transactional
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void getWeather() {
        birthdayRepository.deleteAll();
        downloadBirthday();
    }

    public void downloadBirthday() {
        try {
            URL url = new URL("https://calendar.yandex.ru/export/rss.xml?private_token=c46780ac8d0b636cd04e4bfbadfb55638ba69b09&tz_id=Europe/Moscow&limit=10");
            InputStream stream = url.openStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);

            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("entry");

            for(int i = 0; i < items.getLength(); i++){
                NodeList item = items.item(i).getChildNodes();
                Birthday birthday = new Birthday();
                for (int j = 0; j < item.getLength(); j++){
                    if (item.item(j).getNodeName() == "title") {
                        birthday.setTitle(item.item(j).getTextContent());
                    }
                    if (item.item(j).getNodeName() == "updated") {
                        birthday.setDate(dateFormat.parse(item.item(j).getTextContent()));
                    }
                }
                birthdayRepository.save(birthday);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
