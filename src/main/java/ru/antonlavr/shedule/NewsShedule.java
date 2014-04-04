package ru.antonlavr.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.antonlavr.model.News;
import ru.antonlavr.repository.NewsRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

@EnableScheduling
@Service
public class NewsShedule {

    @Autowired
    NewsRepository newsRepository;

    public NewsShedule() {}

    @Transactional
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void getWeather() {
        newsRepository.deleteAll();
        downloadNews();
    }

    public void downloadNews() {
        try {
            URL url = new URL("http://prochurch.info/index.php/news/rss_2.0");
            InputStream stream = url.openStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);

            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for(int i = 0; i < items.getLength(); i++){
                NodeList item = items.item(i).getChildNodes();
                News oneNews = new News();
                for (int j = 0; j < item.getLength(); j++){
                    if (item.item(j).getNodeName() == "title") {
                        oneNews.setTitle(item.item(j).getTextContent());
                    }
                    if (item.item(j).getNodeName() == "description") {
                        oneNews.setDescription(item.item(j).getTextContent());
                    }
                    if (item.item(j).getNodeName() == "link") {
                        oneNews.setLink(item.item(j).getTextContent());
                    }
                }
                newsRepository.save(oneNews);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
